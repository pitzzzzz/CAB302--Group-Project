package com.javaninjas.careerpathway.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class NavigationService {
    private static Stage primary;

    private NavigationService() {}

    public static void init(Stage stage) { primary = stage; }

    public static void go(String fxmlClasspath) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationService.class.getResource(fxmlClasspath));
            Scene scene = new Scene(loader.load(), primary.getScene().getWidth(), primary.getScene().getHeight());
            scene.getStylesheets().add(
                NavigationService.class.getResource("/com/javaninjas/careerpathway/app/app.css").toExternalForm()
            );
            primary.setScene(scene);
        } catch (IOException e) { throw new RuntimeException(e); }
    }
}