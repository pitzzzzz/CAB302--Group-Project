package com.javaninjas.careerpathway.pages.quiz.controllers;

import com.javaninjas.careerpathway.core.services.NavigationService;
import com.javaninjas.careerpathway.pages.quiz.components.ProgressDots;
import com.javaninjas.careerpathway.pages.quiz.models.Question;
import com.javaninjas.careerpathway.pages.quiz.models.QuizData;
import com.javaninjas.careerpathway.pages.quiz.services.QuizService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class QuizQuestionController {

    @FXML private VBox questionsContainer;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Button submitButton;
    @FXML private ProgressDots progressDots; // <-- custom control injected

    private QuizService quizService;
    private List<List<Question>> questionSets;
    private String[] selectedAnswers;
    private int currentPageIndex = 0;
    private int totalQuestions;

    @FXML
    public void initialize() {
        // Setup quiz data
        this.quizService = new QuizService(new QuizData());
        this.questionSets = quizService.getQuestionSets();
        this.totalQuestions = (int) questionSets.stream().mapToLong(List::size).sum();
        this.selectedAnswers = new String[totalQuestions];

        // Setup progress dots
        progressDots.setTotal(questionSets.size());   // total number of pages
        progressDots.setCurrent(0);                   // highlight first page

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
            // 🔽 changed HBox → VBox so options stack vertically
            VBox optionsBox = new VBox(10);
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
        progressDots.setCurrent(pageIndex); // update dot highlight
    }

    private void updateButtonStates() {
        prevButton.setDisable(currentPageIndex == 0);
        // Only enable Next (or Submit on last page) when all questions on the current page are answered
        boolean pageComplete = isCurrentPageComplete();

        boolean isLastPage = currentPageIndex == questionSets.size() - 1;

        // Swap visibility: show Continue on non-last pages, show Submit on last page
        nextButton.setVisible(!isLastPage);
        submitButton.setVisible(isLastPage);

        if (!isLastPage) {
            nextButton.setDisable(!pageComplete);
            submitButton.setDisable(true); // hidden anyway
        } else {
            nextButton.setDisable(true);
            submitButton.setDisable(!pageComplete);
        }
    }

    /**
     * Returns true when every question on the current page has a selected answer.
     */
    private boolean isCurrentPageComplete() {
        List<Question> pageQuestions = questionSets.get(currentPageIndex);
        for (int i = 0; i < pageQuestions.size(); i++) {
            int questionIndex = currentPageIndex * 5 + i;
            if (questionIndex < 0 || questionIndex >= selectedAnswers.length) return false;
            if (selectedAnswers[questionIndex] == null) return false;
        }
        return true;
    }

    // === Button Handlers ===

    @FXML
    private void handlePrev() {
        if (currentPageIndex > 0) {
            currentPageIndex--;
            displayPage(currentPageIndex);
        }
    }

    @FXML
    private void handleNext() {
        if (currentPageIndex < questionSets.size() - 1) {
            currentPageIndex++;
            displayPage(currentPageIndex);
        }
    }

    @FXML
    private void handleSubmit() {
        // Before navigating, ensure we have a user session for this quiz-taker
        try {
            com.javaninjas.careerpathway.core.auth.UserSession existing = com.javaninjas.careerpathway.core.auth.UserSession.getInstance();
            if (existing == null) {
                // Create a lightweight session using available info. Use userID=0 for anonymous/new users.
                com.javaninjas.careerpathway.core.auth.UserSession.getInstance(
                        0,
                        "quizuser@example.com",
                        "student",
                        "Quiz",
                        "Taker",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                );
            }

            // Collect answers and persist/analyse using QuizService
            List<String> allAnswers = java.util.Arrays.asList(selectedAnswers);
            quizService.calculateResult(allAnswers);

        } catch (Exception e) {
            // don't block navigation for minor session/analytics failures
            e.printStackTrace();
        }

        // Step 1: go to loading screen and start AI analysis
        NavigationService.go(
            "/com/javaninjas/careerpathway/pages/loading/views/LoadingScreen.fxml",
            (controller) -> {
                if (controller instanceof com.javaninjas.careerpathway.pages.loading.controllers.LoadingScreenController loadingController) {
                    List<String> allAnswers = java.util.Arrays.asList(selectedAnswers);
                    loadingController.loadData(
                        () -> {
                            // Run AI analysis and persist answers
                            try {
                                return quizService.calculateResult(allAnswers);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        },
                        (result) -> {
                            // When done, go to results page
                            NavigationService.go("/com/javaninjas/careerpathway/pages/quiz/views/QuizResult.fxml");
                        }
                    );
                }
            }
        );
    }
}