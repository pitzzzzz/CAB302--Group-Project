package com.javaninjas.careerpathway.core.controllers;

import com.javaninjas.careerpathway.app.NavigationService;
import javafx.fxml.FXML;

public class BackController {
    @FXML
    private void handleBack() {
        NavigationService.goBack();
    }
}
