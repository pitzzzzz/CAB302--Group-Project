package com.javaninjas.careerpathway.usersession;

import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.core.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class UserSessionTest {

    // Test data
    private static final int USER_ID = 101;
    private static final String EMAIL = "test@example.com";
    private static final String USER_TYPE = "Student";
    private static final String FIRST_NAME = "Test";
    private static final String LAST_NAME = "User";
    private static final String DOB = "1990-01-01";
    private static final String EDUCATION = "Bachelor's";
    private static final String WORK_EXP = "5 years";
    private static final String INTERESTS = "Programming";
    private static final String CERTIFICATIONS = "Java Cert";
    private static final String SALARY = "80000";
    private static final String WORK_HOURS = "Full-time";
    private static final String PHONE = "1234567890";
    private static final String RECOMMENDED_COURSE = "Advanced Java";

    private static final int NEW_USER_ID = 102;
    private static final String NEW_EMAIL = "newuser@example.com";

    // Helper method to create a UserSession instance with standard data
    private UserSession createTestSession() {
        return UserSession.getInstance(
                USER_ID, EMAIL, USER_TYPE, FIRST_NAME, LAST_NAME, DOB,
                EDUCATION, WORK_EXP, INTERESTS, CERTIFICATIONS, SALARY,
                WORK_HOURS, PHONE, RECOMMENDED_COURSE
        );
    }

    // Helper method to create a UserSession instance with new data
    private UserSession createNewTestSession() {
        return UserSession.getInstance(
                NEW_USER_ID, NEW_EMAIL, USER_TYPE, FIRST_NAME, LAST_NAME, DOB,
                EDUCATION, WORK_EXP, INTERESTS, CERTIFICATIONS, SALARY,
                WORK_HOURS, PHONE, RECOMMENDED_COURSE
        );
    }

    @BeforeEach
    @AfterEach
    void cleanUp() {
        // Ensure the session is logged out before and after each test
        UserSession.logout();
    }

    // --- Singleton Pattern Tests ---

    @Test
    @DisplayName("Should return the same instance when called multiple times")
    void testSingletonInstance() {
        UserSession session1 = createTestSession();
        UserSession session2 = UserSession.getInstance(); // No-arg getter
        UserSession session3 = createTestSession(); // Call with args again (replaces the instance)

        assertNotNull(session1, "First instance should not be null");
        assertNotNull(session2, "Second instance (no-arg) should not be null");
        assertNotNull(session3, "Third instance (with args) should not be null");

        // The implementation *replaces* the instance on subsequent calls with arguments.
        // The second call with no arguments should return the first instance created.
        assertEquals(session1, session2, "No-arg getter should return the currently set instance.");

        // The third call with arguments replaces the instance, so session2 and session3 should not be the same reference
        // if session1 and session3 have different values (which they do in the getInstance logic).
        assertNotSame(session1, session3, "Calling getInstance with args should replace the instance.");
        
        // Assert session3 contains the new values from the second call with args
        assertEquals(USER_ID, session3.getUserID(), "Instance ID should be the one set in the last getInstance call with arguments.");
    }
    
    @Test
    @DisplayName("Should correctly handle concurrent creation of the singleton")
    void testThreadSafety() throws InterruptedException {
        // Use a fixed thread pool to simulate concurrent access
        int numThreads = 10;
        ExecutorService service = Executors.newFixedThreadPool(numThreads);
        AtomicReference<UserSession> lastInstance = new AtomicReference<>();

        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            service.submit(() -> {
                UserSession session;
                // Alternate between creating a new session and retrieving the current one
                if (threadId % 2 == 0) {
                     session = createTestSession();
                } else {
                     session = createNewTestSession();
                }
                lastInstance.set(session);
            });
        }

        service.shutdown();
        // Wait for all threads to finish, or timeout after 5 seconds
        assertTrue(service.awaitTermination(5, TimeUnit.SECONDS), "Threads should terminate within the timeout.");

        // After all threads have run, the 'instance' variable should hold the *last* instance created
        // by the thread that finished last (which is hard to predict), but crucially,
        // no NullPointerException or data corruption should occur.
        UserSession finalSession = UserSession.getInstance();
        assertNotNull(finalSession, "The final instance should not be null after concurrent calls.");

        // The implementation *replaces* the instance on every call to getInstance(with args).
        // Since threads run concurrently, the final state is the result of the thread that performed the *last* write.
        // We can't strictly assert the content without more complex mechanisms, but we assert its non-nullness.
    }

    // --- Data and Getter Tests ---

    @Test
    @DisplayName("Should correctly set and retrieve all user session properties")
    void testGetters() {
        UserSession session = createTestSession();

        assertEquals(USER_ID, session.getUserID());
        assertEquals(EMAIL, session.getEmail());
        assertEquals(USER_TYPE, session.getUserType());
        assertEquals(FIRST_NAME, session.getFirstName());
        assertEquals(LAST_NAME, session.getLastName());
        assertEquals(DOB, session.getDateOfBirth());
        assertEquals(EDUCATION, session.getEducationLevel());
        assertEquals(WORK_EXP, session.getWorkExperience());
        assertEquals(INTERESTS, session.getInterests());
        assertEquals(CERTIFICATIONS, session.getCertifications());
        assertEquals(SALARY, session.getDesiredSalary());
        assertEquals(WORK_HOURS, session.getPreferredWorkHours());
        assertEquals(PHONE, session.getPhoneNumber());
        assertEquals(RECOMMENDED_COURSE, session.getRecommendedCourse());
    }

    @Test
    @DisplayName("Should return a User object with correct session data")
    void testGetLoggedInUser() {
        UserSession session = createTestSession();
        User user = session.getLoggedInUser();

        assertNotNull(user, "User object should not be null");
        assertEquals(USER_ID, user.getUserID());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(RECOMMENDED_COURSE, user.getRecommendedCourse());

        // Check fields expected to be null or default in the User object from the session
        assertNull(user.getPasswordHash(), "Password hash should be null");
        assertNull(user.getSuggestedCareer(), "Suggested career should be null");
        assertFalse(user.isAnonymous(), "Anonymous should be false");
    }

    @Test
    @DisplayName("Should return null for logged-in user when no session is active")
    void testGetLoggedInUserWhenLoggedOut() {
        UserSession.logout(); // Explicitly ensure logged out
        UserSession session = UserSession.getInstance(); // Should be null
        
        assertNull(session, "Instance should be null after logout when using no-arg getter.");
    }
    
    // --- Session Management Tests ---
    
    @Test
    @DisplayName("Should correctly clear the instance on logout")
    void testLogout() {
        createTestSession();
        assertNotNull(UserSession.getInstance(), "Instance should exist before logout.");
        
        UserSession.logout();
        
        assertNull(UserSession.getInstance(), "Instance should be null after logout.");
    }

    @Test
    @DisplayName("Should be null when calling no-arg getInstance before login")
    void testInitialState() {
        assertNull(UserSession.getInstance(), "Initial state of the instance should be null.");
    }
}
