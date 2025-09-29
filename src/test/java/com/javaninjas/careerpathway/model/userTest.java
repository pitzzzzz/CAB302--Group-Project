package com.javaninjas.careerpathway.model;

import at.favre.lib.crypto.bcrypt.BCrypt;

import com.javaninjas.careerpathway.core.models.Career;
import com.javaninjas.careerpathway.core.models.CareerStrategyPlan;
import com.javaninjas.careerpathway.core.models.User;
import com.javaninjas.careerpathway.pages.results.models.QuizResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// --- Dummy/Mock Classes for Dependencies ---
class MockCareer extends Career {
    public MockCareer(String id, String name) {
        super(id, name, null, null, null);
    }
}

class MockCareerStrategyPlan extends CareerStrategyPlan {
    public MockCareerStrategyPlan(String id, Career career) {
        super(id, career);
    }
}

class MockQuizResult extends QuizResult {
    public MockQuizResult(String id) {
        super(id, null, null, null);
    }
}
// -------------------------------------------


class UserTest {

    // --- Test Data ---
    private static final int USER_ID = 1;
    private static final String FIRST_NAME = "Alice";
    private static final String LAST_NAME = "Smith";
    private static final String EMAIL = "alice@example.com";
    private static final String RAW_PASSWORD = "Password123!";
    private static final String CITY = "Brisbane";
    private static final String AGE_GROUP = "25-34";
    private static final String PROFILE_STAGE = "Career Exploration";
    private static final boolean ANONYMOUS = false;
    private static final String DOB = "1995-05-15";
    private static final String EDUCATION = "Bachelors";
    private static final String WORK_EXP = "5 years in marketing";
    private static final String INTERESTS = "AI, Data Science";
    private static final String CERTIFICATIONS = "PMP";
    private static final String SALARY = "90000";
    private static final String WORK_HOURS = "Flexible";
    private static final String PHONE = "0412345678";
    private static final String RECOMMENDED_COURSE = "Advanced Statistics";
    private static final String SUGGESTED_CAREER = "Data Analyst";
    
    private String hashedPassword;

    @BeforeEach
    void setUp() {
        // Hash a password once for testing
        hashedPassword = BCrypt.withDefaults().hashToString(12, RAW_PASSWORD.toCharArray());
    }

    // Helper method for the full constructor
    private User createFullUser() {
        return new User(USER_ID, FIRST_NAME, LAST_NAME, EMAIL, hashedPassword, CITY, AGE_GROUP, PROFILE_STAGE, ANONYMOUS,
                DOB, EDUCATION, WORK_EXP, INTERESTS, CERTIFICATIONS, SALARY, WORK_HOURS, PHONE, RECOMMENDED_COURSE, SUGGESTED_CAREER);
    }

    // --- Constructor Tests ---

    @Test
    @DisplayName("Default constructor should create a user with default values (0, null, false)")
    void testDefaultConstructor() {
        User user = new User();
        assertEquals(0, user.getUserID());
        assertNull(user.getEmail());
        assertFalse(user.isAnonymous());
    }

    @Test
    @DisplayName("Full constructor should initialize all fields correctly")
    void testFullConstructor() {
        User user = createFullUser();

        assertEquals(USER_ID, user.getUserID());
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(hashedPassword, user.getPasswordHash());
        assertEquals(DOB, user.getDateOfBirth());
        assertEquals(RECOMMENDED_COURSE, user.getRecommendedCourse());
        assertEquals(SUGGESTED_CAREER, user.getSuggestedCareer());
    }

    @Test
    @DisplayName("Convenience constructor should initialize core fields and set profile fields to null")
    void testConvenienceConstructor() {
        User user = new User(USER_ID, FIRST_NAME, LAST_NAME, EMAIL, hashedPassword, CITY, AGE_GROUP, PROFILE_STAGE, true);

        assertEquals(USER_ID, user.getUserID());
        assertTrue(user.isAnonymous());
        
        assertNull(user.getDateOfBirth(), "DOB should be null from convenience constructor.");
        assertNull(user.getWorkExperience(), "WorkExperience should be null from convenience constructor.");
        assertNull(user.getRecommendedCourse(), "RecommendedCourse should be null from convenience constructor.");
    }

    // --- Getters and Setters Tests ---

    @Test
    @DisplayName("All core and profile getters and setters should function correctly")
    void testAllGettersAndSetters() {
        User user = new User();
        
        // Test a few representative fields
        user.setUserID(2);
        user.setFirstName("Bob");
        user.setAnonymous(true);
        user.setDesiredSalary("120000");
        user.setSuggestedCareer("Product Manager");

        assertEquals(2, user.getUserID());
        assertEquals("Bob", user.getFirstName());
        assertTrue(user.isAnonymous());
        assertEquals("120000", user.getDesiredSalary());
        assertEquals("Product Manager", user.getSuggestedCareer());
    }

    // --- Career/Quiz Tests ---

    @Test
    @DisplayName("Should correctly set and retrieve quiz results list")
    void testSetAndGetQuizResults() {
        User user = new User();
        List<QuizResult> results = Arrays.asList(new MockQuizResult("q1"), new MockQuizResult("q2"));
        
        user.setQuizResults(results);
        
        assertEquals(2, user.getQuizResults().size());
        assertEquals(results, user.getQuizResults());
    }

    @Test
    @DisplayName("linkCareerWithPlan should correctly set both selectedCareer and careerStrategyPlan")
    void testLinkCareerWithPlan() {
        User user = new User();
        Career career = new MockCareer("C1", "Tester");
        CareerStrategyPlan plan = new MockCareerStrategyPlan("P1", career);

        user.linkCareerWithPlan(career, plan);

        assertEquals(career, user.getSelectedCareer());
        assertEquals(plan, user.getCareerStrategyPlan());
    }
    
    @Test
    @DisplayName("Should correctly set and retrieve recommendedCourse")
    void testRecommendedCourse() {
        User user = new User();
        user.setRecommendedCourse("Data Analysis 101");
        assertEquals("Data Analysis 101", user.getRecommendedCourse());
    }

    // --- Utility Methods Tests (Password Hashing) ---

    @Test
    @DisplayName("hashPassword should produce a valid BCrypt hash")
    void testHashPassword() {
        String hash = User.hashPassword(RAW_PASSWORD);
        assertNotNull(hash);
        assertTrue(hash.startsWith("$2a$12$")); // BCrypt prefix with 12 rounds
    }

    @Test
    @DisplayName("verifyPassword should return true for a correct password")
    void testVerifyPassword_Success() {
        String hash = User.hashPassword(RAW_PASSWORD);
        assertTrue(User.verifyPassword(RAW_PASSWORD, hash), "Verification should succeed for the correct password.");
    }

    @Test
    @DisplayName("verifyPassword should return false for an incorrect password")
    void testVerifyPassword_Failure() {
        String hash = User.hashPassword(RAW_PASSWORD);
        assertFalse(User.verifyPassword("WrongPassword", hash), "Verification should fail for an incorrect password.");
    }
    
    @Test
    @DisplayName("verifyPassword should return false when the provided hash is null")
    void testVerifyPassword_NullHash() {
        assertFalse(User.verifyPassword(RAW_PASSWORD, null), "Verification should fail if the hash is null.");
    }

    // --- Equals and HashCode Tests ---
    
    @Test
    @DisplayName("equals/hashCode: Two users with the same ID and data should be equal")
    void testEqualsAndHashCode_Equal() {
        User user1 = createFullUser();
        User user2 = createFullUser(); // Second instance with identical data

        assertTrue(user1.equals(user2), "Identical users should be equal.");
        assertEquals(user1.hashCode(), user2.hashCode(), "Equal objects must have equal hash codes.");
    }
    
    @Test
    @DisplayName("equals/hashCode: Two users with different IDs should not be equal")
    void testEqualsAndHashCode_DifferentId() {
        User user1 = createFullUser();
        
        User user2 = new User(2, FIRST_NAME, LAST_NAME, EMAIL, hashedPassword, CITY, AGE_GROUP, PROFILE_STAGE, ANONYMOUS,
                DOB, EDUCATION, WORK_EXP, INTERESTS, CERTIFICATIONS, SALARY, WORK_HOURS, PHONE, RECOMMENDED_COURSE, SUGGESTED_CAREER);

        assertFalse(user1.equals(user2), "Users with different IDs should not be equal.");
        assertNotEquals(user1.hashCode(), user2.hashCode(), "Unequal objects should typically have unequal hash codes.");
    }

    @Test
    @DisplayName("equals: Should return false for null or different class type")
    void testEquals_NullOrDifferentType() {
        User user = createFullUser();
        assertFalse(user.equals(null), "Should be false when comparing against null.");
        assertFalse(user.equals("A string object"), "Should be false when comparing against different type.");
    }

    // --- ToString Test ---

    @Test
    @DisplayName("toString should contain key user and career-related information")
    void testToString() {
        User user = createFullUser();
        String result = user.toString();
        
        // Check core fields
        assertTrue(result.contains("userID=" + USER_ID));
        assertTrue(result.contains("email='" + EMAIL + "'"));
        
        // Check career/plan fields when null
        assertTrue(result.contains("selectedCareer=none"));
        assertTrue(result.contains("strategyPlan=none"));
        assertTrue(result.contains("recommendedCourse=" + RECOMMENDED_COURSE));
        
        // Check career/plan fields when linked
        Career career = new MockCareer("C1", "Tester");
        CareerStrategyPlan plan = new MockCareerStrategyPlan("P1", career);
        user.linkCareerWithPlan(career, plan);
        String updatedResult = user.toString();
        
        assertTrue(updatedResult.contains("selectedCareer=Tester"));
        assertTrue(updatedResult.contains("strategyPlan=P1"));
    }
}