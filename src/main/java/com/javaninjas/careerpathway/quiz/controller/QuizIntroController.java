package com.javaninjas.careerpathway.quiz.controller;

import com.javaninjas.careerpathway.app.NavigationService;
import com.javaninjas.careerpathway.quiz.model.Question;
import com.javaninjas.careerpathway.quiz.model.Quiz;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.List;

public class QuizIntroController {

    @FXML
    private void handleStartButtonAction() {
        // Build a quiz with 4 stages of 5 questions each (20 questions total).
        List<Question> questions = new ArrayList<>();
        for (int s = 1; s <= 4; s++) {
            for (int q = 1; q <= 5; q++) {
                String prompt = "Stage " + s + " - Question " + q + ": Do you prefer option A, B, C?";
                List<String> opts = List.of("Strongly disagree", "Disagree", "Neutral", "Agree", "Strongly agree");
                questions.add(new Question(prompt, opts));
            }
        }

        Quiz quiz = new Quiz(questions);

        NavigationService.go("/com/javaninjas/careerpathway/quiz/view/quizQuestion.fxml", (controller) -> {
            if (controller instanceof QuizQuestionController qc) {
                qc.setQuiz(quiz);
            }
        });
    }
}
