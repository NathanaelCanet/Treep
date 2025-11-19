package com.Treep.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "*") // Permet les requêtes depuis le frontend
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "Bienvenue sur l'API Treep Backend !";
    }

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello from Treep Backend API!";
    }

    @GetMapping("/api/status")
    public String status() {
        return "{\"status\":\"OK\",\"message\":\"Treep Backend is running\"}";
    }
}