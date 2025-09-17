package com.javaninjas.careerpathway.pages;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class registrationPage {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField cityField;

    @FXML
    private ComboBox<Integer> ageSelect;

    @FXML
    private ComboBox<String> profileRole;

    @FXML
    private CheckBox anonymousCheck;

    @FXML
    private Button registerButton;

    @FXML
    private void initialize() {

        for (int i = 14; i <= 100; i++) {
            ageSelect.getItems().add(i);
        }

        profileRole.getItems().addAll(
                "Undecided Explorer",
                "Decision Ready Planner",
                "Underperforming ATAR",
                "The Course Switcher",
                "International Student",
                "Regional / Relocation Constrained Student"
        );
    }

    private boolean validateFields() {
        if (firstNameField.getText().trim().isEmpty() ||
            lastNameField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty() ||
            cityField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required!");
            return false;
        }
        return true;
    }

    private boolean validateEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid email address!");
            return false;
        }
        return true;
    }

    private boolean validateSelections(Integer age, String role) {
        if (age == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select your age!");
            return false;
        }
        if (role == null || role.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a profile role!");
            return false;
        }
        return true;
    }


    @FXML
    private void handleRegister() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String city = cityField.getText().trim();
        Integer age = ageSelect.getValue();
        String role = profileRole.getValue();
        boolean anonymous = anonymousCheck.isSelected();

        if (!validateFields() || !validateEmail(email) || !validateSelections(age, role)) return;


        System.out.println("=== Registration Info ===");
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Email: " + email);
        System.out.println("City: " + city);
        System.out.println("Age: " + age);
        System.out.println("Role: " + role);
        System.out.println("Anonymous: " + anonymous);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful!");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
