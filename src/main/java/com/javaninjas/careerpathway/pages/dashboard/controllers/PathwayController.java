package com.javaninjas.careerpathway.pages.dashboard.controllers;

import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.core.services.NavigationService;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.geometry.Pos;

import com.javaninjas.careerpathway.core.models.Job;
import com.javaninjas.careerpathway.pages.components.JobCardController;

import java.io.IOException;

public class PathwayController {

    @FXML
    private StackPane rootPane;

    @FXML
    private BorderPane mainContent;

    @FXML
    private Pane blurPane;

    @FXML
    private Label welcomeLabel;

    @FXML
    private HBox jobsContainer;

    // keep a copy of the initial job card nodes so we can restore them if they are accidentally removed
    private java.util.List<javafx.scene.Node> jobCardNodes = new java.util.ArrayList<>();

    private Node popupContent;

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

        // Show personalized jobs if suggested_career is null, else show weekly guide for the career
        try {
            com.javaninjas.careerpathway.core.auth.UserSession session = com.javaninjas.careerpathway.core.auth.UserSession.getInstance();
            int userId = session != null ? session.getUserID() : 0;
            String suggestedCareer = null;
            if (userId > 0) {
                try (java.sql.Connection conn = com.javaninjas.careerpathway.db.connection.Database.getConnection()) {
                    com.javaninjas.careerpathway.db.dao.UserDao userDao = new com.javaninjas.careerpathway.db.dao.UserDao(conn);
                    com.javaninjas.careerpathway.core.models.User user = userDao.getUserById(userId);
                    if (user != null) {
                        suggestedCareer = user.getSuggestedCareer();
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }

            if (suggestedCareer == null || suggestedCareer.isBlank()) {
                // Show personalized jobs (existing logic)
                if (jobsContainer != null) {
                    jobsContainer.getChildren().clear();
                    String recommendedCourse = session != null ? session.getRecommendedCourse() : null;
                    java.util.List<com.javaninjas.careerpathway.core.models.Job> allJobs = com.javaninjas.careerpathway.db.dao.JobDao.getAllJobs();
                    java.util.List<com.javaninjas.careerpathway.core.models.Job> filteredJobs = new java.util.ArrayList<>();
                    if (recommendedCourse != null && !recommendedCourse.isBlank()) {
                        java.util.List<com.javaninjas.careerpathway.core.models.Course> allCourses = com.javaninjas.careerpathway.db.dao.CourseDao.getAllCourses();
                        java.util.Set<Integer> matchingCourseIds = new java.util.HashSet<>();
                        for (com.javaninjas.careerpathway.core.models.Course course : allCourses) {
                            if (course.getCourseName() != null && course.getCourseName().toLowerCase().contains(recommendedCourse.toLowerCase())) {
                                matchingCourseIds.add(course.getCourseID());
                            } else if (course.getCourseMajor() != null && course.getCourseMajor().toLowerCase().contains(recommendedCourse.toLowerCase())) {
                                matchingCourseIds.add(course.getCourseID());
                            }
                        }
                        for (com.javaninjas.careerpathway.core.models.Job job : allJobs) {
                            if (matchingCourseIds.contains(job.getCourseID())) {
                                filteredJobs.add(job);
                            }
                        }
                    }
                    java.util.List<com.javaninjas.careerpathway.core.models.Job> jobsToShow;
                    if (!filteredJobs.isEmpty()) {
                        jobsToShow = filteredJobs;
                    } else {
                        java.util.Collections.shuffle(allJobs);
                        jobsToShow = allJobs.subList(0, Math.min(5, allJobs.size()));
                    }
                    for (com.javaninjas.careerpathway.core.models.Job job : jobsToShow) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javaninjas/careerpathway/pages/components/jobCard.fxml"));
                            Node node = loader.load();
                            JobCardController controller = loader.getController();
                            if (controller != null) {
                                controller.setJob(job);
                                controller.setOnApply(j -> {
                                    showPopup(j);
                                });
                            }
                            jobsContainer.getChildren().add(node);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                // Show 10-week guide for the suggested career (fetch from AI)
                if (mainContent != null) {
                    mainContent.setCenter(null);
                    VBox guideBox = new VBox(20);
                    guideBox.setStyle("-fx-padding: 32 0 0 0;");
                    Label title = new Label("Your Pathway To Becoming a " + suggestedCareer);
                    title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-padding: 0 0 32 0;");
                    guideBox.getChildren().add(title);
                    try {
                        // Fetch 10-week plan from AI
                        String apiKey = com.javaninjas.careerpathway.core.config.OpenAIConfig.getApiKey();
                        if (apiKey != null && !apiKey.isBlank()) {
                            com.javaninjas.careerpathway.core.integrations.openai.ChatGptClient client = new com.javaninjas.careerpathway.core.integrations.openai.ChatGptClient(apiKey);
                            com.javaninjas.careerpathway.core.integrations.openai.ChatGptService chatGptService = new com.javaninjas.careerpathway.core.integrations.openai.ChatGptService(client);
                            com.javaninjas.careerpathway.core.integrations.openai.models.CareerPlan plan = chatGptService.requestCareerPlan(suggestedCareer);
                            if (plan != null && plan.getPlan() != null) {
                                for (com.javaninjas.careerpathway.core.integrations.openai.models.CareerPlan.WeekPlan weekPlan : plan.getPlan()) {
                                    VBox weekBox = new VBox(8);
                                    weekBox.setStyle("-fx-background-color: #fff; -fx-border-radius: 16; -fx-background-radius: 16; -fx-padding: 18 24 18 24; -fx-border-color: #e0e6ef; -fx-border-width: 1; -fx-effect: dropshadow(gaussian, #e0e6ef, 4, 0, 0, 2);");
                                    Label weekLabel = new Label("Week " + weekPlan.getWeek());
                                    weekLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
                                    VBox tasksBox = new VBox(4);
                                    if (weekPlan.getTasks() != null) {
                                        for (String task : weekPlan.getTasks()) {
                                            Label taskLabel = new Label("• " + task);
                                            taskLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #444;");
                                            tasksBox.getChildren().add(taskLabel);
                                        }
                                    }
                                    Button completeBtn = new Button("Complete Tasks");
                                    completeBtn.setStyle("-fx-background-color: #8bb6e6; -fx-text-fill: #fff; -fx-font-size: 16px; -fx-background-radius: 16; -fx-padding: 6 24 6 24; -fx-font-weight: bold;");
                                    weekBox.getChildren().addAll(weekLabel, tasksBox, completeBtn);
                                    guideBox.getChildren().add(weekBox);
                                }
                            } else {
                                guideBox.getChildren().add(new Label("Could not fetch a weekly plan from AI."));
                            }
                        } else {
                            guideBox.getChildren().add(new Label("AI is not enabled. No API key found."));
                        }
                    } catch (Exception ex) {
                        guideBox.getChildren().add(new Label("Error fetching plan from AI: " + ex.getMessage()));
                        ex.printStackTrace();
                    }
                    mainContent.setCenter(guideBox);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Make blurPane cover the whole root and block mouse events when visible
        try {
            if (blurPane != null && rootPane != null) {
                blurPane.prefWidthProperty().bind(rootPane.widthProperty());
                blurPane.prefHeightProperty().bind(rootPane.heightProperty());
                // Initially let clicks through when not visible
                blurPane.setMouseTransparent(true);
            }
        } catch (Exception ignored) {}
    }

    private void showPopup(Job job) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javaninjas/careerpathway/pages/dashboard/views/pathwayPopup.fxml"));
            popupContent = loader.load();
            PathwayPopupController popupController = loader.getController();
            popupController.setJob(job);
            popupController.setCloseHandler(this::hidePopup);
            // Remove any existing dark overlays (for example jobDetail overlays) so we only show the main modal
            try {
                // remove overlays directly attached to rootPane or mainContent
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

                // also walk the scene graph to remove overlays that were attached deeper in the hierarchy
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

        // If for any reason the job cards were removed while the popup was open, restore them
        try {
            if (jobsContainer != null && jobsContainer.getChildren().isEmpty() && !jobCardNodes.isEmpty()) {
                jobsContainer.getChildren().setAll(jobCardNodes);
            }
        } catch (Exception ignored) {}
    }

    // Recursively remove dark full-screen overlay nodes from the provided parent
    private void removeDarkOverlaysRecursive(javafx.scene.Parent parent) {
        if (parent == null) return;
        try {
            java.util.List<javafx.scene.Node> toRemove = new java.util.ArrayList<>();
            for (javafx.scene.Node n : parent.getChildrenUnmodifiable()) {
                try {
                    String s = n.getStyle();
                    if (s != null && (s.contains("rgba(0,0,0") || s.contains("rgba(0, 0, 0"))) {
                        toRemove.add(n);
                        continue;
                    }
                } catch (Exception ignored) {}

                if (n instanceof javafx.scene.Parent p) {
                    removeDarkOverlaysRecursive(p);
                }
            }

            // attempt to remove from parent if possible
            if (parent instanceof javafx.scene.layout.Pane pane) {
                for (javafx.scene.Node n : toRemove) pane.getChildren().remove(n);
            }
        } catch (Exception ignored) {}
    }

    @FXML
    private void handleLogout() {
        UserSession.logout();
        NavigationService.go("/com/javaninjas/careerpathway/pages/login/views/LoginPage.fxml");
    }
}
