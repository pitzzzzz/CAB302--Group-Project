package com.javaninjas.careerpathway.pages.components;

import com.javaninjas.careerpathway.core.models.Job;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Region;

import java.io.IOException;

public class JobCell extends ListCell<Job> {
    private Region root;
    private JobCardController controller;

    @Override
    protected void updateItem(Job item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
            setText(null);
            return;
        }

        if (root == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javaninjas/careerpathway/pages/components/jobCard.fxml"));
                root = loader.load();
                controller = loader.getController();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (controller != null) {
            controller.setJob(item);
            controller.setOnApply(job -> {
                // Default behavior: print to console. Parent controller can override by listening to selections or events.
                System.out.println("Learn more clicked for job: " + job.getJobName());
            });
        }

        setGraphic(root);
    }
}
