package com.javaninjas.careerpathway.core.controllers;

import com.javaninjas.careerpathway.core.services.NavigationService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class SideNavMenuController implements Initializable {
    @FXML private VBox sideMenuContainer;
    @FXML private Button closeMenuBtn, dashboardBtn, profileBtn, progressBtn, logoutBtn;
    private Consumer<Void> onMenuClose;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Optional: add hover effects or style logic
    }

    public void setOnMenuClose(Consumer<Void> callback) {
        this.onMenuClose = callback;
    }

    @FXML private void closeMenu() {
        if (onMenuClose != null) onMenuClose.accept(null);
    }

    @FXML private void navigateToDashboard() {
        closeMenu();
        NavigationService.go("/com/javaninjas/careerpathway/pages/dashboard/views/userPathway.fxml");
    }
    @FXML private void navigateToProfile() {
        closeMenu();
        NavigationService.go("/com/javaninjas/careerpathway/pages/dashboard/views/userProfile.fxml");
    }
    @FXML private void navigateToExplore() {
        closeMenu();
        NavigationService.go("/com/javaninjas/careerpathway/pages/dashboard/views/explorePathways.fxml");
    }
    @FXML private void handleLogout() {
        closeMenu();
        NavigationService.go("/com/javaninjas/careerpathway/pages/login/views/LoginPage.fxml");
    }
}