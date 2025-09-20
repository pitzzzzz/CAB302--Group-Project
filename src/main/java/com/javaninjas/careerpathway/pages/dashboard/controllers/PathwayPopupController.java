package com.javaninjas.careerpathway.pages.dashboard.controllers;

import com.javaninjas.careerpathway.core.models.Job;
import com.javaninjas.careerpathway.core.models.Course;
import com.javaninjas.careerpathway.core.services.NavigationService;
import com.javaninjas.careerpathway.db.dao.CourseDao;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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

    @FXML
    private void handleBack() {
        NavigationService.goBack();
    }
}
