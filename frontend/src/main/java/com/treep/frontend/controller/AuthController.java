package com.treep.frontend.controller;

import com.treep.frontend.model.User;
import com.treep.frontend.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class AuthController {

    // Champs Connexion
    @FXML private TextField loginUsername;
    @FXML private PasswordField loginPassword;
    @FXML private Label loginError;

    // Champs Inscription
    @FXML private TextField registerUsername;
    @FXML private PasswordField registerPassword;
    @FXML private PasswordField registerPasswordConfirm;
    @FXML private Label registerError;

    private final AuthService authService = new AuthService();

    @FXML
    public void onLogin() {
        String username = loginUsername.getText();
        String password = loginPassword.getText();

        if (username.isBlank() || password.isBlank()) {
            loginError.setText("Veuillez remplir tous les champs.");
            return;
        }

        try {
            User user = authService.login(username, password);
            if (user != null) {
                openHubView();
            } else {
                loginError.setText("Identifiants incorrects.");
            }
        } catch (Exception e) {
            loginError.setText("Erreur de connexion au serveur.");
            e.printStackTrace();
        }
    }

    @FXML
    public void onRegister() {
        String username = registerUsername.getText();
        String password = registerPassword.getText();
        String passwordConfirm = registerPasswordConfirm.getText();

        if (username.isBlank() || password.isBlank() || passwordConfirm.isBlank()) {
            registerError.setText("Veuillez remplir tous les champs.");
            return;
        }

        if (!password.equals(passwordConfirm)) {
            registerError.setText("Les mots de passe ne correspondent pas.");
            return;
        }

        try {
            boolean success = authService.register(username, password);
            if (success) {
                // Inscription réussie, on connecte automatiquement
                User user = authService.login(username, password);
                if (user != null) {
                    openHubView();
                }
            } else {
                registerError.setText("Ce login existe déjà.");
            }
        } catch (Exception e) {
            registerError.setText("Erreur de connexion au serveur.");
            e.printStackTrace();
        }
    }

    private void openHubView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/hub-view.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) loginUsername.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            
            stage.setScene(scene);
            stage.setTitle("Treep - Mes Voyages");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
