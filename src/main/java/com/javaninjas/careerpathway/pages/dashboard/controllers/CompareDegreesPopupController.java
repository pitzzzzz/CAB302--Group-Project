package com.javaninjas.careerpathway.pages.dashboard.controllers;

import com.javaninjas.careerpathway.core.models.Course;
import com.javaninjas.careerpathway.core.models.Job;
import com.javaninjas.careerpathway.core.utils.CurrencyUtils;
import com.javaninjas.careerpathway.db.dao.CourseDao;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Popup controller that compares two degree pathways side-by-side, highlighting key differences.
 */
public class CompareDegreesPopupController {

    @FXML private Label comparisonSummaryLabel;
    @FXML private Label leftTitleLabel;
    @FXML private Label leftSalaryLabel;
    @FXML private Label leftCourseLabel;
    @FXML private Label leftSelectionRankLabel;
    @FXML private VBox leftHighlightsContainer;

    @FXML private Label rightTitleLabel;
    @FXML private Label rightSalaryLabel;
    @FXML private Label rightCourseLabel;
    @FXML private Label rightSelectionRankLabel;
    @FXML private VBox rightHighlightsContainer;

    @FXML private Label salaryDifferenceLabel;
    @FXML private Label skillDifferenceLabel;
    @FXML private Label additionalInsightsLabel;

    private Runnable closeHandler;

    public void setJobs(Job left, Job right) {
        if (left == null || right == null) {
            return;
        }
        populateUi(left, right);
    }

    public void setCloseHandler(Runnable closeHandler) {
        this.closeHandler = closeHandler;
    }

    @FXML
    private void handleClose() {
        if (closeHandler != null) {
            closeHandler.run();
        }
    }

    private void populateUi(Job leftJob, Job rightJob) {
        leftTitleLabel.setText(leftJob.getJobName());
        rightTitleLabel.setText(rightJob.getJobName());
        comparisonSummaryLabel.setText("Comparing " + leftJob.getJobName() + " and " + rightJob.getJobName() + ".");

        leftSalaryLabel.setText("Avg salary - " + CurrencyUtils.formatCurrency(leftJob.getJobSalary()));
        rightSalaryLabel.setText("Avg salary - " + CurrencyUtils.formatCurrency(rightJob.getJobSalary()));

        Course leftCourse = CourseDao.getCourseById(leftJob.getCourseID());
        Course rightCourse = CourseDao.getCourseById(rightJob.getCourseID());
        Integer leftRank = leftCourse != null ? CourseDao.getSelectionRankForCourse(leftCourse.getCourseID()) : null;
        Integer rightRank = rightCourse != null ? CourseDao.getSelectionRankForCourse(rightCourse.getCourseID()) : null;

        leftCourseLabel.setText(buildCourseSummary(leftCourse));
        rightCourseLabel.setText(buildCourseSummary(rightCourse));

        leftSelectionRankLabel.setText("Selection rank: " + formatSelectionRank(leftRank));
        rightSelectionRankLabel.setText("Selection rank: " + formatSelectionRank(rightRank));

        populateHighlights(leftHighlightsContainer, leftJob.getJobDescription());
        populateHighlights(rightHighlightsContainer, rightJob.getJobDescription());

        updateComparisonInsights(leftJob, rightJob, leftCourse, rightCourse, leftRank, rightRank);
    }

    private void populateHighlights(VBox container, String description) {
        if (container == null) return;
        container.getChildren().clear();

        List<String> highlights = extractHighlights(description);
        for (String highlight : highlights) {
            Label label = new Label("• " + highlight);
            label.setWrapText(true);
            label.setStyle("-fx-font-size:12; -fx-text-fill:#0f172a;");
            container.getChildren().add(label);
        }
    }

    private List<String> extractHighlights(String description) {
        if (description == null || description.isBlank()) {
            return Collections.singletonList("No description available.");
        }

    String sanitized = description.replace("\n", " ");
    String[] sentences = sanitized.split("(?<=[.!?])\\s+");
        List<String> highlights = new ArrayList<>();
        for (String sentence : sentences) {
            String trimmed = sentence.trim();
            if (!trimmed.isEmpty()) {
                highlights.add(trimmed);
            }
            if (highlights.size() == 3) break;
        }
        if (highlights.isEmpty()) {
            highlights.add(description);
        }
        return highlights;
    }

    private String buildCourseSummary(Course course) {
        if (course == null) {
            return "Course details unavailable.";
        }
        List<String> parts = new ArrayList<>();
        if (course.getCourseName() != null && !course.getCourseName().isBlank()) {
            parts.add(course.getCourseName());
        }
        if (course.getCourseMajor() != null && !course.getCourseMajor().isBlank()) {
            parts.add("Major: " + course.getCourseMajor());
        }
        if (course.getCourseCode() != null && !course.getCourseCode().isBlank()) {
            parts.add("Code: " + course.getCourseCode());
        }
        if (course.getQtacCode() != null && !course.getQtacCode().isBlank()) {
            parts.add("QTAC: " + course.getQtacCode());
        }
        return parts.isEmpty() ? "Course details unavailable." : parts.stream().collect(Collectors.joining(" • "));
    }

    private String formatSelectionRank(Integer rank) {
        return rank != null ? String.valueOf(rank) : "N/A";
    }

    private void updateComparisonInsights(Job leftJob, Job rightJob, Course leftCourse, Course rightCourse, Integer leftRank, Integer rightRank) {
        int salaryDiff = leftJob.getJobSalary() - rightJob.getJobSalary();
        if (salaryDiff == 0) {
            salaryDifferenceLabel.setText("Both roles offer similar salary potential.");
            salaryDifferenceLabel.setStyle("-fx-font-size:12; -fx-text-fill:#1e293b; -fx-background-color:#e2e8f0; -fx-background-radius:14; -fx-padding:6 12;");
        } else if (salaryDiff > 0) {
            salaryDifferenceLabel.setText(leftJob.getJobName() + " pays " + CurrencyUtils.formatCurrency(Math.abs(salaryDiff)) + " more.");
            salaryDifferenceLabel.setStyle("-fx-font-size:12; -fx-text-fill:#065f46; -fx-background-color:#d1fae5; -fx-background-radius:14; -fx-padding:6 12;");
        } else {
            salaryDifferenceLabel.setText(rightJob.getJobName() + " pays " + CurrencyUtils.formatCurrency(Math.abs(salaryDiff)) + " more.");
            salaryDifferenceLabel.setStyle("-fx-font-size:12; -fx-text-fill:#065f46; -fx-background-color:#d1fae5; -fx-background-radius:14; -fx-padding:6 12;");
        }

        String skillInsight = buildSkillDifferenceInsight(leftCourse, rightCourse, leftJob, rightJob);
        skillDifferenceLabel.setText(skillInsight);
        skillDifferenceLabel.setStyle("-fx-font-size:12; -fx-text-fill:#312e81; -fx-background-color:#ede9fe; -fx-background-radius:14; -fx-padding:6 12;");

        String additional = buildAdditionalInsights(leftCourse, rightCourse, leftRank, rightRank);
        additionalInsightsLabel.setText(additional);
    }

    private String buildSkillDifferenceInsight(Course leftCourse, Course rightCourse, Job leftJob, Job rightJob) {
        String leftFocus = Optional.ofNullable(leftCourse).map(Course::getCourseMajor).filter(s -> !s.isBlank()).orElse(leftJob.getJobName());
        String rightFocus = Optional.ofNullable(rightCourse).map(Course::getCourseMajor).filter(s -> !s.isBlank()).orElse(rightJob.getJobName());

        if (!Objects.equals(leftFocus, rightFocus)) {
            return "Focus: " + leftFocus + " vs " + rightFocus + ".";
        }

        // If majors are similar, highlight description tone
        List<String> leftHighlights = extractHighlights(leftJob.getJobDescription());
        List<String> rightHighlights = extractHighlights(rightJob.getJobDescription());
        String leftKey = leftHighlights.isEmpty() ? leftJob.getJobName() : leftHighlights.get(0);
        String rightKey = rightHighlights.isEmpty() ? rightJob.getJobName() : rightHighlights.get(0);
        return "Emphasis: " + leftKey + " | " + rightKey;
    }

    private String buildAdditionalInsights(Course leftCourse, Course rightCourse, Integer leftRank, Integer rightRank) {
        if (leftRank == null && rightRank == null) {
            return "Selection rank data unavailable for these pathways.";
        }
        if (leftRank != null && rightRank != null) {
            if (leftRank.equals(rightRank)) {
                return "Both pathways list a similar selection rank (" + leftRank + ").";
            }
            String higher = leftRank < rightRank ? "lower" : "higher";
            return "Selection rank difference: " + Math.abs(leftRank - rightRank) + " points (" + higher + " rank indicates tougher entry).";
        }
        if (leftRank != null) {
            String courseName = leftCourse != null && leftCourse.getCourseName() != null ? leftCourse.getCourseName() : "This pathway";
            return "Only " + courseName + " reports a selection rank (" + leftRank + ").";
        }
        String courseName = rightCourse != null && rightCourse.getCourseName() != null ? rightCourse.getCourseName() : "This pathway";
        return "Only " + courseName + " reports a selection rank (" + rightRank + ").";
    }
}
