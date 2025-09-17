package com.javaninjas.careerpathway.pages.registration.controllers;

import com.javaninjas.careerpathway.core.services.NavigationService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class RegistrationPageController {
    @FXML
    private Button createAccountButton;

    @FXML
    private void initialize() {
        // noop
    }

    @FXML
    private void handleCreateAccount(ActionEvent event) {
    NavigationService.go("/com/javaninjas/careerpathway/registration/views/userRegistrationDetailsPage.fxml");
    }
}
