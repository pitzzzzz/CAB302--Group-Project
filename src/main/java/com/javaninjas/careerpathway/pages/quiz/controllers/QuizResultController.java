package com.javaninjas.careerpathway.pages.quiz.controllers;

import com.javaninjas.careerpathway.core.services.NavigationService;
import com.javaninjas.careerpathway.pages.quiz.components.ProgressDots; // ✅ use this instead
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

    @FXML private VBox detailsBox;
    @FXML private Button dashboardButton;
    @FXML private ProgressDots progressDots;

    @FXML
    private void initialize() {
        // Setup progress dots
        QuizService quizService = new QuizService(new QuizData());
        int totalPages = quizService.getQuestionSets().size();
        progressDots.setTotal(totalPages);
        progressDots.setCurrent(totalPages - 1); // highlight last page (results)

        // Load suggestion
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
        NavigationService.go("/com/javaninjas/careerpathway/pages/dashboard/views/userPathway.fxml");
    }
}