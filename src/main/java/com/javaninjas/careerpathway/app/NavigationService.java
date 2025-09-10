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
            if (primary == null) {
                throw new IllegalStateException("NavigationService not initialized. Call NavigationService.init(stage) before navigating.");
            }

            // If the stage already has a scene use its size; otherwise fall back to stage dimensions or sensible defaults
            double width;
            double height;
            if (primary.getScene() != null) {
                width = primary.getScene().getWidth();
                height = primary.getScene().getHeight();
            } else {
                // primary.getWidth()/getHeight() may be 0 before showing; use defaults if they are not set
                width = primary.getWidth() > 0 ? primary.getWidth() : 800;
                height = primary.getHeight() > 0 ? primary.getHeight() : 600;
            }

            Scene scene = new Scene(loader.load(), width, height);
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
