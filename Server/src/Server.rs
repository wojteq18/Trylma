use std::io::{BufRead, BufReader, Write};
use std::net::{TcpListener, TcpStream}; //protokol TCP
use std::sync::{Arc, Mutex};
use std::thread;

fn handle_client(stream: TcpStream, clients: Arc<Mutex<Vec<TcpStream>>>) //Arc - pozwala na wspoldzielenie listy miedzy wieloma watkami,
//Mutex - zapewnia bezpieczny dostep do listy klientow z roznych watkow, zapobiegajac konfliktom
{
    let mut reader = BufReader::new(&stream); //umozliwia latwiejsze i bardziej wydajne odczytywanie danych
    let mut buffer = String::new(); //zmienna do przechowywania pojedynczej wiadomosci od klienta

    loop
    {
        buffer.clear();
        match reader.read_line(&mut buffer)
        {
            Ok(0) => 
            {
                //klient zamknal polaczenie
                println!("Klient rozlaczyl sie: {}", stream.peer_addr().unwrap());
                break;
            }

            Ok(_) =>
            {
                //odczytano wiadomosc od klienta
                println!("Otrzymano wiadomosc: {}", buffer.trim()); //jezeli odczyt zakonczyl sie sukcesem, wiadomosc jest wypisywana w konsoli
                let mut client_guard = clients.lock().unwrap(); //uzyskuje dostep do listy klientow (zabezpieczonej mutexem)
                //lock() blokuje dostep do tej listy dla innych watkow, by uniknac konfliktow
                for client in client_guard.iter_mut()
                {
                    if client.peer_addr().unwrap() != stream.peer_addr().unwrap() //wiadomosc wysylana do kazdego klienta z wyjatkiem nadawcy (adresy ip sa porownywane)
                    {
                        //przekaz wiadomosc innym klientom
                        writeln!(client, "{}", buffer.trim()).unwrap_or_else(|e|
                        {
                            println!("Blad podczas wysylania wiadomosci: {}", e);
                        });
                        client.flush().unwrap_or_else(|e| //flush wymusza natychmiastoew wyslanie wiadomosci przez siec
                        {
                            println!("Blad podczas czyszczenia bufora: {}", e);
                        });
                    }
                }
            }
            Err(e) => {
                println!("Blad odczytu od klienta: {}", e);
                break;
            }
        }
    }

    //po zakonczeniu polaczenia, klient jest usuwany z listy
    let mut clients_guard = clients.lock().unwrap();
    clients_guard.retain(|c| c.peer_addr().unwrap() != stream.peer_addr().unwrap());
    println!("Klient usuniety");
}


fn main() -> std::io::Result<()>
{
    let listener = TcpListener::bind("127.0.0.1:9999")?; //nasluchuje na porcie 9999, znak ? sprawdza czy zakonczono powodzeniem
    println!("Serwer uruchomiony na 127.0.0.1:9999");

    //wspoldzielona lista klientow
    let clients = Arc::new(Mutex::new(Vec::new())); //Arc pozwala na wspoldzielenie listy klientow miedzy wieloma watkami

    for stream in listener.incoming() {
        match stream {
            Ok(stream) => {
                println!("Nowe połączenie: {}", stream.peer_addr().unwrap());
                let clients = Arc::clone(&clients); // kopia referencji do listy klientów
    
                // Dodaj klienta do listy
                {
                    let mut clients_guard = clients.lock().unwrap();
                    clients_guard.push(stream.try_clone().unwrap()); // tworzy kopię strumienia
                }
    
                // Uruchom nowy wątek do obsługi klienta
                thread::spawn(move || {
                    handle_client(stream, clients); // obsługa każdego klienta w osobnym wątku
                });
            }
            Err(e) => {
                println!("Błąd połączenia: {}", e);
            }
        }
    }    
    Ok(()) //program zakonczyl sie pomyslnie
}