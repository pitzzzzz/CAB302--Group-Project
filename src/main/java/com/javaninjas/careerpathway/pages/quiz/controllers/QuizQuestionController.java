package com.javaninjas.careerpathway.pages.quiz.controllers;

import com.javaninjas.careerpathway.core.services.NavigationService;
import com.javaninjas.careerpathway.pages.quiz.models.Question;
import com.javaninjas.careerpathway.pages.quiz.models.QuizData;
import com.javaninjas.careerpathway.pages.quiz.services.QuizService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

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
    private List<List<Question>> questionSets;
    private String[] selectedAnswers;
    private int currentPageIndex = 0;
    private int totalQuestions;

    @FXML
    public void initialize() {
        this.quizService = new QuizService(new QuizData());
        this.questionSets = quizService.getQuestionSets();
        this.totalQuestions = (int) questionSets.stream().mapToLong(List::size).sum();
        this.selectedAnswers = new String[totalQuestions];
        displayPage(currentPageIndex);
        updateButtonVisibility();
    }

    private void displayPage(int pageIndex) {
        questionsContainer.getChildren().clear();
        List<Question> pageQuestions = questionSets.get(pageIndex);

        for (int i = 0; i < pageQuestions.size(); i++) {
            Question question = pageQuestions.get(i);
            int questionIndex = pageIndex * 5 + i;

            Label questionLabel = new Label((questionIndex + 1) + ". " + question.prompt());
            questionLabel.setWrapText(true);
            questionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            VBox questionBox = new VBox(10);
            questionBox.setPadding(new Insets(10, 0, 20, 0));
            questionBox.getChildren().add(questionLabel);

            ToggleGroup answerGroup = new ToggleGroup();
            HBox optionsBox = new HBox(15);
            optionsBox.setPadding(new Insets(10, 0, 0, 0));

            for (String answer : question.options()) {
                RadioButton rb = new RadioButton(answer);
                rb.setToggleGroup(answerGroup);
                rb.setUserData(answer);
                optionsBox.getChildren().add(rb);

                if (selectedAnswers[questionIndex] != null && selectedAnswers[questionIndex].equals(answer)) {
                    rb.setSelected(true);
                }
            }

            answerGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    selectedAnswers[questionIndex] = (String) newToggle.getUserData();
                }
            });

            questionBox.getChildren().add(optionsBox);
            questionsContainer.getChildren().add(questionBox);
        }
    }

    @FXML
    private void handleNext() {
        if (currentPageIndex < questionSets.size() - 1) {
            currentPageIndex++;
            displayPage(currentPageIndex);
            updateButtonVisibility();
        }
    }

    @FXML
    private void handlePrev() {
        if (currentPageIndex > 0) {
            currentPageIndex--;
            displayPage(currentPageIndex);
            updateButtonVisibility();
        }
    }

    private void updateButtonVisibility() {
        prevButton.setVisible(currentPageIndex > 0);
        nextButton.setVisible(currentPageIndex < questionSets.size() - 1);
        submitButton.setVisible(currentPageIndex == questionSets.size() - 1);
    }

    @FXML
    private void handleSubmit() {
        List<String> answers = Arrays.asList(selectedAnswers);
        quizService.calculateResult(answers);
        NavigationService.go("/com/javaninjas/careerpathway/pages/quiz/views/QuizResult.fxml");
    }

    @FXML
    private void handleQuit() {
        // Optional: Add a confirmation dialog before quitting
        // For now, just navigate back to the intro
        // NavigationService.go("/com/javaninjas/careerpathway/pages/quiz/views/QuizIntro.fxml");
    }
}