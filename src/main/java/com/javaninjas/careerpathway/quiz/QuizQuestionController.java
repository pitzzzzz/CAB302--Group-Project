package com.javaninjas.careerpathway.quiz;

import com.javaninjas.careerpathway.quiz.model.Question;
import com.javaninjas.careerpathway.app.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
// ...existing code...
import java.util.List;

public class QuizQuestionController {
    private List<List<Question>> questionSets;

    @FXML private Label titleLabel;
    @FXML private Label q1Label;
    @FXML private Label q2Label;
    @FXML private Label q3Label;
    @FXML private Label q4Label;
    @FXML private Label q5Label;

    // containers where option ToggleButtons will be created dynamically
    @FXML private VBox q1Options;
    @FXML private VBox q2Options;
    @FXML private VBox q3Options;
    @FXML private VBox q4Options;
    @FXML private VBox q5Options;

    // internal ToggleGroups for each question
    private ToggleGroup group1;
    private ToggleGroup group2;
    private ToggleGroup group3;
    private ToggleGroup group4;
    private ToggleGroup group5;

    // accumulate answers across sets (we expect 4 sets of 5 = 20 answers)
    private final java.util.List<Integer> accumulatedAnswers = new java.util.ArrayList<>();
    private int currentSetIndex = 0;

    @FXML private Label feedbackLabel;
    @FXML private Button continueButton;

    @FXML
    private void initialize() {
    questionSets = com.javaninjas.careerpathway.quiz.QuizData.getQuizQuestionSets();
        // load and render the first set
        renderCurrentSet();
        if (continueButton != null) {
            continueButton.setOnAction(e -> onContinue());
        }
    }

    private void renderCurrentSet() {
        List<Question> set = questionSets.get(currentSetIndex);
    int base = currentSetIndex * 5;
    q1Label.setText((base + 1) + ". " + set.get(0).prompt());
    q2Label.setText((base + 2) + ". " + set.get(1).prompt());
    q3Label.setText((base + 3) + ". " + set.get(2).prompt());
    q4Label.setText((base + 4) + ". " + set.get(3).prompt());
    q5Label.setText((base + 5) + ". " + set.get(4).prompt());

        // Clear previous option buttons
        q1Options.getChildren().clear();
        q2Options.getChildren().clear();
        q3Options.getChildren().clear();
        q4Options.getChildren().clear();
        q5Options.getChildren().clear();

        // Create groups and populate options dynamically for each question
        group1 = buildOptionsForQuestion(set.get(0), q1Options);
        group2 = buildOptionsForQuestion(set.get(1), q2Options);
        group3 = buildOptionsForQuestion(set.get(2), q3Options);
        group4 = buildOptionsForQuestion(set.get(3), q4Options);
        group5 = buildOptionsForQuestion(set.get(4), q5Options);

        addSelectionListener(group1);
        addSelectionListener(group2);
        addSelectionListener(group3);
        addSelectionListener(group4);
        addSelectionListener(group5);

        // clear feedback
        if (feedbackLabel != null) feedbackLabel.setText("");

        // bind label widths to their container so long questions wrap instead of truncating
        try {
            Region r1 = (Region) q1Options.getParent();
            Region r2 = (Region) q2Options.getParent();
            Region r3 = (Region) q3Options.getParent();
            Region r4 = (Region) q4Options.getParent();
            Region r5 = (Region) q5Options.getParent();

            q1Label.setWrapText(true);
            q1Label.maxWidthProperty().bind(r1.widthProperty().subtract(12));
            q2Label.setWrapText(true);
            q2Label.maxWidthProperty().bind(r2.widthProperty().subtract(12));
            q3Label.setWrapText(true);
            q3Label.maxWidthProperty().bind(r3.widthProperty().subtract(12));
            q4Label.setWrapText(true);
            q4Label.maxWidthProperty().bind(r4.widthProperty().subtract(12));
            q5Label.setWrapText(true);
            q5Label.maxWidthProperty().bind(r5.widthProperty().subtract(12));
        } catch (ClassCastException ignored) {
            // if parent isn't a Region for some reason, skip binding; labels already have a reasonable maxWidth
        }
    }

    private int selectedIndex(ToggleGroup g) {
        if (g == null || g.getSelectedToggle() == null) return -1;
        return g.getToggles().indexOf(g.getSelectedToggle());
    }

    private ToggleGroup buildOptionsForQuestion(Question q, VBox container) {
        ToggleGroup group = new ToggleGroup();
        // create a ToggleButton for each option text
        for (String opt : q.options()) {
            ToggleButton tb = new ToggleButton(opt);
            tb.setPrefHeight(36);
            tb.setWrapText(true);
            tb.setAlignment(Pos.CENTER_LEFT);
            tb.setMaxWidth(Double.MAX_VALUE);
            tb.getStyleClass().add("quiz-option");
            tb.setToggleGroup(group);
            // make the toggle expand to the width of the question container
            tb.maxWidthProperty().bind(container.widthProperty());
            VBox.setVgrow(tb, Priority.NEVER);
            container.getChildren().add(tb);
        }
        return group;
    }

    private void addSelectionListener(ToggleGroup g) {
        final String unselected = "-fx-background-radius:14; -fx-border-color:#9ca3af; -fx-border-radius:14; -fx-background-color: white;";
        final String selected = "-fx-background-radius:14; -fx-border-color:#3b82f6; -fx-border-radius:14; -fx-background-color: #3b82f6; -fx-text-fill: white;";

        g.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            // set unselected style for all toggles in the group
            for (Toggle t : g.getToggles()) {
                if (t instanceof ToggleButton tb) {
                    tb.setStyle(unselected);
                }
            }

            if (newT instanceof ToggleButton nb) {
                nb.setStyle(selected);
            }
        });
    }

    @FXML
    private void onContinue() {
        int a1 = selectedIndex(group1);
        int a2 = selectedIndex(group2);
        int a3 = selectedIndex(group3);
        int a4 = selectedIndex(group4);
        int a5 = selectedIndex(group5);

        if (a1 < 0 || a2 < 0 || a3 < 0 || a4 < 0 || a5 < 0) {
            feedbackLabel.setText("Please answer all questions before continuing.");
            return;
        }

        // append these answers to accumulated list
        accumulatedAnswers.add(a1);
        accumulatedAnswers.add(a2);
        accumulatedAnswers.add(a3);
        accumulatedAnswers.add(a4);
        accumulatedAnswers.add(a5);

        // If there are more sets, advance and re-render; otherwise compute result
        currentSetIndex++;
        if (currentSetIndex < questionSets.size()) {
            renderCurrentSet();
            return;
        }

        // All sets answered: compute result
        QuizService svc = new QuizService(questionSets);
        var result = svc.calculateResult(accumulatedAnswers);

        // Save suggestion (in-memory) and navigate to results
    var repo = com.javaninjas.careerpathway.quiz.InMemoryQuizSuggestionRepository.getInstance();
        var suggestion = new com.javaninjas.careerpathway.quiz.model.QuizPathwaySuggestion(result.title(), result.description(), accumulatedAnswers, "anonymous");
        repo.saveSuggestion(suggestion);

        // Pass control to result view
        NavigationService.go("/com/javaninjas/careerpathway/quiz/view/QuizResult.fxml");
    }
}
