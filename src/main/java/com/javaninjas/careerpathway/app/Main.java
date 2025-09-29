package com.javaninjas.careerpathway.app;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import com.javaninjas.careerpathway.core.services.NavigationService;
import com.javaninjas.careerpathway.db.connection.DatabaseInitializer;

public class Main extends Application {

    private final int windowWidth = 1200;
    private final int windowHeight = 800;

    @Override
    public void start(Stage stage) throws IOException {
        // Ensure application connection is successful before UI loads
        DatabaseInitializer.initialize();

        // Start cache maintenance service
        com.javaninjas.careerpathway.core.services.CacheMaintenanceService.startMaintenance();

        // Initialize navigation
        NavigationService.init(stage);
        stage.setTitle("CAB302: Career Pathway Application");

        // Set window dimensions
        stage.setWidth(windowWidth); 
        stage.setHeight(windowHeight);

        // Load first page
        NavigationService.go("/com/javaninjas/careerpathway/pages/login/views/LoginPage.fxml");

        stage.show();
    }
    
    @Override
    public void stop() throws Exception {
        // Stop cache maintenance service when application closes
        com.javaninjas.careerpathway.core.services.CacheMaintenanceService.stopMaintenance();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}