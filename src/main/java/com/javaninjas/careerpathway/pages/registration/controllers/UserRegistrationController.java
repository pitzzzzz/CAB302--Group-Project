package com.javaninjas.careerpathway.pages.registration.controllers;

import com.javaninjas.careerpathway.core.services.NavigationService;
import com.javaninjas.careerpathway.db.connection.Database;

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

    private String getFirstName() {return firstNameField != null ? firstNameField.getText().trim() : "";}
    private String getLastName() {return lastNameField != null ? lastNameField.getText().trim() : "";}
    private String getEmail() {return  emailField != null ? emailField.getText().trim() : "";}
    private String getCity() {return cityField != null ? cityField.getText().trim() : "";}
    private String getPassword() {return passwordField != null ? passwordField.getText() : "";}


    private boolean validateNotBlank(TextField field, String fieldName) {
        if (field.getText() == null || field.getText().isBlank()) {
            showAlertAndFocus(Alert.AlertType.ERROR, "Validation Error",
                    "Please enter your " + fieldName + ".", field);
            return false;
        }
        return true;
    }

    private boolean validateEmailFormat(TextField field) {
        String email = field.getText();
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showAlertAndFocus(Alert.AlertType.ERROR, "Validation Error",
                    "Please enter a valid email address.", field);
            return false;
        }
        return true;
    }

    private boolean isEmailDuplicate(TextField field) {
        String email = field.getText();
        try (Connection conn = Database.getConnection();
             PreparedStatement check = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE email = ?")) {
            check.setString(1, email);
            var rs = check.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                showAlertAndFocus(Alert.AlertType.ERROR, "Registration Error",
                        "An account with this email already exists.", emailField);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to check email.");
            return true; // treat as duplicate to stop registration
        }
        return false;
    }


    private boolean validatePassword(TextField field) {
        if (!validateNotBlank(field, "password")) {
            return false;
        }
        if (field.getText().isBlank() || field.getText().length() < 8) {
            showAlertAndFocus(Alert.AlertType.ERROR, "Validation Error",
                    "Please provide a password of at least 8 characters", field);
            return false;
        }
        return true;
    }

    private boolean validateRegistration() {
        if (!validateNotBlank(firstNameField, "first name")) return false;
        if (!validateNotBlank(lastNameField, "last name")) return false;
        if (!validateNotBlank(emailField, "email") || !validateEmailFormat(emailField) || isEmailDuplicate(emailField)) return false;
        if (!validatePassword(passwordField)) return false;
        return true;
    }

    public String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    private boolean saveUserToDatabase(String hashedPassword) {
        String sql = "INSERT INTO users(first_name,last_name,email,password_hash,city,age_group,profile_stage,anonymous) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, getFirstName());
            ps.setString(2, getLastName());
            ps.setString(3, getEmail());
            ps.setString(4, hashedPassword);
            ps.setString(5, getCity());
            ps.setString(6, ageSelect != null ? ageSelect.getValue() : null);
            ps.setString(7, profileStage != null ? profileStage.getValue() : null);
            ps.setInt(8, anonymousCheck != null && anonymousCheck.isSelected() ? 1 : 0);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", "Failed to register user: " + ex.getMessage());
            return false;
        }
    }

    private void showRegistrationSuccess() {
        showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Your account has been created successfully.");
        NavigationService.go("/com/javaninjas/careerpathway/registration/views/successfulRegistrationPage.fxml",
                (SuccessfulRegistrationController controller) -> controller.setWelcomeName(getFirstName()));
    }



    @FXML
    private void onRegister() {
        if (!validateRegistration()) return;

        String hashed = hashPassword(getPassword());

        if (!saveUserToDatabase(hashed)) return;

        showRegistrationSuccess();
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

    public boolean isValidPassword(String password) {return password != null && !password.isBlank() && password.length() >= 8;}

    public boolean isValidCity(String city) {
        return city != null && !city.trim().isEmpty();
    }

    // ===== Validation helpers extracted from pages.registrationPage =====

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

