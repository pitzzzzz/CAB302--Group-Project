package com.javaninjas.careerpathway.pages.dashboard.controllers;

import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.core.models.Job;
import com.javaninjas.careerpathway.db.dao.JobDao;
import com.javaninjas.careerpathway.core.services.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

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
        Job selectedJob = jobListView.getSelectionModel().getSelectedItem();
        if (selectedJob != null) {
            System.out.println("Explore clicked for: " + selectedJob.getJobName());
            NavigationService.go("/com/javaninjas/careerpathway/pages/dashboard/views/pathwayPopup.fxml");

        }
    }
    @FXML
    private void handleLogout() {
        UserSession.getInstance().logout();
        NavigationService.go("/com/javaninjas/careerpathway/pages/login/views/LoginPage.fxml");
    }


}
