package com.treep.frontend.controller;

import com.treep.frontend.model.Activity;
import com.treep.frontend.model.Trip;
import com.treep.frontend.service.ApiClientServices;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    @FXML private ListView<String> tripList;
    @FXML private ListView<HBox> activityDisplayList;
    @FXML private TextField destInput;
    @FXML private TextField priceInput;
    @FXML private DatePicker dateInput;
    @FXML private DatePicker dateFinInput;
    @FXML private TextField actTitleInput;
    @FXML private TextField actDescInput;
    @FXML private TextField actCostInput;
    @FXML private DatePicker actDateInput;
    @FXML private ComboBox<String> actStatusInput;
    @FXML private ListView<String> pendingActivitiesList;

    private final ApiClientServices api;
    private List<Trip> currentTrips = new ArrayList<>();
    private List<Activity> currentActivities = new ArrayList<>();
    private List<Activity> pendingActivities = new ArrayList<>();

    public DashboardController(ApiClientServices api) {
        this.api = api;
    }

    @FXML
    public void initialize() {
        refreshData();
        tripList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            int selectedIndex = tripList.getSelectionModel().getSelectedIndex();
            onTripSelected(selectedIndex);
        });
        if(actStatusInput != null) actStatusInput.setValue("To Do");
    }

    @FXML
    public void onRefresh() {
        refreshData();
    }

    @FXML
    public void onAddActivity() {
        try {
            String title = actTitleInput.getText().trim();
            if (title.isEmpty()) {
                showAlert("Erreur", "Le titre de l'activité est requis");
                return;
            }
            String desc = actDescInput.getText().isEmpty() ? "Description par défaut" : actDescInput.getText();
            double cost = actCostInput.getText().isEmpty() ? 0.0 : Double.parseDouble(actCostInput.getText());
            
            // --- ACTIVITÉ : On GARDE l'heure (backend: LocalDateTime) ---
            String date = (actDateInput.getValue() != null) 
                ? actDateInput.getValue().toString() + "T09:00:00" 
                : (dateInput.getValue() != null ? dateInput.getValue().toString() + "T09:00:00" : "2025-01-01T09:00:00");
            
            String status = actStatusInput.getValue();

            Activity act = new Activity(title, desc, cost, date, status);
            pendingActivities.add(act);

            pendingActivitiesList.getItems().add("• " + title + " (" + cost + "€)");

            actTitleInput.clear();
            actDescInput.clear();
            actCostInput.clear();
            actDateInput.setValue(null);
            actStatusInput.setValue("To Do");

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le coût doit être un nombre");
        }
    }

    @FXML
    public void onSubmit() {
        try {
            // --- CORRECTION VOYAGE : On ENLÈVE l'heure (backend: LocalDate) ---
            String dateDebut = (dateInput.getValue() != null) 
                ? dateInput.getValue().toString() // Juste la date YYYY-MM-DD
                : "2025-01-01";
                
            String dateFin = (dateFinInput.getValue() != null) 
                ? dateFinInput.getValue().toString() // Juste la date YYYY-MM-DD
                : dateDebut;
            // ------------------------------------------------------------------

            Double budget = Double.parseDouble(priceInput.getText());
            List<Activity> activityList = new ArrayList<>(pendingActivities);

            Trip tripToCreate = new Trip(null, destInput.getText(), dateDebut, dateFin, budget, new ArrayList<>());

            Trip createdTrip = api.addTrip(tripToCreate);
            
            if (createdTrip != null && createdTrip.getId() != null) {
                for (Activity activity : activityList) {
                    api.addActivity(createdTrip.getId(), activity);
                }
                refreshData();
                clearForm();
                
                // Petit message de succès
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Succès");
                info.setHeaderText(null);
                info.setContentText("Voyage créé avec succès !");
                info.showAndWait();
            } else {
                showAlert("Erreur", "Echec de création (Vérifiez les dates/budget).");
            }

        } catch (NumberFormatException e) {
            showAlert("Erreur de Saisie", "Le budget doit être un chiffre.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", e.getMessage());
        }
    }

    private void refreshData() {
        activityDisplayList.getItems().clear();
        tripList.getItems().clear();
        currentTrips = api.getAllTrips();

        if (currentTrips != null) {
            for (Trip t : currentTrips) {
                tripList.getItems().add(t.getDestination() + " (" + t.getBudgetTotal() + " €)");
            }
        }
    }

    private void onTripSelected(int index) {
        if (currentTrips == null || index < 0 || index >= currentTrips.size()) return;

        Trip selectedTrip = currentTrips.get(index);
        activityDisplayList.getItems().clear();
        
        currentActivities = api.getActivitiesForTrip(selectedTrip.getId().toString());
        
        if (currentActivities != null && !currentActivities.isEmpty()) {
            for (Activity activity : currentActivities) {
                Label actLabel = new Label("• " + activity.getTitre() + " - " + activity.getCout() + "€ [" + activity.getStatut() + "]");
                actLabel.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");
                activityDisplayList.getItems().add(new HBox(actLabel));
            }
        } else {
            Label empty = new Label("Aucune activité.");
            empty.setStyle("-fx-text-fill: gray;");
            activityDisplayList.getItems().add(new HBox(empty));
        }
    }

    private void clearForm() {
        destInput.clear();
        priceInput.clear();
        dateInput.setValue(null);
        dateFinInput.setValue(null);
        pendingActivities.clear();
        pendingActivitiesList.getItems().clear();
        actTitleInput.clear();
        actDescInput.clear();
        actCostInput.clear();
        actDateInput.setValue(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}