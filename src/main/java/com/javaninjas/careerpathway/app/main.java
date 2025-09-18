package com.javaninjas.careerpathway.app;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import com.javaninjas.careerpathway.core.services.NavigationService;

public class Main extends Application {

    private int windowWidth = 1200;
    private int windowHeight = 800;

    @Override
    public void start(Stage stage) throws IOException {
        NavigationService.init(stage);
        stage.setTitle("CAB302: Career Pathway Application");
        // Set window dimensions
        stage.setWidth(windowWidth); 
        stage.setHeight(windowHeight);
        NavigationService.go("/com/javaninjas/careerpathway/login/views/loginPage.fxml");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}