package com.treep.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityDTO {
    private String titre;
    private String description;
    private Double cout;
    private String statut;
}