package com.javaninjas.careerpathway.registration.controllers;

import com.javaninjas.careerpathway.registration.controllers.UserRegistrationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegistrationPageControllersTest {
    private UserRegistrationController UserRegistration;

    @BeforeEach
    void setUp() {
        UserRegistration = new UserRegistrationController();
    }

    // For now, we can create helper methods in the controller to test business logic

    @Test
    public void testControllerCreation() {
        // Basic test to ensure controller can be instantiated
        assertNotNull(UserRegistration);
    }


    @Test
    public void testValidateFirstName() {
        assertTrue(UserRegistration.isValidFirstName("John"));
        assertFalse(UserRegistration.isValidFirstName(""));
        assertFalse(UserRegistration.isValidFirstName("   "));
        assertFalse(UserRegistration.isValidFirstName(null));
    }

    @Test
    public void testValidateLastName() {
        assertTrue(UserRegistration.isValidLastName("Doe"));
        assertFalse(UserRegistration.isValidLastName(""));
        assertFalse(UserRegistration.isValidLastName("   "));
        assertFalse(UserRegistration.isValidLastName(null));
    }

    @Test
    public void testValidateEmailFormat() {
        assertTrue(UserRegistration.isValidEmail("user@example.com"));
        assertTrue(UserRegistration.isValidEmail("test.email+tag@domain.co.uk"));

        assertFalse(UserRegistration.isValidEmail("invalid-email"));
        assertFalse(UserRegistration.isValidEmail("@domain.com"));
        assertFalse(UserRegistration.isValidEmail("user@"));
        assertFalse(UserRegistration.isValidEmail(""));
        assertFalse(UserRegistration.isValidEmail(null));
    }

    @Test
    public void testValidatePassword() {
        assertTrue(UserRegistration.isValidPassword("password123"));
        assertTrue(UserRegistration.isValidPassword("12345678"));

        assertFalse(UserRegistration.isValidPassword("short"));
        assertFalse(UserRegistration.isValidPassword("1234567")); // 7 chars
        assertFalse(UserRegistration.isValidPassword(""));
        assertFalse(UserRegistration.isValidPassword("   "));
        assertFalse(UserRegistration.isValidPassword(null));
    }

    @Test
    public void testValidateCity() {
        assertTrue(UserRegistration.isValidCity("Brisbane"));
        assertTrue(UserRegistration.isValidCity("New York"));

        // City might be optional, depending on your requirements
        assertFalse(UserRegistration.isValidCity(""));
        assertFalse(UserRegistration.isValidCity("   "));
        assertFalse(UserRegistration.isValidCity(null));
    }

    @Test
    public void testPasswordHashing() {
        String password = "testPassword123";
        String hash = UserRegistration.hashPassword(password);

        assertNotNull(hash);
        assertNotEquals(password, hash); // Hash should be different from original
        assertTrue(UserRegistration.verifyPassword(password, hash));
        assertFalse(UserRegistration.verifyPassword("wrongPassword", hash));
    }

    @Test
    public void testGetAgeOptions() {
        String[] expectedAges = {"Under 18", "18-24", "25-34", "35-44", "45+"};
        String[] actualAges = UserRegistration.getAgeOptions();
        assertArrayEquals(expectedAges, actualAges);
    }

    @Test
    public void testGetStageOptions() {
        String[] expectedStages = {"Student", "Graduate", "Early career", "Experienced"};
        String[] actualStages = UserRegistration.getStageOptions();
        assertArrayEquals(expectedStages, actualStages);
    }

    @Test
    public void testCompleteFormValidation() {
        // Test a complete form validation scenario
        UserRegistrationController.RegistrationData data = new UserRegistrationController.RegistrationData();
        data.firstName = "John";
        data.lastName = "Doe";
        data.email = "john@example.com";
        data.password = "password123";
        data.city = "Brisbane";

        assertTrue(UserRegistration.validateRegistrationData(data));

        // Test with invalid data
        data.email = "invalid-email";
        assertFalse(UserRegistration.validateRegistrationData(data));
    }

}