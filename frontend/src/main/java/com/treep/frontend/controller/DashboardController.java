package com.treep.frontend.controller;

import com.treep.frontend.model.Activity;
import com.treep.frontend.model.Trip;
import com.treep.frontend.service.ApiClientServices;
import com.treep.frontend.service.AuthService;
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
    @FXML private DatePicker dateFinInput;
    @FXML private TextField activityInput;
    @FXML private ListView<String> activityListView;

    private final ApiClientServices api = ApiClientServices.getInstance();
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
            String dateFin = (dateFinInput.getValue() != null) ? dateFinInput.getValue().toString() : dateDebut;
            
            // Vérifier que la date de fin n'est pas avant la date de début
            if (dateInput.getValue() != null && dateFinInput.getValue() != null && dateFinInput.getValue().isBefore(dateInput.getValue())) {
                showAlert("Erreur", "La date de fin ne peut pas être avant la date de début.");
                return;
            }
            
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

            Double budget = Double.parseDouble(priceInput.getText());

                Trip newTrip = new Trip(
                    null,
                    destInput.getText(),
                    dateDebut,
                    dateFin,
                    budget,
                    activityList,
                    AuthService.getCurrentUser() // Associe l'utilisateur courant
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