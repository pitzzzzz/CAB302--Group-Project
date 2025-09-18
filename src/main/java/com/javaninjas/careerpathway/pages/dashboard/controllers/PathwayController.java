package com.javaninjas.careerpathway.pages.dashboard.controllers;

import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.core.services.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PathwayController {

    @FXML
    public void initialize() {
    }

    @FXML
    private void handleLogout() {
        UserSession.getInstance().logout();
        NavigationService.go("/com/javaninjas/careerpathway/login/views/loginPage.fxml");
    }
}
