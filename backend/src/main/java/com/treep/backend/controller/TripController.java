package com.treep.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.treep.backend.model.Trip;
import com.treep.backend.repository.TripRepository;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    @Autowired
    private TripRepository tripRepo;

    @GetMapping
    public List<Trip> getAllTrips() {
        return tripRepo.findAll();
    }

    @PostMapping
    public Trip createTrip(@RequestBody Trip trip) {
        // Lier chaque activité au trip 
        if (trip.getActivities() != null) {
            for (var activity : trip.getActivities()) {
                activity.setTrip(trip);
            }
        }
        return tripRepo.save(trip);
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