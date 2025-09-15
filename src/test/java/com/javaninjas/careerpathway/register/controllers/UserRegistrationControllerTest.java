package com.javaninjas.careerpathway.register.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.javaninjas.careerpathway.login.controllers.LoginPageController;
import com.javaninjas.careerpathway.registration.controllers.UserRegistrationController;

import static org.junit.jupiter.api.Assertions.*;

public class UserRegistrationControllerTest {

    private UserRegistrationController registerController;

    @BeforeEach
    void setUp() {
        registerController = new UserRegistrationController();
    }

    // Note: Most methods in LoginPageController are difficult to unit test because:
    // 1. They depend on JavaFX UI components (@FXML fields)
    // 2. They make database calls
    // 3. They show alerts and navigate pages

    // For now, we can create helper methods in the controller to test business logic

    @Test
    public void testControllerCreation() {
        // Basic test to ensure controller can be instantiated
        assertNotNull(registerController);
    }


    // These tests would require refactoring the controller to separate business logic
    // from UI logic. For example, you could create these methods in LoginPageController:

    /*
    @Test
    void testValidateEmail() {
        assertTrue(controller.isEmailValid("user@example.com"));
        assertFalse(controller.isEmailValid("invalid-email"));
        assertFalse(controller.isEmailValid(""));
        assertFalse(controller.isEmailValid(null));
    }

    @Test
    void testValidatePassword() {
        assertTrue(controller.isPasswordValid("password123"));
        assertFalse(controller.isPasswordValid(""));
        assertFalse(controller.isPasswordValid("short"));
        assertFalse(controller.isPasswordValid(null));
    }

    @Test
    void testValidateName() {
        assertTrue(controller.isNameValid("John", "Doe"));
        assertFalse(controller.isNameValid("", "Doe"));
        assertFalse(controller.isNameValid("John", ""));
        assertFalse(controller.isNameValid(null, "Doe"));
        assertFalse(controller.isNameValid("John", null));
    }
    */
}