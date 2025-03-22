# Trylma – Chinese Checkers (Client-Server)

**Trylma** is a networked version of Chinese Checkers (also known as "Trylma"), built with a **client-server architecture**. 
The project allows multiple human players to play together or play against a simple bot.

## Features

-  Play with other human players over the network  
-  Play against a bot if no other players are available  
-  Save and load previous games using an SQL database  
-  Watch replays of finished games  
-  Server-side logic and game coordination written in **Rust**  
-  Client built using **Spring Framework** in **Java**  
-  Secure and efficient database integration using SQL  

## Architecture

This project follows a **client-server model**:

- **Server**: Written in Rust. Handles game logic, player communication, move validation, and replay recording.
- **Client**: Written in Java using Spring. Provides the user interface and interacts with the server and the database.
- **Database**: Stores saved games and replays. Used by the client to allow users to review or continue past games.

## Technologies Used

- Rust (server)
- Java + Spring Framework (client)
- SQL (database operations)

This project was created for educational purposes at **Wrocław University of Technology**, in collaboration with [@Siemoniere](https://github.com/Siemoniere).
