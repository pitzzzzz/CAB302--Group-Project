package com.javaninjas.careerpathway.app;

import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

public final class StageUtils {
    private StageUtils() {
    }

    /**
        * Bind the given stage so it preserves the provided aspect ratio while
     * @param stage       the Stage to configure
     * @param aspectRatio width / height (e.g. 3.0/2.0)
     * @param minWidth    minimum width in pixels
     * @param minHeight   minimum height in pixels
     */

    public static void bindAspectRatio(Stage stage, double aspectRatio, double minWidth, double minHeight) {
        if (stage == null || aspectRatio <= 0)
            return;

        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight);

        AtomicBoolean adjusting = new AtomicBoolean(false);

        stage.widthProperty().addListener((obs, oldW, newW) -> {
            if (adjusting.get())
                return;
            adjusting.set(true);
            stage.setHeight(newW.doubleValue() / aspectRatio);
            adjusting.set(false);
        });

        stage.heightProperty().addListener((obs, oldH, newH) -> {
            if (adjusting.get())
                return;
            adjusting.set(true);
            stage.setWidth(newH.doubleValue() * aspectRatio);
            adjusting.set(false);
        });
    }
}
