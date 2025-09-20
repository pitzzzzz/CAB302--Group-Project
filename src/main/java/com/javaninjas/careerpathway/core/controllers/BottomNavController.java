package com.javaninjas.careerpathway.core.controllers;

import com.javaninjas.careerpathway.core.services.NavigationService;
import javafx.fxml.FXML;

public class BottomNavController {

    @FXML
    private void handleExplore() {
        NavigationService.go("/com/javaninjas/careerpathway/pages/dashboard/views/explorePathways.fxml");
    }

    @FXML
    private void handlePathway() {
        NavigationService.go("/com/javaninjas/careerpathway/pages/dashboard/views/userPathway.fxml");
    }

    @FXML
    private void handleProfile() {
        // If there's no active session, go to login instead of profile.
        com.javaninjas.careerpathway.core.auth.UserSession session = com.javaninjas.careerpathway.core.auth.UserSession.getInstance();
        if (session == null) {
            NavigationService.go("/com/javaninjas/careerpathway/pages/login/views/LoginPage.fxml");
        } else {
            NavigationService.go("/com/javaninjas/careerpathway/pages/dashboard/views/userProfile.fxml");
        }
    }
}