package com.javaninjas.careerpathway.updateUSerDetailsController;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.javaninjas.careerpathway.core.controllers.UpdateUserDetailsController;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UpdateUserDetailsController.
 */
class UpdateUserDetailsControllerTest {

    private final UpdateUserDetailsController controller = new UpdateUserDetailsController();

    // Helper method to create UserDetails with base valid data
    private UpdateUserDetailsController.UserDetails createValidDetails() {
        UpdateUserDetailsController.UserDetails details = new UpdateUserDetailsController.UserDetails();
        details.firstName = "Valid";
        details.lastName = "User";
        details.email = "valid.user@example.com";
        details.password = "secureP@ssw0rd"; // > 8 chars
        details.phone = "0412345678"; // Valid Australian mobile
        return details;
    }

    // -------------------------------------------------------------------------
    // isValidFirstName Tests
    // -------------------------------------------------------------------------
    
    @Test
    @DisplayName("isValidFirstName should return true for valid name")
    void testIsValidFirstName_Valid() {
        assertTrue(controller.isValidFirstName("John"), "Should be true for a non-empty name.");
    }

    @Test
    @DisplayName("isValidFirstName should return false for null")
    void testIsValidFirstName_Null() {
        assertFalse(controller.isValidFirstName(null), "Should be false for null input.");
    }

    @Test
    @DisplayName("isValidFirstName should return false for empty string")
    void testIsValidFirstName_Empty() {
        assertFalse(controller.isValidFirstName(""), "Should be false for an empty string.");
    }

    @Test
    @DisplayName("isValidFirstName should return false for only whitespace")
    void testIsValidFirstName_Whitespace() {
        assertFalse(controller.isValidFirstName("   "), "Should be false for only whitespace.");
    }

    // -------------------------------------------------------------------------
    // isValidLastName Tests
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("isValidLastName should return true for valid name")
    void testIsValidLastName_Valid() {
        assertTrue(controller.isValidLastName("Doe"), "Should be true for a non-empty name.");
    }

    @Test
    @DisplayName("isValidLastName should return false for null")
    void testIsValidLastName_Null() {
        assertFalse(controller.isValidLastName(null), "Should be false for null input.");
    }

    @Test
    @DisplayName("isValidLastName should return false for only whitespace")
    void testIsValidLastName_Whitespace() {
        assertFalse(controller.isValidLastName(" \t "), "Should be false for only whitespace.");
    }

    // -------------------------------------------------------------------------
    // isValidEmail Tests
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("isValidEmail should return true for a standard valid email")
    void testIsValidEmail_ValidStandard() {
        assertTrue(controller.isValidEmail("user.name123@domain-name.co.uk"), "Should be true for a complex valid email.");
    }

    @Test
    @DisplayName("isValidEmail should return false for a null email")
    void testIsValidEmail_Null() {
        assertFalse(controller.isValidEmail(null), "Should be false for null email.");
    }

    @Test
    @DisplayName("isValidEmail should return false for an email without an @ symbol")
    void testIsValidEmail_NoAtSign() {
        assertFalse(controller.isValidEmail("invalidemail.com"), "Should be false for missing '@'.");
    }

    @Test
    @DisplayName("isValidEmail should return false for an email without a domain")
    void testIsValidEmail_NoDomain() {
        assertFalse(controller.isValidEmail("user@"), "Should be false for missing domain name.");
    }

    @Test
    @DisplayName("isValidEmail should return false for leading/trailing whitespace")
    // NOTE: The implementation only checks for `isEmpty()` after `trim()`, but the regex fails
    // on whitespace anyway, so this should pass.
    void testIsValidEmail_Whitespace() {
        assertFalse(controller.isValidEmail(" user@example.com "), "Should be false for whitespace.");
    }

    // -------------------------------------------------------------------------
    // isValidPassword Tests
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("isValidPassword should return true for password with length 8")
    void testIsValidPassword_MinLength() {
        assertTrue(controller.isValidPassword("12345678"), "Should be true for password of length 8.");
    }

    @Test
    @DisplayName("isValidPassword should return true for long password")
    void testIsValidPassword_Long() {
        assertTrue(controller.isValidPassword("veryLongAndSecurePassword"), "Should be true for a long password.");
    }

    @Test
    @DisplayName("isValidPassword should return false for password with length 7")
    void testIsValidPassword_TooShort() {
        assertFalse(controller.isValidPassword("1234567"), "Should be false for password shorter than 8 chars.");
    }

    @Test
    @DisplayName("isValidPassword should return false for null")
    void testIsValidPassword_Null() {
        assertFalse(controller.isValidPassword(null), "Should be false for null password.");
    }

    // -------------------------------------------------------------------------
    // isValidPhoneNumber Tests
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("isValidPhoneNumber should return true for 10-digit mobile (04 format)")
    void testIsValidPhoneNumber_ValidMobile() {
        assertTrue(controller.isValidPhoneNumber("0412345678"), "Should be true for 04xx-xxx-xxx format.");
    }

    @Test
    @DisplayName("isValidPhoneNumber should return true for 8-digit landline (local format)")
    void testIsValidPhoneNumber_ValidLandline8() {
        assertTrue(controller.isValidPhoneNumber("98765432"), "Should be true for 8-digit local number.");
    }
    
    @Test
    @DisplayName("isValidPhoneNumber should return true for 10-digit number")
    void testIsValidPhoneNumber_Valid10Digit() {
        // e.g., 02 9876 5432 (area code 2 + 8 digits = 10 digits total)
        assertTrue(controller.isValidPhoneNumber("0298765432"), "Should be true for a 10-digit number.");
    }

    @Test
    @DisplayName("isValidPhoneNumber should return false for null")
    void testIsValidPhoneNumber_Null() {
        assertFalse(controller.isValidPhoneNumber(null), "Should be false for null input.");
    }

    @Test
    @DisplayName("isValidPhoneNumber should return false for empty string")
    void testIsValidPhoneNumber_Empty() {
        assertFalse(controller.isValidPhoneNumber(""), "Should be false for empty string.");
    }

    @Test
    @DisplayName("isValidPhoneNumber should return false for invalid characters")
    void testIsValidPhoneNumber_InvalidChars() {
        assertFalse(controller.isValidPhoneNumber("0412 345 678"), "Should be false for spaces.");
        assertFalse(controller.isValidPhoneNumber("(04)12345678"), "Should be false for parentheses.");
    }

    @Test
    @DisplayName("isValidPhoneNumber should return false for 7-digit number (too short)")
    void testIsValidPhoneNumber_TooShort() {
        assertFalse(controller.isValidPhoneNumber("1234567"), "Should be false for number shorter than 8 digits.");
    }

    // -------------------------------------------------------------------------
    // validateUpdateDetails Tests (Integration)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("validateUpdateDetails should return true for all valid fields")
    void testValidateUpdateDetails_AllValid() {
        UpdateUserDetailsController.UserDetails details = createValidDetails();
        assertTrue(controller.validateUpdateDetails(details), "Should be true when all fields are valid.");
    }

    @Test
    @DisplayName("validateUpdateDetails should return false if firstName is invalid")
    void testValidateUpdateDetails_InvalidFirstName() {
        UpdateUserDetailsController.UserDetails details = createValidDetails();
        details.firstName = " "; // Invalid
        assertFalse(controller.validateUpdateDetails(details), "Should be false due to invalid firstName.");
    }

    @Test
    @DisplayName("validateUpdateDetails should return false if email is invalid")
    void testValidateUpdateDetails_InvalidEmail() {
        UpdateUserDetailsController.UserDetails details = createValidDetails();
        details.email = "bad-email-at-domain"; // Invalid
        assertFalse(controller.validateUpdateDetails(details), "Should be false due to invalid email.");
    }

    @Test
    @DisplayName("validateUpdateDetails should return false if password is too short")
    void testValidateUpdateDetails_InvalidPassword() {
        UpdateUserDetailsController.UserDetails details = createValidDetails();
        details.password = "short"; // Invalid
        assertFalse(controller.validateUpdateDetails(details), "Should be false due to short password.");
    }

    @Test
    @DisplayName("validateUpdateDetails should return false if phone number is invalid")
    void testValidateUpdateDetails_InvalidPhone() {
        UpdateUserDetailsController.UserDetails details = createValidDetails();
        details.phone = "123-456-7890"; // Invalid
        assertFalse(controller.validateUpdateDetails(details), "Should be false due to invalid phone number format.");
    }
}