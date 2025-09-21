package com.javaninjas.careerpathway.pages.dashboard.controllers;

import com.javaninjas.careerpathway.core.models.Job;
import com.javaninjas.careerpathway.core.models.Course;
import com.javaninjas.careerpathway.core.models.User;
import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.core.services.NavigationService;
import com.javaninjas.careerpathway.db.dao.CourseDao;
import com.javaninjas.careerpathway.db.dao.UserDao;
import com.javaninjas.careerpathway.db.connection.Database;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;

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
    private Label courseDescriptionLabel;

    @FXML
    private ComboBox<String> courseComboBox; // must match fx:id in FXML

    private User currentUser;
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

        jobNameLabel.setText(job.getJobName());
        jobDescriptionLabel.setText(job.getJobDescription());
        salaryLabel.setText(String.valueOf(job.getJobSalary()));

        Course course = CourseDao.getCourseById(job.getCourseID());
        if (course != null) {
            courseNameLabel.setText(course.getCourseName());
            courseMajorLabel.setText(course.getCourseMajor());
            courseCodeLabel.setText(course.getCourseCode());
            qtacCodeLabel.setText(course.getQtacCode());
            courseDescriptionLabel.setText(course.getDescription());
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

        // Get the course name directly from the label
        String selectedCourse = courseNameLabel.getText();
        if (selectedCourse != null && !selectedCourse.isEmpty() && !selectedCourse.equals("No course found")) {
            try {
                // Update the User object in memory
                currentUser.setRecommendedCourse(selectedCourse);

                // Update the database
                UserDao userDao = new UserDao(Database.getConnection());
                userDao.updateRecommendedCourse(currentUser.getUserID(), selectedCourse);

                System.out.println("Select Course button clicked! Course saved to DB: " + selectedCourse);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No valid course to save.");
        }
    }


    @FXML
    private void handleClose() {
        if (closeHandler != null) {
            closeHandler.run();
        }
    }
}
