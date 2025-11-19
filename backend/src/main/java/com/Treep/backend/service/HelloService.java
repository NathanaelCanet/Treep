package com.Treep.backend.service;

import com.Treep.backend.model.StatusResponse;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String getHomeMessage() {
        return "Bienvenue sur l'API Treep Backend !";
    }

    public String getHelloMessage() {
        return "Hello from Treep Backend API!!!";
    }

    public StatusResponse getAppStatus() {
        return new StatusResponse("OK", "Treep Backend is running");
    }
}
