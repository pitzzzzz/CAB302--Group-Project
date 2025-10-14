package com.javaninjas.careerpathway.core.services;

import com.javaninjas.careerpathway.core.models.Job;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple per-user cache for the top-N recommended jobs so the UI doesn't
 * reshuffle recommendations on every page load. This is an in-memory cache
 * keyed by userId and is cleared on logout. Entries expire after a configurable
 * duration.
 */
public class JobRecommendationCacheService {

    private static class CacheEntry {
        private final List<Job> jobs;
        private final LocalDateTime timestamp;

        public CacheEntry(List<Job> jobs) {
            this.jobs = jobs;
            this.timestamp = LocalDateTime.now();
        }

        public List<Job> getJobs() { return jobs; }

        public boolean isExpired(long maxAgeMinutes) {
            return ChronoUnit.MINUTES.between(timestamp, LocalDateTime.now()) > maxAgeMinutes;
        }

        public LocalDateTime getTimestamp() { return timestamp; }
    }

    private static final Map<Integer, CacheEntry> cache = new ConcurrentHashMap<>();

    // Default expiry: 24 hours (in minutes) - adjust if needed
    private static final long DEFAULT_EXPIRY_MINUTES = 24 * 60;

    public static List<Job> getCachedRecommendations(int userId) {
        if (userId <= 0) return null;
        CacheEntry entry = cache.get(userId);
        if (entry == null) return null;
        if (entry.isExpired(DEFAULT_EXPIRY_MINUTES)) {
            cache.remove(userId);
            return null;
        }
        return entry.getJobs();
    }

    public static void cacheRecommendations(int userId, List<Job> jobs) {
        if (userId <= 0 || jobs == null) return;
        cache.put(userId, new CacheEntry(jobs));
    }

    public static void clearUserCache(int userId) {
        if (userId <= 0) return;
        cache.remove(userId);
    }

    public static void clearCache() {
        cache.clear();
    }
}
