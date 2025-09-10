package com.javaninjas.careerpathway.registration.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class UserRegistrationController implements Initializable {
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField cityField;

    @FXML
    private ComboBox<String> ageSelect;

    @FXML
    private ComboBox<String> profileStage;

    @FXML
    private CheckBox anonymousCheck;

    @FXML
    private Button registerButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate the combo boxes with a small sensible default set so the view is usable
        if (ageSelect != null) {
            ageSelect.getItems().addAll(Arrays.asList("Under 18", "18-24", "25-34", "35-44", "45+"));
        }
        if (profileStage != null) {
            profileStage.getItems().addAll(Arrays.asList("Student", "Graduate", "Early career", "Experienced"));
        }

        // Optional: wire a simple handler if the FXML doesn't. This keeps the button safe to click.
        if (registerButton != null) {
            registerButton.setOnAction(e -> onRegister());
        }
    }

    private void onRegister() {
        // Minimal behaviour: could validate and forward to navigation/service logic later.
        System.out.println("Register clicked: " +
            (firstNameField != null ? firstNameField.getText() : "<no-first>") + " " +
            (lastNameField != null ? lastNameField.getText() : "<no-last>"));
    }
}
