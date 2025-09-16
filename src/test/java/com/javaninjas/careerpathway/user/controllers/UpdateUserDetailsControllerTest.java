package com.javaninjas.careerpathway.user.controllers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class UpdateUserDetailsControllerTest {
    private UpdateUserDetailsController controller;

    @BeforeEach
    void setUp() {
        controller = new UpdateUserDetailsController();
    }

    @Test
    public void testControllerCreation() {
        assertNotNull(controller);
    }

    @Test
    public void testValidateFirstName() {
        assertTrue(controller.isValidFirstName("John"));
        assertFalse(controller.isValidFirstName(""));
        assertFalse(controller.isValidFirstName("   "));
        assertFalse(controller.isValidFirstName(null));
    }

    @Test
    public void testValidateLastName() {
        assertTrue(controller.isValidLastName("Doe"));
        assertFalse(controller.isValidLastName(""));
        assertFalse(controller.isValidLastName("   "));
        assertFalse(controller.isValidLastName(null));
    }

    @Test
    public void testValidateEmailFormat() {
        assertTrue(controller.isValidEmail("user@example.com"));
        assertTrue(controller.isValidEmail("test.email+tag@domain.co.uk"));

        assertFalse(controller.isValidEmail("invalid-email"));
        assertFalse(controller.isValidEmail("@domain.com"));
        assertFalse(controller.isValidEmail("user@"));
        assertFalse(controller.isValidEmail(""));
        assertFalse(controller.isValidEmail(null));
    }

    @Test
    public void testValidatePassword() {
        assertTrue(controller.isValidPassword("password123"));
        assertTrue(controller.isValidPassword("12345678"));

        assertFalse(controller.isValidPassword("short"));
        assertFalse(controller.isValidPassword("1234567")); // 7 chars
        assertFalse(controller.isValidPassword(""));
        assertFalse(controller.isValidPassword("   "));
        assertFalse(controller.isValidPassword(null));
    }

    @Test
    public void testValidatePhoneNumber() {
        assertTrue(controller.isValidPhoneNumber("1234567890"));
        assertTrue(controller.isValidPhoneNumber("0412345678"));

        assertFalse(controller.isValidPhoneNumber(""));
        assertFalse(controller.isValidPhoneNumber("abcde"));
        assertFalse(controller.isValidPhoneNumber(null));
    }

    @Test
    public void testCompleteUpdateDetails() {
        // Simulate a complete update scenario
        UpdateUserDetailsController.UserDetails details = new UpdateUserDetailsController.UserDetails();
        details.firstName = "Jane";
        details.lastName = "Smith";
        details.email = "jane.smith@example.com";
        details.password = "securePassword123";
        details.phone = "0412345678";

        assertTrue(controller.validateUpdateDetails(details));

        // Test with invalid email
        details.email = "invalid-email";
        assertFalse(controller.validateUpdateDetails(details));
    }
}