package com.javaninjas.careerpathway.pages.dashboard.controllers;

import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.core.services.NavigationService;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import com.javaninjas.careerpathway.core.models.Job;
import com.javaninjas.careerpathway.pages.components.JobCardController;

public class PathwayController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private Label welcomeLabel;

    @FXML
    private HBox jobsContainer;

    @FXML
    public void initialize() {
        // Populate welcome label with the session user's name (first name preferred)
        try {
            com.javaninjas.careerpathway.core.auth.UserSession session = com.javaninjas.careerpathway.core.auth.UserSession.getInstance();
            String display = "Guest";
            if (session != null) {
                String first = session.getFirstName();
                if (first != null && !first.isEmpty()) display = first;
                else if (session.getEmail() != null && !session.getEmail().isEmpty()) display = session.getEmail();
            }
            if (welcomeLabel != null) {
                welcomeLabel.setText("Welcome " + display + ",");
            }
        } catch (Exception ignored) {
            // ignore — keep placeholder
        }
        // Highlight the 'Your Pathway' button in the included bottom nav after the scene is ready
        Platform.runLater(() -> {
            try {
                javafx.scene.Node node = rootPane.lookup("#pathwayBtn");
                if (node instanceof Button) {
                    ((Button) node).setStyle("-fx-background-color: #a8c9ef; -fx-background-radius:16; -fx-font-weight:bold;");
                }
            } catch (Exception ignored) {
                // ignore if lookup fails in some contexts
            }
        });

        // TODO: If the user has no selected career, populate the UI with recommended job cards
        // Replace placeholders: populate the jobsContainer HBox with cards loaded from jobCard.fxml
        try {
            if (jobsContainer != null) {
                Job[] jobs = new Job[] {
                    new Job(1, 0, "Software Engineer", "Design and build software applications.", 90000),
                    new Job(2, 0, "Data Analyst", "Analyse data and produce insights.", 75000),
                    new Job(3, 0, "UX Designer", "Design user experiences and interfaces.", 70000),
                    new Job(4, 0, "Network Engineer", "Maintain and design network systems.", 80000),
                    new Job(5, 0, "Product Manager", "Coordinate product development across teams.", 95000)
                };

                for (Job job : jobs) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javaninjas/careerpathway/pages/components/jobCard.fxml"));
                        Node node = loader.load();
                        JobCardController controller = loader.getController();
                        if (controller != null) {
                            controller.setJob(job);
                            controller.setOnApply(j -> {
                                // default: navigate or log; replace with NavigationService if desired
                                System.out.println("Learn more clicked for: " + j.getJobName());
                            });
                        }
                        jobsContainer.getChildren().add(node);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    @FXML
    private void handleLogout() {
        UserSession.logout();
        NavigationService.go("/com/javaninjas/careerpathway/pages/login/views/LoginPage.fxml");
    }
}
