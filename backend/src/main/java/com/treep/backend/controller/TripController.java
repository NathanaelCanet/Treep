package com.treep.backend.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
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
        Trip trip = new Trip();
        
        trip.setDestination(tripDTO.getDestination());
        trip.setDateDebut(tripDTO.getDateDebut());
        trip.setDateFin(tripDTO.getDateFin());
        trip.setBudgetTotal(tripDTO.getBudgetTotal());

        System.out.println("Saving Trip: " + trip.getDestination()); // Log de vérification
        return tripRepo.save(trip);
    }
}