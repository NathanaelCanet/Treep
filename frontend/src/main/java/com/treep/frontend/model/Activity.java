package com.treep.frontend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Setters et Getters
@NoArgsConstructor // Constructeur sans argument
@AllArgsConstructor // Constructeur avec argument
public class Activity {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("titre")
    private String titre;

    @JsonProperty("description")
    private String description;
    private Double cout;
    private String datePrevue;

    @JsonProperty("statut")
    private String statut;
}