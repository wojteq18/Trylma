use std::io::{BufReader, BufRead, Write};
use std::net::TcpStream;
use std::thread;

fn main() -> std::io::Result<()> {
    // Łączenie z serwerem
    let mut stream = TcpStream::connect("127.0.0.1:9999")?;
    println!("Połączono z serwerem");

    // Odbieranie wiadomości od serwera
    let mut stream_clone = stream.try_clone()?; // Tworzenie klonu strumienia, by odbierać wiadomości od serwera
    thread::spawn(move || {
        let reader = BufReader::new(&mut stream_clone);
        for line in reader.lines() {
            match line {
                Ok(msg) => println!("Serwer: {}", msg),
                Err(e) => {
                    println!("Błąd odczytu od serwera: {}", e);
                    break;
                }
            }
        }
    });

    // Wysyłanie wiadomości do serwera
    let stdin = std::io::stdin();
    for line in stdin.lock().lines() {
        let line = line?; // Operator propagacji błędów
        // Zakomentowano analizę i budowanie wiadomości
        /*
        if let Some(first_word) = line.split_whitespace().next() {
            let message = match first_word {
                "move" => {
                    let args: Vec<&str> = line.split_whitespace().collect();
                    if args.len() == 3 {
                        let from = parse_coords(args[1]);
                        let to = parse_coords(args[2]);
                        builder::MessageBuilder::new()
                            .kind("move")
                            .from(from)
                            .to(to)
                            .build()
                    } else {
                        builder::MessageBuilder::new()
                            .kind("error")
                            .error_code(2)
                            .error_message("Invalid number of arguments")
                            .build()
                    }
                }
                _ => {
                    builder::MessageBuilder::new()
                        .kind("error")
                        .error_code(1)
                        .error_message("Unknown command")
                        .build()
                }
            };
            println!("Wysyłanie: {}", message.get_content());
        }
        */
        // Wysyłanie wpisanego tekstu bez dodatkowej logiki
        stream.write_all(line.as_bytes())?;
        stream.write_all(b"\n")?; // Znak nowej linii wymagany przez serwer
    }
    Ok(())
}
