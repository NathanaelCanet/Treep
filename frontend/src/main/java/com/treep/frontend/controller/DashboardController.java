package com.treep.frontend.controller;

import com.treep.frontend.service.ApiClientServices;
import com.treep.frontend.model.Activity;
import com.treep.frontend.model.Trip;
import com.treep.frontend.service.ApiClientServices;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    @FXML private ListView<String> tripList;
    @FXML private ListView<String> activityDisplayList;
    @FXML private TextField destInput;
    @FXML private TextField priceInput;
    @FXML private DatePicker dateInput;
    @FXML private TextField activitiesInput;

    private final ApiClient api = new ApiClient();
    private List<Trip> currentTrips = new ArrayList<>();

    @FXML
    public void initialize() {
        refreshData();

        // INFO: Ajout d'un listener pour charger les activités quand on sélectionne un voyage
        tripList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            int selectedIndex = tripList.getSelectionModel().getSelectedIndex();
            onTripSelected(selectedIndex);
        });
    }

    @FXML
    public void onRefresh() {
        refreshData();
    }

    @FXML
    public void onSubmit() {
        try {
            List<Activity> activityList = new ArrayList<>();
            if (activitiesInput.getText() != null && !activitiesInput.getText().isBlank()) {
                String[] rawActivities = activitiesInput.getText().split(",");
                for (String actTitle : rawActivities) {
                    Activity act = new Activity(
                            actTitle.trim(),
                            "Description par défaut",
                            0.0,
                            "2025-01-01",
                            "To Do"
                    );
                    activityList.add(act);
                }
            }

            String dateDebut = (dateInput.getValue() != null) ? dateInput.getValue().toString() : "2025-01-01";
            String dateFin = dateDebut; // TODO: Ajouter un champ date fin plus tard
            Double budget = Double.parseDouble(priceInput.getText());

            Trip newTrip = new Trip(
                    null,
                    destInput.getText(),
                    dateDebut,
                    dateFin,
                    budget,
                    activityList
            );

            if (api.addTrip(newTrip)) {
                refreshData();
                clearForm();
            } else {
                showAlert("Erreur", "Le backend ne répond pas !");
            }

        } catch (NumberFormatException e) {
            showAlert("Erreur de Saisie", "Le budget doit être un chiffre (utilisez le point .)");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", e.getMessage());
        }
    }

    private void refreshData() {
        activityDisplayList.getItems().clear();
        tripList.getItems().clear();
        List<Trip> trips = api.getAllTrips();

        for (Trip t : trips) {
            String label = t.getDestination() + " (" + t.getBudgetTotal() + " €)";
            tripList.getItems().add(label);
        }
    }

    private void onTripSelected(int index) {
        if (index < 0 || index >= currentTrips.size()) {
            return;
        }

        Trip selectedTrip = currentTrips.get(index);
        activityDisplayList.getItems().clear();
        activityDisplayList.getItems().add("Chargement des activités...");

        // INFO: On utilise la nouvelle méthode de l'API
        List<Activity> activities = api.getActivitiesForTrip(selectedTrip.getId().toString());
        activityDisplayList.getItems().clear();
        activities.forEach(activity -> activityDisplayList.getItems().add(activity.getName() + " (" + activity.getPrice() + "€)"));
    }

    private void clearForm() {
        destInput.clear();
        priceInput.clear();
        activitiesInput.clear();
        dateInput.setValue(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}