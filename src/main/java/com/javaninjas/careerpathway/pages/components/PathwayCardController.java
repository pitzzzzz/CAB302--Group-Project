package com.javaninjas.careerpathway.pages.components;

import com.javaninjas.careerpathway.core.models.Job;
import com.javaninjas.careerpathway.core.services.NavigationService;
import com.javaninjas.careerpathway.pages.dashboard.controllers.PathwayPopupController;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Consumer;

public class PathwayCardController {

    @FXML private VBox cardRoot;
    @FXML private Label jobTitle;
    @FXML private Label jobDescription;
    @FXML private Label pathwaySalaryLabel;
    @FXML private Button learnMoreBtn;

    private Job job;
    private Consumer<Job> onLearnMore;

    @FXML
    private void initialize() {
        setupHoverAnimation();
        setupLearnMoreAction();
    }

    /**
     * Sets the job data for this card
     */
    public void setJob(Job job) {
        this.job = job;
        if (job != null) {
            updateCardDisplay();
        }
    }

    /**
     * Sets a custom action handler for the Learn More button
     */
    public void setOnLearnMore(Consumer<Job> handler) {
        this.onLearnMore = handler;
    }

    /**
     * Returns the current job
     */
    public Job getJob() {
        return job;
    }

    /**
     * Updates the visual elements with job data
     */
    private void updateCardDisplay() {
        if (job == null) return;

        // Set job title
        jobTitle.setText(job.getJobName());

        // Set job description (truncate if too long)
        String description = job.getJobDescription();
        if (description.length() > 100) {
            description = description.substring(0, 97) + "...";
        }
        jobDescription.setText(description);

        // Format and set salary
        pathwaySalaryLabel.setText("Avg salary - $" + job.getJobSalary());
    }

    /**
     * Sets up hover animation effects
     */
    private void setupHoverAnimation() {
        if (cardRoot == null) return;

        final double liftAmount = -8.0;
        final double scaleAmount = 1.03;
        final Duration animationDuration = Duration.millis(200);

        // Hover enter animation
        TranslateTransition liftUp = new TranslateTransition(animationDuration, cardRoot);
        liftUp.setToY(liftAmount);

        ScaleTransition scaleUp = new ScaleTransition(animationDuration, cardRoot);
        scaleUp.setToX(scaleAmount);
        scaleUp.setToY(scaleAmount);

        // Hover exit animation
        TranslateTransition liftDown = new TranslateTransition(animationDuration, cardRoot);
        liftDown.setToY(0);

        ScaleTransition scaleDown = new ScaleTransition(animationDuration, cardRoot);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        // Add event handlers
        cardRoot.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            liftDown.stop();
            scaleDown.stop();
            liftUp.play();
            scaleUp.play();
        });

        cardRoot.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            liftUp.stop();
            scaleUp.stop();
            liftDown.play();
            scaleDown.play();
        });
    }

    /**
     * Sets up the Learn More button action
     */
    private void setupLearnMoreAction() {
        if (learnMoreBtn == null) return;

        learnMoreBtn.setOnAction(event -> {
            if (job == null) return;

            // Use custom handler if provided
            if (onLearnMore != null) {
                onLearnMore.accept(job);
                return;
            }

            // Default behavior: Navigate to pathway popup
            NavigationService.go(
                    "/com/javaninjas/careerpathway/pages/dashboard/views/pathwayPopup.fxml",
                    (PathwayPopupController controller) -> controller.setJob(job)
            );
        });
    }

    /**
     * Handles direct clicks on the entire card
     */
    @FXML
    private void handleCardClick() {
        if (learnMoreBtn != null) {
            learnMoreBtn.fire(); // Simulate button click
        }
    }
}