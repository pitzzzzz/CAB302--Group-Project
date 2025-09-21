package com.javaninjas.careerpathway.pages.dashboard.controllers;

import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.core.services.NavigationService;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.geometry.Pos;

import com.javaninjas.careerpathway.core.models.Job;
import com.javaninjas.careerpathway.pages.components.JobCardController;

import java.io.IOException;

public class PathwayController {

    @FXML
    private StackPane rootPane;

    @FXML
    private BorderPane mainContent;

    @FXML
    private Pane blurPane;

    @FXML
    private Label welcomeLabel;

    @FXML
    private HBox jobsContainer;

    private Node popupContent;

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
                                showPopup(j);
                            });
                        }
                        jobsContainer.getChildren().add(node);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception ignored) {}

        // Make blurPane cover the whole root and block mouse events when visible
        try {
            if (blurPane != null && rootPane != null) {
                blurPane.prefWidthProperty().bind(rootPane.widthProperty());
                blurPane.prefHeightProperty().bind(rootPane.heightProperty());
                // Initially let clicks through when not visible
                blurPane.setMouseTransparent(true);
            }
        } catch (Exception ignored) {}
    }

    private void showPopup(Job job) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javaninjas/careerpathway/pages/dashboard/views/pathwayPopup.fxml"));
            popupContent = loader.load();
            PathwayPopupController popupController = loader.getController();
            popupController.setJob(job);
            popupController.setCloseHandler(this::hidePopup);
            // Remove any existing dark overlays (for example jobDetail overlays) so we only show the main modal
            try {
                // remove overlays directly attached to rootPane or mainContent
                rootPane.getChildren().removeIf(n -> {
                    if (n == blurPane || n == mainContent) return false;
                    String s = n.getStyle();
                    if (s != null && (s.contains("rgba(0,0,0") || s.contains("rgba(0, 0, 0"))) {
                        return true; // remove dark fullscreen overlays
                    }
                    return false;
                });

                if (mainContent instanceof javafx.scene.layout.Pane pane) {
                    pane.getChildren().removeIf(n -> {
                        if (n == blurPane) return false;
                        String s = n.getStyle();
                        if (s != null && (s.contains("rgba(0,0,0") || s.contains("rgba(0, 0, 0"))) {
                            return true;
                        }
                        return false;
                    });
                }

                // also walk the scene graph to remove overlays that were attached deeper in the hierarchy
                try {
                    if (rootPane.getScene() != null && rootPane.getScene().getRoot() instanceof javafx.scene.Parent sceneRoot) {
                        removeDarkOverlaysRecursive(sceneRoot);
                    }
                } catch (Exception ignored2) {}
            } catch (Exception ignored) {}

            // Add popup to root stack and center it
            rootPane.getChildren().add(popupContent);
            StackPane.setAlignment(popupContent, Pos.CENTER);

            // Ensure the blur overlay covers the background and blocks interaction
            if (blurPane != null) {
                blurPane.setVisible(true);
                blurPane.toFront();
                // When visible we want it to intercept mouse events so clicks don't reach the underlying UI
                blurPane.setMouseTransparent(false);
            }

            // Blur the main content behind the overlay
            if (mainContent != null) mainContent.setEffect(new BoxBlur(5, 5, 3));

            // Bring popup above the blur pane
            if (popupContent != null) popupContent.toFront();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void hidePopup() {
        if (mainContent != null) mainContent.setEffect(null);
        if (blurPane != null) {
            blurPane.setVisible(false);
            // allow mouse events through when not visible
            blurPane.setMouseTransparent(true);
        }
        if (popupContent != null) {
            rootPane.getChildren().remove(popupContent);
            popupContent = null;
        }
    }

    // Recursively remove dark full-screen overlay nodes from the provided parent
    private void removeDarkOverlaysRecursive(javafx.scene.Parent parent) {
        if (parent == null) return;
        try {
            java.util.List<javafx.scene.Node> toRemove = new java.util.ArrayList<>();
            for (javafx.scene.Node n : parent.getChildrenUnmodifiable()) {
                try {
                    String s = n.getStyle();
                    if (s != null && (s.contains("rgba(0,0,0") || s.contains("rgba(0, 0, 0"))) {
                        toRemove.add(n);
                        continue;
                    }
                } catch (Exception ignored) {}

                if (n instanceof javafx.scene.Parent p) {
                    removeDarkOverlaysRecursive(p);
                }
            }

            // attempt to remove from parent if possible
            if (parent instanceof javafx.scene.layout.Pane pane) {
                for (javafx.scene.Node n : toRemove) pane.getChildren().remove(n);
            }
        } catch (Exception ignored) {}
    }

    @FXML
    private void handleLogout() {
        UserSession.logout();
        NavigationService.go("/com/javaninjas/careerpathway/pages/login/views/LoginPage.fxml");
    }
}
