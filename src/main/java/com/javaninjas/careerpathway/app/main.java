package com.javaninjas.careerpathway.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
                FXMLLoader fxmlLoader = new FXMLLoader(
                main.class.getResource("/com/javaninjas/careerpathway/login/views/loginPage.fxml"));

        // Constants for window size (3:2 aspect ratio)
        double defaultWidth = 1200;
        double defaultHeight = 800;
        Scene scene = new Scene(fxmlLoader.load(), defaultWidth, defaultHeight);
        stage.setTitle("Login");
        stage.setScene(scene);

        // Configure stage sizing/behavior via helper
        StageUtils.bindAspectRatio(stage, 3.0 / 2.0, 800, 533);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}