package com.javaninjas.careerpathway.pages.dashboard.controllers;

import com.javaninjas.careerpathway.core.auth.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.javaninjas.careerpathway.core.services.NavigationService;

public class ExploreController {

    @FXML
    private Button logoutBtn;

    @FXML
    public void initialize() {
    }

    @FXML
    private void handleLogout() {
        UserSession.getInstance().logout();
        NavigationService.go("/com/javaninjas/careerpathway/pages/login/views/LoginPage.fxml");
    }
}
