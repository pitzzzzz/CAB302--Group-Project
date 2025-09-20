package com.javaninjas.careerpathway.pages.quiz.controllers;

import com.javaninjas.careerpathway.pages.quiz.models.QuizData;
import com.javaninjas.careerpathway.pages.quiz.services.QuizService;
import com.javaninjas.careerpathway.pages.results.models.QuizPathwaySuggestion;
import com.javaninjas.careerpathway.pages.results.services.QuizResultService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class QuizResultController {
    @FXML
    private VBox detailsBox;
    @FXML
    private Button dashboardButton;

    @FXML
    private ProgressDotsController progressDotsController;

    @FXML
    private void initialize() {
        if (progressDotsController != null) {
            QuizService quizService = new QuizService(new QuizData());
            int totalPages = quizService.getQuestionSets().size();
            progressDotsController.totalProperty().set(totalPages);
            progressDotsController.currentProperty().set(totalPages - 1);
        }

        QuizPathwaySuggestion suggestion = QuizResultService.getSuggestion();

        if (suggestion == null) {
            detailsBox.getChildren().add(new Label("No quiz result found."));
            return;
        }

        detailsBox.getChildren().add(new Label("Based on your answers, here are some insights about you:"));

        List<String> traits = suggestion.getTraits();
        if (traits != null && !traits.isEmpty()) {
            for (String trait : traits) {
                Label traitLabel = new Label("• " + trait);
                traitLabel.setWrapText(true);
                detailsBox.getChildren().add(traitLabel);
            }
        } else {
            detailsBox.getChildren().add(new Label("Could not determine your traits from the answers."));
        }
    }

    @FXML
    private void goToDashboard() {
        com.javaninjas.careerpathway.core.services.NavigationService.go(
                "/com/javaninjas/careerpathway/pages/dashboard/views/userPathway.fxml");
    }
}
