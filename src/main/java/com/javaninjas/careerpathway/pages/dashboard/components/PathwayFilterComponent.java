package com.javaninjas.careerpathway.pages.dashboard.components;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Modular filter component controller for explorePathways view.
 * Exposes a simple API to read selected filters and dispatch an action.
 */
public class PathwayFilterComponent {

    @FXML
    private FlowPane tagButtons;

    @FXML
    private ComboBox<String> salaryCombo;

    @FXML
    private ComboBox<String> satisfactionCombo;

    @FXML
    private Button openPresetsBtn;

    @FXML
    private Button applyBtn;

    @FXML
    private Button clearBtn;

    @FXML
    public void initialize() {
        salaryCombo.setItems(FXCollections.observableArrayList(
                "Any",
                "<$50k",
                "$50k-$80k",
                "$80k-$120k",
                ">$120k"));
        salaryCombo.getSelectionModel().selectFirst();

        satisfactionCombo.setItems(FXCollections.observableArrayList(
                "Any",
                "Low",
                "Medium",
                "High"));
        satisfactionCombo.getSelectionModel().selectFirst();

        // Ensure tag toggle buttons carry the theme class and update visual state on
        // selection
        tagButtons.getChildren().stream().filter(node -> node instanceof ToggleButton).forEach(node -> {
            ToggleButton tb = (ToggleButton) node;
            if (!tb.getStyleClass().contains("tag-toggle"))
                tb.getStyleClass().add("tag-toggle");
            tb.selectedProperty().addListener((obs, oldV, newV) -> {
                if (newV) {
                    if (!tb.getStyleClass().contains("selected"))
                        tb.getStyleClass().add("selected");
                } else {
                    tb.getStyleClass().remove("selected");
                }
            });
        });
    }

    /**
     * Called when Apply is pressed. Fires a simple console log for now.
     * In the containing controller (ExploreController) you can look up this
     * component
     * and call getSelectedFilters() to apply filtering logic.
     */
    @FXML
    private void applyFilters() {
        if (onApply != null) {
            onApply.accept(getSelectedFilters());
        }
    }

    @FXML
    private void clearFilters() {
        salaryCombo.getSelectionModel().selectFirst();
        satisfactionCombo.getSelectionModel().selectFirst();
        tagButtons.getChildren().stream().filter(node -> node instanceof ToggleButton).forEach(node -> {
            ToggleButton tb = (ToggleButton) node;
            tb.setSelected(false);
            tb.setStyle("");
        });
        if (onClear != null) onClear.run();
    }

    public List<String> getSelectedTags() {
        List<String> tags = new ArrayList<>();
        tagButtons.getChildren().stream().filter(n -> n instanceof ToggleButton).forEach(n -> {
            ToggleButton t = (ToggleButton) n;
            if (t.isSelected())
                tags.add(t.getText());
        });
        return tags;
    }

    public String getSalaryFilter() {
        return salaryCombo.getSelectionModel().getSelectedItem();
    }

    public String getSatisfactionFilter() {
        return satisfactionCombo.getSelectionModel().getSelectedItem();
    }

    public PathwayFilterState getSelectedFilters() {
        return new PathwayFilterState(getSelectedTags(), getSalaryFilter(), getSatisfactionFilter());
    }

    // Callbacks so container controller can react to apply/clear
    private Consumer<PathwayFilterState> onApply;
    private Runnable onClear;

    public void setOnApply(Consumer<PathwayFilterState> onApply) {
        this.onApply = onApply;
    }

    public void setOnClear(Runnable onClear) {
        this.onClear = onClear;
    }

    public void resetFilters() {
        clearFilters();
    }

}
