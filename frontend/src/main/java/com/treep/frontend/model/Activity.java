package com.treep.frontend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Setters et Getters
@NoArgsConstructor // Constructeur sans argument
@AllArgsConstructor // Constructeur avec argument
public class Activity {
    private Long id;
    private String titre;
    private String description;
    private Double cout;
    private String datePrevue;
    private String statut;
}