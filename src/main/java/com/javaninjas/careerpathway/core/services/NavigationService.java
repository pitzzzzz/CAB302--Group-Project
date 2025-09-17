package com.javaninjas.careerpathway.core.services;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Instance-based NavigationService with a static façade to preserve existing call sites.
 * - Reuses the application's Scene where possible (calls setRoot) so window size and stylesheets persist.
 * - Provides controller injection for controllers that implement {@link NavigationAware} via FXMLLoader's controllerFactory.
 * - Keeps a simple history stack for goBack().
 */
public class NavigationService {
    private static NavigationService instance;

    private final Stage primary;
    private final Deque<String> history = new ArrayDeque<>();
    private final Map<String, Parent> cache = new HashMap<>();
    private final boolean enableCache = false; // keep caching off by default for simplicity

    private NavigationService(Stage primary) {
        this.primary = primary;
    }

    public static synchronized void init(Stage stage) {
        if (stage == null) throw new IllegalArgumentException("stage cannot be null");
        instance = new NavigationService(stage);
    }

    private static NavigationService get() {
        if (instance == null) {
            throw new IllegalStateException("NavigationService not initialized. Call NavigationService.init(stage) before navigating.");
        }
        return instance;
    }

    // --- Static façade (keeps existing controllers working) ---
    public static <T> void go(String fxmlClasspath, Consumer<T> controllerConfiguration) {
        get().navigate(fxmlClasspath, controllerConfiguration);
    }

    public static void go(String fxmlClasspath) {
        go(fxmlClasspath, null);
    }

    public static void goBack() {
        get().navigateBack();
    }

    // --- Instance implementation ---
    private <T> void navigate(String fxmlClasspath, Consumer<T> controllerConfiguration) {
        Platform.runLater(() -> {
            try {
                ViewLoadResult<T> result = loadView(fxmlClasspath);
                T controller = result.controller;

                if (controller != null && controllerConfiguration != null) {
                    controllerConfiguration.accept(controller);
                }

                // push current scene path to history if available
                if (primary.getScene() != null && primary.getScene().getRoot() != null && primary.getScene().getRoot().getUserData() instanceof String) {
                    String current = (String) primary.getScene().getRoot().getUserData();
                    if (current != null && !current.isEmpty()) {
                        history.push(current);
                    }
                }

                Parent root = result.root;
                root.setUserData(fxmlClasspath);

                if (primary.getScene() != null) {
                    // reuse scene and keep stylesheets
                    Scene scene = primary.getScene();
                    scene.setRoot(root);
                    ensureStylesheet(scene);
                } else {
                    double width = primary.getWidth() > 0 ? primary.getWidth() : 800;
                    double height = primary.getHeight() > 0 ? primary.getHeight() : 600;
                    Scene scene = new Scene(root, width, height);
                    ensureStylesheet(scene);
                    primary.setScene(scene);
                }

                if (controller instanceof NavigationAware) {
                    ((NavigationAware) controller).onShow();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void navigateBack() {
        Platform.runLater(() -> {
            if (history.isEmpty()) return;
            String previous = history.pop();
            try {
                ViewLoadResult<?> result = loadView(previous);
                Parent root = result.root;
                root.setUserData(previous);
                if (primary.getScene() != null) {
                    primary.getScene().setRoot(root);
                    ensureStylesheet(primary.getScene());
                } else {
                    double width = primary.getWidth() > 0 ? primary.getWidth() : 800;
                    double height = primary.getHeight() > 0 ? primary.getHeight() : 600;
                    Scene scene = new Scene(root, width, height);
                    ensureStylesheet(scene);
                    primary.setScene(scene);
                }

                if (result.controller instanceof NavigationAware) {
                    ((NavigationAware) result.controller).onShow();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void ensureStylesheet(Scene scene) {
        String css = NavigationService.class.getResource("/com/javaninjas/careerpathway/app/app.css").toExternalForm();
        if (!scene.getStylesheets().contains(css)) {
            scene.getStylesheets().add(css);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> ViewLoadResult<T> loadView(String fxmlClasspath) throws IOException {
        if (enableCache && cache.containsKey(fxmlClasspath)) {
            Parent cached = cache.get(fxmlClasspath);
            T controller = (T) cached.getUserData();
            return new ViewLoadResult<>(cached, controller);
        }

        FXMLLoader loader = new FXMLLoader(NavigationService.class.getResource(fxmlClasspath));
        loader.setControllerFactory(param -> {
            try {
                Object ctrl = param.getDeclaredConstructor().newInstance();
                if (ctrl instanceof NavigationAware) {
                    ((NavigationAware) ctrl).setNavigationService(this);
                }
                return ctrl;
            } catch (ReflectiveOperationException ex) {
                throw new RuntimeException(ex);
            }
        });

        Parent root = loader.load();
        T controller = (T) loader.getController();

        if (enableCache) {
            // store controller on root for simple retrieval
            root.setUserData(controller);
            cache.put(fxmlClasspath, root);
        }

        return new ViewLoadResult<>(root, controller);
    }

    private static final class ViewLoadResult<T> {
        final Parent root;
        final T controller;

        ViewLoadResult(Parent root, T controller) {
            this.root = root;
            this.controller = controller;
        }
    }
}
