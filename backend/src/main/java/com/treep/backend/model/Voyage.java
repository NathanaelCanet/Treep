package com.treep.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Voyage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String destination;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Double budgetTotal;


    @OneToMany(mappedBy = "voyage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Activite> activites = new ArrayList<>();
    
    public void addActivite(Activite activite) {
        activites.add(activite);
        activite.setVoyage(this);
    }
}