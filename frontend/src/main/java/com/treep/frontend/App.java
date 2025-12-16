package com.treep.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Chargement du FXML Authentification
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/auth-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 500);

        // Ajouter BootstrapFX
        scene.getStylesheets().add(org.kordamp.bootstrapfx.BootstrapFX.bootstrapFXStylesheet());

        stage.setTitle("Treep - Connexion");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}