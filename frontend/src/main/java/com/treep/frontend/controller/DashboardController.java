package com.treep.frontend.controller;

import com.treep.frontend.model.Activity;
import com.treep.frontend.model.Trip;
import com.treep.frontend.service.ApiClientServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    @FXML private TextField destInput;
    @FXML private TextField priceInput;
    @FXML private DatePicker dateInput;
    @FXML private TextField activityInput;
    @FXML private ListView<String> activityListView;

    private final ApiClientServices api = new ApiClientServices();
    private final ObservableList<String> activities = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        activityListView.setItems(activities);
        
        // Double-clic pour supprimer une activité
        activityListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                int selectedIndex = activityListView.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0) {
                    activities.remove(selectedIndex);
                }
            }
        });
    }

    @FXML
    public void onAddActivity() {
        String activityName = activityInput.getText();
        if (activityName != null && !activityName.isBlank()) {
            activities.add(activityName.trim());
            activityInput.clear();
            activityInput.requestFocus();
        }
    }

    @FXML
    public void onSubmit() {
        try {
            String dateDebut = (dateInput.getValue() != null) ? dateInput.getValue().toString() : "2025-01-01";
            
            List<Activity> activityList = new ArrayList<>();
            for (String actName : activities) {
                Activity act = new Activity(
                        null,
                        actName,
                        "Description par défaut",
                        0.0,
                        dateDebut + "T10:00:00",
                        "To Do"
                );
                activityList.add(act);
            }

            String dateFin = dateDebut;
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
                Stage stage = (Stage) destInput.getScene().getWindow();
                stage.close();
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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}