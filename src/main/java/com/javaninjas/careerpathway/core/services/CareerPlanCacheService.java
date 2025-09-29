package com.javaninjas.careerpathway.core.services;

import com.javaninjas.careerpathway.core.integrations.openai.models.CareerPlan;
import com.javaninjas.careerpathway.core.auth.UserSession;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Service for caching career plans to avoid overwhelming AI generation requests.
 * Uses session-based caching with time-based expiration.
 */
public class CareerPlanCacheService {
    
    // Cache entry to store both the plan and timestamp
    private static class CacheEntry {
        private final CareerPlan plan;
        private final LocalDateTime timestamp;
        
        public CacheEntry(CareerPlan plan) {
            this.plan = plan;
            this.timestamp = LocalDateTime.now();
        }
        
        public CareerPlan getPlan() {
            return plan;
        }
        
        public boolean isExpired(long maxAgeMinutes) {
            return ChronoUnit.MINUTES.between(timestamp, LocalDateTime.now()) > maxAgeMinutes;
        }
        
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
    
    // Static cache to store career plans by user session
    private static final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    
    // Cache expiration time in minutes (default: 30 minutes)
    private static final long DEFAULT_CACHE_EXPIRY_MINUTES = 30;
    
    /**
     * Gets a cached career plan for the given career name and current user session.
     * Returns null if no valid cache entry exists.
     * 
     * @param careerName The name of the career
     * @return Cached CareerPlan or null if not found/expired
     */
    public static CareerPlan getCachedPlan(String careerName) {
        return getCachedPlan(careerName, DEFAULT_CACHE_EXPIRY_MINUTES);
    }
    
    /**
     * Gets a cached career plan with custom expiry time.
     * 
     * @param careerName The name of the career
     * @param maxAgeMinutes Maximum age in minutes before cache expires
     * @return Cached CareerPlan or null if not found/expired
     */
    public static CareerPlan getCachedPlan(String careerName, long maxAgeMinutes) {
        String cacheKey = generateCacheKey(careerName);
        if (cacheKey == null) {
            return null;
        }
        
        CacheEntry entry = cache.get(cacheKey);
        if (entry == null || entry.isExpired(maxAgeMinutes)) {
            // Remove expired entry
            if (entry != null) {
                cache.remove(cacheKey);
            }
            return null;
        }
        
        return entry.getPlan();
    }
    
    /**
     * Caches a career plan for the current user session.
     * 
     * @param careerName The name of the career
     * @param plan The career plan to cache
     */
    public static void cachePlan(String careerName, CareerPlan plan) {
        String cacheKey = generateCacheKey(careerName);
        if (cacheKey != null && plan != null) {
            cache.put(cacheKey, new CacheEntry(plan));
        }
    }
    
    /**
     * Clears the cache for the current user session.
     */
    public static void clearUserCache() {
        UserSession session = UserSession.getInstance();
        if (session != null) {
            String userPrefix = "user_" + session.getUserID() + "_";
            cache.entrySet().removeIf(entry -> entry.getKey().startsWith(userPrefix));
        }
    }
    
    /**
     * Clears all expired entries from the cache.
     */
    public static void clearExpiredEntries() {
        cache.entrySet().removeIf(entry -> 
            entry.getValue().isExpired(DEFAULT_CACHE_EXPIRY_MINUTES));
    }
    
    /**
     * Gets cache statistics for monitoring.
     * 
     * @return Map containing cache size and other stats
     */
    public static Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new ConcurrentHashMap<>();
        stats.put("totalEntries", cache.size());
        
        long expiredCount = cache.values().stream()
            .mapToLong(entry -> entry.isExpired(DEFAULT_CACHE_EXPIRY_MINUTES) ? 1 : 0)
            .sum();
        
        stats.put("expiredEntries", expiredCount);
        stats.put("validEntries", cache.size() - expiredCount);
        
        return stats;
    }
    
    /**
     * Generates a cache key for the given career name and current user session.
     * 
     * @param careerName The career name
     * @return Cache key string or null if no valid session
     */
    private static String generateCacheKey(String careerName) {
        UserSession session = UserSession.getInstance();
        if (session == null || careerName == null || careerName.trim().isEmpty()) {
            return null;
        }
        
        // Create a unique key based on user ID and normalized career name
        String normalizedCareer = careerName.trim().toLowerCase().replaceAll("\\s+", "_");
        return "user_" + session.getUserID() + "_career_" + normalizedCareer;
    }
    
    /**
     * Checks if a plan is cached for the given career name.
     * 
     * @param careerName The career name to check
     * @return true if a valid cached plan exists
     */
    public static boolean hasCachedPlan(String careerName) {
        return getCachedPlan(careerName) != null;
    }
    
    /**
     * Gets the timestamp when the plan was cached.
     * 
     * @param careerName The career name
     * @return LocalDateTime when cached, or null if not found
     */
    public static LocalDateTime getCacheTimestamp(String careerName) {
        String cacheKey = generateCacheKey(careerName);
        if (cacheKey == null) {
            return null;
        }
        
        CacheEntry entry = cache.get(cacheKey);
        return entry != null ? entry.getTimestamp() : null;
    }
}