package com.javaninjas.careerpathway.pages.dashboard.controllers;

import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.core.models.Job;
import com.javaninjas.careerpathway.db.dao.JobDao;
import com.javaninjas.careerpathway.core.services.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.List;

public class ExploreController {

    @FXML
    private ListView<Job> jobListView;

    @FXML
    private Button logoutBtn;

    @FXML
    public void initialize() {
        List<Job> jobs = JobDao.getAllJobs();
        jobListView.getItems().setAll(jobs);
    }

    @FXML
    private void handleExplore() {
        // Get the selected job from your list or table
        Job selectedJob = jobListView.getSelectionModel().getSelectedItem();
        if (selectedJob == null) {
            // Optionally warn user if nothing is selected
            System.out.println("No job selected.");
            return;
        }

        // Open the popup and inject the job
        NavigationService.go(
                "/com/javaninjas/careerpathway/pages/dashboard/views/PathwayPopup.fxml",
                (PathwayPopupController controller) -> controller.setJob(selectedJob)
        );
    }

    @FXML
    private void handleLogout() {
        UserSession.getInstance().logout();
        NavigationService.go("/com/javaninjas/careerpathway/pages/login/views/LoginPage.fxml");
    }
}
