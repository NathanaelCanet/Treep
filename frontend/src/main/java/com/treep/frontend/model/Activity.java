package com.treep.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Activity {
    
    private Long id;
    private String titre;
    private String description;
    private Double cout;
    private String datePrevue;
    private String statut;

    public Activity() {}

    public Activity(String titre, String description, Double cout, String statut) {
        this.titre = titre;
        this.description = description;
        this.cout = cout;
        this.statut = statut;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getCout() { return cout; }
    public void setCout(Double cout) { this.cout = cout; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}