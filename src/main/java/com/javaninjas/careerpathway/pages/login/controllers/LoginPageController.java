package com.javaninjas.careerpathway.pages.login.controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.core.services.NavigationService;
import com.javaninjas.careerpathway.db.connection.Database;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPageController {

    @FXML
    private StackPane rootPane;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    private Label messageLabel;

    @FXML
    private Label welcomeText;

    public void initialize() {
        // Initialization logic can go here if needed
    }

    // ---------------------------------------------------------------------
    // Helper methods (kept public for unit testing)
    // ---------------------------------------------------------------------

    /**
     * Simple email format validation.
     * Returns false for null or empty strings.
     */
    public boolean isValidEmail(String email) {
        if (email == null) return false;
        email = email.trim();
        if (email.isEmpty()) return false;
        // Basic regex that accepts common email formats
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    /**
     * Basic password validation. Currently ensures password is not null/blank.
     */
    public boolean isValidPassword(String password) {
        if (password == null) return false;
        return !password.trim().isEmpty();
    }

    /**
     * Hash a password using bcrypt. Returns null if input is null.
     */
    public String hashPassword(String password) {
        if (password == null) return null;
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    /**
     * Verify a plaintext password against a bcrypt hash. Returns false for null inputs.
     */
    public boolean verifyPassword(String plainPassword, String hash) {
        if (plainPassword == null || hash == null) return false;
        BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), hash);
        return result.verified;
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Email and password cannot be empty.");
            return;
        }

        // Asynchronous database call
        new Thread(() -> {
            try (Connection conn = Database.getConnection()) {
                String sql = "SELECT * FROM users WHERE email = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, email);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), storedHash);

                    if (result.verified) {
                        int userID = rs.getInt("id");
                        String userType = "user"; // rs.getString("UserType");
                        String firstName = rs.getString("first_name");
                        String lastName = rs.getString("last_name");
                        String dateOfBirth = null; // rs.getString("DateOfBirth");
                        String educationLevel = null; // rs.getString("EducationLevel");
                        String workExperience = null; // rs.getString("WorkExperience");
                        String interests = null; // rs.getString("Interests");
                        String certifications = null; // rs.getString("Certifications");
                        String desiredSalary = null; // rs.getString("DesiredSalary");
                        String preferredWorkHours = null; // rs.getString("PreferredWorkHours");

                        // Update UI on the JavaFX Application Thread
                        Platform.runLater(() -> {
                            UserSession.getInstance(userID, email, userType, firstName, lastName, dateOfBirth, educationLevel, workExperience, interests, certifications, desiredSalary, preferredWorkHours);
                            messageLabel.setText("Login successful!");
                            NavigationService.go("/com/javaninjas/careerpathway/dashboard/views/userPathway.fxml");
                        });
                    } else {
                        Platform.runLater(() -> messageLabel.setText("Invalid email or password."));
                    }
                } else {
                    Platform.runLater(() -> messageLabel.setText("Invalid email or password."));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Platform.runLater(() -> messageLabel.setText("Database error."));
            }
        }).start();
    }

    @FXML
    private void handleRegisterLink() {
        NavigationService.go("/com/javaninjas/careerpathway/registration/views/registrationPage.fxml");
    }
}
