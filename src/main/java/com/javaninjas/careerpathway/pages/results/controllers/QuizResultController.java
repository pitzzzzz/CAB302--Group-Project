package com.javaninjas.careerpathway.pages.results.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

import com.javaninjas.careerpathway.pages.results.models.QuizPathwaySuggestion;
import com.javaninjas.careerpathway.pages.results.services.QuizResultService;

public class QuizResultController {
    @FXML
    private VBox detailsBox;
    @FXML
    private VBox resultCard;
    @FXML
    private Button dashboardButton;

    @FXML
    private void initialize() {
        // Make resultCard grow to use available center space (keep some padding)
        javafx.application.Platform.runLater(() -> {
            if (resultCard != null && resultCard.getParent() instanceof javafx.scene.layout.Region parent) {
                double avail = parent.getBoundsInParent().getHeight() - 40; // leave some padding
                if (avail > 200) {
                    resultCard.setPrefHeight(avail);
                    resultCard.setMaxHeight(avail);
                }
            }
        });

        QuizPathwaySuggestion suggestion = QuizResultService.getSuggestion();

        if (suggestion == null) {
            detailsBox.getChildren().add(new Label("No quiz result found."));
            return;
        }


        if (suggestion.getRecommendedDegree() != null && !suggestion.getRecommendedDegree().isBlank()) {
            Label degreeLabel = new Label("Recommended Degree: " + suggestion.getRecommendedDegree());
            degreeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #4f8cff; -fx-padding: 0 0 12 0;");
            detailsBox.getChildren().add(degreeLabel);
        }

        detailsBox.getChildren().add(new Label("Based on your answers, here are some insights about you:"));

        List<String> traits = suggestion.getTraits();
        if (traits != null && !traits.isEmpty()) {
            detailsBox.getChildren().add(new Label("Traits:"));
            for (String trait : traits) {
                Label traitLabel = new Label("• " + trait);
                traitLabel.setWrapText(true);
                detailsBox.getChildren().add(traitLabel);
            }
        } else {
            detailsBox.getChildren().add(new Label("Could not determine your traits from the answers."));
        }

        List<String> reflections = suggestion.getReflections();
        if (reflections != null && !reflections.isEmpty()) {
            detailsBox.getChildren().add(new Label("\nReflections:"));
            for (String reflection : reflections) {
                Label reflLabel = new Label("- " + reflection);
                reflLabel.setWrapText(true);
                detailsBox.getChildren().add(reflLabel);
            }
        }
    }

    @FXML
    private void goToDashboard() {
        com.javaninjas.careerpathway.core.services.NavigationService.go(
                "/com/javaninjas/careerpathway/pages/dashboard/views/userPathway.fxml");
    }
}