package com.javaninjas.careerpathway.pages.quiz;

import com.javaninjas.careerpathway.pages.quiz.models.Question;
import com.javaninjas.careerpathway.pages.quiz.models.QuizResult;
import com.javaninjas.careerpathway.pages.quiz.services.QuizService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuizController {
    private final QuizService quizService;
    private final List<Integer> answers = new ArrayList<>();
    private int currentSet = 0;
    private int currentQuestion = 0;

    @FXML private Label questionLabel;
    @FXML private HBox likertScaleBox;
    @FXML private ProgressBar progressBar;
    @FXML private Button continueButton;
    @FXML private Button backButton;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @FXML
    public void initialize() {
        loadCurrentQuestion();
    }

    private void loadCurrentQuestion() {
        // TODO: Load question text and update UI
    }

    @FXML
    private void onContinue() {
        // TODO: Store answer, move to next question or set, or show results
    }

    @FXML
    private void onBack() {
        // TODO: Move to previous question or set
    }

    private void showResults() {
        QuizResult result = quizService.calculateResult(answers);
        // TODO: Show results screen and save to database
    }
}
