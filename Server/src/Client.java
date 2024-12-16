import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client 
{
    public static void main(String[] args) {
        try
        {

        
            Socket socket = new Socket("127.0.0.1", 9999);
            System.out.println("Polaczono z serwerem");

            //tworzy klon strumienia dla watku odbierajacaego wiadomosci od serwera
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //tworzy watek odbierajacy wiadomosci od serwera
            Thread t = new Thread(() -> {
                try {
                    String msg;
                    while((msg = serverInput.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (Exception e) {
                    System.out.println("Blad odczytu od serwera " + e.getMessage());
                }
            });

            t.start();

            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                String input = scanner.nextLine();
                output.println(input); //wyslanie tesktu de serwera 
                if ("exit".equals(input)) {
                    break;
                }
            }
            socket.close();
            scanner.close();
            System.out.println("Rozlaczono z serwerem");
        }
        catch (IOException e) {
            System.out.println("Blad polaczenia z serwerem " + e.getMessage());
        }
    }

}