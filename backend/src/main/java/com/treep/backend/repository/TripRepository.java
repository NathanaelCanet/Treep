package com.treep.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treep.backend.model.Trip;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    
    // Recherche par destination
    List<Trip> findByDestinationContainingIgnoreCase(String destination);

    // Recherche par propriétaire (user id)
    List<Trip> findByUser_Id(Long userId);

    // Récupérer les voyages publics (isPrivate = false OU isPrivate = null)
    List<Trip> findByIsPrivateFalseOrIsPrivateIsNull();

    // Récupérer les voyages publics en excluant ceux d'un utilisateur
    List<Trip> findByUser_IdNotAndIsPrivateFalseOrUser_IdNotAndIsPrivateIsNull(Long userId1, Long userId2);

    // Recherche par destination parmi les voyages publics
    List<Trip> findByIsPrivateFalseOrIsPrivateIsNullAndDestinationContainingIgnoreCase(String destination);

    // Recherche par destination en excluant les voyages d'un utilisateur
    List<Trip> findByUser_IdNotAndIsPrivateFalseAndDestinationContainingIgnoreCaseOrUser_IdNotAndIsPrivateIsNullAndDestinationContainingIgnoreCase(Long userId1, String dest1, Long userId2, String dest2);
}