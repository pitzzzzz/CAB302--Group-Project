package com.javaninjas.careerpathway.pages.quiz.controllers;

import com.javaninjas.careerpathway.core.services.NavigationService;
import com.javaninjas.careerpathway.pages.quiz.components.ProgressDots; // ✅ import this
import com.javaninjas.careerpathway.pages.quiz.models.QuizData;
import com.javaninjas.careerpathway.pages.quiz.services.QuizService;
import javafx.fxml.FXML;

public class QuizIntroController {

    @FXML
    private ProgressDots progressDots; // injected custom control

    @FXML
    public void initialize() {
        QuizService quizService = new QuizService(new QuizData());
        progressDots.setTotal(quizService.getQuestionSets().size());
        progressDots.setCurrent(0);
    }

    @FXML
    private void startQuiz() {
        NavigationService.go("/com/javaninjas/careerpathway/pages/quiz/views/QuizQuestion.fxml");
    }
}