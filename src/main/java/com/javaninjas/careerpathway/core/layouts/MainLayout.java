package com.javaninjas.careerpathway.core.layouts;


import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class MainLayout {
    private final BorderPane root;

    public MainLayout() {
        root = new BorderPane();

        // Example: Top navigation
        HBox navBar = new HBox(10);
        Button dashboardBtn = new Button("Dashboard");
        Button profileBtn = new Button("Profile");

        navBar.getChildren().addAll(dashboardBtn, profileBtn);
        root.setTop(navBar);
    }

    // Swap content (center area)
    public void setContent(Parent content) {
        root.setCenter(content);
    }

    public Parent getRoot() {
        return root;
    }
}
