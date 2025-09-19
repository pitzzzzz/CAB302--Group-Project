package com.javaninjas.careerpathway.pages.quiz.controllers;

import com.javaninjas.careerpathway.core.services.NavigationService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class QuizIntroController {
    @FXML
    private Button startButton;

    @FXML
    private void startQuiz() {
        NavigationService.go("/com/javaninjas/careerpathway/pages/loading/views/LoadingScreen.fxml", controller -> {
            com.javaninjas.careerpathway.pages.loading.controllers.LoadingScreenController loadingController = (com.javaninjas.careerpathway.pages.loading.controllers.LoadingScreenController) controller;
            loadingController.loadData(() -> {
                // Simulate long-running task
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return ""; // No data to pass
            }, (data) -> {
                // Navigate to the next page
                NavigationService.go("/com/javaninjas/careerpathway/pages/quiz/views/QuizQuestion.fxml");
            });
        });
    }
}
