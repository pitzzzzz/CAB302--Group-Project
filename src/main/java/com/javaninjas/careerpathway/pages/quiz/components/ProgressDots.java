package com.javaninjas.careerpathway.pages.quiz.components;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class ProgressDots extends HBox {

    private final IntegerProperty total = new SimpleIntegerProperty(0);
    private final IntegerProperty current = new SimpleIntegerProperty(0);

    public ProgressDots() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/com/javaninjas/careerpathway/core/views/ProgressDots.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load ProgressDots.fxml", e);
        }

        // React to property changes
        total.addListener((obs, oldVal, newVal) -> rebuildDots());
        current.addListener((obs, oldVal, newVal) -> updateHighlight());
    }

    // === Properties ===
    public IntegerProperty totalProperty() {
        return total;
    }

    public IntegerProperty currentProperty() {
        return current;
    }

    public void setTotal(int value) {
        total.set(value);
    }

    public void setCurrent(int value) {
        current.set(value);
    }

    // === Logic ===
    private void rebuildDots() {
        getChildren().clear();
        for (int i = 0; i < total.get(); i++) {
            Circle circle = new Circle(6);
            circle.setFill(Color.GRAY);
            getChildren().add(circle);
        }
        updateHighlight();
    }

    private void updateHighlight() {
        if (getChildren().isEmpty()) return;

        for (int i = 0; i < getChildren().size(); i++) {
            Circle circle = (Circle) getChildren().get(i);
            if (i == current.get()) {
                circle.setFill(Color.DODGERBLUE); // highlight
            } else {
                circle.setFill(Color.GRAY);       // default
            }
        }
    }
}
