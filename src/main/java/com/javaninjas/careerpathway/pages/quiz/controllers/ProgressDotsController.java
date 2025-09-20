package com.javaninjas.careerpathway.pages.quiz.controllers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

public class ProgressDotsController {

    @FXML private HBox root;  // bound to fx:root

    private final IntegerProperty total = new SimpleIntegerProperty(0);
    private final IntegerProperty current = new SimpleIntegerProperty(0);

    @FXML
    private void initialize() {
        total.addListener((obs, oldVal, newVal) -> rebuildDots());
        current.addListener((obs, oldVal, newVal) -> updateHighlight());
    }

    private void rebuildDots() {
        root.getChildren().clear();
        for (int i = 0; i < total.get(); i++) {
            Circle dot = new Circle(6);
            dot.getStyleClass().add("progress-dot");
            root.getChildren().add(dot);
        }
        updateHighlight();
    }

    private void updateHighlight() {
        for (int i = 0; i < root.getChildren().size(); i++) {
            Circle dot = (Circle) root.getChildren().get(i);
            if (i == current.get()) {
                dot.setStyle("-fx-fill: #357ABD;"); // active color
            } else {
                dot.setStyle("-fx-fill: #cccccc;"); // inactive color
            }
        }
    }

    // Properties for binding
    public IntegerProperty totalProperty() { return total; }
    public IntegerProperty currentProperty() { return current; }
}