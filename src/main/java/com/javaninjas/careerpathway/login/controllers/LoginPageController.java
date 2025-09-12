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
    // Directly navigate to user pathway without credential checks.
    // Navigate to the Explore Pathways view instead of the user pathway.
    NavigationService.go("/com/javaninjas/careerpathway/dashboard/views/userPathway.fxml");
    }

    @FXML
    private void handleRegisterLink(ActionEvent event) {
    NavigationService.go("/com/javaninjas/careerpathway/registration/views/registrationPage.fxml");
    }
}