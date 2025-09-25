package com.javaninjas.careerpathway.pages.dashboard.controllers;

import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.core.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.javaninjas.careerpathway.core.services.NavigationService;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.javaninjas.careerpathway.db.dao.UserDao;
import com.javaninjas.careerpathway.db.dao.JobDao;
import com.javaninjas.careerpathway.db.connection.Database;
import com.javaninjas.careerpathway.core.models.Job;

public class ProfileController {

    public BorderPane mainContent;
    @FXML
    private Label firstNameLabel;
    @FXML
    private TextField firstNameField;
    @FXML
    private Label lastNameLabel;
    @FXML
    private TextField lastNameField;
    @FXML
    private Label emailLabel;
    @FXML
    private TextField emailField;
    @FXML
    private Label phoneLabel;
    @FXML
    private TextField phoneField;
    @FXML
    private Button editButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button logoutBtn;
    @FXML
    private HBox favouritesContainer;
    @FXML
    private javafx.scene.control.Button favPrevBtn;
    @FXML
    private javafx.scene.control.Button favNextBtn;
    @FXML
    private javafx.scene.control.ScrollPane favouritesScroll;

    // slider state
    private int favIndex = 0;

    private User currentUser;

    @FXML
    public void initialize() {
        UserSession session = UserSession.getInstance();
        if (session == null) {
            // If there's no session, redirect to login
            NavigationService.go("/com/javaninjas/careerpathway/pages/login/views/LoginPage.fxml");
            return;
        }

        currentUser = session.getLoggedInUser();
        if (currentUser != null) {
            populateUserData();
            populateFavourites();
        }
    }

    private void populateFavourites() {
        if (favouritesContainer == null || currentUser == null) return;
        favouritesContainer.getChildren().clear();
        try (java.sql.Connection conn = Database.getConnection()) {
            UserDao userDao = new UserDao(conn);
            java.util.List<Integer> favIds = userDao.getFavouriteJobIds(currentUser.getUserID());
            for (Integer jobId : favIds) {
                Job job = JobDao.getJobById(jobId);
                if (job != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javaninjas/careerpathway/pages/components/pathwayCard.fxml"));
                    VBox card = loader.load();
                    com.javaninjas.careerpathway.pages.components.PathwayCardController controller = loader.getController();
                    controller.setJob(job);
                    favouritesContainer.getChildren().add(card);
                }
            }
            // attach slider controls
            setupFavSliderControls();
        } catch (Exception e) {
            System.err.println("Error loading favourites: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupFavSliderControls() {
        if (favPrevBtn != null) {
            favPrevBtn.setOnAction(e -> slidePrev());
        }
        if (favNextBtn != null) {
            favNextBtn.setOnAction(e -> slideNext());
        }
        // reset index
        favIndex = 0;
        // ensure first item visible
        scrollToIndex(favIndex);
    }

    private void slideNext() {
        int total = favouritesContainer.getChildren().size();
        if (total == 0) return;
        favIndex = (favIndex + 1) % total; // wrap-around
        scrollToIndex(favIndex);
    }

    private void slidePrev() {
        int total = favouritesContainer.getChildren().size();
        if (total == 0) return;
        favIndex = (favIndex - 1 + total) % total; // wrap-around
        scrollToIndex(favIndex);
    }

    private void scrollToIndex(int index) {
        // compute target x offset of child
        if (favouritesContainer.getChildren().isEmpty() || favouritesScroll == null) return;
        javafx.scene.Node node = favouritesContainer.getChildren().get(index);
    // layout may not be computed yet; request layout then run later
        favouritesContainer.requestLayout();
        javafx.application.Platform.runLater(() -> {
            double contentWidth = favouritesContainer.getWidth();
            double viewportWidth = favouritesScroll.getViewportBounds().getWidth();
            double nodeX = node.getBoundsInParent().getMinX();
            double h = Math.max(0, nodeX - (viewportWidth - node.getBoundsInParent().getWidth()) / 2);
            double hMax = Math.max(0, contentWidth - viewportWidth);
            double hNorm = hMax == 0 ? 0 : Math.min(1.0, h / hMax);
            favouritesScroll.setHvalue(hNorm);
        });
    }

    private void populateUserData() {
        firstNameLabel.setText(currentUser.getFirstName());
        lastNameLabel.setText(currentUser.getLastName());
        emailLabel.setText(currentUser.getEmail());
        phoneLabel.setText(currentUser.getPhoneNumber());

        firstNameField.setText(currentUser.getFirstName());
        lastNameField.setText(currentUser.getLastName());
        emailField.setText(currentUser.getEmail());
        phoneField.setText(currentUser.getPhoneNumber());
    }

    @FXML
    private void handleEdit() {
        toggleEditMode(true);
    }

    @FXML
    private void handleSave() {
        // Here you would typically save the data to a database or other persistent storage
        currentUser.setFirstName(firstNameField.getText());
        currentUser.setLastName(lastNameField.getText());
        currentUser.setEmail(emailField.getText());
        currentUser.setPhoneNumber(phoneField.getText());

        populateUserData();
        toggleEditMode(false);
    }

    @FXML
    private void handleCancel() {
        // Reset fields to original values
        firstNameField.setText(currentUser.getFirstName());
        lastNameField.setText(currentUser.getLastName());
        emailField.setText(currentUser.getEmail());
        phoneField.setText(currentUser.getPhoneNumber());

        toggleEditMode(false);
    }

    private void toggleEditMode(boolean editMode) {
        // Toggle visibility of labels and text fields
        firstNameLabel.setVisible(!editMode);
        lastNameLabel.setVisible(!editMode);
        emailLabel.setVisible(!editMode);
        phoneLabel.setVisible(!editMode);

        firstNameField.setVisible(editMode);
        lastNameField.setVisible(editMode);
        emailField.setVisible(editMode);
        phoneField.setVisible(editMode);

        firstNameField.setManaged(editMode);
        lastNameField.setManaged(editMode);
        emailField.setManaged(editMode);
        phoneField.setManaged(editMode);

        // Toggle visibility of buttons
        editButton.setVisible(!editMode);
        saveButton.setVisible(editMode);
        cancelButton.setVisible(editMode);

        editButton.setManaged(!editMode);
        saveButton.setManaged(editMode);
        cancelButton.setManaged(editMode);
    }

    @FXML
    private void handleLogout() {
        UserSession.logout();
        NavigationService.go("/com/javaninjas/careerpathway/pages/login/views/LoginPage.fxml");
    }

    @FXML
    private void handleBack() {
        NavigationService.goBack();
    }

    @FXML
    private void handleViewPathwayReport() {
        // TODO: Implement navigation to the pathway report view
    }
}
