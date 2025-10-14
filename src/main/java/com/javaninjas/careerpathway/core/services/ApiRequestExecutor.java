package com.javaninjas.careerpathway.core.services;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * Simple shared executor for API/IO requests. Provides convenience methods to
 * run work off the JavaFX thread and return CompletableFutures.
 */
public class ApiRequestExecutor {

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors()));

    public static ExecutorService getExecutor() {
        return EXECUTOR;
    }

    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, EXECUTOR);
    }

    public static void shutdown() {
        EXECUTOR.shutdown();
    }
}
