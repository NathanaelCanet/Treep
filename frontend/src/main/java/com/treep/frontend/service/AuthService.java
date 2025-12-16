package com.treep.frontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.treep.frontend.model.Role;
import com.treep.frontend.model.User;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthService {

    private static final String BASE_URL = "http://localhost:8080/api/users";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Stocke l'utilisateur connecté
    private static User currentUser = null;

    public User login(String login, String password) throws Exception {
        User loginRequest = new User();
        loginRequest.setLogin(login);
        loginRequest.setPassword(password);
        
        String json = objectMapper.writeValueAsString(loginRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            currentUser = objectMapper.readValue(response.body(), User.class);
            return currentUser;
        }
        return null;
    }

    public boolean register(String login, String password) throws Exception {
        User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(password);
        newUser.setRole(Role.USER);
        
        String json = objectMapper.writeValueAsString(newUser);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.statusCode() == 200 || response.statusCode() == 201;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }
}
