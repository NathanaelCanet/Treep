package com.treep.frontend.controller;

import com.treep.frontend.service.ApiClientServices;
import com.treep.frontend.model.Trip;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HubController {

    @FXML private VBox mainContainer;
    @FXML private TextField searchField;
    @FXML private ToggleButton allTripsBtn;
    @FXML private ToggleButton myTripsBtn;

    private final ApiClientServices api = ApiClientServices.getInstance();

    @FXML
    public void initialize() {
        // Toggle group for tabs
        ToggleGroup group = new ToggleGroup();
        allTripsBtn.setToggleGroup(group);
        myTripsBtn.setToggleGroup(group);
        allTripsBtn.setSelected(true);

        allTripsBtn.setOnAction(e -> refreshHub());
        myTripsBtn.setOnAction(e -> refreshHub());

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
        List<Trip> trips;
        if (myTripsBtn.isSelected()) {
            var user = com.treep.frontend.service.AuthService.getCurrentUser();
            if (user == null || user.getId() == null) {
                trips = List.of();
            } else {
                trips = api.getTripsForUser(user.getId());
            }
        } else {
            trips = api.getAllTrips();
        }

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
}