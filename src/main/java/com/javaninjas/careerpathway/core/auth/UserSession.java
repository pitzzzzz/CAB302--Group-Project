package com.javaninjas.careerpathway.core.auth;

import java.util.Objects;

/**
 * Thread-safe singleton to hold the current logged-in user's session information.
 * Usage:
 *   UserSession.getInstance(...); // to create or update the singleton with user data
 *   UserSession.getInstance(); // to retrieve the current instance (may return null if not initialised)
 */
public final class UserSession {

	private static volatile UserSession instance;

	private final int userID;
	private final String email;
	private final String userType;
	private final String firstName;
	private final String lastName;
	private final String dateOfBirth;
	private final String educationLevel;
	private final String workExperience;
	private final String interests;
	private final String certifications;
	private final String desiredSalary;
	private final String preferredWorkHours;

	private UserSession(int userID,
						String email,
						String userType,
						String firstName,
						String lastName,
						String dateOfBirth,
						String educationLevel,
						String workExperience,
						String interests,
						String certifications,
						String desiredSalary,
						String preferredWorkHours) {
		this.userID = userID;
		this.email = email;
		this.userType = userType;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.educationLevel = educationLevel;
		this.workExperience = workExperience;
		this.interests = interests;
		this.certifications = certifications;
		this.desiredSalary = desiredSalary;
		this.preferredWorkHours = preferredWorkHours;
	}

	/**
	 * Create or replace the singleton instance with provided user details in a thread-safe manner.
	 * This matches the call seen in the login controller.
	 */
	public static UserSession getInstance(int userID,
										  String email,
										  String userType,
										  String firstName,
										  String lastName,
										  String dateOfBirth,
										  String educationLevel,
										  String workExperience,
										  String interests,
										  String certifications,
										  String desiredSalary,
										  String preferredWorkHours) {
		UserSession result = instance;
		if (result == null) {
			synchronized (UserSession.class) {
				result = instance;
				if (result == null) {
					instance = result = new UserSession(userID, email, userType, firstName, lastName, dateOfBirth, educationLevel, workExperience, interests, certifications, desiredSalary, preferredWorkHours);
				} else {
					// replace existing
					instance = result = new UserSession(userID, email, userType, firstName, lastName, dateOfBirth, educationLevel, workExperience, interests, certifications, desiredSalary, preferredWorkHours);
				}
			}
		} else {
			// replace existing without locking - create new instance atomically
			UserSession newInst = new UserSession(userID, email, userType, firstName, lastName, dateOfBirth, educationLevel, workExperience, interests, certifications, desiredSalary, preferredWorkHours);
			synchronized (UserSession.class) {
				instance = newInst;
				result = instance;
			}
		}
		return result;
	}

	/**
	 * Return the current instance, or null if not created yet.
	 */
	public static UserSession getInstance() {
		return instance;
	}

	/**
	 * Clears the current session (logout).
	 */
	public static void clear() {
		synchronized (UserSession.class) {
			instance = null;
		}
	}

	// Getters
	public int getUserID() { return userID; }
	public String getEmail() { return email; }
	public String getUserType() { return userType; }
	public String getFirstName() { return firstName; }
	public String getLastName() { return lastName; }
	public String getDateOfBirth() { return dateOfBirth; }
	public String getEducationLevel() { return educationLevel; }
	public String getWorkExperience() { return workExperience; }
	public String getInterests() { return interests; }
	public String getCertifications() { return certifications; }
	public String getDesiredSalary() { return desiredSalary; }
	public String getPreferredWorkHours() { return preferredWorkHours; }

	@Override
	public String toString() {
		return "UserSession{" +
				"userID=" + userID +
				", email='" + email + '\'' +
				", userType='" + userType + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", dateOfBirth='" + dateOfBirth + '\'' +
				", educationLevel='" + educationLevel + '\'' +
				", workExperience='" + workExperience + '\'' +
				", interests='" + interests + '\'' +
				", certifications='" + certifications + '\'' +
				", desiredSalary='" + desiredSalary + '\'' +
				", preferredWorkHours='" + preferredWorkHours + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserSession that = (UserSession) o;
		return userID == that.userID &&
				Objects.equals(email, that.email) &&
				Objects.equals(userType, that.userType) &&
				Objects.equals(firstName, that.firstName) &&
				Objects.equals(lastName, that.lastName) &&
				Objects.equals(dateOfBirth, that.dateOfBirth) &&
				Objects.equals(educationLevel, that.educationLevel) &&
				Objects.equals(workExperience, that.workExperience) &&
				Objects.equals(interests, that.interests) &&
				Objects.equals(certifications, that.certifications) &&
				Objects.equals(desiredSalary, that.desiredSalary) &&
				Objects.equals(preferredWorkHours, that.preferredWorkHours);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userID, email, userType, firstName, lastName, dateOfBirth, educationLevel, workExperience, interests, certifications, desiredSalary, preferredWorkHours);
	}
}
