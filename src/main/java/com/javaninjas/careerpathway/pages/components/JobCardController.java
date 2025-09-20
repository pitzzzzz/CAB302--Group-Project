package com.javaninjas.careerpathway.pages.components;

import com.javaninjas.careerpathway.core.models.Job;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;

public class JobCardController {

    @FXML
    private VBox root;

    @FXML
    private Label titleLabel;

    @FXML
    private Label descLabel;

    @FXML
    private Label salaryLabel;

    @FXML
    private Button applyButton;

    private Job job;
    private Consumer<Job> onApply;

    public void setJob(Job job) {
        this.job = job;
        if (job != null) {
            titleLabel.setText(job.getJobName());
            descLabel.setText(job.getJobDescription());
            salaryLabel.setText("Avg salary - $" + job.getJobSalary());
        }
    }

    public Job getJob() { return job; }

    public void setOnApply(Consumer<Job> handler) {
        this.onApply = handler;
        if (applyButton != null) {
            applyButton.setOnAction(evt -> {
                if (onApply != null && job != null) onApply.accept(job);
            });
        }
    }

    @FXML
    private void initialize() {
        // add hover lift animation
        if (root != null) {
            final double lift = -6.0;
            final double scale = 1.02;
            final Duration dur = Duration.millis(160);

            TranslateTransition ttEnter = new TranslateTransition(dur, root);
            ttEnter.setToY(lift);
            ScaleTransition stEnter = new ScaleTransition(dur, root);
            stEnter.setToX(scale);
            stEnter.setToY(scale);

            TranslateTransition ttExit = new TranslateTransition(dur, root);
            ttExit.setToY(0);
            ScaleTransition stExit = new ScaleTransition(dur, root);
            stExit.setToX(1.0);
            stExit.setToY(1.0);

            root.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                ttExit.stop(); stExit.stop();
                ttEnter.playFromStart(); stEnter.playFromStart();
            });

            root.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                ttEnter.stop(); stEnter.stop();
                ttExit.playFromStart(); stExit.playFromStart();
            });
        }
    }
}
