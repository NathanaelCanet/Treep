package com.treep.frontend.ui;

import com.treep.frontend.api.ApiClient;
import com.treep.frontend.model.Activity;
import com.treep.frontend.model.Trip;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    // INFO: Liens avec le fichier FXML
    @FXML private ListView<String> tripList;
    @FXML private TextField destInput;
    @FXML private TextField priceInput;
    @FXML private DatePicker dateInput;
    @FXML private TextField activitiesInput; // Ex: "Plongée, Tennis"

    private final ApiClient api = new ApiClient();

    @FXML
    public void initialize() {
        // INFO: Chargement des données au démarrage
        refreshData();
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
                for (String act : rawActivities) {
                    // DEBUG: On met un prix par défaut de 0 pour l'activité pour l'instant
                    activityList.add(new Activity(act.trim(), 0.0));
                }
            }

            Trip newTrip = new Trip(
                    null, // L'ID sera généré par le backend
                    destInput.getText(),
                    Double.parseDouble(priceInput.getText()),
                    (dateInput.getValue() != null) ? dateInput.getValue().toString() : "N/A",
                    activityList
            );

            if (api.addTrip(newTrip)) {
                refreshData();
                clearForm();
            } else {
                showAlert("Erreur", "Le backend ne répond pas !");
            }

        } catch (NumberFormatException e) {
            showAlert("Erreur de Saisie", "Le prix doit être un chiffre (utilisez le point .)");
        } catch (Exception e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    private void refreshData() {
        tripList.getItems().clear();
        // INFO: Appel API asynchrone simulé
        List<Trip> trips = api.getAllTrips();

        for (Trip t : trips) {
            StringBuilder sb = new StringBuilder();
            sb.append(t.getDestination()).append(" (").append(t.getPrice()).append("€)");

            // Affichage propre des activités
            if (!t.getActivities().isEmpty()) {
                sb.append("\n  ↳ Activités: ");
                t.getActivities().forEach(a -> sb.append(a.getName()).append(", "));
                // Retire la dernière virgule
                sb.setLength(sb.length() - 2);
            }
            tripList.getItems().add(sb.toString());
        }
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