package com.javaninjas.careerpathway.core.controllers;

import com.javaninjas.careerpathway.core.services.NavigationService;

import javafx.fxml.FXML;

public class BackController {
    @FXML
    private void handleBack() {
        NavigationService.goBack();
    }
}
