package com.treep.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.treep.backend.model.Activity;
import com.treep.backend.model.Trip;
import com.treep.backend.repository.ActivityRepository;
import com.treep.backend.repository.TripRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityRepository activityRepo;
    private final TripRepository tripRepo;

    // Récupérer les activités d'un voyage spécifique (pour JavaFX)
    @GetMapping("/trip/{tripId}")
    public List<Activity> getByVoyageId(@PathVariable Long tripId) {
        return activityRepo.findByTripId(tripId);
    }

    // Créer une activité liée à un voyage
    @PostMapping("/trip/{tripId}")
    public Activity createActivite(@PathVariable Long tripId, @RequestBody Activity activity) {
        Trip trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voyage introuvable"));
        
        activity.setTrip(trip);
        return activityRepo.save(activity);
    }
}