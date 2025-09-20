package com.javaninjas.careerpathway.pages.quiz.controllers;

import com.javaninjas.careerpathway.core.services.NavigationService;
import com.javaninjas.careerpathway.pages.loading.controllers.LoadingScreenController;
import com.javaninjas.careerpathway.pages.quiz.models.Question;
import com.javaninjas.careerpathway.pages.quiz.models.QuizData;
import com.javaninjas.careerpathway.pages.quiz.services.QuizService;
import com.javaninjas.careerpathway.pages.quiz.components.ProgressDots;
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

    @FXML private VBox questionsContainer;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Button submitButton;
    @FXML private ProgressDots progressDots; // <-- custom control directly injected

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

        // Configure the progress dots
        progressDots.totalProperty().set(questionSets.size());
        progressDots.currentProperty().set(0);

        displayPage(currentPageIndex);
    }

    private void displayPage(int pageIndex) {
        questionsContainer.getChildren().clear();
        List<Question> pageQuestions = questionSets.get(pageIndex);

        for (int i = 0; i < pageQuestions.size(); i++) {
            Question question = pageQuestions.get(i);
            final int questionIndex = pageIndex * 5 + i;

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
                } else {
                    selectedAnswers[questionIndex] = null;
                }
                updateButtonStates();
            });

            questionBox.getChildren().add(optionsBox);
            questionsContainer.getChildren().add(questionBox);
        }
        updateButtonStates();
    }

    @FXML
    private void handleNext() {
        if (currentPageIndex < questionSets.size() - 1) {
            currentPageIndex++;
            displayPage(currentPageIndex);
            progressDots.currentProperty().set(currentPageIndex);
        }
    }

    @FXML
    private void handlePrev() {
        if (currentPageIndex > 0) {
            currentPageIndex--;
            displayPage(currentPageIndex);
            progressDots.currentProperty().set(currentPageIndex);
        }
    }

    private void updateButtonStates() {
        prevButton.setVisible(currentPageIndex > 0);
        boolean isLastPage = currentPageIndex == questionSets.size() - 1;
        nextButton.setVisible(!isLastPage);
        submitButton.setVisible(isLastPage);

        boolean allAnswered = areAllQuestionsOnPageAnswered();
        nextButton.setDisable(!allAnswered);
        submitButton.setDisable(!allAnswered);
    }

    private boolean areAllQuestionsOnPageAnswered() {
        List<Question> pageQuestions = questionSets.get(currentPageIndex);
        int questionsPerPage = 5;
        for (int i = 0; i < pageQuestions.size(); i++) {
            int questionIndex = currentPageIndex * questionsPerPage + i;
            if (questionIndex < selectedAnswers.length && selectedAnswers[questionIndex] == null) {
                return false;
            }
        }
        return true;
    }

    @FXML
    private void handleSubmit() {
        if (!areAllQuestionsOnPageAnswered()) {
            return;
        }
        List<String> answers = Arrays.asList(selectedAnswers);
        NavigationService.go(
                "/com/javaninjas/careerpathway/pages/loading/views/LoadingScreen.fxml",
                (LoadingScreenController controller) -> {
                    controller.loadData(
                            () -> quizService.calculateResult(answers),
                            (suggestion) -> {
                                NavigationService.go("/com/javaninjas/careerpathway/pages/quiz/views/QuizResult.fxml");
                            }
                    );
                }
        );
    }

    @FXML
    private void handleQuit() {
        // Optional: Add confirmation or return to intro
    }
}