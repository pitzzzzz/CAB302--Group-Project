package com.javaninjas.careerpathway.pages.dashboard.controllers;

import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.core.services.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PathwayPopup {

    @FXML
    public void initialize() {
    }

    @FXML
    private void handleLogout() {
        UserSession.getInstance().logout();
        NavigationService.go("/com/javaninjas/careerpathway/pages/login/views/LoginPage.fxml");
    }
}
