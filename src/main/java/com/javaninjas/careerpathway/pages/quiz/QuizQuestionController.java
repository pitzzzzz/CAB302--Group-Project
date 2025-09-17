package com.javaninjas.careerpathway.pages.quiz;

import com.javaninjas.careerpathway.core.services.NavigationService;
import com.javaninjas.careerpathway.pages.quiz.model.AnswerScale;
import com.javaninjas.careerpathway.pages.quiz.model.Question;
import com.javaninjas.careerpathway.pages.quiz.model.QuizData;
import com.javaninjas.careerpathway.pages.quiz.services.QuizService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.ArrayList;

public class QuizQuestionController {

    @FXML
    private VBox questionsContainer;

    @FXML
    private Button prevButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button submitButton;

    private QuizService quizService;
    private List<Question> questions;
    private List<AnswerScale> selectedAnswers;
    private int currentQuestionIndex = 0;

    @FXML
    public void initialize() {
        this.quizService = new QuizService(new QuizData());
        this.questions = quizService.getQuestions();
        // initialize selected answers list with nulls for each question
        this.selectedAnswers = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) selectedAnswers.add(null);
        displayQuestion(currentQuestionIndex);
        updateButtonVisibility();
    }

    private void displayQuestion(int index) {
        questionsContainer.getChildren().clear();
        Question question = questions.get(index);

    Label questionLabel = new Label((index + 1) + ". " + question.prompt());
        questionLabel.setWrapText(true);
        questionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox questionBox = new VBox(10);
        questionBox.setPadding(new Insets(10, 0, 20, 0));
        questionBox.getChildren().add(questionLabel);

        ToggleGroup answerGroup = new ToggleGroup();
        HBox optionsBox = new HBox(15);
        optionsBox.setPadding(new Insets(10, 0, 0, 0));

        for (AnswerScale answer : AnswerScale.values()) {
            RadioButton rb = new RadioButton(answer.getLabel());
            rb.setToggleGroup(answerGroup);
            rb.setUserData(answer);
            optionsBox.getChildren().add(rb);

            // mark selected if previously answered
            if (selectedAnswers.get(index) == answer) {
                rb.setSelected(true);
            }
        }

        answerGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                selectedAnswers.set(index, (AnswerScale) newToggle.getUserData());
            }
        });

        questionBox.getChildren().add(optionsBox);
        questionsContainer.getChildren().add(questionBox);
    }

    @FXML
    private void handleNext() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            displayQuestion(currentQuestionIndex);
            updateButtonVisibility();
        }
    }

    @FXML
    private void handlePrev() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            displayQuestion(currentQuestionIndex);
            updateButtonVisibility();
        }
    }

    private void updateButtonVisibility() {
        prevButton.setVisible(currentQuestionIndex > 0);
        nextButton.setVisible(currentQuestionIndex < questions.size() - 1);
        submitButton.setVisible(currentQuestionIndex == questions.size() - 1);
    }

    @FXML
    private void handleSubmit() {
        // Map selected AnswerScale values to integers (use their ordinal+1 or value)
        List<Integer> answers = new ArrayList<>();
        for (AnswerScale a : selectedAnswers) {
            answers.add(a == null ? 0 : a.value);
        }
        // calculateResult currently returns a QuizResult; store or pass it to the next view as needed
        quizService.calculateResult(answers);
        NavigationService.go("/com/javaninjas/careerpathway/pages/quiz/view/QuizResult.fxml");
    }

    @FXML
    private void handleQuit() {
        // Optional: Add a confirmation dialog before quitting
        // For now, just navigate back to the intro
        // NavigationService.go("/com/javaninjas/careerpathway/quiz/view/QuizIntro.fxml");
    }
}
