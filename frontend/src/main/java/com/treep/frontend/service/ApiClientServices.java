package com.treep.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.treep.frontend.model.Activity;
import com.treep.frontend.model.Trip;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class ApiClientServices {
    private static final String BASE_URL = "http://localhost:8080/api/trips";
    private final HttpClient client;
    private final ObjectMapper mapper;

    public ApiClientServices() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public List<Trip> getAllTrips() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), new TypeReference<List<Trip>>(){});
            }
        } catch (Exception e) {
            System.err.println("DEBUG: Erreur GET -> " + e.getMessage());
        }
        return Collections.emptyList();
    }

    // GET - Récupérer les activités d'un voyage donné
    public List<Activity> getActivitiesForTrip(String tripId) {
        try {
            String url = "http://localhost:8080/api/activities/trip/" + tripId;
            System.out.println("DEBUG: GET activities for trip " + tripId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("DEBUG: Activities response status = " + response.statusCode());

            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), new TypeReference<List<Activity>>(){});
            }
        } catch (Exception e) {
            System.err.println("DEBUG: Erreur GET activities -> " + e.getMessage());
        }
        return Collections.emptyList();
    }

    public boolean addTrip(Trip trip) {
        try {
            String json = mapper.writeValueAsString(trip);
            System.out.println("DEBUG: Sending POST to " + BASE_URL);
            System.out.println("DEBUG: JSON payload = " + json);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("DEBUG: Response status = " + response.statusCode());
            System.out.println("DEBUG: Response body = " + response.body());
            
            return response.statusCode() == 200 || response.statusCode() == 201;
        } catch (Exception e) {
            System.err.println("DEBUG: POST error -> " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}