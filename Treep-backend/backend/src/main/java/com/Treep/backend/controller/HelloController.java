package com.Treep.backend.controller;

import com.Treep.backend.model.StatusResponse;
import com.Treep.backend.service.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "*") // Permet les requêtes depuis le frontend
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/")
    public String home() {
        return helloService.getHomeMessage();
    }

    @GetMapping("/api/hello")
    public String hello() {
        return helloService.getHelloMessage();
    }

    @GetMapping("/api/status")
    public StatusResponse status() {
        return helloService.getAppStatus();
    }
}