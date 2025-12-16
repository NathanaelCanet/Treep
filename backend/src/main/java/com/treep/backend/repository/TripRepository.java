package com.treep.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treep.backend.model.Trip;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    
    // Recherche par destination
    List<Trip> findByDestinationContainingIgnoreCase(String destination);
}