package com.treep.frontend.controller;

import com.treep.frontend.service.ApiClientServices;
import com.treep.frontend.model.Trip;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HubController {

    @FXML private VBox mainContainer;
    @FXML private TextField searchField;

    private final ApiClientServices api = new ApiClientServices();

    @FXML
    public void initialize() {
        refreshHub();
    }

    @FXML
    public void onRefresh() {
        searchField.clear();
        refreshHub();
    }

    @FXML
    public void onSearch() {
        String searchText = searchField.getText().trim();
        mainContainer.getChildren().clear();
        
        List<Trip> trips = searchText.isEmpty() ? api.getAllTrips() : api.searchTrips(searchText);

        for (Trip trip : trips) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/trip-card.fxml"));
                VBox card = loader.load();
                TripCardController cardController = loader.getController();
                cardController.setTrip(trip);
                cardController.setOnDeleteCallback(this::refreshHub);
                mainContainer.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onOpenCreate() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();

            // Rendre le popup modal (bloque l'interaction avec le hub)
            stage.initModality(Modality.APPLICATION_MODAL);
            Stage ownerStage = (Stage) mainContainer.getScene().getWindow();
            stage.initOwner(ownerStage);

            Scene scene = new Scene(root);
            stage.setTitle("Nouveau Voyage");
            stage.setScene(scene);

            // Centrer le popup par rapport au hub
            stage.setOnShown(e -> {
                stage.setX(ownerStage.getX() + (ownerStage.getWidth() - stage.getWidth()) / 2);
                stage.setY(ownerStage.getY() + (ownerStage.getHeight() - stage.getHeight()) / 2);
            });

            stage.showAndWait(); // Attend que le popup soit fermé

            onRefresh(); // Rafraîchir après fermeture
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