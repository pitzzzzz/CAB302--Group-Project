package com.javaninjas.careerpathway.pages.dashboard.controllers;

import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.core.models.Job;
import com.javaninjas.careerpathway.db.dao.JobDao;
import com.javaninjas.careerpathway.core.services.NavigationService;
import com.javaninjas.careerpathway.pages.components.PathwayCardController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class ExploreController {

    @FXML private FlowPane pathwayCardsContainer;
    @FXML private Button logoutBtn;

    @FXML
    public void initialize() {
        loadPathwayCards();
    }

    /**
     * Loads all job pathways and creates cards for them
     */
    private void loadPathwayCards() {
        if (pathwayCardsContainer == null) return;

        // Clear existing cards
        pathwayCardsContainer.getChildren().clear();

        try {
            // Get all jobs from database
            List<Job> jobs = JobDao.getAllJobs();

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

        try {
            NavigationService.go(
                    "/com/javaninjas/careerpathway/pages/dashboard/views/pathwayPopup.fxml",
                    (PathwayPopupController controller) -> {
                        controller.setJob(selectedJob);
                        System.out.println("Opened pathway popup for: " + selectedJob.getJobName());
                    }
            );
        } catch (Exception e) {
            System.err.println("Error opening pathway popup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles refresh pathways button click
     */
    @FXML
    private void handleRefreshPaths() {
        System.out.println("Refreshing pathway cards...");
        loadPathwayCards();
    }

    /**
     * Handles logout button click
     */
    @FXML
    private void handleLogout() {
        UserSession.getInstance().logout();
        NavigationService.go("/com/javaninjas/careerpathway/pages/login/views/LoginPage.fxml");
    }
}