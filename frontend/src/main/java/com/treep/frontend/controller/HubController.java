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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HubController {

    @FXML
    private VBox mainContainer;
    @FXML
    private TextField searchField;
    @FXML
    private ToggleButton allTripsBtn;
    @FXML
    private ToggleButton myTripsBtn;

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

        // Charger les favoris pour marquer les étoiles
        var user = com.treep.frontend.service.AuthService.getCurrentUser();
        List<Trip> favoriteTrips = new ArrayList<>();
        if (user != null && user.getId() != null) {
            favoriteTrips = api.getFavorites(user.getId());
        }

        // Marquer les voyages favoris
        Set<Long> favoriteTripIds = new HashSet<>();
        for (Trip fav : favoriteTrips) {
            favoriteTripIds.add(fav.getId());
        }

        for (Trip trip : trips) {
            if (favoriteTripIds.contains(trip.getId())) {
                trip.setFavorite(true);
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/trip-card.fxml"));
                VBox card = loader.load();
                TripCardController cardController = loader.getController();
                cardController.setTrip(trip);
                cardController.setOnDeleteCallback(this::refreshHub);
                cardController.setOnRefreshCallback(this::refreshHub);
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
        List<Trip> favoriteTrips = new ArrayList<>();

        var user = com.treep.frontend.service.AuthService.getCurrentUser();

        if (myTripsBtn.isSelected()) {
            // "Mes voyages" : voyages créés + favoris
            if (user == null || user.getId() == null) {
                trips = List.of();
            } else {
                // Récupérer les voyages créés par l'utilisateur
                List<Trip> ownedTrips = api.getTripsForUser(user.getId());

                // Récupérer les voyages favoris
                favoriteTrips = api.getFavorites(user.getId());

                // Fusionner les deux listes (éviter les doublons)
                Set<Long> tripIds = new HashSet<>();
                trips = new ArrayList<>();

                // Ajouter les voyages créés
                for (Trip trip : ownedTrips) {
                    tripIds.add(trip.getId());
                    trips.add(trip);
                }

                // Ajouter les favoris qui ne sont pas déjà dans la liste
                for (Trip trip : favoriteTrips) {
                    if (!tripIds.contains(trip.getId())) {
                        trip.setFavorite(true);
                        trips.add(trip);
                    }
                }
            }
        } else {
            // "Tous les voyages"
            trips = api.getAllTrips();

            // Charger les favoris pour marquer les étoiles
            if (user != null && user.getId() != null) {
                favoriteTrips = api.getFavorites(user.getId());
            }
        }

        // Marquer les voyages favoris
        Set<Long> favoriteTripIds = new HashSet<>();
        for (Trip fav : favoriteTrips) {
            favoriteTripIds.add(fav.getId());
        }

        for (Trip trip : trips) {
            if (favoriteTripIds.contains(trip.getId())) {
                trip.setFavorite(true);
            }

            try {
                // Charger le template FXML de la carte
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/trip-card.fxml"));
                VBox card = loader.load();

                // Récupérer le contrôleur et lui passer les données
                TripCardController cardController = loader.getController();
                cardController.setTrip(trip);
                cardController.setOnDeleteCallback(this::refreshHub);
                cardController.setOnRefreshCallback(this::refreshHub);

                mainContainer.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}