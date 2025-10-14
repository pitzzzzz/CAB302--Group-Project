package com.javaninjas.careerpathway.pages.dashboard.controllers;

import com.javaninjas.careerpathway.core.models.Job;
import com.javaninjas.careerpathway.core.models.Course;
import com.javaninjas.careerpathway.core.models.User;
import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.core.services.JobCacheService;
import com.javaninjas.careerpathway.db.dao.UserDao;
import com.javaninjas.careerpathway.db.connection.Database;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import com.javaninjas.careerpathway.core.services.NavigationService;

import java.sql.SQLException;

public class PathwayPopupController {

    @FXML
    private Label jobNameLabel;
    @FXML
    private Label jobDescriptionLabel;
    @FXML
    private Label salaryLabel;

    @FXML
    private Label courseNameLabel;
    @FXML
    private Label courseMajorLabel;
    @FXML
    private Label courseCodeLabel;
    @FXML
    private Label qtacCodeLabel;
    @FXML
    private Label selectionRankLabel;
    @FXML
    private Label courseDescriptionLabel;

    @FXML
    private ComboBox<String> courseComboBox; // must match fx:id in FXML

    private User currentUser;
    private Job currentJob;
    private Runnable closeHandler;

    @FXML
    public void initialize() {
        // Get logged-in user from session
        UserSession session = UserSession.getInstance();
        if (session != null) {
            currentUser = session.getLoggedInUser();
        }
    }

    public void setJob(Job job) {
        if (job == null) return;
        
        // Store reference to current job
        this.currentJob = job;

        jobNameLabel.setText(job.getJobName());
        jobDescriptionLabel.setText(job.getJobDescription());
    salaryLabel.setText(com.javaninjas.careerpathway.core.utils.CurrencyUtils.formatCurrency(job.getJobSalary()));

        // Use cached course data to avoid repeated database queries
        Course course = JobCacheService.getCourseById(job.getCourseID());
        if (course != null) {
            courseNameLabel.setText(course.getCourseName());
            courseMajorLabel.setText(course.getCourseMajor());
            courseCodeLabel.setText(course.getCourseCode());
            qtacCodeLabel.setText(course.getQtacCode());
            courseDescriptionLabel.setText(course.getDescription());
            // Try to fetch selection rank from entry requirements table and display it
            try {
                Integer rank = com.javaninjas.careerpathway.db.dao.CourseDao.getSelectionRankForCourse(course.getCourseID());
                if (rank != null) {
                    selectionRankLabel.setText(String.valueOf(rank));
                } else {
                    selectionRankLabel.setText("N/A");
                }
            } catch (Exception ex) {
                selectionRankLabel.setText("N/A");
            }
        } else {
            courseNameLabel.setText("No course found");
        }
    }

    public void setCloseHandler(Runnable handler) {
        this.closeHandler = handler;
    }

    @FXML
    private void handleSelectCourse() {
        if (currentUser == null) {
            System.out.println("No logged-in user found.");
            return;
        }

        if (currentJob == null) {
            System.out.println("No job selected.");
            return;
        }

        try {
            UserDao userDao = new UserDao(Database.getConnection());
            
            // Save the career title to suggested_career column
            String careerTitle = currentJob.getJobName();
            currentUser.setSuggestedCareer(careerTitle);
            userDao.updateSuggestedCareer(currentUser.getUserID(), careerTitle);
            System.out.println("Selected career saved to DB: " + careerTitle);
            
            // Also save the course name if available
            String selectedCourse = courseNameLabel.getText();
            if (selectedCourse != null && !selectedCourse.isEmpty() && !selectedCourse.equals("No course found")) {
                currentUser.setRecommendedCourse(selectedCourse);
                userDao.updateRecommendedCourse(currentUser.getUserID(), selectedCourse);
                System.out.println("Recommended course saved to DB: " + selectedCourse);
            }
            
            System.out.println("Career selection completed successfully!");
            
            // Close the popup after successful selection
            if (closeHandler != null) {
                closeHandler.run();
            }

            // Refresh the pathway page so saved selection is reflected immediately
            try {
                // Navigate to the pathway view (reuse application's NavigationService)
                NavigationService.go("/com/javaninjas/careerpathway/pages/dashboard/views/userPathway.fxml");
            } catch (Exception ex) {
                // Fallback: nothing critical, log for debugging
                System.err.println("Failed to reload pathway page after selecting career: " + ex.getMessage());
            }
            
        } catch (SQLException e) {
            System.err.println("Error saving career selection: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void handleClose() {
        if (closeHandler != null) {
            closeHandler.run();
        }
    }
}
