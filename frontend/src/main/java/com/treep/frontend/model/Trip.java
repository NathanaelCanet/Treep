package com.treep.frontend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data // Setters et Getters
@NoArgsConstructor // Constructeur sans argument
@AllArgsConstructor // Constructeur avec argument
public class Trip {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("dateDebut")
    private String dateDebut;

    @JsonProperty("dateFin")
    private String dateFin;

    @JsonProperty("budgetTotal")
    private Double budgetTotal;

    @JsonProperty("activities")
    private List<Activity> activities = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("user")
    private User user;

    @JsonProperty("isPrivate")
    private Boolean isPrivate = false;

    // Champ local (non envoyé au backend) pour savoir si c'est un favori
    private boolean isFavorite = false;
}