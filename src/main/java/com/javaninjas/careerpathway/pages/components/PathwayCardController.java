package com.javaninjas.careerpathway.pages.components;

import com.javaninjas.careerpathway.core.models.Job;
import com.javaninjas.careerpathway.core.services.NavigationService;
import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.db.connection.Database;
import com.javaninjas.careerpathway.db.dao.UserDao;
import com.javaninjas.careerpathway.pages.dashboard.controllers.PathwayPopupController;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

// ...existing imports...
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PathwayCardController {

    @FXML private VBox cardRoot;
    @FXML private Label jobTitle;
    @FXML private Label jobDescription;
    @FXML private Label pathwaySalaryLabel;
    @FXML private Button learnMoreBtn;
    @FXML private javafx.scene.control.ToggleButton favouriteBtn;
    @FXML private Label heartLabel;
    @FXML private CheckBox compareToggle;

    private Job job;
    private Consumer<Job> onLearnMore;
    private boolean isFavourite = false;
    private BiConsumer<Job, Boolean> compareSelectionHandler;
    private String baseCardStyle;
    private boolean suppressCompareEvent = false;

    @FXML
    private void initialize() {
        if (cardRoot != null) {
            baseCardStyle = cardRoot.getStyle();
        }
        setupHoverAnimation();
        setupLearnMoreAction();
        setupCompareToggle();
    }

    /**
     * Sets the job data for this card
     */
    public void setJob(Job job) {
        this.job = job;
        if (job != null) {
            updateCardDisplay();
            updateFavouriteState();
        }
    }

    /**
     * Sets a custom action handler for the Learn More button
     */
    public void setOnLearnMore(Consumer<Job> handler) {
        this.onLearnMore = handler;
    }

    /**
     * Sets a handler that will be notified when the compare toggle changes state.
     */
    public void setCompareSelectionHandler(BiConsumer<Job, Boolean> handler) {
        this.compareSelectionHandler = handler;
    }

    /**
     * Allows parent controllers to programmatically update the compare toggle state.
     */
    public void setCompareSelected(boolean selected) {
        if (compareToggle != null) {
            suppressCompareEvent = true;
            compareToggle.setSelected(selected);
            suppressCompareEvent = false;
        }
        updateCompareVisual(selected);
    }

    /**
     * Returns the current job
     */
    public Job getJob() {
        return job;
    }

    /**
     * Updates the visual elements with job data
     */
    private void updateCardDisplay() {
        if (job == null) return;

        // Set job title
        jobTitle.setText(job.getJobName());

        // Set job description (truncate if too long)
        String description = job.getJobDescription();
        if (description.length() > 100) {
            description = description.substring(0, 97) + "...";
        }
        jobDescription.setText(description);

    // Format and set salary
    pathwaySalaryLabel.setText("Avg salary - " + com.javaninjas.careerpathway.core.utils.CurrencyUtils.formatCurrency(job.getJobSalary()));
    }

    private void setupCompareToggle() {
        if (compareToggle == null) return;
        compareToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            updateCompareVisual(Boolean.TRUE.equals(newVal));
            if (!suppressCompareEvent && compareSelectionHandler != null && job != null) {
                compareSelectionHandler.accept(job, Boolean.TRUE.equals(newVal));
            }
        });
    }

    private void updateCompareVisual(boolean selected) {
        if (cardRoot == null) return;
        if (selected) {
            cardRoot.setStyle((baseCardStyle != null ? baseCardStyle : "") + " -fx-border-color:#3b82f6; -fx-border-width:2;");
        } else {
            if (baseCardStyle != null) {
                cardRoot.setStyle(baseCardStyle);
            }
        }
    }

    private void updateFavouriteState() {
        try {
            UserSession session = UserSession.getInstance();
            if (session == null) return; // not logged in

            int userId = session.getUserID();
            try (java.sql.Connection conn = Database.getConnection()) {
                UserDao userDao = new UserDao(conn);
                isFavourite = userDao.getFavouriteJobIds(userId).contains(job.getJobID());
            }
            applyHeartVisual();
        } catch (Exception e) {
            System.err.println("Error updating favourite state: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void applyHeartVisual() {
        if (heartLabel == null || favouriteBtn == null) return;
        if (isFavourite) {
            heartLabel.setText("♥");
            heartLabel.setStyle("-fx-font-size:18; -fx-text-fill: #e53e3e;");
            favouriteBtn.setSelected(true);
        } else {
            heartLabel.setText("♡");
            heartLabel.setStyle("-fx-font-size:18; -fx-text-fill: #cbd5e1;");
            favouriteBtn.setSelected(false);
        }
    }

    /**
     * Sets up hover animation effects
     */
    private void setupHoverAnimation() {
        if (cardRoot == null) return;

        final double liftAmount = -8.0;
        final double scaleAmount = 1.03;
        final Duration animationDuration = Duration.millis(200);

        // Hover enter animation
        TranslateTransition liftUp = new TranslateTransition(animationDuration, cardRoot);
        liftUp.setToY(liftAmount);

        ScaleTransition scaleUp = new ScaleTransition(animationDuration, cardRoot);
        scaleUp.setToX(scaleAmount);
        scaleUp.setToY(scaleAmount);

        // Hover exit animation
        TranslateTransition liftDown = new TranslateTransition(animationDuration, cardRoot);
        liftDown.setToY(0);

        ScaleTransition scaleDown = new ScaleTransition(animationDuration, cardRoot);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        // Add event handlers
        cardRoot.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            liftDown.stop();
            scaleDown.stop();
            liftUp.play();
            scaleUp.play();
        });

        cardRoot.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            liftUp.stop();
            scaleUp.stop();
            liftDown.play();
            scaleDown.play();
        });
    }

    /**
     * Sets up the Learn More button action
     */
    private void setupLearnMoreAction() {
        if (learnMoreBtn == null) return;

        learnMoreBtn.setOnAction(event -> {
            if (job == null) return;

            // Use custom handler if provided
            if (onLearnMore != null) {
                onLearnMore.accept(job);
                return;
            }

            // Default behavior: Navigate to pathway popup
            NavigationService.go(
                    "/com/javaninjas/careerpathway/pages/dashboard/views/pathwayPopup.fxml",
                    (PathwayPopupController controller) -> controller.setJob(job)
            );
        });

        // Favourite button handler
        if (favouriteBtn != null) {
            favouriteBtn.setOnAction(evt -> {
                try {
                    UserSession session = UserSession.getInstance();
                    if (session == null) {
                        System.out.println("User not logged in - cannot favourite job");
                        return;
                    }
                    int userId = session.getUserID();
                    try (java.sql.Connection conn = Database.getConnection()) {
                        UserDao userDao = new UserDao(conn);
                        if (!isFavourite) {
                            userDao.addFavourite(userId, job.getJobID());
                            isFavourite = true;
                        } else {
                            userDao.removeFavourite(userId, job.getJobID());
                            isFavourite = false;
                        }
                        applyHeartVisual();
                    }
                } catch (Exception ex) {
                    System.err.println("Error toggling favourite: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
        }
    }

    /**
     * Handles direct clicks on the entire card
     */
    @FXML
    private void handleCardClick() {
        if (learnMoreBtn != null) {
            learnMoreBtn.fire(); // Simulate button click
        }
    }
}