package com.javaninjas.careerpathway.core.services;

import javafx.concurrent.Task;
import javafx.concurrent.ScheduledService;
import javafx.util.Duration;

/**
 * Background service to periodically clean up expired cache entries.
 * Runs automatically to maintain cache hygiene without manual intervention.
 */
public class CacheMaintenanceService extends ScheduledService<Void> {
    
    private static CacheMaintenanceService instance;
    private static boolean isRunning = false;
    
    private CacheMaintenanceService() {
        // Set period to run every 15 minutes
        setPeriod(Duration.minutes(15));
        setMaximumCumulativePeriod(Duration.hours(1));
        
        // Handle failures gracefully
        setOnFailed(e -> {
            System.err.println("Cache maintenance failed: " + getException().getMessage());
            // Restart the service after failure
            restart();
        });
    }
    
    /**
     * Gets the singleton instance of the cache maintenance service.
     */
    public static synchronized CacheMaintenanceService getInstance() {
        if (instance == null) {
            instance = new CacheMaintenanceService();
        }
        return instance;
    }
    
    /**
     * Starts the cache maintenance service if not already running.
     */
    public static synchronized void startMaintenance() {
        if (!isRunning) {
            getInstance().start();
            isRunning = true;
            System.out.println("Cache maintenance service started");
        }
    }
    
    /**
     * Stops the cache maintenance service.
     */
    public static synchronized void stopMaintenance() {
        if (isRunning && instance != null) {
            instance.cancel();
            isRunning = false;
            System.out.println("Cache maintenance service stopped");
        }
    }
    
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    // Clear expired entries from all caches
                    CareerPlanCacheService.clearExpiredEntries();
                    JobCacheService.cleanupExpired();
                    
                    // Log cache statistics for monitoring
                    var careerStats = CareerPlanCacheService.getCacheStats();
                    var jobStats = JobCacheService.getCacheStats();
                    System.out.println("Cache maintenance completed.");
                    System.out.println("Career Plan Cache: " + careerStats);
                    System.out.println("Job Data Cache: " + jobStats);
                    
                } catch (Exception e) {
                    System.err.println("Error during cache maintenance: " + e.getMessage());
                    e.printStackTrace();
                    throw e;
                }
                return null;
            }
        };
    }
    
    /**
     * Checks if the maintenance service is currently running.
     */
    public static boolean isMaintenanceRunning() {
        return isRunning;
    }
}