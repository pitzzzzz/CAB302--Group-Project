package com.javaninjas.careerpathway.registration.controllers;

import com.javaninjas.careerpathway.app.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import com.javaninjas.careerpathway.app.NavigationService;

public class SuccessfulRegistrationController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private Button quizButton;

    public void setWelcomeName(String name) {
        if (welcomeLabel != null) {
            if (name == null || name.isEmpty()) {
                welcomeLabel.setText("Welcome");
            } else {
                welcomeLabel.setText("Welcome " + name);
            }
        }
    }

    @FXML
    private void handleQuizButtonAction() {
        NavigationService.go("/com/javaninjas/careerpathway/quiz/view/QuizIntro.fxml");
    }

    @FXML
    private void initialize() {
        if (quizButton != null) {
            quizButton.setOnAction(e -> NavigationService.go("/com/javaninjas/careerpathway/quiz/view/QuizIntro.fxml"));
        }
    }
}
