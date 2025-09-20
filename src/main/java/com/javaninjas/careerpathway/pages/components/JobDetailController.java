package com.javaninjas.careerpathway.pages.components;

import com.javaninjas.careerpathway.core.models.Job;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;

import java.util.function.Consumer;

public class JobDetailController {

    @FXML private StackPane root;
    @FXML private Pane card;
    @FXML private Label titleLabel;
    @FXML private Label descLabel;
    @FXML private Label salaryLabel;
    @FXML private Button closeBtn;
    @FXML private Button selectBtn;

    private Consumer<Job> onSelect;
    private Job job;
    private Node backdropNode;

    public void setOnSelect(Consumer<Job> handler) {
        this.onSelect = handler;
    }

    public void setBackdropNode(Node node) {
        this.backdropNode = node;
        if (this.backdropNode != null) {
            this.backdropNode.setEffect(new GaussianBlur(8));
        }
    }

    public void setJob(Job job) {
        this.job = job;
        if (job != null) {
            titleLabel.setText(job.getJobName());
            descLabel.setText(job.getJobDescription());
            salaryLabel.setText("Avg salary - $" + job.getJobSalary());
        }
    }

    @FXML
    private void initialize() {
        closeBtn.setOnAction(evt -> closeOverlay());
        selectBtn.setOnAction(evt -> {
            if (onSelect != null && job != null) onSelect.accept(job);
            closeOverlay();
        });
    }

    private void closeOverlay() {
        if (backdropNode != null) backdropNode.setEffect(null);
        if (root != null && root.getParent() instanceof Pane parent) {
            parent.getChildren().remove(root);
        }
    }
}