package com.javaninjas.careerpathway.app;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        NavigationService.init(stage);
        stage.setTitle("CAB302: Career Pathway Application");
        // Set window dimensions
        stage.setWidth(1200);    // Set your desired width
        stage.setHeight(800);    // Set your desired height
        NavigationService.go("/com/javaninjas/careerpathway/login/views/loginPage.fxml");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
