package com.javaninjas.careerpathway.pages.quiz.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaninjas.careerpathway.pages.quiz.ChatGptClient;
import com.javaninjas.careerpathway.pages.quiz.InMemoryQuizSuggestionRepository;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class QuizResultController {
    @FXML private VBox detailsBox;
    @FXML private Button dashboardButton;

    @FXML
    private void initialize() {
    // Set dashboard action to go to the login page (as requested)
        // Load last suggestion
        var repo = com.javaninjas.careerpathway.pages.quiz.InMemoryQuizSuggestionRepository.getInstance();
        List<com.javaninjas.careerpathway.pages.quiz.models.QuizPathwaySuggestion> all = repo.getAllSuggestions();
        if (all.isEmpty()) return;
        var suggestion = all.get(all.size() - 1);

        String userAnswersText = "Answers: " + suggestion.getAnswers().toString();

        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey != null && !apiKey.isBlank()) {
            // call AI in background thread
            new Thread(() -> {
                try {
                    ChatGptClient client = new ChatGptClient(apiKey);
                    JsonNode resp = client.requestCareerSuggestion(userAnswersText);
                    Platform.runLater(() -> populateFromAiResponse(resp));
                } catch (IOException | InterruptedException ex) {
                    Platform.runLater(() -> showFallback(suggestion));
                }
            }).start();
        } else {
            showFallback(suggestion);
        }
    }

    private void populateFromAiResponse(JsonNode resp) {
        // Expected shape: { traits: [...], degreeRecommendations: [ {degree, reason, relatedTraits}, ... ] }
        detailsBox.getChildren().clear();

        JsonNode traits = resp.path("traits");
        if (traits.isArray()) {
            HBox h = new HBox(8);
            h.getChildren().add(new Label("Top traits: "));
            StringBuilder sb = new StringBuilder();
            for (JsonNode t : traits) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(t.asText());
            }
            h.getChildren().add(new Label(sb.toString()));
            detailsBox.getChildren().add(h);
        }

        JsonNode recs = resp.path("degreeRecommendations");
        if (recs.isArray()) {
            int count = 0;
            for (JsonNode r : recs) {
                if (count++ >= 5) break;
                String degree = r.path("degree").asText();
                String reason = r.path("reason").asText();
                JsonNode related = r.path("relatedTraits");
                Label title = new Label((count) + ". " + degree);
                title.setStyle("-fx-font-weight:bold; -fx-font-size:13px;");
                detailsBox.getChildren().add(title);
                detailsBox.getChildren().add(new Label(reason));
                if (related.isArray()) {
                    StringBuilder sb = new StringBuilder("Traits: ");
                    for (int i = 0; i < related.size(); i++) {
                        if (i > 0) sb.append(", ");
                        sb.append(related.get(i).asText());
                    }
                    detailsBox.getChildren().add(new Label(sb.toString()));
                }
            }
        }
    }

    private void showFallback(com.javaninjas.careerpathway.pages.quiz.models.QuizPathwaySuggestion suggestion) {
        detailsBox.getChildren().clear();
        detailsBox.getChildren().add(new Label("AI not configured or failed — showing simple suggestions."));

        // naive deterministic mapping: tally answers favoring traits
        List<Integer> answers = suggestion.getAnswers();
        int idx = 0;
        int peopleScore = 0, dataScore = 0, creativeScore = 0, handsScore = 0;
        for (int a : answers) {
            int q = (idx % 5);
            if (q == 0) { // team vs independent
                if (a == 0) peopleScore++; else dataScore++;
            } else if (q == 1) { // people vs data
                if (a == 0) peopleScore++; else dataScore++;
            } else if (q == 2) { // creative vs process
                if (a == 0) creativeScore++; else handsScore++;
            } else if (q == 3) { // office vs hands
                if (a == 0) dataScore++; else handsScore++;
            } else if (q == 4) { // routine vs variety
                if (a == 0) dataScore++; else creativeScore++;
            }
            idx++;
        }

        detailsBox.getChildren().add(new Label("Top traits (simple):"));
        detailsBox.getChildren().add(new Label("People: " + peopleScore + ", Data: " + dataScore + ", Creative: " + creativeScore + ", Hands-on: " + handsScore));

        // propose top 5 degrees simply
        detailsBox.getChildren().add(new Label("Top degree suggestions:"));
        detailsBox.getChildren().add(new Label("1. Bachelor of Information Technology — aligns with Data and Problem Solving"));
        detailsBox.getChildren().add(new Label("2. Bachelor of Business — good for people and leadership roles"));
        detailsBox.getChildren().add(new Label("3. Bachelor of Design — for creative learners"));
        detailsBox.getChildren().add(new Label("4. Bachelor of Engineering — hands-on, technical"));
        detailsBox.getChildren().add(new Label("5. Bachelor of Health Sciences — service/people focused"));
    }


    @FXML
    private void goToDashboard() {
        com.javaninjas.careerpathway.core.services.NavigationService.go(
                "/com/javaninjas/careerpathway/dashboard/views/userPathway.fxml");
    }

}
