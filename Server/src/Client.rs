use std::io::{BufReader, BufRead, Write};
use std::net::{TcpListener, TcpStream};
use std::sync::{Arc, Mutex};
use std::thread;

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
        if(line == "exit")
        {
            println!("Zamykanie polaczenia");
            break;
        }
        stream.write_all(line.as_bytes());
        stream.write_all(b"\n")?; //znak nowej lini, wyamagny przez serwer
    }
    Ok(())
}