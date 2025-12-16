package com.treep.frontend.controller;

import com.treep.frontend.service.ApiClientServices;
import com.treep.frontend.model.Activity;
import com.treep.frontend.model.Trip;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class HubController {

    @FXML private VBox mainContainer;

    private final ApiClientServices api;

    public HubController(ApiClientServices api) {
        this.api = api;
    }

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
            fxmlLoader.setControllerFactory(type -> {
                try {
                    if (type == DashboardController.class) return new DashboardController(api);
                    return type.getDeclaredConstructor().newInstance();
                } catch (Exception e) { throw new RuntimeException(e); }
            });
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Nouveau Voyage");
            stage.setScene(new Scene(root));
            stage.show();

            stage.setOnHidden(e -> onRefresh());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshHub() {
        mainContainer.getChildren().clear();
        List<Trip> trips = api.getAllTrips();

        if (trips != null) {
            for (Trip trip : trips) {
                VBox tripCard = createTripCard(trip);
                mainContainer.getChildren().add(tripCard);
            }
        }
    }

    private VBox createTripCard(Trip trip) {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setPadding(new Insets(15));
        // Style CSS directement ici pour faire joli (Ombre, fond blanc, bords arrondis)
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        BorderPane header = new BorderPane();
        
        VBox titleBox = new VBox(5);
        Label destLabel = new Label(trip.getDestination());
        destLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Label dateLabel = new Label("📅 " + trip.getDateDebut() + " au " + trip.getDateFin());
        dateLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");
        
        titleBox.getChildren().addAll(destLabel, dateLabel);

        Button btnBudget = new Button(trip.getBudgetTotal() + " €");
        btnBudget.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-weight: bold;");

        header.setLeft(titleBox);
        header.setRight(btnBudget);

        VBox activitiesBox = new VBox(5);
        activitiesBox.setPadding(new Insets(10, 0, 0, 0));
        
        Label progTitle = new Label("Programme :");
        progTitle.setStyle("-fx-font-weight: bold; -fx-underline: true; -fx-text-fill: #34495e;");
        activitiesBox.getChildren().add(progTitle);

        List<Activity> acts = trip.getActivities();
        
        if (acts == null || acts.isEmpty()) {
            Label empty = new Label("Aucune activité prévue pour le moment.");
            empty.setStyle("-fx-font-style: italic; -fx-text-fill: gray;");
            activitiesBox.getChildren().add(empty);
        } else {
            for (Activity act : acts) {
                HBox actRow = new HBox(10);
                actRow.setPadding(new Insets(5));
                actRow.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 5;");
                
                String icon = "Booked".equals(act.getStatut()) ? "✅" : "📝";
                Label lbl = new Label(icon + " " + act.getTitre() + " (" + act.getCout() + "€)");
                lbl.setStyle("-fx-text-fill: #2c3e50;");
                
                actRow.getChildren().add(lbl);
                activitiesBox.getChildren().add(actRow);
            }
        }

        // Ajout d'un séparateur visuel
        Separator sep = new Separator();

        card.getChildren().addAll(header, sep, activitiesBox);
        
        VBox.setMargin(card, new Insets(0, 0, 20, 0));

        return card;
    }
}