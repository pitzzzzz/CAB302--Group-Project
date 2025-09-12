package com.javaninjas.careerpathway.quiz;

import com.javaninjas.careerpathway.quiz.model.Question;
import com.javaninjas.careerpathway.app.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    // ToggleButtons (left/right) for each question
    @FXML private ToggleButton q1bLeft;
    @FXML private ToggleButton q1bRight;

    @FXML private ToggleButton q2bLeft;
    @FXML private ToggleButton q2bRight;

    @FXML private ToggleButton q3bLeft;
    @FXML private ToggleButton q3bRight;

    @FXML private ToggleButton q4bLeft;
    @FXML private ToggleButton q4bRight;

    @FXML private ToggleButton q5bLeft;
    @FXML private ToggleButton q5bRight;

    // internal ToggleGroups for each question (two options)
    private ToggleGroup group1;
    private ToggleGroup group2;
    private ToggleGroup group3;
    private ToggleGroup group4;
    private ToggleGroup group5;

    @FXML private Label feedbackLabel;
    @FXML private Button continueButton;

    @FXML
    private void initialize() {
    questionSets = com.javaninjas.careerpathway.quiz.QuizData.getQuizQuestionSets();
        // Load first set (5 questions) into the labels
        List<Question> set = questionSets.get(0);
        q1Label.setText(set.get(0).prompt());
        q2Label.setText(set.get(1).prompt());
        q3Label.setText(set.get(2).prompt());
        q4Label.setText(set.get(3).prompt());
        q5Label.setText(set.get(4).prompt());


    // create ToggleGroups and assign left/right buttons so only one can be selected per question
    group1 = new ToggleGroup();
    q1bLeft.setToggleGroup(group1); q1bRight.setToggleGroup(group1);

    group2 = new ToggleGroup();
    q2bLeft.setToggleGroup(group2); q2bRight.setToggleGroup(group2);

    group3 = new ToggleGroup();
    q3bLeft.setToggleGroup(group3); q3bRight.setToggleGroup(group3);

    group4 = new ToggleGroup();
    q4bLeft.setToggleGroup(group4); q4bRight.setToggleGroup(group4);

    group5 = new ToggleGroup();
    q5bLeft.setToggleGroup(group5); q5bRight.setToggleGroup(group5);

        // add listeners to update visual selected style
        addSelectionListener(group1);
        addSelectionListener(group2);
        addSelectionListener(group3);
        addSelectionListener(group4);
        addSelectionListener(group5);

        if (continueButton != null) {
            continueButton.setOnAction(e -> onContinue());
        }
    }

    private int selectedIndex(ToggleGroup g) {
        if (g == null || g.getSelectedToggle() == null) return -1;
        return g.getToggles().indexOf(g.getSelectedToggle());
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

        // Convert selections to a simple list of ints and calculate result
        java.util.List<Integer> answers = java.util.Arrays.asList(a1, a2, a3, a4, a5);
        QuizService svc = new QuizService(questionSets);
        var result = svc.calculateResult(answers);

        // Save suggestion (in-memory) and navigate to results
    var repo = new com.javaninjas.careerpathway.quiz.InMemoryQuizSuggestionRepository();
    var suggestion = new com.javaninjas.careerpathway.quiz.model.QuizPathwaySuggestion(result.title(), result.description(), answers, "anonymous");
    repo.saveSuggestion(suggestion);

        // Pass control to result view
        NavigationService.go("/com/javaninjas/careerpathway/quiz/view/QuizResult.fxml");
    }
}
