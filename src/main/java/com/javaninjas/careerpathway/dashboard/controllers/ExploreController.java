package com.javaninjas.careerpathway.dashboard.controllers;

import com.javaninjas.careerpathway.app.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ExploreController {
    @FXML private Button exploreBtn;
    @FXML private Button pathwayBtn;
    @FXML private Button profileBtn;

    @FXML
    private void initialize() {
        // mark explore as active
        if (exploreBtn != null) exploreBtn.getStyleClass().add("active");

        if (exploreBtn != null) exploreBtn.setOnAction(e -> NavigationService.go("/com/javaninjas/careerpathway/dashboard/views/explorePathways.fxml"));
        if (pathwayBtn != null) pathwayBtn.setOnAction(e -> NavigationService.go("/com/javaninjas/careerpathway/dashboard/views/userPathway.fxml"));
        if (profileBtn != null) profileBtn.setOnAction(e -> NavigationService.go("/com/javaninjas/careerpathway/dashboard/views/userProfile.fxml"));
    }
}
