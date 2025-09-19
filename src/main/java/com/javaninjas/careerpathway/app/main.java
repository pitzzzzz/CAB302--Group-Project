package com.javaninjas.careerpathway.app;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import com.javaninjas.careerpathway.core.services.NavigationService;
import com.javaninjas.careerpathway.db.connection.DatabaseInitializer;

public class main extends Application {

    private final int windowWidth = 1200;
    private final int windowHeight = 800;

    @Override
    public void start(Stage stage) throws IOException {

        // Ensure application connection is successful before UI loads
        DatabaseInitializer.initialize();

        // Initialize navigation
        NavigationService.init(stage);
        stage.setTitle("CAB302: Career Pathway Application");

        // Set window dimensions
        stage.setWidth(windowWidth); 
        stage.setHeight(windowHeight);

        // Load first page
        NavigationService.go("/com/javaninjas/careerpathway/login/views/loginPage.fxml");

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}