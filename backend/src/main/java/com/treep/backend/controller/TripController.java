package com.treep.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.treep.backend.model.Trip;
import com.treep.backend.repository.TripRepository;
import com.treep.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor

public class TripController {
    private final TripRepository tripRepo;
    private final UserRepository userRepo;

    @GetMapping
    public List<Trip> getAllTrips(@RequestParam(required = false) Long excludeUserId) {
        // Retourne uniquement les voyages publics (non privés ou NULL)
        if (excludeUserId != null) {
            // Exclure les voyages de l'utilisateur
            return tripRepo.findByUser_IdNotAndIsPrivateFalseOrUser_IdNotAndIsPrivateIsNull(excludeUserId, excludeUserId);
        }
        return tripRepo.findByIsPrivateFalseOrIsPrivateIsNull();
    }

    @GetMapping("/search")
    public List<Trip> searchTrips(@RequestParam String destination, @RequestParam(required = false) Long excludeUserId) {
        // Recherche uniquement parmi les voyages publics
        if (excludeUserId != null) {
            return tripRepo.findByUser_IdNotAndIsPrivateFalseAndDestinationContainingIgnoreCaseOrUser_IdNotAndIsPrivateIsNullAndDestinationContainingIgnoreCase(excludeUserId, destination, excludeUserId, destination);
        }
        return tripRepo.findByIsPrivateFalseOrIsPrivateIsNullAndDestinationContainingIgnoreCase(destination);
    }

    @PostMapping
    public Trip createTrip(@RequestBody Trip trip) {
        // Lier chaque activité au trip 
        if (trip.getActivities() != null) {
            for (var activity : trip.getActivities()) {
                activity.setTrip(trip);
            }
        }
        // Lier le user si présent dans le JSON
        if (trip.getUser() != null && trip.getUser().getId() != null) {
            userRepo.findById(trip.getUser().getId()).ifPresentOrElse(
                trip::setUser,
                () -> { throw new RuntimeException("Utilisateur avec id=" + trip.getUser().getId() + " introuvable"); }
            );
        }
        return tripRepo.save(trip);
    }

    @GetMapping("/user/{userId}")
    public List<Trip> getTripsByUser(@PathVariable Long userId) {
        return tripRepo.findByUser_Id(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteTrip(@PathVariable Long id) {
        tripRepo.deleteById(id);
    }

    @DeleteMapping
    public void deleteTrip(@RequestBody Trip trip) {
        tripRepo.delete(trip);
    }
}