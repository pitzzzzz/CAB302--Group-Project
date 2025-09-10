package com.javaninjas.careerpathway.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public final class NavigationService {
    private static Stage primary;

    private NavigationService() {}

    public static void init(Stage stage) { primary = stage; }

    public static <T> void go(String fxmlClasspath, Consumer<T> controllerConfiguration) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationService.class.getResource(fxmlClasspath));
            Scene scene = new Scene(loader.load(), primary.getScene().getWidth(), primary.getScene().getHeight());
            scene.getStylesheets().add(
                    NavigationService.class.getResource("/com/javaninjas/careerpathway/app/app.css").toExternalForm()
            );

            T controller = loader.getController();
            if (controller != null && controllerConfiguration != null) {
                controllerConfiguration.accept(controller);
            }

            primary.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void go(String fxmlClasspath) {
        go(fxmlClasspath, null);
    }
}
