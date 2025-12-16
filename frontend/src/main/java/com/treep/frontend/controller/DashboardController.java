package com.treep.frontend.controller;

import com.treep.frontend.model.Activity;
import com.treep.frontend.model.Trip;
import com.treep.frontend.service.ApiClientServices;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    @FXML private TextField destInput;
    @FXML private TextField priceInput;
    @FXML private DatePicker dateInput;
    @FXML private TextField activitiesInput;

    private final ApiClientServices api = new ApiClientServices();

    @FXML
    public void onSubmit() {
        try {
            List<Activity> activityList = new ArrayList<>();
            String dateDebut = (dateInput.getValue() != null) ? dateInput.getValue().toString() : "2025-01-01";
            
            if (activitiesInput.getText() != null && !activitiesInput.getText().isBlank()) {
                String[] rawActivities = activitiesInput.getText().split(",");
                for (String actTitle : rawActivities) {
                    Activity act = new Activity(
                            null,
                            actTitle.trim(),
                            "Description par défaut",
                            0.0,
                            dateDebut + "T10:00:00",
                            "To Do"
                    );
                    activityList.add(act);
                }
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
                // Fermer la fenêtre après création
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