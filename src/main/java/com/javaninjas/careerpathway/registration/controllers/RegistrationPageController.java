package com.javaninjas.careerpathway.registration.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Objects;

public class RegistrationPageController {
    @FXML
    private Button createAccountButton;

    @FXML
    private void initialize() {
        // noop
    }

    @FXML
    private void handleCreateAccount(ActionEvent event) {
        try {
            Parent details = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/javaninjas/careerpathway/registration/views/userRegistrationDetailsPage.fxml")));
            Stage stage = (Stage) createAccountButton.getScene().getWindow();
            stage.setScene(new Scene(details));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
