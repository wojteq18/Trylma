use std::io::{BufRead, BufReader, Write};
use std::net::{TcpListener, TcpStream};
use std::sync::{Arc, Mutex};
use std::thread;
use std::process::{Command, Stdio};
use std::io;

fn handle_client( //obsluguje klienta, odbiera wiadomosci od klienta, przekazuje je do procesu javy, odbiera odpowiedz od javy, przekazuje ja do klienta
    stream: TcpStream, //reprezentuje polaczenie z jednym klientem, jest to gniazdo TCP
    clients: Arc<Mutex<Vec<TcpStream>>>, //lista przechowujaca polaczenia z klientami
    current_player: Arc<Mutex<usize>>, //indeks aktualnego gracza
    java_stdin: Arc<Mutex<std::process::ChildStdin>>, //wspoldzielony strumien wejsciowy
    java_reader: Arc<Mutex<BufReader<std::process::ChildStdout>>>, //wspoldzielony obiekt do odczytu odpowiedzi od javy
) {
    let reader_stream = stream.try_clone().expect("Błąd klonowania strumienia");
    let mut writer_stream = stream;
    let peer_addr = writer_stream //adres IP klienta
        .peer_addr()
        .expect("Nie można uzyskać adresu IP klienta");

    let mut reader = BufReader::new(reader_stream); // Umożliwia łatwiejsze i bardziej wydajne odczytywanie danych
    let mut buffer = String::new(); // Zmienna do przechowywania pojedynczej wiadomości od klienta

    loop {
        buffer.clear();
 
        match reader.read_line(&mut buffer) {
            Ok(0) => {
                // Klient zamknął połączenie
                println!("Klient rozłączył się: {}", peer_addr);
                break;
            }

            Ok(_) => {
                let message = buffer.trim();

                // Sprawdź, czy to jest tura gracza
                let player_index;
                {
                    let clients_guard = clients.lock().unwrap();
                    player_index = clients_guard
                        .iter()
                        .position(|c| c.peer_addr().unwrap() == peer_addr)
                        .unwrap();
                }

                {
                    let current_player_guard = current_player.lock().unwrap();
                    if player_index != *current_player_guard {
                        writeln!(
                            writer_stream,
                            "Nie twoja kolej, aktualny gracz: {}",
                            *current_player_guard + 1
                        )
                        .unwrap();
                        continue;
                    }
                }

                println!("Gracz {}: {}", player_index + 1, message);

                // Prześlij wiadomość do procesu Javy
                {
                    let mut java_stdin_guard = java_stdin.lock().unwrap();
                    writeln!(java_stdin_guard, "{}", message)
                        .expect("Nie udało się wysłać do procesu Javy");
                }

                // Odbierz odpowiedź z Javy
                let java_response = {
                    let mut java_reader_guard = java_reader.lock().unwrap();
                    let mut response = String::new();
                    //println!("Hał, hał2");
                    java_reader_guard
                        .read_line(&mut response)
                        .expect("Błąd odczytu z procesu Javy");
                    response
                    
                };
                
                println!("Teraz: {}", java_response);
                writeln!(writer_stream, "{}", java_response.trim()).unwrap();
                let first_word = java_response.trim().split_whitespace().next().unwrap_or("");

                if first_word == "ok" || java_response.trim() == "Ok" {
                    let mut current_player_guard = current_player.lock().unwrap();
                    {
                        let clients_guard = clients.lock().unwrap();
                        *current_player_guard = (*current_player_guard + 1) % clients_guard.len();

                        // Wyślij powiadomienie do następnego gracza
                        if let Some(next_client) = clients_guard.get(*current_player_guard) {
                            let mut next_writer = next_client
                                .try_clone()
                                .expect("Błąd klonowania TcpStream");
                            writeln!(next_writer, "Gracz {}: {}", player_index + 1, message)
                                .expect("Błąd wysyłania powiadomienia do następnego gracza");
                            writeln!(next_writer, "Twoja kolej!")
                                .expect("Błąd wysyłania powiadomienia do następnego gracza");
                        }
                    }
                } else if first_word == "error" || java_response.trim() == "Error" {
                    writeln!(writer_stream, "Błąd: Powtórz ruch").unwrap();
                } else if first_word == "Pionki: " || first_word == "Pionki" {
                    writeln!(writer_stream, "{}", java_response.trim()).unwrap();
                    continue;
                } else {
                    writeln!(writer_stream, "Wykonaj ruch lub podświetl swoje pionki!").unwrap();
                }
            }
            Err(e) => {
                println!("Błąd odczytu od klienta: {}", e);
                break;
            }
        }
        println!("hejlakoniec");
    }

    // Po zakończeniu połączenia, klient jest usuwany z listy
    {
        let mut clients_guard = clients.lock().unwrap();
        clients_guard.retain(|c| c.peer_addr().unwrap() != peer_addr);
    }
    println!("Klient usunięty: {}", peer_addr);
}

fn initialize_server(address: &str) -> std::io::Result<TcpListener> { //inicjalizuje serwer na podanym adresie
    let listener = TcpListener::bind(address)?;
    println!("Serwer uruchomiony na {}", address);
    Ok(listener)
}

fn setup_java_process( //uruchamia proces javy
    class_path: &str,
    main_class: &str,
) -> (
    Arc<Mutex<std::process::ChildStdin>>,
    Arc<Mutex<BufReader<std::process::ChildStdout>>>,
) {
    let mut java_process = Command::new("java")
        .arg("-cp")
        .arg(class_path)
        .arg(main_class)
        .stdin(Stdio::piped())
        .stdout(Stdio::piped())
        .spawn()
        .expect("Nie udało się uruchomić logiki gry w Javie.");

    let java_stdin = Arc::new(Mutex::new(
        java_process.stdin.take().expect("Błąd dostępu do stdin Javy"),
    ));

    let java_reader = Arc::new(Mutex::new(BufReader::new(
        java_process.stdout.take().expect("Błąd dostępu do stdout Javy"),
    )));

    return (java_stdin, java_reader)
}

fn accept_clients(listener: &TcpListener, max_players: usize) -> Vec<TcpStream> { //oczekuje na klientow oraz dodaje ich do listy klientow
    let mut clients = Vec::new();
    println!("Oczekiwanie na graczy...");

    while clients.len() < max_players {
        match listener.accept() {
            Ok((stream, _)) => {
                println!("Nowe połączenie: {}", stream.peer_addr().unwrap());
                clients.push(stream);
            }
            Err(e) => {
                println!("Błąd połączenia: {}", e);
            }
        }
    }

    for client in &mut clients {
        writeln!(client, "Wszyscy gracze dołączyli, gra się rozpoczyna!").expect("Błąd wysyłania wiadomości do klienta");
    }
    clients
}

fn initialize_game(max_players: usize, java_stdin: &Arc<Mutex<std::process::ChildStdin>>) { //informuje proces java o liczbie graczy,  TODO przekazac strategie
    let mut java_stdin_guard = java_stdin.lock().unwrap();
    writeln!(java_stdin_guard, "{}", max_players).expect("Nie udało się przesłać liczby graczy do Javy");
}

fn start_game( //rozpoczyna gre, tworzy wspoldzielona liste klientow, tworzy osobny watek dla klienta w ktorym dziala handle_client
    clients: Vec<TcpStream>,
    current_player: Arc<Mutex<usize>>,
    java_stdin: Arc<Mutex<std::process::ChildStdin>>,
    java_reader: Arc<Mutex<BufReader<std::process::ChildStdout>>>,
) {
    let clients = Arc::new(Mutex::new(clients));

    {
        let clients_guard = clients.lock().unwrap();
        for client in clients_guard.iter() {
            let client = client.try_clone().expect("Błąd klonowania TcpStream");
            let clients = Arc::clone(&clients);
            let current_player = Arc::clone(&current_player);
            let java_stdin = Arc::clone(&java_stdin);
            let java_reader = Arc::clone(&java_reader);

            thread::spawn(move || {
                handle_client(client, clients, current_player, java_stdin, java_reader);
            });
        }
    }
}

fn run_server_loop() { //utrzymuje dzialanie serwera
    loop {
        thread::sleep(std::time::Duration::from_secs(1));
    }
}

fn main() -> std::io::Result<()> {
    let max_players = {
        println!("Podaj ilość graczy: ");
        let mut max_players = String::new();
        io::stdin().read_line(&mut max_players).expect("Błąd odczytu");
        let max_players: usize = max_players.trim().parse().expect("Błąd parsowania");
        max_players  // zwracamy przetworzoną wartość
    };

    let strategy = {
        println!("Podaj numer strategii: ");
        println!("1. Brak");  //zmieniłem strategie na nasze konkretne
        println!("2. Yin and Yang");
        println!("3. Order Out of Chaos");
        
        let mut strategy = String::new();
        io::stdin().read_line(&mut strategy).expect("Błąd odczytu");
        let strategy: usize = strategy.trim().parse().expect("Błąd parsowania");
        strategy  // zwracamy przetworzoną wartość
    };

    let listener = initialize_server("127.0.0.1:9999")?;

    let clients = accept_clients(&listener, max_players);

    let (java_stdin, java_reader) = setup_java_process(
        "/home/user/Programowanie/3sem/TP/2czesc/Trylma/target/classes", // Nowa ścieżka do klasy Java
        "com.example.Main", // Główna klasa Java
    );

    initialize_game(max_players, &java_stdin); //TODO: przekazac strategie

    let current_player = Arc::new(Mutex::new(0));

    start_game(clients, current_player, java_stdin, java_reader);

    run_server_loop();

    Ok(())
}
