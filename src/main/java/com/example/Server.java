package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server
{
    private static final int PORT = 12345;
    private ServerSocket serverSocket; //serversocket jest uzywany do nasluchiwania na konkretnym porcie i akceptowanie polaczen od klientow
    private ExecutorService threadPool; //executor service jest interfejsem, ktory zapewnia metody do zarzadzania zadaniami w wielowatkowym srodowisku
    private GameManager gameManager;

    public Server() throws IOException
    {
        serverSocket = new ServerSocket(PORT);
        threadPool = Executors.newFixedThreadPool(6); //Maksymalnie 6 graczy
    }
}
