package com.treep.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.treep.backend.model.Activity;
import com.treep.backend.model.ActivityDTO;
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

    @PostMapping("/trip/{tripId}")
    public Activity createActivite(@PathVariable Long tripId, @RequestBody ActivityDTO activityDTO) {
        Trip trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voyage introuvable"));
        
        Activity activity = new Activity();
        activity.setTitre(activityDTO.getTitre());
        activity.setDescription(activityDTO.getDescription());
        activity.setCout(activityDTO.getCout());
        activity.setStatut(activityDTO.getStatut());

        activity.setDatePrevue(LocalDateTime.now());

        activity.setTrip(trip);
        return activityRepo.save(activity);
    }
}