package com.javaninjas.careerpathway.core.controllers;

public class UpdateUserDetailsController {

    public boolean isValidFirstName(String firstName) {
        return firstName != null && !firstName.trim().isEmpty();
    }

    public boolean isValidLastName(String lastName) {
        return lastName != null && !lastName.trim().isEmpty();
    }

    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        // Simple regex for email validation
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }

    public boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) return false;
        // Accepts digits, spaces, and basic Australian mobile format
        return phone.matches("^(\\d{8,12}|04\\d{8})$");
    }

    public boolean validateUpdateDetails(UserDetails details) {
        return isValidFirstName(details.firstName)
            && isValidLastName(details.lastName)
            && isValidEmail(details.email)
            && isValidPassword(details.password)
            && isValidPhoneNumber(details.phone);
    }

    public static class UserDetails {
        public String firstName;
        public String lastName;
        public String email;
        public String password;
        public String phone;
    }
}
