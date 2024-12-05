use std::io::{BufRead, BufReader, Write};
use std::net::{TcpListener, TcpStream}; //protokol TCP
use std::sync::{Arc, Mutex};
use std::thread;

fn handle_client(
    mut stream: TcpStream,
    clients: Arc<Mutex<Vec<TcpStream>>>,
    current_player: Arc<Mutex<usize>>,
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
                let mut clients_guard = clients.lock().unwrap();
                let player_index = clients_guard
                    .iter()
                    .position(|c| c.peer_addr().unwrap() == peer_addr)
                    .unwrap();

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

                println!("Gracz {}: {}", player_index + 1, buffer.trim());
                let command = buffer.trim();

                let response = if command.starts_with("move") {
                    "Ok, ruch wykonany"
                } else {
                    "Niepoprawna komenda"
                };
                writeln!(writer_stream, "{}", response).unwrap();

                if response.starts_with("Ok") {
                    for client in clients_guard.iter_mut() {
                        writeln!(
                            client,
                            "Gracz {} wykonał ruch: {}",
                            player_index + 1,
                            buffer.trim()
                        )
                        .unwrap();
                    }
                    // Przejdź do następnego gracza
                    let mut current_player_guard = current_player.lock().unwrap();
                    *current_player_guard = (*current_player_guard + 1) % clients_guard.len();
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
    let listener = TcpListener::bind("127.0.0.1:9999")?; //nasluchuje na porcie 9999, znak ? sprawdza czy zakonczono powodzeniem
    println!("Serwer uruchomiony na 127.0.0.1:9999");

    //wspoldzielona lista klientow
    let clients = Arc::new(Mutex::new(Vec::new())); //Arc pozwala na wspoldzielenie listy klientow miedzy wieloma watkami
    let current_player = Arc::new(Mutex::new(0));

    for stream in listener.incoming() {
        match stream {
            Ok(stream) => {
                println!("Nowe połączenie: {}", stream.peer_addr().unwrap());
                let clients = Arc::clone(&clients); // kopia referencji do listy klientów
                let current_player = Arc::clone(&current_player);
    
                // Dodaj klienta do listy
                {
                    let mut clients_guard = clients.lock().unwrap();
                    clients_guard.push(stream.try_clone().unwrap()); // tworzy kopię strumienia
                    let num_clients = clients_guard.len();

                    if num_clients == 2 || num_clients == 3 || num_clients == 4 || num_clients == 6
                    {
                        println!("Liczba graczy odpowiednia {}", num_clients);
                    }
                    else
                    {
                        println!("Liczba graczy nieodpowiednia: {}, powinna wynosić 2 lub 3 lub 4 lub 6", num_clients);
                    }
                }
    
                // Uruchom nowy wątek do obsługi klienta
                thread::spawn(move || {
                    handle_client(stream, clients, current_player); // obsługa każdego klienta w osobnym wątku
                });
            }
            Err(e) => {
                println!("Błąd połączenia: {}", e);
            }
        }
    }    
    Ok(()) //program zakonczyl sie pomyslnie
}