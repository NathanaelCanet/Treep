package com.treep.frontend.controller;

import com.treep.frontend.service.ApiClientServices;
import com.treep.frontend.model.Trip;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
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
            try {
                // Charger le template FXML de la carte
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/trip-card.fxml"));
                VBox card = loader.load();
                
                // Récupérer le contrôleur et lui passer les données
                TripCardController cardController = loader.getController();
                cardController.setTrip(trip);
                cardController.setOnDeleteCallback(this::refreshHub);
                
                mainContainer.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}