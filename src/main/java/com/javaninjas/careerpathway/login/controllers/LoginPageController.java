package com.javaninjas.careerpathway.login.controllers;

import com.javaninjas.careerpathway.app.NavigationService;
import com.javaninjas.careerpathway.app.Database;

import at.favre.lib.crypto.bcrypt.BCrypt;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.animation.FillTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class LoginPageController {
    @FXML
    private Label welcomeText;

    @FXML
    private StackPane rootPane;

    @FXML
    private Rectangle bgRect;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        // bind background rectangle to root size
        if (bgRect != null && rootPane != null) {
            bgRect.widthProperty().bind(rootPane.widthProperty());
            bgRect.heightProperty().bind(rootPane.heightProperty());

            // simple color pulse animation
            FillTransition ft = new FillTransition(Duration.seconds(6), bgRect, Color.web("#f4f6f8"), Color.web("#eef7ff"));
            ft.setCycleCount(FillTransition.INDEFINITE);
            ft.setAutoReverse(true);
            ft.play();
        }
    }

    @FXML
    private void handleLogin() {
        String email = emailField != null ? emailField.getText().trim() : "";
        String password = passwordField != null ? passwordField.getText() : "";

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Please enter email and password.");
            return;
        }

        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT password_hash FROM users WHERE email = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, email);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String hash = rs.getString("password_hash");
                        BCrypt.Result res = BCrypt.verifyer().verify(password.toCharArray(), hash);
                        if (res.verified) {
                            NavigationService.go("/com/javaninjas/careerpathway/dashboard/views/userPathway.fxml");
                            return;
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Login failed: " + ex.getMessage());
            return;
        }

        showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid email or password.");
    }


    // Methods for LoginPageController for testing
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.contains("@") && email.contains(".");
    }

    public boolean isValidPassword(String password) {
        return password != null && !password.trim().isEmpty();
    }

    public String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public boolean verifyPassword(String password, String hash) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hash);
        return result.verified;
    }

    @FXML
    private void handleRegisterLink(ActionEvent event) {
    NavigationService.go("/com/javaninjas/careerpathway/registration/views/registrationPage.fxml");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}