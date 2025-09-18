package com.javaninjas.careerpathway.pages.dashboard.controllers;

import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.core.services.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PathwayController {

    @FXML
    private Button exploreBtn;

    @FXML
    private Button pathwayBtn;

    @FXML
    private Button profileBtn;

    @FXML
    public void initialize() {
        if (exploreBtn != null) exploreBtn.setOnAction(e -> NavigationService.go("/com/javaninjas/careerpathway/dashboard/views/explorePathways.fxml"));
        if (pathwayBtn != null) pathwayBtn.setOnAction(e -> NavigationService.go("/com/javaninjas/careerpathway/dashboard/views/userPathway.fxml"));
        if (profileBtn != null) profileBtn.setOnAction(e -> NavigationService.go("/com/javaninjas/careerpathway/dashboard/views/userProfile.fxml"));
    }

    @FXML
    private void handleLogout() {
        UserSession.getInstance().logout();
        NavigationService.go("/com/javaninjas/careerpathway/login/views/loginPage.fxml");
    }
}
