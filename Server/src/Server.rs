use std::io::{BufRead, BufReader, Write};
use std::net::{TcpListener, TcpStream};
use std::sync::{Arc, Mutex};
use std::thread;
use std::process::{Command, Stdio};
use std::io;

fn initialize_server(address: &str) -> std::io::Result<TcpListener> 
{
    let listener = TcpListener::bind(address)?;
    println!("Serwer uruchomiony na {}", address);
    Ok(listener)
}

fn setup_java_process(class_path: &str, main_class: &str) -> (Arc<Mutex<std::process::ChildStdin>>, Arc<Mutex<BufReader<std::process::ChildStdout>>>) 
{
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

    (java_stdin, java_reader)
}

fn accept_clients(listener: &TcpListener, max_players: usize) -> Vec<TcpStream> 
{
    let mut clients = Vec::new();
    println!("Oczekiwanie na graczy...");

    while clients.len() < max_players 
    {
        match listener.accept() {
            Ok((stream, _)) => 
            {
                println!("Nowe połączenie: {}", stream.peer_addr().unwrap());
                clients.push(stream);
            }
            Err(e) => 
            {
                println!("Błąd połączenia: {}", e);
            }
        }
    }

    println!("Wszyscy gracze dołączyli, rozpocznij grę");
    clients
}

fn initialize_game(max_players: usize, java_stdin: &Arc<Mutex<std::process::ChildStdin>>) 
{
    let mut java_stdin_guard = java_stdin.lock().unwrap();
    writeln!(java_stdin_guard, "{}", max_players).expect("Nie udało się przesłać liczby graczy do Javy");
}

fn read_from_java_process(java_reader: &Arc<Mutex<BufReader<std::process::ChildStdout>>>) -> String 
{
    let mut java_reader_guard = java_reader.lock().unwrap();
    let mut response = String::new();
    java_reader_guard
        .read_line(&mut response)
        .expect("Błąd odczytu z procesu Javy");
    response.trim().to_string()
}

fn start_game(
    clients: Vec<TcpStream>,
    current_player: Arc<Mutex<usize>>,
    java_stdin: Arc<Mutex<std::process::ChildStdin>>,
    java_reader: Arc<Mutex<BufReader<std::process::ChildStdout>>>,
) 
{
    let clients = Arc::new(Mutex::new(clients));

    {
        let clients_guard = clients.lock().unwrap();
        for (index, client) in clients_guard.iter().enumerate() 
        {
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

fn run_server_loop() 
{
    loop {
        thread::sleep(std::time::Duration::from_secs(1));
    }
}

fn notify_next_player(clients: &Arc<Mutex<Vec<TcpStream>>>, next_player: usize) {
    let clients_guard = clients.lock().unwrap();
    if let Some(client) = clients_guard.get(next_player) {
        let mut stream = client.try_clone().expect("Błąd klonowania TcpStream");
        writeln!(stream, "Twoja kolej!").expect("Błąd wysyłania wiadomości do klienta");
    }
}

fn handle_client(
    mut stream: TcpStream,
    clients: Arc<Mutex<Vec<TcpStream>>>,
    current_player: Arc<Mutex<usize>>,
    java_stdin: Arc<Mutex<std::process::ChildStdin>>,
    java_reader: Arc<Mutex<BufReader<std::process::ChildStdout>>>,
) {
    let peer_addr = stream.peer_addr().expect("Nie można uzyskać adresu IP klienta");
    let mut reader = BufReader::new(stream.try_clone().expect("Błąd klonowania strumienia"));
    let mut buffer = String::new();

    loop {
        buffer.clear();
        match reader.read_line(&mut buffer) {
            Ok(0) => {
                println!("Klient rozłączył się: {}", peer_addr);
                break;
            }
            Ok(_) => {
                let message = buffer.trim();
                println!("Otrzymano wiadomość od {}: {}", peer_addr, message);

                let player_index = {
                    let clients_guard = clients.lock().unwrap();
                    clients_guard.iter().position(|c| c.peer_addr().unwrap() == peer_addr).unwrap()
                };

                {
                    let mut current_player_guard = current_player.lock().unwrap();
                    if player_index != *current_player_guard {
                        writeln!(
                            stream,
                            "Nie twoja kolej, aktualny gracz: {}",
                            *current_player_guard + 1
                        ).unwrap();
                        continue;
                    }
                }

                {
                    let mut java_stdin_guard = java_stdin.lock().unwrap();
                    writeln!(java_stdin_guard, "{}", message).expect("Błąd wysyłania wiadomości do Javy");
                }

                let response = read_from_java_process(&java_reader);
                println!("Odpowiedź Javy: {}", response);

                writeln!(stream, "{}", response).expect("Błąd wysyłania wiadomości do klienta");

                if response.starts_with("ok") {
                    let mut current_player_guard = current_player.lock().unwrap();
                    *current_player_guard = (*current_player_guard + 1) % clients.lock().unwrap().len();
                    notify_next_player(&clients, *current_player_guard);
                }
            }
            Err(e) => {
                println!("Błąd odczytu od klienta: {}", e);
                break;
            }
        }
    }

    let mut clients_guard = clients.lock().unwrap();
    clients_guard.retain(|c| c.peer_addr().unwrap() != peer_addr);
    println!("Klient usunięty: {}", peer_addr);
}

fn main() -> std::io::Result<()> {
    println!("Podaj ilość graczy: ");
    let mut max_players = String::new();
    io::stdin().read_line(&mut max_players).expect("Błąd odczytu");
    let max_players: usize = max_players.trim().parse().expect("Błąd parsowania");

    let listener = initialize_server("127.0.0.1:9999")?;
    let (java_stdin, java_reader) = setup_java_process("/home/vostok/codes/Trylma/src/main/java", "com.example.GameLogic");

    initialize_game(max_players, &java_stdin);
    let initial_response = read_from_java_process(&java_reader);
    println!("Proces Java: {}", initial_response);

    let clients = accept_clients(&listener, max_players);
    let current_player = Arc::new(Mutex::new(0));

    start_game(clients, current_player, java_stdin, java_reader);

    run_server_loop();

    Ok(())
}
