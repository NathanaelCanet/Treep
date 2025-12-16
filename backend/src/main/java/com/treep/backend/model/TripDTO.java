package com.treep.backend.model;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TripDTO {
    private String destination;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Double budgetTotal;
}