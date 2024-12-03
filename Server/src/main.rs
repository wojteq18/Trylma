use std::io::{BufRead, Write};

fn main() 
{
    let listener = std::net::TcpListener::bind("127.0.0.1:9999").unwrap(); //adres localhosta
    for mut stream in listener.incoming().flatten() //flatten pomija potencjalne bledy 
    {
        let mut rdr = std::io::BufReader::new(&mut stream);
        loop
        {
            let mut l = String::new();
            rdr.read_line(&mut l);
            if l.trim().is_empty() { break; }
            println!("{}", l);
        }
        stream.write_all(b"HTTP/1.1 200 OK\r\n\r\nHello").unwrap();
    }
}
