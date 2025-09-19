package com.javaninjas.careerpathway.pages.quiz;

import com.javaninjas.careerpathway.pages.quiz.controllers.ProgressDotsController;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

public class ProgressDots extends HBox {
    public ProgressDots() {
        try {
            FXMLLoader f = new FXMLLoader(getClass().getResource("/com/javaninjas/careerpathway/core/ProgressDots.fxml"));
            f.setRoot(this);
            f.setController(new ProgressDotsController(this));
            f.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
