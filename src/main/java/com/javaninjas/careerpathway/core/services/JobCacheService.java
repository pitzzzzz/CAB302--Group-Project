package com.javaninjas.careerpathway.core.services;

import com.javaninjas.careerpathway.core.models.Job;
import com.javaninjas.careerpathway.core.models.Course;
import com.javaninjas.careerpathway.db.dao.JobDao;
import com.javaninjas.careerpathway.db.dao.CourseDao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Caching service for job and course data to reduce database queries
 * and improve performance in the explore page and pathway popup.
 */
public class JobCacheService {
    
    private static class CachedData<T> {
        private final T data;
        private final LocalDateTime timestamp;
        
        public CachedData(T data) {
            this.data = data;
            this.timestamp = LocalDateTime.now();
        }
        
        public T getData() {
            return data;
        }
        
        public boolean isExpired(int minutes) {
            return timestamp.plusMinutes(minutes).isBefore(LocalDateTime.now());
        }
        
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
    
    // Cache for all jobs list (used by explore page)
    private static volatile CachedData<List<Job>> allJobsCache;
    
    // Cache for individual courses by ID (used by popup)
    private static final Map<Integer, CachedData<Course>> courseCache = new ConcurrentHashMap<>();
    
    // Cache for filtered jobs by search query
    private static final Map<String, CachedData<List<Job>>> filteredJobsCache = new ConcurrentHashMap<>();
    
    // Cache expiration times (in minutes)
    private static final int JOB_CACHE_EXPIRATION = 15; // Jobs don't change frequently
    private static final int COURSE_CACHE_EXPIRATION = 30; // Courses change even less frequently
    private static final int SEARCH_CACHE_EXPIRATION = 5; // Search results should be fresher
    
    /**
     * Gets all jobs from cache or database if cache is expired
     */
    public static List<Job> getAllJobs() {
        if (allJobsCache == null || allJobsCache.isExpired(JOB_CACHE_EXPIRATION)) {
            try {
                List<Job> jobs = JobDao.getAllJobs();
                allJobsCache = new CachedData<>(jobs);
                System.out.println("JobCacheService: Loaded " + jobs.size() + " jobs from database and cached");
            } catch (Exception e) {
                System.err.println("JobCacheService: Error loading jobs from database: " + e.getMessage());
                e.printStackTrace();
                // Return empty list or cached data if available
                return allJobsCache != null ? allJobsCache.getData() : List.of();
            }
        } else {
            System.out.println("JobCacheService: Using cached jobs data");
        }
        
        return allJobsCache.getData();
    }
    
    /**
     * Gets filtered jobs by search query from cache or filters fresh data
     */
    public static List<Job> getJobsBySearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllJobs();
        }
        
        String normalizedQuery = query.toLowerCase().trim();
        CachedData<List<Job>> cachedResults = filteredJobsCache.get(normalizedQuery);
        
        if (cachedResults == null || cachedResults.isExpired(SEARCH_CACHE_EXPIRATION)) {
            List<Job> allJobs = getAllJobs();
            List<Job> filteredJobs = allJobs.stream()
                .filter(job -> job.getJobName().toLowerCase().contains(normalizedQuery))
                .toList();
            
            filteredJobsCache.put(normalizedQuery, new CachedData<>(filteredJobs));
            System.out.println("JobCacheService: Filtered and cached " + filteredJobs.size() + " jobs for query: " + query);
            return filteredJobs;
        } else {
            System.out.println("JobCacheService: Using cached search results for query: " + query);
            return cachedResults.getData();
        }
    }
    
    /**
     * Gets course by ID from cache or database if cache is expired
     */
    public static Course getCourseById(int courseId) {
        CachedData<Course> cachedCourse = courseCache.get(courseId);
        
        if (cachedCourse == null || cachedCourse.isExpired(COURSE_CACHE_EXPIRATION)) {
            try {
                Course course = CourseDao.getCourseById(courseId);
                courseCache.put(courseId, new CachedData<>(course));
                System.out.println("JobCacheService: Loaded course " + courseId + " from database and cached");
                return course;
            } catch (Exception e) {
                System.err.println("JobCacheService: Error loading course " + courseId + " from database: " + e.getMessage());
                e.printStackTrace();
                // Return cached data if available, even if expired
                return cachedCourse != null ? cachedCourse.getData() : null;
            }
        } else {
            System.out.println("JobCacheService: Using cached course data for ID: " + courseId);
            return cachedCourse.getData();
        }
    }
    
    /**
     * Invalidates all cached data - useful when data might have been updated
     */
    public static void clearCache() {
        allJobsCache = null;
        courseCache.clear();
        filteredJobsCache.clear();
        System.out.println("JobCacheService: All caches cleared");
    }
    
    /**
     * Invalidates only the jobs cache - useful after job data updates
     */
    public static void clearJobsCache() {
        allJobsCache = null;
        filteredJobsCache.clear();
        System.out.println("JobCacheService: Jobs cache cleared");
    }
    
    /**
     * Invalidates course cache for a specific course ID
     */
    public static void clearCourseCache(int courseId) {
        courseCache.remove(courseId);
        System.out.println("JobCacheService: Course cache cleared for ID: " + courseId);
    }
    
    /**
     * Gets cache statistics for monitoring
     */
    public static String getCacheStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("JobCacheService Statistics:\n");
        
        if (allJobsCache != null) {
            stats.append("- All Jobs: Cached at ")
                 .append(allJobsCache.getTimestamp())
                 .append(", Expired: ")
                 .append(allJobsCache.isExpired(JOB_CACHE_EXPIRATION))
                 .append("\n");
        } else {
            stats.append("- All Jobs: Not cached\n");
        }
        
        stats.append("- Courses in cache: ").append(courseCache.size()).append("\n");
        stats.append("- Search results in cache: ").append(filteredJobsCache.size()).append("\n");
        
        return stats.toString();
    }
    
    /**
     * Cleanup expired entries to prevent memory leaks
     */
    public static void cleanupExpired() {
        // Clean up expired course cache entries
        courseCache.entrySet().removeIf(entry -> 
            entry.getValue().isExpired(COURSE_CACHE_EXPIRATION));
        
        // Clean up expired search cache entries
        filteredJobsCache.entrySet().removeIf(entry -> 
            entry.getValue().isExpired(SEARCH_CACHE_EXPIRATION));
        
        System.out.println("JobCacheService: Cleaned up expired cache entries");
    }
}