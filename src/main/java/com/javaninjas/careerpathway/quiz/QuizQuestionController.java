package com.javaninjas.careerpathway.quiz;

import com.javaninjas.careerpathway.quiz.model.Question;
import com.javaninjas.careerpathway.app.NavigationService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestionController {
    private List<List<Question>> questionSets;

    @FXML private HBox progressDots;

    @FXML private Label titleLabel;
    @FXML private Label q1Label;
    @FXML private Label q2Label;
    @FXML private Label q3Label;
    @FXML private Label q4Label;
    @FXML private Label q5Label;

    @FXML private VBox q1Options;
    @FXML private VBox q2Options;
    @FXML private VBox q3Options;
    @FXML private VBox q4Options;
    @FXML private VBox q5Options;

    private ToggleGroup group1;
    private ToggleGroup group2;
    private ToggleGroup group3;
    private ToggleGroup group4;
    private ToggleGroup group5;

    // answersPerSet stores 5 ints per set (-1 = unanswered)
    private final List<int[]> answersPerSet = new ArrayList<>();
    private int currentSetIndex = 0;

    @FXML private Label feedbackLabel;
    @FXML private Button continueButton;
    @FXML private Button previousButton;

    @FXML
    private void initialize() {
        questionSets = QuizData.getQuizQuestionSets();

        // prepare answersPerSet with -1 placeholders
        answersPerSet.clear();
        for (int i = 0; i < questionSets.size(); i++) {
            answersPerSet.add(new int[]{ -1, -1, -1, -1, -1 });
        }

        // build progress dots UI
        buildProgressDots();

        // render first set
        renderCurrentSet();

        if (continueButton != null) continueButton.setOnAction(e -> onContinue());
        if (previousButton != null) previousButton.setOnAction(e -> onPrevious());
    }

    private void buildProgressDots() {
        if (progressDots == null) return;
        progressDots.getChildren().clear();
        for (int i = 0; i < questionSets.size(); i++) {
            Circle c = new Circle(6);
            c.getStyleClass().add("dot");
            progressDots.getChildren().add(c);
        }
        updateProgressDots();
    }

    private void updateProgressDots() {
        if (progressDots == null) return;
        for (int i = 0; i < progressDots.getChildren().size(); i++) {
            if (!(progressDots.getChildren().get(i) instanceof Circle)) continue;
            Circle c = (Circle) progressDots.getChildren().get(i);
            c.getStyleClass().removeAll("dot-active", "dot");
            if (i == currentSetIndex) c.getStyleClass().add("dot-active");
            else c.getStyleClass().add("dot");
        }
    }

    private void renderCurrentSet() {
        List<Question> set = questionSets.get(currentSetIndex);
        int base = currentSetIndex * 5;
        titleLabel.setText("Questions " + (base + 1) + "–" + (base + 5));
        q1Label.setText((base + 1) + ". " + set.get(0).prompt());
        q2Label.setText((base + 2) + ". " + set.get(1).prompt());
        q3Label.setText((base + 3) + ". " + set.get(2).prompt());
        q4Label.setText((base + 4) + ". " + set.get(3).prompt());
        q5Label.setText((base + 5) + ". " + set.get(4).prompt());

        // clear previous option nodes
        q1Options.getChildren().clear();
        q2Options.getChildren().clear();
        q3Options.getChildren().clear();
        q4Options.getChildren().clear();
        q5Options.getChildren().clear();

        // build options and attach group listeners bound to question index
        group1 = buildOptionsForQuestion(set.get(0), q1Options, 0);
        group2 = buildOptionsForQuestion(set.get(1), q2Options, 1);
        group3 = buildOptionsForQuestion(set.get(2), q3Options, 2);
        group4 = buildOptionsForQuestion(set.get(3), q4Options, 3);
        group5 = buildOptionsForQuestion(set.get(4), q5Options, 4);

        // pre-select if answers present
        int[] answers = answersPerSet.get(currentSetIndex);
        restoreSelection(group1, answers[0]);
        restoreSelection(group2, answers[1]);
        restoreSelection(group3, answers[2]);
        restoreSelection(group4, answers[3]);
        restoreSelection(group5, answers[4]);

        // clear feedback
        if (feedbackLabel != null) feedbackLabel.setText("");

        // update progress dots
        updateProgressDots();

        // ensure labels wrap to available width
        bindLabelWidthToContainer(q1Label, q1Options);
        bindLabelWidthToContainer(q2Label, q2Options);
        bindLabelWidthToContainer(q3Label, q3Options);
        bindLabelWidthToContainer(q4Label, q4Options);
        bindLabelWidthToContainer(q5Label, q5Options);
    }

    private void bindLabelWidthToContainer(Label label, VBox optionsContainer) {
        try {
            Region parent = (Region) optionsContainer.getParent();
            label.setWrapText(true);
            label.maxWidthProperty().bind(parent.widthProperty().subtract(12));
        } catch (ClassCastException ignored) {}
    }

    private void restoreSelection(ToggleGroup group, int selectedIndex) {
        if (group == null || selectedIndex < 0) return;
        if (selectedIndex >= 0 && selectedIndex < group.getToggles().size()) {
            Toggle t = group.getToggles().get(selectedIndex);
            if (t != null) {
                Platform.runLater(() -> t.setSelected(true));
            }
        }
    }

    private int selectedIndex(ToggleGroup g) {
        if (g == null || g.getSelectedToggle() == null) return -1;
        return g.getToggles().indexOf(g.getSelectedToggle());
    }

    /**
     * Build option ToggleButtons for a question and wire them to update answersPerSet[currentSetIndex][qIndex].
     */
    private ToggleGroup buildOptionsForQuestion(Question q, VBox container, int qIndex) {
        ToggleGroup group = new ToggleGroup();
        List<String> opts = q.options();
        for (int i = 0; i < opts.size(); i++) {
            String opt = opts.get(i);
            ToggleButton tb = new ToggleButton(opt);
            tb.setPrefHeight(36);
            tb.setWrapText(true);
            tb.setAlignment(Pos.CENTER_LEFT);
            tb.setMaxWidth(Double.MAX_VALUE);
            tb.getStyleClass().add("quiz-option");
            tb.setToggleGroup(group);
            // expand to container width
            tb.maxWidthProperty().bind(container.widthProperty());
            VBox.setVgrow(tb, Priority.NEVER);
            container.getChildren().add(tb);
        }

        // selection listener updates answersPerSet for this set and question index
        group.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            // style management (visual feedback)
            final String unselected = "-fx-background-radius:14; -fx-border-color:#9ca3af; -fx-border-radius:14; -fx-background-color: white; -fx-text-fill: #111827;";
            final String selected = "-fx-background-radius:14; -fx-border-color:#3b82f6; -fx-border-radius:14; -fx-background-color: #3b82f6; -fx-text-fill: white;";

            for (Toggle t : group.getToggles()) {
                if (t instanceof ToggleButton tb) {
                    tb.setStyle(unselected);
                }
            }
            if (newT instanceof ToggleButton nb) {
                nb.setStyle(selected);
            }

            // store selection index in answersPerSet for the active set
            int selIdx = selectedIndex(group);
            answersPerSet.get(currentSetIndex)[qIndex] = selIdx;
        });

        return group;
    }

    @FXML
    private void onContinue() {
        // verify all five answers selected for current set
        int[] answers = answersPerSet.get(currentSetIndex);
        for (int i = 0; i < 5; i++) {
            if (answers[i] < 0) {
                if (feedbackLabel != null) feedbackLabel.setText("Please answer all questions before continuing.");
                return;
            }
        }

        // if more sets remain, advance
        if (currentSetIndex < questionSets.size() - 1) {
            currentSetIndex++;
            renderCurrentSet();
            return;
        }

        // otherwise flatten answers and compute result
        List<Integer> accumulatedAnswers = new ArrayList<>();
        for (int[] setAnswers : answersPerSet) {
            for (int v : setAnswers) accumulatedAnswers.add(v);
        }

        QuizService svc = new QuizService(questionSets);
        var result = svc.calculateResult(accumulatedAnswers);

        // Save suggestion and navigate to result view
        var repo = com.javaninjas.careerpathway.quiz.InMemoryQuizSuggestionRepository.getInstance();
        var suggestion = new com.javaninjas.careerpathway.quiz.model.QuizPathwaySuggestion(result.title(), result.description(), accumulatedAnswers, "anonymous");
        repo.saveSuggestion(suggestion);

        NavigationService.go("/com/javaninjas/careerpathway/quiz/view/QuizResult.fxml");
    }

    @FXML
    private void onPrevious() {
        // If at the first set, do nothing (or optionally navigate back to intro)
        if (currentSetIndex <= 0) {
            // optionally navigate back to intro:
            // NavigationService.go("/com/javaninjas/careerpathway/quiz/view/QuizIntro.fxml");
            return;
        }
        // just move back and render — previous answers are retained in answersPerSet
        currentSetIndex--;
        renderCurrentSet();
    }
}
