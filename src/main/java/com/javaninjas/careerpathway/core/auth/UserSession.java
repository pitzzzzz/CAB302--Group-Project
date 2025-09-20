package com.javaninjas.careerpathway.core.auth;

import java.util.Objects;
import com.javaninjas.careerpathway.core.models.User;

/**
 * Thread-safe singleton to hold the current logged-in user's session information.
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
	private final String phoneNumber;
	private String recommendedCourse; // newly added

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
						String preferredWorkHours,
						String phoneNumber,
						String recommendedCourse) {
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
		this.phoneNumber = phoneNumber;
		this.recommendedCourse = recommendedCourse; // set
	}

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
										  String preferredWorkHours,
										  String phoneNumber,
										  String recommendedCourse) {
		UserSession result = instance;
		if (result == null) {
			synchronized (UserSession.class) {
				result = instance;
				if (result == null) {
					instance = result = new UserSession(userID, email, userType, firstName, lastName, dateOfBirth,
							educationLevel, workExperience, interests, certifications, desiredSalary, preferredWorkHours,
							phoneNumber, recommendedCourse);
				} else {
					instance = result = new UserSession(userID, email, userType, firstName, lastName, dateOfBirth,
							educationLevel, workExperience, interests, certifications, desiredSalary, preferredWorkHours,
							phoneNumber, recommendedCourse);
				}
			}
		} else {
			UserSession newInst = new UserSession(userID, email, userType, firstName, lastName, dateOfBirth,
					educationLevel, workExperience, interests, certifications, desiredSalary, preferredWorkHours,
					phoneNumber, recommendedCourse);
			synchronized (UserSession.class) {
				instance = newInst;
				result = instance;
			}
		}
		System.out.println("User session started for user: " + email);
		return result;
	}

	public static UserSession getInstance() {
		return instance;
	}

	public User getLoggedInUser() {
		if (instance == null) return null;
		return new User(
				this.userID,
				this.firstName,
				this.lastName,
				this.email,
				null, // passwordHash is not stored in session
				null, // city is not stored in session
				null, // ageGroup is not stored in session
				null, // profileStage is not stored in session
				false, // anonymous is not stored in session
				this.dateOfBirth,
				this.educationLevel,
				this.workExperience,
				this.interests,
				this.certifications,
				this.desiredSalary,
				this.preferredWorkHours,
				this.phoneNumber,
				this.recommendedCourse // include recommendedCourse
		);
	}

	public static void logout() {
		synchronized (UserSession.class) {
			instance = null;
		}
		System.out.println("User session ended.");
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
	public String getPhoneNumber() { return phoneNumber; }
	public String getRecommendedCourse() { return recommendedCourse; } // getter

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
				", recommendedCourse='" + recommendedCourse + '\'' +
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
				Objects.equals(preferredWorkHours, that.preferredWorkHours) &&
				Objects.equals(phoneNumber, that.phoneNumber) &&
				Objects.equals(recommendedCourse, that.recommendedCourse);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userID, email, userType, firstName, lastName, dateOfBirth, educationLevel,
				workExperience, interests, certifications, desiredSalary, preferredWorkHours, phoneNumber, recommendedCourse);
	}
}
