package com.treep.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.treep.frontend.model.Activity;
import com.treep.frontend.model.Trip;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class ApiClientServices {
    private static final String BASE = System.getProperty(
        "treep.api.base", System.getenv().getOrDefault("TREEP_API_BASE_URL", "http://localhost:8080"));
    private static final String TRIPS = BASE + "/api/trips";
    private static final String ACTIVITIES_BY_TRIP = BASE + "/api/activities/trip/";
    
    private final HttpClient client;
    private final ObjectMapper mapper;

    public ApiClientServices() {
        this.client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
        this.mapper = new ObjectMapper();
    }

    public List<Trip> getAllTrips() {
        try {
            System.out.println("[API DEBUG] Getting all trips from: " + TRIPS);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TRIPS))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), new TypeReference<List<Trip>>(){});
            } else {
                System.err.println("[API ERROR] getAllTrips Status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("[API ERROR] Erreur GET -> " + e.getMessage());
        }
        return Collections.emptyList();
    }

    // GET - Récupérer les activités d'un voyage donné
    public List<Activity> getActivitiesForTrip(String tripId) {
        try {
            String url = ACTIVITIES_BY_TRIP + tripId;
            System.out.println("[API DEBUG] GET activities for trip " + tripId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), new TypeReference<List<Activity>>(){});
            } else {
                System.out.println("[API ERROR] getActivities Status: " + response.statusCode());
                System.out.println("[API ERROR] Body: " + response.body());
            }
        } catch (Exception e) {
            System.err.println("[API ERROR] Erreur GET activities -> " + e.getMessage());
        }
        return Collections.emptyList();
    }

    public Trip addTrip(Trip trip) {
        try {
            String json = mapper.writeValueAsString(trip);
            System.out.println("[API DEBUG] Adding trip: " + json);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TRIPS))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("[API DEBUG] AddTrip Status: " + response.statusCode());
            
            if (response.statusCode() == 200 || response.statusCode() == 201) {
                return mapper.readValue(response.body(), Trip.class);
            } else {
                System.err.println("[API ERROR] Failed to add trip. Body: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Activity addActivity(Long tripId, Activity activity) {
        try {
            String json = mapper.writeValueAsString(activity);
            String url = ACTIVITIES_BY_TRIP + tripId;
            System.out.println("[API DEBUG] Adding activity to " + url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200 || response.statusCode() == 201) {
                return mapper.readValue(response.body(), Activity.class);
            } else {
                System.err.println("[API ERROR] Failed to add activity. Status: " + response.statusCode());
                System.err.println("[API ERROR] Body: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteTrip(Long tripId) {
        try {
            String url = TRIPS + "/" + tripId;
            System.out.println("[API DEBUG] TENTATIVE DELETE TRIP: " + url);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // --- AFFICHE LA REPONSE DU SERVEUR ---
            System.out.println("[API DEBUG] Response Code: " + response.statusCode()); 
            System.out.println("[API DEBUG] Response Body: " + response.body()); 
            // -------------------------------------

            return response.statusCode() == 200 || response.statusCode() == 204;
        } catch (Exception e) {
            System.err.println("[API CRASH] Erreur durant deleteTrip");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteActivity(Long activityId) {
        try {
            String url = BASE + "/api/activities/" + activityId;
            System.out.println("[API DEBUG] TENTATIVE DELETE ACTIVITY: " + url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("[API DEBUG] Response Code: " + response.statusCode());
            System.out.println("[API DEBUG] Response Body: " + response.body());

            return response.statusCode() == 200 || response.statusCode() == 204;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}