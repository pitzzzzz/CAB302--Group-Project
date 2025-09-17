package com.javaninjas.careerpathway.pages.registration.controllers;

import com.javaninjas.careerpathway.core.services.NavigationService;
import com.javaninjas.careerpathway.db.Database;

import at.favre.lib.crypto.bcrypt.BCrypt;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    private javafx.scene.control.PasswordField passwordField;

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
        if (ageSelect != null) {
            ageSelect.getItems().addAll(Arrays.asList("Under 18", "18-24", "25-34", "35-44", "45+"));
        }
        if (profileStage != null) {
            profileStage.getItems().addAll(Arrays.asList("Student", "Graduate", "Early career", "Experienced"));
        }
    }

    @FXML
    private void onRegister() {
        String firstName = firstNameField != null ? firstNameField.getText().trim() : "";
        String lastName = lastNameField != null ? lastNameField.getText().trim() : "";
        String email = emailField != null ? emailField.getText().trim() : "";
        String city = cityField != null ? cityField.getText().trim() : "";
        String password = passwordField != null ? passwordField.getText() : "";

    // Basic validation
    if (firstName.isBlank()) { showAlertAndFocus(Alert.AlertType.ERROR, "Validation Error", "Please enter your first name.", firstNameField); return; }
    if (lastName.isBlank()) { showAlertAndFocus(Alert.AlertType.ERROR, "Validation Error", "Please enter your last name.", lastNameField); return; }
    if (email.isBlank()) { showAlertAndFocus(Alert.AlertType.ERROR, "Validation Error", "Please enter your email.", emailField); return; }
    if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) { showAlertAndFocus(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid email address.", emailField); return; }
    if (password.isBlank() || password.length() < 8) { showAlertAndFocus(Alert.AlertType.ERROR, "Validation Error", "Please provide a password of at least 8 characters.", passwordField); return; }

        String hashed = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        try (Connection conn = Database.getConnection()) {
            // check for duplicate email
            try (PreparedStatement check = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE email = ?")) {
                check.setString(1, email);
                var rs = check.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    showAlertAndFocus(Alert.AlertType.ERROR, "Registration Error", "An account with this email already exists.", emailField);
                    return;
                }
            }

            String sql = "INSERT INTO users(first_name,last_name,email,password_hash,city,age_group,profile_stage,anonymous) VALUES(?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, email);
                ps.setString(4, hashed);
                ps.setString(5, city);
                ps.setString(6, ageSelect != null ? ageSelect.getValue() : null);
                ps.setString(7, profileStage != null ? profileStage.getValue() : null);
                ps.setInt(8, anonymousCheck != null && anonymousCheck.isSelected() ? 1 : 0);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", "Failed to register user: " + ex.getMessage());
            return;
        }

        // Show a confirmation to the user that registration was saved
        showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Your account has been created successfully.");

        // Navigate to the existing success page and pass the first name
        NavigationService.go("/com/javaninjas/careerpathway/registration/views/successfulRegistrationPage.fxml", (SuccessfulRegistrationController controller) -> {
            controller.setWelcomeName(firstName);
        });
    }

    // ===== HELPER METHODS FOR TESTING =====

    public boolean isValidFirstName(String firstName) {
        return firstName != null && !firstName.trim().isEmpty();
    }

    public boolean isValidLastName(String lastName) {
        return lastName != null && !lastName.trim().isEmpty();
    }

    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public boolean isValidPassword(String password) {
        return password != null && !password.isBlank() && password.length() >= 8;
    }

    public boolean isValidCity(String city) {
        return city != null && !city.trim().isEmpty();
    }

    // ===== Validation helpers extracted from pages.registrationPage =====
    private boolean validateFields() {
        if (firstNameField == null || firstNameField.getText().trim().isEmpty() ||
            lastNameField == null || lastNameField.getText().trim().isEmpty() ||
            emailField == null || emailField.getText().trim().isEmpty() ||
            cityField == null || cityField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required!");
            return false;
        }
        return true;
    }

    private boolean validateEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid email address!");
            return false;
        }
        return true;
    }

    private boolean validateSelections(String age, String role) {
        if (age == null || age.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select your age!");
            return false;
        }
        if (role == null || role.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a profile role!");
            return false;
        }
        return true;
    }

    public String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public boolean verifyPassword(String password, String hash) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hash);
        return result.verified;
    }

    public String[] getAgeOptions() {
        return new String[]{"Under 18", "18-24", "25-34", "35-44", "45+"};
    }

    public String[] getStageOptions() {
        return new String[]{"Student", "Graduate", "Early career", "Experienced"};
    }

    // Helper class for testing complete form data
    public static class RegistrationData {
        public String firstName, lastName, email, password, city;

        public RegistrationData() {}

        public RegistrationData(String firstName, String lastName, String email, String password, String city) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
            this.city = city;
        }
    }

    public boolean validateRegistrationData(RegistrationData data) {
        return isValidFirstName(data.firstName) &&
                isValidLastName(data.lastName) &&
                isValidEmail(data.email) &&
                isValidPassword(data.password) &&
                isValidCity(data.city);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }

    private void showAlertAndFocus(Alert.AlertType type, String title, String message, Control focusTarget) {
        showAlert(type, title, message);
        if (focusTarget != null) {
            focusTarget.requestFocus();
        }
    }
}

