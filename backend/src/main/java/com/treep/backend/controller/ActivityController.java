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
import com.treep.backend.model.ActivityDTO; // Import du DTO
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

    @GetMapping("/trip/{tripId}")
    public List<Activity> getByVoyageId(@PathVariable Long tripId) {
        return activityRepo.findByTripId(tripId);
    }

    // --- MISE A JOUR ICI : Utilisation de ActivityDTO ---
    @PostMapping("/trip/{tripId}")
    public Activity createActivite(@PathVariable Long tripId, @RequestBody ActivityDTO activityDTO) {
        
        Trip trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voyage introuvable"));
        
        // Conversion DTO -> Entity
        Activity activity = new Activity();
        activity.setTitre(activityDTO.getTitre());
        activity.setDescription(activityDTO.getDescription());
        activity.setCout(activityDTO.getCout());
        activity.setDatePrevue(activityDTO.getDatePrevue());
        activity.setStatut(activityDTO.getStatut());
        
        // Liaison
        activity.setTrip(trip);
        
        return activityRepo.save(activity);
    }
}