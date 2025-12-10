package com.treep.frontend.api;

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

public class ApiClient {

    // TODO: Port backend Docker
    private static final String BASE_URL = "http://localhost:8080/api/trips";

    private final HttpClient client;
    private final ObjectMapper mapper;

    public ApiClient() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    // INFO: GET - Récupérer tous les voyages
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
            System.err.println("DEBUG: Impossible de joindre le backend -> " + e.getMessage());
        }
        return Collections.emptyList();
    }

    // INFO: GET - Récupérer les activités d'un voyage donne
    public List<Activity> getActivitiesForTrip(String tripId) {
        try {
            // Construit l'URL complète avec l'ID du voyage (tripId)
            String url = "http://localhost:8080/api/activities/trip/" + tripId;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), new TypeReference<List<Activity>>(){});
            }
        } catch (Exception e) {
            System.err.println("DEBUG: Impossible de joindre le backend pour les activités -> " + e.getMessage());
        }
        return Collections.emptyList();
    }

    // INFO: POST - Créer un nouveau voyage
    public boolean addTrip(Trip trip) {
        try {
            String json = mapper.writeValueAsString(trip);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200 || response.statusCode() == 201;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}