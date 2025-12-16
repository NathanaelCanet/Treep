package com.treep.frontend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class Trip {
    private Long id;
    private String destination;

    @JsonProperty("date_debut")
    private String dateDebut;

    @JsonProperty("date_fin")
    private String dateFin;

    @JsonProperty("budget_total")
    private Double budgetTotal;

    private List<Activity> activities = new ArrayList<>();

    public Trip() {}

    public Trip(Long id, String destination, String dateDebut, String dateFin, Double budgetTotal, List<Activity> activities) {
        this.id = id;
        this.destination = destination;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.budgetTotal = budgetTotal;
        this.activities = activities;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getDateDebut() { return dateDebut; }
    public void setDateDebut(String dateDebut) { this.dateDebut = dateDebut; }

    public String getDateFin() { return dateFin; }
    public void setDateFin(String dateFin) { this.dateFin = dateFin; }

    public Double getBudgetTotal() { return budgetTotal; }
    public void setBudgetTotal(Double budgetTotal) { this.budgetTotal = budgetTotal; }

    public List<Activity> getActivities() { return activities; }
    public void setActivities(List<Activity> activities) { this.activities = activities; }
}