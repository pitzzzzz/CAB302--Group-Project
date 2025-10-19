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
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExploreController {

    @FXML private FlowPane pathwayCardsContainer;
    @FXML private Button logoutBtn;
    @FXML private Button compareButton;
    @FXML private Button clearCompareButton;
    @FXML private Label compareSlotOneLabel;
    @FXML private Label compareSlotTwoLabel;
    @FXML private Label compareHintLabel;
    
    // Popup overlay fields
    @FXML private StackPane rootPane;
    @FXML private BorderPane mainContent;
    @FXML private Pane blurPane;
    private Node popupContent;

    private final List<Job> compareSelections = new ArrayList<>(2);
    private final Map<Integer, PathwayCardController> cardControllerMap = new HashMap<>();
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
    /**
     * Loads all job pathways and creates cards for them
     */
    private void loadPathwayCards() {
        if (pathwayCardsContainer == null) return;

        // Clear existing cards
        pathwayCardsContainer.getChildren().clear();
        cardControllerMap.clear();

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

        updateCompareUi();
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

                cardController.setCompareSelectionHandler(this::handleCompareSelection);
                cardController.setCompareSelected(isJobSelectedForCompare(job));
                cardControllerMap.put(job.getJobID(), cardController);
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

    private void handleCompareSelection(Job job, boolean selected) {
        if (job == null) return;

        if (selected) {
            if (isJobSelectedForCompare(job)) {
                return;
            }
            if (compareSelections.size() >= 2) {
                PathwayCardController controller = cardControllerMap.get(job.getJobID());
                if (controller != null) {
                    controller.setCompareSelected(false);
                }
                if (compareHintLabel != null) {
                    compareHintLabel.setText("Only two degrees can be compared at once.");
                    compareHintLabel.setStyle("-fx-font-size:12; -fx-text-fill:#dc2626;");
                }
                return;
            }
            compareSelections.add(job);
        } else {
            compareSelections.removeIf(j -> j.getJobID() == job.getJobID());
        }

        if (compareHintLabel != null) {
            if (compareSelections.size() == 2) {
                compareHintLabel.setText("Ready to compare! Click Compare to see the breakdown.");
                compareHintLabel.setStyle("-fx-font-size:12; -fx-text-fill:#0f766e;");
            } else {
                compareHintLabel.setText("Select any two cards below to activate comparison.");
                compareHintLabel.setStyle("-fx-font-size:12; -fx-text-fill:#64748b;");
            }
        }

        updateCompareUi();
    }

    private boolean isJobSelectedForCompare(Job job) {
        return compareSelections.stream().anyMatch(j -> j.getJobID() == job.getJobID());
    }

    private void updateCompareUi() {
        if (compareSlotOneLabel != null) {
            String text = compareSelections.size() >= 1 ? compareSelections.get(0).getJobName() : "First degree";
            compareSlotOneLabel.setText(text);
        }
        if (compareSlotTwoLabel != null) {
            String text = compareSelections.size() >= 2 ? compareSelections.get(1).getJobName() : "Second degree";
            compareSlotTwoLabel.setText(text);
        }

        if (compareButton != null) {
            compareButton.setDisable(compareSelections.size() != 2);
        }
        if (clearCompareButton != null) {
            clearCompareButton.setDisable(compareSelections.isEmpty());
        }
    }

    @FXML
    private void handleCompare(ActionEvent event) {
        if (compareSelections.size() < 2) return;
        Job first = compareSelections.get(0);
        Job second = compareSelections.get(1);
        openComparePopup(first, second);
    }

    @FXML
    private void handleClearCompare(ActionEvent event) {
        for (Job job : new ArrayList<>(compareSelections)) {
            PathwayCardController controller = cardControllerMap.get(job.getJobID());
            if (controller != null) {
                controller.setCompareSelected(false);
            }
        }
        compareSelections.clear();
        if (compareHintLabel != null) {
            compareHintLabel.setText("Select any two cards below to activate comparison.");
            compareHintLabel.setStyle("-fx-font-size:12; -fx-text-fill:#64748b;");
        }
        updateCompareUi();
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

    private void openComparePopup(Job first, Job second) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javaninjas/careerpathway/pages/dashboard/views/compareDegreesPopup.fxml"));
            popupContent = loader.load();
            com.javaninjas.careerpathway.pages.dashboard.controllers.CompareDegreesPopupController controller = loader.getController();
            controller.setJobs(first, second);
            controller.setCloseHandler(this::hidePopup);

            rootPane.getChildren().removeIf(n -> n != blurPane && n != mainContent);

            rootPane.getChildren().add(popupContent);
            StackPane.setAlignment(popupContent, Pos.CENTER);

            if (blurPane != null) {
                blurPane.setVisible(true);
                blurPane.toFront();
                blurPane.setMouseTransparent(false);
            }
            if (mainContent != null) mainContent.setEffect(new BoxBlur(5, 5, 3));
            if (popupContent != null) popupContent.toFront();
        } catch (Exception e) {
            System.err.println("Error showing comparison popup: " + e.getMessage());
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