package com.javaninjas.careerpathway.pages.components;

import com.javaninjas.careerpathway.core.models.Job;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
                // If an external handler is provided (e.g., PathwayController.showPopup), prefer it
                if (onApply != null && job != null) {
                    onApply.accept(job);
                    return; // prevent the card from creating its own overlay
                }

                // No external handler — fallback to showing job detail overlay from this card
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javaninjas/careerpathway/pages/components/jobDetail.fxml"));
                    Node overlay = loader.load();
                    JobDetailController ctrl = loader.getController();
                    if (ctrl != null) {
                        ctrl.setJob(job);

                        Scene scene = null;
                        if (root != null) scene = root.getScene();

                        Node backdrop = null;
                        Parent attachParent = null;

                        if (scene != null) {
                            // prefer explicit fx:id nodes used by PathwayController
                            Node rootPaneNode = scene.lookup("#rootPane");
                            Node mainContentNode = scene.lookup("#mainContent");

                            if (rootPaneNode instanceof StackPane) attachParent = (Parent) rootPaneNode;
                            else if (scene.getRoot() instanceof StackPane) attachParent = scene.getRoot();
                            else if (scene.getRoot() instanceof Pane) attachParent = scene.getRoot();

                            backdrop = mainContentNode != null ? mainContentNode : scene.getRoot();
                        }

                        // Attach overlay to the top-level stack/pane so it sits above the pathway UI
                        if (attachParent instanceof StackPane) {
                            StackPane stack = (StackPane) attachParent;
                            StackPane.setAlignment(overlay, javafx.geometry.Pos.CENTER);
                            stack.getChildren().add(overlay);
                        } else if (attachParent instanceof Pane) {
                            ((Pane) attachParent).getChildren().add(overlay);
                        } else if (root != null && root.getScene() != null && root.getScene().getRoot() instanceof Pane) {
                            ((Pane) root.getScene().getRoot()).getChildren().add(overlay);
                        }

                        // Give the overlay controller the backdrop node so it can apply/remove blur
                        ctrl.setBackdropNode(backdrop);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
