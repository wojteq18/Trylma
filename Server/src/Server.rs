use std::io::{BufRead, BufReader, Write};
use std::net::{TcpListener, TcpStream}; // protokół TCP
use std::sync::{Arc, Mutex};
use std::thread;
use std::process::{Command, Stdio};
use std::io;

fn handle_client(
    mut stream: TcpStream,
    clients: Arc<Mutex<Vec<TcpStream>>>,
    current_player: Arc<Mutex<usize>>,
    java_stdin: Arc<Mutex<std::process::ChildStdin>>,
    java_reader: Arc<Mutex<BufReader<std::process::ChildStdout>>>,
) {
    let reader_stream = stream.try_clone().expect("Błąd klonowania strumienia");
    let mut writer_stream = stream;
    let peer_addr = writer_stream
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
                let player_index = {
                    let clients_guard = clients.lock().unwrap();
                    clients_guard
                        .iter()
                        .position(|c| c.peer_addr().unwrap() == peer_addr)
                        .unwrap()
                };

                {
                    let mut current_player_guard = current_player.lock().unwrap();
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
                    java_reader_guard
                        .read_line(&mut response)
                        .expect("Błąd odczytu z procesu Javy");
                    response 
                };

                println!("Logika Javy: {}", java_response.trim());

                // Wyślij odpowiedź do klienta
                writeln!(writer_stream, "{}", java_response.trim()).unwrap();

                if java_response.trim().starts_with("ok") {
                    // Przejdź do następnego gracza, jeśli odpowiedź Javy jest "ok"
                    let mut current_player_guard = current_player.lock().unwrap();
                    *current_player_guard = (*current_player_guard + 1) % clients.lock().unwrap().len();
                }
            }

            Err(e) => {
                println!("Błąd odczytu od klienta: {}", e);
                break;
            }
        }
    }

    // Po zakończeniu połączenia, klient jest usuwany z listy
    let mut clients_guard = clients.lock().unwrap();
    clients_guard.retain(|c| c.peer_addr().unwrap() != peer_addr);
    println!("Klient usunięty: {}", peer_addr);
}

fn main() -> std::io::Result<()> 
{

    //pytanie o ilosc klientow
    println!("Podaj ilość graczy: ");
    let mut max_players = String::new();
    io::stdin().read_line(&mut max_players).expect("Błąd odczytu");
    let max_players: u32 = max_players.trim().parse().expect("Błąd parsowania");

    let listener = TcpListener::bind("127.0.0.1:9999")?; // nasłuchuje na porcie 9999
    println!("Serwer uruchomiony na 127.0.0.1:9999");

    // Uruchom proces Java
    let mut java_process = Command::new("java")
        .arg("-cp")
        .arg("/home/vostok/codes/Trylma/src/main/java") // Ścieżka do katalogu bazowego dla pakietu
        .arg("com.example.GameLogic") // Klasa z przestrzenią nazw
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

    // Prześlij liczbę graczy do procesu Javy
{
    let mut java_stdin_guard = java_stdin.lock().unwrap();
    writeln!(java_stdin_guard, "{}", max_players).expect("Nie udało się przesłać liczby graczy do Javy");
}

// Odbierz informację zwrotną od Javy
{
    let mut java_reader_guard = java_reader.lock().unwrap();
    let mut response = String::new();
    java_reader_guard.read_line(&mut response).expect("Błąd odczytu z procesu Javy");
    println!("Proces Java: {}", response.trim());
}

    // Współdzielona lista klientów
    let clients = Arc::new(Mutex::new(Vec::new()));
    let current_player = Arc::new(Mutex::new(0));

    while clients.lock().unwrap().len() < max_players as usize
    {
        println!("Oczekiwanie na graczy...");
        match listener.accept()
        {
            Ok((stream, _addr)) => {
                println!("Nowe połączenie: {}", stream.peer_addr().unwrap());
                let mut clients_guard = clients.lock().unwrap();
                clients_guard.push(stream.try_clone().expect("Błąd klonowania")); // Dodaj klienta do listy
            }
            Err(e) => {
                println!("Błąd połączenia: {}", e);
            }
        }
    }

    println!("Wszyscy gracze dołączyli, rozpocznij grę");

    //rozpocznij obsluge wszystkich klientow
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
    //serwer pozostaje aktywny
    loop {
        thread::sleep(std::time::Duration::from_secs(1));
    }
    Ok(()) 
}             



