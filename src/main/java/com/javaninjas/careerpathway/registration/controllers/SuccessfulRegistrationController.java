package com.javaninjas.careerpathway.registration.controllers;

import com.javaninjas.careerpathway.app.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SuccessfulRegistrationController {
    @FXML
    private Label welcomeLabel;

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
        NavigationService.go("/com/javaninjas/careerpathway/quiz/view/quizIntro.fxml");
    }
}
