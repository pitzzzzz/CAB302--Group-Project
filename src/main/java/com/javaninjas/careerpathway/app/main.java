package com.javaninjas.careerpathway.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        NavigationService.init(stage);
        stage.setTitle("Career Pathway");
        NavigationService.go("/com/javaninjas/careerpathway/registration/views/userRegistrationDetailsPage.fxml");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
