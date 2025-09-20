package com.javaninjas.careerpathway.pages.quiz.controllers;

import com.javaninjas.careerpathway.core.services.NavigationService;
import com.javaninjas.careerpathway.pages.quiz.models.QuizData;
import com.javaninjas.careerpathway.pages.quiz.services.QuizService;
import javafx.fxml.FXML;

public class QuizIntroController {

    @FXML
    private ProgressDotsController progressDotsController; // Injected from <fx:include>

    @FXML
    public void initialize() {
        if (progressDotsController != null) {
            QuizService quizService = new QuizService(new QuizData());
            progressDotsController.totalProperty().set(quizService.getQuestionSets().size());
            progressDotsController.currentProperty().set(0);
        }
    }

    @FXML
    private void startQuiz() {
        NavigationService.go("/com/javaninjas/careerpathway/pages/quiz/views/QuizQuestion.fxml");
    }
}
