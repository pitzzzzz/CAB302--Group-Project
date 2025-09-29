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
    private Label descriptionLabel;

    @FXML
    private HBox jobsContainer;

    @FXML
    private VBox mainContentContainer;

    @FXML
    private VBox headerSection;

    @FXML
    private javafx.scene.control.ScrollPane contentScrollPane;

    @FXML
    private VBox scrollableContent;

    @FXML
    private VBox jobRecommendationsSection;

    @FXML
    private VBox weeklyGuideContainer;

    @FXML
    private VBox weeklyGuideSection;

    @FXML
    private javafx.scene.control.ScrollPane weeklyGuideScrollPane;

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
                // Show job recommendations mode
                showJobRecommendations();
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
                
                // Populate job cards
                if (jobsContainer != null) {
                    jobsContainer.getChildren().clear();
                    jobCardNodes.clear(); // Clear backup list
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
                            jobCardNodes.add(node); // Backup the job card node
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                // Show weekly guide mode
                showWeeklyGuide(suggestedCareer);
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
                // Only remove specific overlay nodes, not job cards or main content
                rootPane.getChildren().removeIf(n -> {
                    if (n == blurPane || n == mainContent || n == popupContent) return false;
                    // Only remove nodes that are clearly overlays (have overlay styling and aren't job containers)
                    String s = n.getStyle();
                    String id = n.getId();
                    if (s != null && (s.contains("rgba(0,0,0") || s.contains("rgba(0, 0, 0"))) {
                        // Don't remove nodes that are part of the main UI structure
                        if (id != null && (id.contains("job") || id.contains("card") || id.contains("container"))) {
                            return false;
                        }
                        return true; // remove dark fullscreen overlays
                    }
                    return false;
                });
                
                // Don't recursively clean mainContent to avoid removing job cards
                // The above cleanup should be sufficient for removing stray overlays
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

    /**
     * Shows the job recommendations mode - hides weekly guide and shows job cards
     */
    private void showJobRecommendations() {
        if (jobRecommendationsSection != null) {
            jobRecommendationsSection.setVisible(true);
            jobRecommendationsSection.setManaged(true);
        }
        if (weeklyGuideSection != null) {
            weeklyGuideSection.setVisible(false);
            weeklyGuideSection.setManaged(false);
        }
        if (descriptionLabel != null) {
            descriptionLabel.setText("Here are the top careers we recommend for you");
        }
        
        // Restore job cards if they're missing
        if (jobsContainer != null && jobsContainer.getChildren().isEmpty() && !jobCardNodes.isEmpty()) {
            jobsContainer.getChildren().setAll(jobCardNodes);
        }
    }

    /**
     * Shows the weekly guide mode - hides job recommendations and shows weekly guide
     */
    private void showWeeklyGuide(String suggestedCareer) {
        if (jobRecommendationsSection != null) {
            jobRecommendationsSection.setVisible(false);
            jobRecommendationsSection.setManaged(false);
        }
        if (weeklyGuideSection != null) {
            weeklyGuideSection.setVisible(true);
            weeklyGuideSection.setManaged(true);
        }
        if (weeklyGuideContainer != null) {
            weeklyGuideContainer.getChildren().clear();
        }
        if (descriptionLabel != null) {
            descriptionLabel.setText("Your Pathway To Becoming a " + suggestedCareer);
        }

        // Populate the weekly guide content
        if (weeklyGuideContainer != null) {
            try {
                // Check cache first to avoid unnecessary AI requests
                com.javaninjas.careerpathway.core.integrations.openai.models.CareerPlan plan = 
                    com.javaninjas.careerpathway.core.services.CareerPlanCacheService.getCachedPlan(suggestedCareer);
                
                if (plan == null) {
                    // No cached plan found, fetch from AI
                    String apiKey = com.javaninjas.careerpathway.core.config.OpenAIConfig.getApiKey();
                    if (apiKey != null && !apiKey.isBlank()) {
                        // Show loading indicator while fetching
                        Label loadingLabel = new Label("Loading your personalized career pathway...");
                        loadingLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666; -fx-padding: 20;");
                        weeklyGuideContainer.getChildren().add(loadingLabel);
                        
                        com.javaninjas.careerpathway.core.integrations.openai.ChatGptClient client = new com.javaninjas.careerpathway.core.integrations.openai.ChatGptClient(apiKey);
                        com.javaninjas.careerpathway.core.integrations.openai.ChatGptService chatGptService = new com.javaninjas.careerpathway.core.integrations.openai.ChatGptService(client);
                        plan = chatGptService.requestCareerPlan(suggestedCareer);
                        
                        // Cache the plan for future requests
                        if (plan != null) {
                            com.javaninjas.careerpathway.core.services.CareerPlanCacheService.cachePlan(suggestedCareer, plan);
                        }
                        
                        // Remove loading indicator
                        weeklyGuideContainer.getChildren().remove(loadingLabel);
                    } else {
                        weeklyGuideContainer.getChildren().add(new Label("AI is not enabled. No API key found."));
                        return;
                    }
                }
                
                // Display the career plan (from cache or newly fetched)
                if (plan != null && plan.getPlan() != null) {
                    for (com.javaninjas.careerpathway.core.integrations.openai.models.CareerPlan.WeekPlan weekPlan : plan.getPlan()) {
                        // Create modern week card
                        VBox weekCard = new VBox(16);
                        weekCard.setStyle("-fx-background-color: linear-gradient(to bottom right, #ffffff, #f8fafc); -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 24; -fx-border-color: #e2e8f0; -fx-border-width: 1.5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 16, 0.2, 0, 4);");
                        
                        // Week header with progress indicator
                        HBox weekHeader = new HBox(16);
                        weekHeader.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                        
                        // Progress circle
                        javafx.scene.layout.StackPane progressCircle = new javafx.scene.layout.StackPane();
                        javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(20);
                        circle.setFill(javafx.scene.paint.Color.web("#3b82f6"));
                        circle.setStroke(javafx.scene.paint.Color.web("#1e40af"));
                        circle.setStrokeWidth(2);
                        Label weekNumber = new Label(String.valueOf(weekPlan.getWeek()));
                        weekNumber.setStyle("-fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: white;");
                        progressCircle.getChildren().addAll(circle, weekNumber);
                        
                        // Week title
                        Label weekTitle = new Label("Week " + weekPlan.getWeek());
                        weekTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: 700; -fx-text-fill: #1e293b;");
                        
                        weekHeader.getChildren().addAll(progressCircle, weekTitle);
                        
                        // Tasks section
                        VBox tasksSection = new VBox(8);
                        if (weekPlan.getTasks() != null) {
                            for (String task : weekPlan.getTasks()) {
                                Label taskLabel = new Label("• " + task);
                                taskLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #374151; -fx-wrap-text: true; -fx-padding: 8 0;");
                                taskLabel.setWrapText(true);
                                tasksSection.getChildren().add(taskLabel);
                            }
                        }
                        
                        // Build the complete week card
                        weekCard.getChildren().addAll(weekHeader, tasksSection);
                        weeklyGuideContainer.getChildren().add(weekCard);
                    }
                    
                    // Add cache info for debugging (optional) - styled nicely
                    java.time.LocalDateTime cacheTime = com.javaninjas.careerpathway.core.services.CareerPlanCacheService.getCacheTimestamp(suggestedCareer);
                    if (cacheTime != null) {
                        Label cacheInfo = new Label("📅 Plan generated at " + cacheTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
                        cacheInfo.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b; -fx-padding: 12 0 0 0; -fx-font-style: italic;");
                        weeklyGuideContainer.getChildren().add(cacheInfo);
                    }
                } else {
                    weeklyGuideContainer.getChildren().add(new Label("Could not fetch a weekly plan from AI."));
                }
            } catch (Exception ex) {
                weeklyGuideContainer.getChildren().add(new Label("Error fetching plan from AI: " + ex.getMessage()));
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void handleLogout() {
        // Clear all caches when user logs out to prevent data leakage between sessions
        com.javaninjas.careerpathway.core.services.CareerPlanCacheService.clearUserCache();
        com.javaninjas.careerpathway.core.services.JobCacheService.clearCache();
        
        UserSession.logout();
        NavigationService.go("/com/javaninjas/careerpathway/pages/login/views/LoginPage.fxml");
    }
}
