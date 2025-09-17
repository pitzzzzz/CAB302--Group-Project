package com.javaninjas.careerpathway.pages.dashboard.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.javaninjas.careerpathway.core.services.NavigationService;

public class ProfileController {

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
}
