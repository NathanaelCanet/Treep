package com.treep.frontend.controller;

import com.treep.frontend.model.Activity;
import com.treep.frontend.model.Trip;
import com.treep.frontend.service.ApiClientServices;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TripCardController {

    @FXML private VBox cardRoot;
    @FXML private Label destinationLabel;
    @FXML private Label datesLabel;
    @FXML private Button budgetButton;
    @FXML private Button deleteButton;
    @FXML private VBox activitiesContainer;

    private Trip trip;
    private Runnable onDeleteCallback;
    private final ApiClientServices api = new ApiClientServices();

    /**
     * Initialise la carte avec les données du voyage
     */
    public void setTrip(Trip trip) {
        this.trip = trip;
        
        // Header
        destinationLabel.setText(trip.getDestination());
        datesLabel.setText(trip.getDateDebut() + " ➔ " + trip.getDateFin());
        budgetButton.setText(trip.getBudgetTotal() + " €");

        // Activités
        activitiesContainer.getChildren().clear();
        
        if (trip.getActivities() == null || trip.getActivities().isEmpty()) {
            Label emptyLabel = new Label("Aucune activité planifiée.");
            emptyLabel.getStyleClass().addAll("lbl", "lbl-warning");
            activitiesContainer.getChildren().add(emptyLabel);
        } else {
            for (Activity act : trip.getActivities()) {
                Label actLabel = new Label("• " + act.getTitre());
                actLabel.getStyleClass().addAll("lbl", "lbl-default");
                activitiesContainer.getChildren().add(actLabel);
            }
        }
    }

    /**
     * Définit le callback à appeler après suppression
     */
    public void setOnDeleteCallback(Runnable callback) {
        this.onDeleteCallback = callback;
    }

    /**
     * Retourne le noeud racine de la carte
     */
    public VBox getRoot() {
        return cardRoot;
    }

    @FXML
    private void onDelete() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer le voyage ?");
        confirm.setContentText("Voulez-vous vraiment supprimer le voyage vers " + trip.getDestination() + " ?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (api.deleteTrip(trip.getId())) {
                    if (onDeleteCallback != null) {
                        onDeleteCallback.run();
                    }
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Erreur");
                    error.setHeaderText(null);
                    error.setContentText("Impossible de supprimer le voyage.");
                    error.showAndWait();
                }
            }
        });
    }
}
