package com.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;


@SpringBootApplication(scanBasePackages = {"com.example", "com.example.DB"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
