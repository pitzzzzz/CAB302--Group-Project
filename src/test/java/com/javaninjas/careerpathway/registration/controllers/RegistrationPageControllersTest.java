package com.javaninjas.careerpathway.registration.controllers;

import com.javaninjas.careerpathway.registration.controllers.UserRegistrationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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

}