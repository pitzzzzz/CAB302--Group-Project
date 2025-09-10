package com.javaninjas.careerpathway.registration.controllers;

import com.javaninjas.careerpathway.app.NavigationService;
import com.javaninjas.careerpathway.registration.controllers.SuccessfulRegistrationController;

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
    }

    @FXML
    private void onRegister() {
        String firstName = firstNameField != null ? firstNameField.getText() : "";
        NavigationService.go("/com/javaninjas/careerpathway/registration/views/successfulRegistrationPage.fxml", (SuccessfulRegistrationController controller) -> {
            controller.setWelcomeName(firstName);
        });
    }
}

