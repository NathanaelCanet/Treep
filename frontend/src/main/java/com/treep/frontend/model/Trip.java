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
    private Long id;
    private String destination;
    private String dateDebut;
    private String dateFin;
    private Double budgetTotal;
    private List<Activity> activities = new ArrayList<>();
}