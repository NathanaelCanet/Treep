package com.treep.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treep.backend.model.Trip;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
}