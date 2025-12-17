package com.treep.frontend.controller;

import com.treep.frontend.model.Activity;
import com.treep.frontend.model.Trip;
import com.treep.frontend.model.User;
import com.treep.frontend.service.ApiClientServices;
import com.treep.frontend.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TripCardController {

    @FXML
    private VBox cardRoot;
    @FXML
    private Label destinationLabel;
    @FXML
    private Label datesLabel;
    @FXML
    private Button budgetButton;
    @FXML
    private Label favoriteButton;
    @FXML
    private Label privateBadge;
    @FXML
    private Button deleteButton;
    @FXML
    private Label authorLabel;
    @FXML
    private VBox activitiesContainer;

    private Trip trip;
    private Runnable onDeleteCallback;
    private Runnable onRefreshCallback;
    private final ApiClientServices api = ApiClientServices.getInstance();

    /**
     * Initialise la carte avec les données du voyage
     */
    public void setTrip(Trip trip) {
        this.trip = trip;

        // Header
        destinationLabel.setText(trip.getDestination());
        datesLabel.setText(trip.getDateDebut() + " ➔ " + trip.getDateFin());
        budgetButton.setText(trip.getBudgetTotal() + " €");

        // Badge privé
        if (trip.getIsPrivate() != null && trip.getIsPrivate()) {
            privateBadge.setVisible(true);
            privateBadge.setManaged(true);
        } else {
            privateBadge.setVisible(false);
            privateBadge.setManaged(false);
        }
        // Afficher le bouton supprimer uniquement si l'utilisateur est le propriétaire
        User currentUser = AuthService.getCurrentUser();
        boolean isOwner = currentUser != null 
                && trip.getUser() != null 
                && currentUser.getId() != null 
                && currentUser.getId().equals(trip.getUser().getId());
        deleteButton.setVisible(isOwner);
        deleteButton.setManaged(isOwner); // Ne pas occuper d'espace si invisible

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

        // Configurer le bouton favori
        updateFavoriteButton();

        // Auteur du voyage
        if (trip.getUser() != null && trip.getUser().getLogin() != null) {
            authorLabel.setText("Par " + trip.getUser().getLogin());
        } else {
            authorLabel.setText("");
        }
    }

    /**
     * Met à jour l'affichage du bouton favori
     */
    private void updateFavoriteButton() {
        if (trip.isFavorite()) {
            favoriteButton.setText("★"); // Étoile pleine
            favoriteButton.getStyleClass().removeAll("favorite-star-empty");
            favoriteButton.getStyleClass().add("favorite-star-filled");
        } else {
            favoriteButton.setText("☆"); // Étoile vide
            favoriteButton.getStyleClass().removeAll("favorite-star-filled");
            favoriteButton.getStyleClass().add("favorite-star-empty");
        }
    }

    /**
     * Définit le callback à appeler après suppression
     */
    public void setOnDeleteCallback(Runnable callback) {
        this.onDeleteCallback = callback;
    }

    /**
     * Définit le callback à appeler après modification des favoris
     */
    public void setOnRefreshCallback(Runnable callback) {
        this.onRefreshCallback = callback;
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

    @FXML
    private void onToggleFavorite() {
        var currentUser = AuthService.getCurrentUser();
        if (currentUser == null || currentUser.getId() == null) {
            System.err.println("Aucun utilisateur connecté");
            return;
        }

        boolean success;
        if (trip.isFavorite()) {
            // Retirer des favoris
            success = api.removeFavorite(currentUser.getId(), trip.getId());
            if (success) {
                trip.setFavorite(false);
                System.out.println("Voyage retiré des favoris");
            }
        } else {
            // Ajouter aux favoris
            success = api.addFavorite(currentUser.getId(), trip.getId());
            if (success) {
                trip.setFavorite(true);
                System.out.println("Voyage ajouté aux favoris");
            }
        }

        if (success) {
            updateFavoriteButton();
            // Rafraîchir la vue si un callback est défini
            if (onRefreshCallback != null) {
                onRefreshCallback.run();
            }
        } else {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Erreur");
            error.setHeaderText(null);
            error.setContentText("Impossible de modifier les favoris.");
            error.showAndWait();
        }
    }
}
