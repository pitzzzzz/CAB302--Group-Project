package com.javaninjas.careerpathway.pages.dashboard.controllers;

import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.core.models.Job;
import com.javaninjas.careerpathway.core.services.NavigationService;
import com.javaninjas.careerpathway.core.services.JobCacheService;
import com.javaninjas.careerpathway.pages.components.PathwayCardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class ExploreController {

    @FXML private FlowPane pathwayCardsContainer;
    @FXML private Button logoutBtn;
    @FXML private TextField searchField;
    
    // Popup overlay fields
    @FXML private StackPane rootPane;
    @FXML private BorderPane mainContent;
    @FXML private Pane blurPane;
    private Node popupContent;
    @FXML public void initialize() {
        loadPathwayCards();
        
        // Initialize blur pane
        try {
            if (blurPane != null) {
                // Initially let clicks through when not visible
                blurPane.setMouseTransparent(true);
            }
        } catch (Exception ignored) {}
    }


    @FXML
    public void handleSearch(ActionEvent actionEvent) {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            loadPathwayCards();
            return;
        }
        // Filter cards based on search query using cache
        pathwayCardsContainer.getChildren().clear();
        try {
            List<Job> filteredJobs = JobCacheService.getJobsBySearch(query);
            for (Job job : filteredJobs) {
                VBox pathwayCard = createPathwayCard(job);
                if (pathwayCard != null) {
                    pathwayCardsContainer.getChildren().add(pathwayCard);
                }
            }
            System.out.println("Search found " + filteredJobs.size() + " matching jobs for query: " + query);
        } catch (Exception e) {
            System.err.println("Error during search: " + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Loads all job pathways and creates cards for them
     */
    private void loadPathwayCards() {
        if (pathwayCardsContainer == null) return;

        // Clear existing cards
        pathwayCardsContainer.getChildren().clear();

        try {
            // Get all jobs from cache or database
            List<Job> jobs = JobCacheService.getAllJobs();

            // Create a card for each job
            for (Job job : jobs) {
                VBox pathwayCard = createPathwayCard(job);
                if (pathwayCard != null) {
                    pathwayCardsContainer.getChildren().add(pathwayCard);
                }
            }

            System.out.println("Loaded " + jobs.size() + " pathway cards");

        } catch (Exception e) {
            System.err.println("Error loading pathway cards: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates a single pathway card for a job
     */
    private VBox createPathwayCard(Job job) {
        try {
            // Load the pathway card FXML
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/javaninjas/careerpathway/pages/components/pathwayCard.fxml")
            );

            VBox cardNode = loader.load();
            PathwayCardController cardController = loader.getController();

            if (cardController != null) {
                // Set the job data
                cardController.setJob(job);

                // Set up the Learn More action to open pathway popup
                cardController.setOnLearnMore(selectedJob -> {
                    openPathwayPopup(selectedJob);
                });
            }

            return cardNode;

        } catch (IOException e) {
            System.err.println("Error creating pathway card for job: " + job.getJobName());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Opens the pathway popup for the selected job
     */
    private void openPathwayPopup(Job selectedJob) {
        if (selectedJob == null) {
            System.out.println("No job selected for pathway popup.");
            return;
        }
        showPopup(selectedJob);
    }
    
    /**
     * Shows the popup overlay for the selected job
     */
    private void showPopup(Job job) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javaninjas/careerpathway/pages/dashboard/views/pathwayPopup.fxml"));
            popupContent = loader.load();
            PathwayPopupController popupController = loader.getController();
            popupController.setJob(job);
            popupController.setCloseHandler(this::hidePopup);
            
            // Remove any existing dark overlays
            try {
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

                // Also walk the scene graph to remove overlays that were attached deeper in the hierarchy
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

        } catch (Exception e) {
            System.err.println("Error showing pathway popup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Hides the popup overlay
     */
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

    /**
     * Recursively remove dark full-screen overlay nodes from the provided parent
     */
    private void removeDarkOverlaysRecursive(javafx.scene.Parent parent) {
        try {
            parent.getChildrenUnmodifiable().removeIf(node -> {
                if (node == blurPane) return false; // Don't remove our own blur pane
                String style = node.getStyle();
                return style != null && (style.contains("rgba(0,0,0") || style.contains("rgba(0, 0, 0"));
            });
            
            for (Node child : parent.getChildrenUnmodifiable()) {
                if (child instanceof javafx.scene.Parent childParent) {
                    removeDarkOverlaysRecursive(childParent);
                }
            }
        } catch (Exception ignored) {}
    }

    /**
     * Handles refresh pathways button click
     */
    @FXML
    private void handleRefreshPaths() {
        System.out.println("Refreshing pathway cards...");
        // Clear cache to ensure fresh data
        JobCacheService.clearJobsCache();
        loadPathwayCards();
    }

    /**
     * Handles logout button click
     */
    @FXML
    private void handleLogout() {
        UserSession.logout();
        NavigationService.go("/com/javaninjas/careerpathway/pages/login/views/LoginPage.fxml");
    }

}