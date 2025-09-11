package com.javaninjas.careerpathway.core.controllers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

import java.util.stream.IntStream;

import com.javaninjas.careerpathway.core.components.ProgressDots;

public class ProgressDotsController {
    @FXML private HBox root;
    private final IntegerProperty total = new SimpleIntegerProperty(1);
    private final IntegerProperty current = new SimpleIntegerProperty(0);

    public ProgressDotsController(ProgressDots ignored) {}

    @FXML
    private void initialize() {
        Runnable rebuild = () -> {
            root.getChildren().setAll(
                IntStream.range(0, total.get())
                    .mapToObj(i -> {
                        Circle c = new Circle(5);
                        c.getStyleClass().add(i == current.get() ? "dot-active" : "dot");
                        return c;
                    }).toList()
            );
        };
        total.addListener((o, a, b) -> rebuild.run());
        current.addListener((o, a, b) -> rebuild.run());
        rebuild.run();

        // Expose as properties via the HBox's properties map for simple access
        root.getProperties().put("total", total);
        root.getProperties().put("current", current);
    }

    // helpers to access from controllers:
    public IntegerProperty totalProperty() { return total; }
    public IntegerProperty currentProperty() { return current; }
}