package com.javaninjas.careerpathway.login.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginPageControllerTest {

    private LoginPageController loginController;

    @BeforeEach
    void setUp() {
        loginController = new LoginPageController();
    }

    // For now, we can create helper methods in the controller to test logic

    @Test
    public void testControllerCreation() {
        // Basic test to ensure controller can be instantiated
        assertNotNull(loginController);
    }


    // These tests would require refactoring the controller to separate business logic
    // from UI logic. For example, you could create these methods in LoginPageController:


    @Test
    public void testValidateEmailFormat() {
        assertTrue(loginController.isValidEmail("user@example.com"));
        assertFalse(loginController.isValidEmail("invalid-email"));
        assertFalse(loginController.isValidEmail(""));
    }

    @Test
    public void testValidatePassword() {
        assertTrue(loginController.isValidPassword("password123"));
        assertFalse(loginController.isValidPassword(""));
        assertFalse(loginController.isValidPassword("   "));
    }

    @Test
    public void testPasswordHashing() {
        String password = "testPassword";
        String hash = loginController.hashPassword(password);
        assertNotNull(hash);
        assertTrue(loginController.verifyPassword(password, hash));
        assertFalse(loginController.verifyPassword("wrongPassword", hash));
    }

    @Test
    public void testValidateEmail_NullInput() {
        assertFalse(loginController.isValidEmail(null), "Null email should be invalid");
    }

    @Test
    public void testValidatePassword_NullInput() {
        assertFalse(loginController.isValidPassword(null), "Null password should be invalid");
    }


}