package com.javaninjas.careerpathway.pages.dashboard.controllers;

import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.core.services.NavigationService;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PathwayController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private Label welcomeLabel;

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
        // For now, the FXML contains placeholder jobs per the design.
    }

    @FXML
    private void handleLogout() {
        UserSession.logout();
        NavigationService.go("/com/javaninjas/careerpathway/pages/login/views/LoginPage.fxml");
    }
}
