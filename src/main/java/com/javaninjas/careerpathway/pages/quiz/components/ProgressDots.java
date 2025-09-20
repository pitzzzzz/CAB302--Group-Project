package com.javaninjas.careerpathway.pages.quiz.components;

import com.javaninjas.careerpathway.pages.quiz.controllers.ProgressDotsController;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ProgressDots extends HBox {

    private ProgressDotsController controller;

    public ProgressDots() {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/javaninjas/careerpathway/core/views/ProgressDots.fxml")
        );
        loader.setRoot(this);
        try {
            loader.load();
            this.controller = loader.getController(); // keep reference
        } catch (IOException e) {
            throw new RuntimeException("Failed to load ProgressDots.fxml", e);
        }
    }

    // Expose the controller’s properties
    public IntegerProperty totalProperty() {
        return controller.totalProperty();
    }

    public IntegerProperty currentProperty() {
        return controller.currentProperty();
    }
}