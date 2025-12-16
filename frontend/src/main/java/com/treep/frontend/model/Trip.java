package com.treep.frontend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data //Setters et Getters
@NoArgsConstructor // Constructeur sans argument
@AllArgsConstructor // Constructeur avec argument
public class Trip {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("destination")
    private String destination;
    private String dateDebut;
    private String dateFin;
    private Double budgetTotal;
<<<<<<< HEAD
=======

    @JsonProperty("activities")
>>>>>>> 50cae20 (feat(frontend): Mappage des des différents modeles)
    private List<Activity> activities = new ArrayList<>();
}