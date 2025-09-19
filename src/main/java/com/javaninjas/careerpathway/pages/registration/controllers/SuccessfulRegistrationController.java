package com.javaninjas.careerpathway.pages.registration.controllers;

import com.javaninjas.careerpathway.core.services.NavigationService;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;

public class SuccessfulRegistrationController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private Button quizButton;

    @FXML
    private Hyperlink loginLink;

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
        NavigationService.go("/com/javaninjas/careerpathway/quiz/view/QuizQuestion.fxml");
    }

    @FXML
    private void handleLoginLinkAction() {
        // Navigate to the login page
        NavigationService.go("/com/javaninjas/careerpathway/login/views/loginPage.fxml");
    }

    @FXML
    private void initialize() {
        if (quizButton != null) {
            quizButton.setOnAction(e -> NavigationService.go("/com/javaninjas/careerpathway/quiz/view/QuizIntro.fxml"));
        }
    }
}
