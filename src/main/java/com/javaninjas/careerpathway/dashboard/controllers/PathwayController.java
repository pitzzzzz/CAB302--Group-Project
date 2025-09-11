package com.javaninjas.careerpathway.dashboard.controllers;

import com.javaninjas.careerpathway.app.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PathwayController {
    @FXML private Button exploreBtn;
    @FXML private Button pathwayBtn;
    @FXML private Button profileBtn;

    @FXML
    private void initialize() {
        if (pathwayBtn != null) pathwayBtn.getStyleClass().add("active");

        if (exploreBtn != null) exploreBtn.setOnAction(e -> NavigationService.go("/com/javaninjas/careerpathway/dashboard/views/explorePathways.fxml"));
        if (pathwayBtn != null) pathwayBtn.setOnAction(e -> NavigationService.go("/com/javaninjas/careerpathway/dashboard/views/userPathway.fxml"));
        if (profileBtn != null) profileBtn.setOnAction(e -> NavigationService.go("/com/javaninjas/careerpathway/dashboard/views/userProfile.fxml"));
    }
}
