package com.javaninjas.careerpathway.quiz;

import com.javaninjas.careerpathway.app.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class QuizIntroController {
    @FXML
    private Button startButton;

    @FXML
    private void initialize() {
        if (startButton != null) {
            startButton.setOnAction(e -> NavigationService.go("/com/javaninjas/careerpathway/quiz/view/QuizQuestion.fxml"));
        }
    }
}
