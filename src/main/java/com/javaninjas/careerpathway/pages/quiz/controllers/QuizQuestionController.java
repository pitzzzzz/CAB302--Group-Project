package com.javaninjas.careerpathway.pages.quiz.controllers;

import com.javaninjas.careerpathway.core.services.NavigationService;
import com.javaninjas.careerpathway.pages.quiz.model.AnswerScale;
import com.javaninjas.careerpathway.pages.quiz.model.Question;
import com.javaninjas.careerpathway.pages.quiz.model.Quiz;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class QuizQuestionController {

    @FXML
    private Label questionNumberLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label questionLabel;

    @FXML
    private HBox choicesRow;

    @FXML
    private VBox questionsContainer;

    @FXML
    private Label stageLabel;

    @FXML
    private ToggleGroup choicesGroup;

    @FXML
    private Button continueButton;

    @FXML
    private Label feedbackLabel;

    private Quiz quiz;

    // number of questions per stage/page
    private static final int QUESTIONS_PER_STAGE = 5;

    // keep simple per-question toggle groups for the currently visible stage
    private final List<ToggleGroup> stageToggleGroups = new ArrayList<>();

    private int currentStage = 0; // 0-based index of stage (0..3)

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        displayQuestion();
    }

    private void displayQuestion() {
        if (quiz == null) return;

        // Render the current stage (5 questions per stage)
        questionsContainer.getChildren().clear();
        stageToggleGroups.clear();

        int totalQuestions = quiz.getTotalQuestions();
        int start = currentStage * QUESTIONS_PER_STAGE;
        int end = Math.min(start + QUESTIONS_PER_STAGE, totalQuestions);

        for (int qi = start; qi < end; qi++) {
            Question q = quiz.getQuestion(qi);
            // Build question block
            VBox qbox = new VBox(6);
            Label qlabel = new Label(q.prompt());
            qlabel.setWrapText(true);
            HBox choices = new HBox(10);
            ToggleGroup tg = new ToggleGroup();
            stageToggleGroups.add(tg);

            for (int opt = 0; opt < q.options().size(); opt++) {
                RadioButton rb = new RadioButton(q.options().get(opt));
                rb.setToggleGroup(tg);
                rb.setUserData(opt);
                choices.getChildren().add(rb);
            }

            qbox.getChildren().addAll(qlabel, choices);
            questionsContainer.getChildren().add(qbox);
        }

        stageLabel.setText("Stage " + (currentStage + 1) + " of 4");
        updateStageIndicators();
    }

    private void updateStageIndicators() {
        // Find the stage indicators HBox in the scene if present
        // We search the scene graph starting from questionNumberLabel's scene
        if (questionNumberLabel.getScene() == null) return;

        HBox indicators = (HBox) questionNumberLabel.getScene().lookup("#stageIndicators");
        if (indicators == null) return;

    int currentIndex = Math.max(0, quiz.getCurrentQuestionIndex());
        int currentStage = (currentIndex / QUESTIONS_PER_STAGE); // 0-based

        for (int i = 0; i < indicators.getChildren().size(); i++) {
            Node n = indicators.getChildren().get(i);
            if (n instanceof Circle c) {
                if (i <= currentStage) {
                    c.setFill(Color.web("#374151"));
                } else {
                    c.setFill(Color.web("#d1d5db"));
                }
            }
        }
    }

    @FXML
    private void handleContinueButtonAction() {
        // Collect answers for all questions in the current stage
        int start = currentStage * QUESTIONS_PER_STAGE;
        boolean allAnswered = true;
        for (int i = 0; i < stageToggleGroups.size(); i++) {
            ToggleGroup tg = stageToggleGroups.get(i);
            if (tg.getSelectedToggle() == null) {
                allAnswered = false;
                break;
            }
            int optIndex = (int) tg.getSelectedToggle().getUserData();
            AnswerScale a = AnswerScale.fromIndex(optIndex);
            quiz.answerQuestionAt(start + i, a);
        }

        if (!allAnswered) {
            feedbackLabel.setText("Please answer all questions on this page.");
            return;
        }

        // move to next stage or finish
        if ((currentStage + 1) * QUESTIONS_PER_STAGE >= quiz.getTotalQuestions()) {
            // finished all stages
            NavigationService.go("/com/javaninjas/careerpathway/quiz/view/QuizResult.fxml");
            return;
        }

        currentStage++;
        displayQuestion();
    }
}
