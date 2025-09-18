package com.javaninjas.careerpathway.pages.dashboard.controllers;

import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.core.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.javaninjas.careerpathway.core.services.NavigationService;

public class ProfileController {

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

    private User currentUser;

    @FXML
    public void initialize() {
        UserSession session = UserSession.getInstance();
        if (session == null) {
            // If there's no session, redirect to login
            NavigationService.go("/com/javaninjas/careerpathway/login/views/loginPage.fxml");
            return;
        }

        currentUser = session.getLoggedInUser();
        if (currentUser != null) {
            populateUserData();
        }
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
        UserSession.getInstance().logout();
        NavigationService.go("/com/javaninjas/careerpathway/login/views/loginPage.fxml");
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
