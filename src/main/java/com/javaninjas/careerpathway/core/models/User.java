package com.javaninjas.careerpathway.core.models;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.javaninjas.careerpathway.pages.quizResults.models.QuizResult;

import java.util.List;
import java.util.Objects;

/**
 * Simple POJO representing an application user.
 * Fields align with the database `users` table and the `UserSession` singleton.
 */
public class User {

	private int userID;
	private String firstName;
	private String lastName;
	private String email;
	private String passwordHash;
	private String city;
	private String ageGroup;
	private String profileStage;
	private boolean anonymous;

	// Additional profile fields used by UserSession
	private String dateOfBirth;
	private String educationLevel;
	private String workExperience;
	private String interests;
	private String certifications;
	private String desiredSalary;
	private String preferredWorkHours;
	private String phoneNumber;

	private List<QuizResult> quizResults;

	// ===== Career-related fields =====
	private Career selectedCareer; // chosen career
	private CareerStrategyPlan careerStrategyPlan; // generated roadmap

	public User() {
	}

	public User(int userID,
			String firstName,
			String lastName,
			String email,
			String passwordHash,
			String city,
			String ageGroup,
			String profileStage,
			boolean anonymous,
			String dateOfBirth,
			String educationLevel,
			String workExperience,
			String interests,
			String certifications,
			String desiredSalary,
			String preferredWorkHours,
			String phoneNumber) {
		this.userID = userID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.passwordHash = passwordHash;
		this.city = city;
		this.ageGroup = ageGroup;
		this.profileStage = profileStage;
		this.anonymous = anonymous;
		this.dateOfBirth = dateOfBirth;
		this.educationLevel = educationLevel;
		this.workExperience = workExperience;
		this.interests = interests;
		this.certifications = certifications;
		this.desiredSalary = desiredSalary;
		this.preferredWorkHours = preferredWorkHours;
		this.phoneNumber = phoneNumber;
	}

	// Convenience constructor without extra profile fields
	public User(int userID, String firstName, String lastName, String email, String passwordHash, String city,
			String ageGroup, String profileStage, boolean anonymous) {
		this(userID, firstName, lastName, email, passwordHash, city, ageGroup, profileStage, anonymous, null, null,
				null, null, null, null, null, null);
	}

	// ===== Getters and setters =====
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAgeGroup() {
		return ageGroup;
	}

	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}

	public String getProfileStage() {
		return profileStage;
	}

	public void setProfileStage(String profileStage) {
		this.profileStage = profileStage;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEducationLevel() {
		return educationLevel;
	}

	public void setEducationLevel(String educationLevel) {
		this.educationLevel = educationLevel;
	}

	public String getWorkExperience() {
		return workExperience;
	}

	public void setWorkExperience(String workExperience) {
		this.workExperience = workExperience;
	}

	public String getInterests() {
		return interests;
	}

	public void setInterests(String interests) {
		this.interests = interests;
	}

	public String getCertifications() {
		return certifications;
	}

	public void setCertifications(String certifications) {
		this.certifications = certifications;
	}

	public String getDesiredSalary() {
		return desiredSalary;
	}

	public void setDesiredSalary(String desiredSalary) {
		this.desiredSalary = desiredSalary;
	}

	public String getPreferredWorkHours() {
		return preferredWorkHours;
	}

	public void setPreferredWorkHours(String preferredWorkHours) {
		this.preferredWorkHours = preferredWorkHours;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<QuizResult> getQuizResults() {
		return quizResults;
	}

	public void setQuizResults(List<QuizResult> quizResults) {
		this.quizResults = quizResults;
	}

	// ===== Career-related getters/setters =====
	public Career getSelectedCareer() {
		return selectedCareer;
	}

	public void setSelectedCareer(Career selectedCareer) {
		this.selectedCareer = selectedCareer;
	}

	public CareerStrategyPlan getCareerStrategyPlan() {
		return careerStrategyPlan;
	}

	public void setCareerStrategyPlan(CareerStrategyPlan careerStrategyPlan) {
		this.careerStrategyPlan = careerStrategyPlan;
	}

	/** Convenience method to link both career and plan */
	public void linkCareerWithPlan(Career career, CareerStrategyPlan plan) {
		this.selectedCareer = career;
		this.careerStrategyPlan = plan;
	}

	// ===== Utility methods =====
	/** Hash a plain-text password using BCrypt. */
	public static String hashPassword(String password) {
		return BCrypt.withDefaults().hashToString(12, password.toCharArray());
	}

	/** Verify a plain-text password against a BCrypt hash. */
	public static boolean verifyPassword(String password, String hash) {
		if (hash == null)
			return false;
		BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hash);
		return result.verified;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		User user = (User) o;
		return userID == user.userID &&
				anonymous == user.anonymous &&
				Objects.equals(firstName, user.firstName) &&
				Objects.equals(lastName, user.lastName) &&
				Objects.equals(email, user.email) &&
				Objects.equals(passwordHash, user.passwordHash) &&
				Objects.equals(city, user.city) &&
				Objects.equals(ageGroup, user.ageGroup) &&
				Objects.equals(profileStage, user.profileStage) &&
				Objects.equals(dateOfBirth, user.dateOfBirth) &&
				Objects.equals(educationLevel, user.educationLevel) &&
				Objects.equals(workExperience, user.workExperience) &&
				Objects.equals(interests, user.interests) &&
				Objects.equals(certifications, user.certifications) &&
				Objects.equals(desiredSalary, user.desiredSalary) &&
				Objects.equals(preferredWorkHours, user.preferredWorkHours) &&
				Objects.equals(phoneNumber, user.phoneNumber) &&
				Objects.equals(quizResults, user.quizResults) &&
				Objects.equals(selectedCareer, user.selectedCareer) &&
				Objects.equals(careerStrategyPlan, user.careerStrategyPlan);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userID, firstName, lastName, email, passwordHash, city, ageGroup, profileStage,
				anonymous, dateOfBirth, educationLevel, workExperience, interests, certifications, desiredSalary,
				preferredWorkHours, phoneNumber, quizResults, selectedCareer, careerStrategyPlan);
	}

	@Override
	public String toString() {
		return "User{" +
				"userID=" + userID +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", email='" + email + '\'' +
				", city='" + city + '\'' +
				", ageGroup='" + ageGroup + '\'' +
				", profileStage='" + profileStage + '\'' +
				", anonymous=" + anonymous +
				", selectedCareer=" + (selectedCareer != null ? selectedCareer.getName() : "none") +
				", strategyPlan=" + (careerStrategyPlan != null ? careerStrategyPlan.getId() : "none") +
				'}';
	}
}
