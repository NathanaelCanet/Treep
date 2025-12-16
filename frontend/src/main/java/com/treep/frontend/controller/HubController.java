package com.treep.frontend.controller;

import com.treep.frontend.service.ApiClientServices; // IMPORT MIS A JOUR
import com.treep.frontend.model.Activity;
import com.treep.frontend.model.Trip;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class HubController {

    @FXML private VBox mainContainer;

    private final ApiClientServices api = new ApiClientServices();

    @FXML
    public void initialize() {
        refreshHub();
    }

    @FXML
    public void onRefresh() {
        refreshHub();
    }

    @FXML
    public void onOpenCreate() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();

            // On charge le CSS
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/hub.css")).toExternalForm());

            stage.setTitle("Nouveau Voyage");
            stage.setScene(scene);
            stage.show();

            stage.setOnHidden(e -> onRefresh());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshHub() {
        mainContainer.getChildren().clear();
        List<Trip> trips = api.getAllTrips();

        for (Trip trip : trips) {
            VBox tripCard = createTripCard(trip);
            mainContainer.getChildren().add(tripCard);
        }
    }

    private VBox createTripCard(Trip trip) {
        // 1. CARTE PRINCIPALE
        VBox card = new VBox();
        card.setSpacing(12);
        card.getStyleClass().add("trip-card");

        // 2. HEADER
        BorderPane header = new BorderPane();

        VBox titleBox = new VBox();
        Label destLabel = new Label(trip.getDestination());
        destLabel.getStyleClass().add("title-label");

        Label dateLabel = new Label(trip.getDateDebut() + " ➔ " + trip.getDateFin());
        dateLabel.getStyleClass().add("subtitle-label");

        titleBox.getChildren().addAll(destLabel, dateLabel);

        Button btnBudget = new Button(trip.getBudgetTotal() + " €");
        btnBudget.getStyleClass().addAll("button", "btn-secondary");

        Button btnDelete = new Button("Supprimer");
        btnDelete.getStyleClass().addAll("button", "btn-danger");
        btnDelete.setOnAction(e -> deleteTrip(trip));

        HBox buttonsBox = new HBox(10);
        buttonsBox.getChildren().addAll(btnBudget, btnDelete);

        header.setLeft(titleBox);
        header.setRight(buttonsBox);

        // 3. CONTENU ACTIVITÉS
        VBox contentBox = new VBox(6);
        Label actTitle = new Label("Programme :");
        actTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3436;");
        contentBox.getChildren().add(actTitle);

        if (trip.getActivities() == null || trip.getActivities().isEmpty()) {
            Label emptyLabel = new Label("Aucune activité planifiée.");
            emptyLabel.setStyle("-fx-text-fill: #B2BEC3; -fx-font-style: italic;");
            contentBox.getChildren().add(emptyLabel);
        } else {
            for (Activity act : trip.getActivities()) {
                String icon = "Booked".equalsIgnoreCase(act.getStatut()) ? "✅" : "📅";
                Label actLabel = new Label(icon + " " + act.getTitre());
                actLabel.getStyleClass().add("text-default");
                contentBox.getChildren().add(actLabel);
            }
        }

        card.getChildren().addAll(header, contentBox);

        // Marge extérieure
        VBox.setMargin(card, new Insets(0, 0, 20, 0));

        return card;
    }

    private void deleteTrip(Trip trip) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer le voyage ?");
        confirm.setContentText("Voulez-vous vraiment supprimer le voyage vers " + trip.getDestination() + " ?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (api.deleteTrip(trip.getId())) {
                    refreshHub();
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