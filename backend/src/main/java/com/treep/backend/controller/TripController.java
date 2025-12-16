package com.treep.backend.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.treep.backend.model.Trip;
import com.treep.backend.model.TripDTO;
import com.treep.backend.repository.TripRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripRepository tripRepo;

    @GetMapping
    public List<Trip> getAllTrips() {
        return tripRepo.findAll();
    }

    @PostMapping
    public Trip createTrip(@RequestBody TripDTO tripDTO) {
        try {
            Trip trip = new Trip();
            trip.setDestination(tripDTO.getDestination());
            System.out.println("Saving trip: " + trip);
            Trip saved = tripRepo.save(trip);
            System.out.println("Saved trip: " + saved);
            return saved;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}