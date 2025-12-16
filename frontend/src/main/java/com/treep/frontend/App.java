package com.treep.frontend;

import com.treep.frontend.controller.DashboardController;
import com.treep.frontend.controller.HubController;
import com.treep.frontend.service.ApiClientServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ApiClientServices api = new ApiClientServices();
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("/fxml/hub-view.fxml"));
        fxml.setControllerFactory(type -> {
            try {
                if (type == HubController.class) return new HubController(api);
                if (type == DashboardController.class) return new DashboardController(api);
                return type.getDeclaredConstructor().newInstance();
            } catch (Exception e) { throw new RuntimeException(e); }
        });
        Scene scene = new Scene(fxml.load(), 1000, 700);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/hub.css")).toExternalForm());
        stage.setTitle("Treep - Hub");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}