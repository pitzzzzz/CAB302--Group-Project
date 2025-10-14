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

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Scene;

public class QuizQuestionController {

    @FXML private VBox questionsContainer;
    @FXML private ScrollPane cardScroll;
    @FXML private VBox questionsBox;
    @FXML private VBox headerBox;
    @FXML private HBox controlsBox;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Button submitButton;
    @FXML private ProgressDots progressDots; // <-- custom control injected

    private QuizService quizService;
    private List<List<Question>> questionSets;
    private String[] selectedAnswers;
    private int currentPageIndex = 0;
    private int totalQuestions;
    private int pageSize = 5; // default

    @FXML
    public void initialize() {
        // Setup quiz data service
        this.quizService = new QuizService(new QuizData());

        // Wait for scene to be ready so we can measure window height and compute page size
        questionsContainer.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Platform.runLater(() -> setupResponsivePaging(newScene));
            }
        });
    }

    private void setupResponsivePaging(Scene scene) {
    // Measure available height; header/control heights can change during layout so read them now
    double sceneHeight = scene.getHeight();
    double headerHeight = headerBox != null ? headerBox.getHeight() : 0;
    double controlsHeight = controlsBox != null ? controlsBox.getHeight() : 0;

    // Conservative padding / margins inside layout
    double verticalMargins = 100; // header/footer/padding cushion (reduced slightly)

    double availableHeight = Math.max(220, sceneHeight - headerHeight - controlsHeight - verticalMargins);

    // Estimate per-question vertical space. Use a more conservative value to avoid overflow
    double perQuestionEstimate = 140; // px per question (more conservative)

    int computedPageSize = Math.max(3, (int) Math.floor(availableHeight / perQuestionEstimate));
    computedPageSize = Math.min(computedPageSize, 5); // clamp to [3,5]

        this.pageSize = computedPageSize;

        // Build paged sets from flat question list
        List<Question> allQuestions = quizService.getQuestions();
        this.totalQuestions = allQuestions.size();
        // Preserve any previously selected answers when recalculating paging
        String[] oldAnswers = this.selectedAnswers;
        this.selectedAnswers = new String[totalQuestions];
        if (oldAnswers != null) {
            System.arraycopy(oldAnswers, 0, this.selectedAnswers, 0, Math.min(oldAnswers.length, this.selectedAnswers.length));
        }
        this.questionSets = chunkQuestions(allQuestions, pageSize);

        // Setup progress dots and show first page
        progressDots.setTotal(questionSets.size());
        progressDots.setCurrent(0);
        displayPage(0);

        // Ensure the questionsContainer itself doesn't exceed the available area (leave some inner padding)
        if (questionsContainer != null) {
            double cardPref = Math.max(200, availableHeight - 40);
            questionsContainer.setPrefHeight(cardPref);
            questionsContainer.setMaxHeight(cardPref);
        }

        // Listen to stage height changes to adapt dynamically and re-measure header/control sizes each time
        scene.getWindow().heightProperty().addListener((o, oldH, newH) -> {
            Platform.runLater(() -> {
                double curSceneH = scene.getHeight();
                double curHeaderH = headerBox != null ? headerBox.getHeight() : 0;
                double curControlsH = controlsBox != null ? controlsBox.getHeight() : 0;
                double curAvailable = Math.max(220, curSceneH - curHeaderH - curControlsH - verticalMargins);
                int newPageSize = Math.max(3, (int) Math.floor(curAvailable / perQuestionEstimate));
                newPageSize = Math.min(newPageSize, 5);
                if (newPageSize != this.pageSize) {
                    this.pageSize = newPageSize;
                    List<Question> allQ = quizService.getQuestions();
                    // Preserve answers across recompute
                    String[] prevAnswers = this.selectedAnswers;
                    this.questionSets = chunkQuestions(allQ, pageSize);
                    this.totalQuestions = allQ.size();
                    this.selectedAnswers = new String[totalQuestions];
                    if (prevAnswers != null) {
                        System.arraycopy(prevAnswers, 0, this.selectedAnswers, 0, Math.min(prevAnswers.length, this.selectedAnswers.length));
                    }
                    progressDots.setTotal(questionSets.size());
                    // ensure currentPageIndex remains in range
                    if (currentPageIndex >= questionSets.size()) currentPageIndex = questionSets.size() - 1;
                    // update card preferred height
                    if (questionsContainer != null) {
                        double cardPref = Math.max(200, curAvailable - 40);
                        questionsContainer.setPrefHeight(cardPref);
                        questionsContainer.setMaxHeight(cardPref);
                    }
                    displayPage(currentPageIndex);
                }
            });
        });
    }

    private List<List<Question>> chunkQuestions(List<Question> all, int size) {
        List<List<Question>> pages = new ArrayList<>();
        for (int i = 0; i < all.size(); i += size) {
            int end = Math.min(all.size(), i + size);
            pages.add(new ArrayList<>(all.subList(i, end)));
        }
        return pages;
    }

    private void displayPage(int pageIndex) {
    // render into the internal questionsBox so we can optionally enable internal scrolling
    questionsBox.getChildren().clear();
    List<Question> pageQuestions = questionSets.get(pageIndex);

        for (int i = 0; i < pageQuestions.size(); i++) {
            Question question = pageQuestions.get(i);
            final int questionIndex = pageIndex * pageSize + i;

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
            questionsBox.getChildren().add(questionBox);
        }

        updateButtonStates();
        progressDots.setCurrent(pageIndex); // update dot highlight
    // After layout, ensure the rendered content fits the card; if not, reduce page size
    ensurePageFits(pageIndex);
    }

    /**
     * Checks whether the currently rendered page fits inside questionsContainer's pref height.
     * If it overflows, reduce pageSize and rebuild pages (preserving answers) until it fits or pageSize==1.
     */
    private void ensurePageFits(int pageIndex) {
        Platform.runLater(() -> {
            if (questionsContainer == null) return;
            double rendered = questionsContainer.getBoundsInParent().getHeight();
            double allowed = questionsContainer.getPrefHeight() > 0 ? questionsContainer.getPrefHeight() : questionsContainer.getHeight();
            // If allowed is zero (not yet laid out), bail out for now
            if (allowed <= 0) return;

            int currentIdx = pageIndex; // use mutable local index
            // If rendered content exceeds allowed space and we can shrink pageSize, do so
            while (rendered > allowed && pageSize > 3) {
                int newPageSize = pageSize - 1;
                // Preserve answers
                String[] prevAnswers = this.selectedAnswers;
                List<Question> allQ = quizService.getQuestions();
                this.pageSize = newPageSize;
                this.questionSets = chunkQuestions(allQ, pageSize);
                this.totalQuestions = allQ.size();
                this.selectedAnswers = new String[totalQuestions];
                if (prevAnswers != null) {
                    System.arraycopy(prevAnswers, 0, this.selectedAnswers, 0, Math.min(prevAnswers.length, this.selectedAnswers.length));
                }
                progressDots.setTotal(questionSets.size());
                if (currentIdx >= questionSets.size()) currentIdx = questionSets.size() - 1;
                // Re-render the page and re-measure
                    questionsBox.getChildren().clear();
                List<Question> pageQuestions = questionSets.get(currentIdx);
                for (int i = 0; i < pageQuestions.size(); i++) {
                    Question question = pageQuestions.get(i);
                    final int questionIndex = currentIdx * pageSize + i;

                    Label questionLabel = new Label((questionIndex + 1) + ". " + question.prompt());
                    questionLabel.setWrapText(true);
                    questionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                    VBox questionBox = new VBox(10);
                    questionBox.setPadding(new Insets(10, 0, 20, 0));
                    questionBox.getChildren().add(questionLabel);

                    ToggleGroup answerGroup = new ToggleGroup();
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
                        questionsBox.getChildren().add(questionBox);
                }
                // force a layout pass and re-measure
                    questionsBox.applyCss();
                    questionsBox.layout();
                    rendered = questionsBox.getBoundsInParent().getHeight();
                    allowed = cardScroll.getViewportBounds().getHeight();
            }
                // If after reducing to minimum page size we still overflow, enable internal scrolling
                if (rendered > allowed && pageSize == 3) {
                    cardScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                } else {
                    cardScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                }
            // Update buttons/progress after possible changes
            updateButtonStates();
            progressDots.setCurrent(currentIdx);
        });
    }

    private void updateButtonStates() {
        prevButton.setDisable(currentPageIndex == 0);
        // Only enable Next (or Submit on last page) when all questions on the current page are answered
        boolean pageComplete = isCurrentPageComplete();

        boolean isLastPage = currentPageIndex == questionSets.size() - 1;

        // Swap visibility: show Continue on non-last pages, show Submit on last page
        nextButton.setVisible(!isLastPage);
        nextButton.setManaged(!isLastPage); // hides space when not visible
        submitButton.setVisible(isLastPage);
        submitButton.setManaged(isLastPage); // shows space only on last page

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
            int questionIndex = currentPageIndex * pageSize + i;
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