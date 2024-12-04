use std::io::{BufReader, BufRead, Write};
use std::net::{TcpListener, TcpStream};
use std::sync::{Arc, Mutex};
use std::thread;
mod Message;
use Message::move_message;
use Message::error_message;
use Message::builder;

fn main() -> std::io::Result<()>
{
    //laczenie z serwerem
    let mut stream = TcpStream::connect("127.0.0.1:9999")?;
    println!("Polaczono z serwerem");

    //odbieranie wiadomosci od serwera
    let mut stream_clone = stream.try_clone()?; //tworzenie klonu strumienia, by odbierac wiadomosci od serwera
    thread::spawn(move || 
    {
        let reader = BufReader::new(&mut stream_clone);
        for line in reader.lines()
        {
            match line
            {
                Ok(msg) => println!("Serwer: {}", msg),
                Err(e) => 
                {
                    println!("Blad odczytu od serwera: {}", e);
                    break;
                }
            }
        }
    });
    
    //wysylanie wiadomosci do serwera
    let stdin = std::io::stdin();
    for line in stdin.lock().lines()
    {
        let line = line?; //operator propagacji bledow
        if let Some(first_word) = line.split_whitespace().next() //pobranie pierwszego slowa z linii
        {
            let message = match first_word
            {
                "move" => {
                    let from = (0, 0);
                    let to = (1, 1);

                    builder::MessageBuilder::new()
                        .kind("move")
                        .from(from)
                        .to(to)
                        .build()
                }
                _ => {
                    builder::MessageBuilder::new()
                        .kind("error")
                        .error_code(1)
                        .error_message("Unknown command")
                        .build()
                }
            };
            println!("Wysylanie: {}", message.get_content());
        }
        stream.write_all(line.as_bytes());
        stream.write_all(b"\n")?; //znak nowej lini, wyamagny przez serwer
    }
    Ok(())
}