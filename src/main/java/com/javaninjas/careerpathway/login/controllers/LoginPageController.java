package com.javaninjas.careerpathway.login.controllers;

import com.javaninjas.careerpathway.app.NavigationService;

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
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both email and password.");
        } else if (email.equals("test@example.com") && password.equals("1234")) {
            messageLabel.setText("Login successful!");
            NavigationService.go("/com/javaninjas/careerpathway/dashboard/views/dashboard.fxml");
        } else {
            messageLabel.setText("Invalid email or password.");
        }
    }

    @FXML
    private void handleRegisterLink(ActionEvent event) {
    NavigationService.go("/com/javaninjas/careerpathway/registration/views/registrationPage.fxml");
    }
}