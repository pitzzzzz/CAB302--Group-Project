package com.javaninjas.careerpathway.pages.loading.controllers;

import com.javaninjas.careerpathway.core.services.NavigationService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.util.function.Supplier;

public class LoadingScreenController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label statusLabel;

    public void initialize() {
        progressBar.setProgress(0);
        statusLabel.setText("Initializing...");
    }

    public <T> void loadData(Supplier<T> dataSupplier, java.util.function.Consumer<T> onLoaded) {
        Task<T> loadDataTask = new Task<>() {
            @Override
            protected T call() throws Exception {
                updateMessage("Loading data...");
                updateProgress(-1, 1); // Indeterminate progress
                T result = dataSupplier.get();
                updateProgress(1, 1); // Complete
                updateMessage("Data loaded successfully!");
                return result;
            }
        };

        loadDataTask.setOnSucceeded(event -> {
            T result = loadDataTask.getValue();
            onLoaded.accept(result);
        });

        loadDataTask.setOnFailed(event -> {
            statusLabel.setText("Failed to load data. Please try again.");
            // Optionally, navigate back or show an error dialog
        });

        progressBar.progressProperty().bind(loadDataTask.progressProperty());
        statusLabel.textProperty().bind(loadDataTask.messageProperty());

        new Thread(loadDataTask).start();
    }
}
