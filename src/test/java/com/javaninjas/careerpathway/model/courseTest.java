package com.javaninjas.careerpathway.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.javaninjas.careerpathway.core.models.Course;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Course model.
 * Focuses on constructor, getters/setters, and the toString method.
 */
public class courseTest {

    // --- Test Data ---
    private static final int ID = 101;
    private static final String MAJOR = "Information Technology";
    private static final String DESCRIPTION = "A comprehensive IT degree.";
    private static final String CODE = "BIT";
    private static final String QTAC_CODE = "123456";
    private static final String NAME = "Bachelor of Information Technology";

    // Helper method to create a standard Course object
    private Course createCourse() {
        return new Course(ID, MAJOR, DESCRIPTION, CODE, QTAC_CODE, NAME);
    }

    // --- Constructor and Getters Tests ---

    @Test
    @DisplayName("Constructor and Getters: Should correctly initialize and retrieve all fields")
    void testConstructorAndGetters() {
        Course course = createCourse();

        assertEquals(ID, course.getCourseID(), "CourseID must match the value passed to the constructor.");
        assertEquals(MAJOR, course.getCourseMajor(), "CourseMajor must match the value passed to the constructor.");
        assertEquals(DESCRIPTION, course.getDescription(), "Description must match the value passed to the constructor.");
        assertEquals(CODE, course.getCourseCode(), "CourseCode must match the value passed to the constructor.");
        assertEquals(QTAC_CODE, course.getQtacCode(), "QtacCode must match the value passed to the constructor.");
        assertEquals(NAME, course.getCourseName(), "CourseName must match the value passed to the constructor.");
    }

    @Test
    @DisplayName("CourseID field should be immutable (final)")
    void testCourseIdImmutability() {
        Course course = createCourse();
        // Since there is no setCourseID method, we just confirm the ID is readable.
        // A check for the absence of a setter is implied by the class structure.
        assertEquals(ID, course.getCourseID());
        assertThrows(NoSuchMethodException.class, () -> Course.class.getMethod("setCourseID", int.class),
                "No setter for the final courseID field should exist.");
    }

    // --- Setters Tests ---

    @Test
    @DisplayName("setCourseMajor: Should correctly update the course major")
    void testSetCourseMajor() {
        Course course = createCourse();
        String newMajor = "Software Engineering";
        course.setCourseMajor(newMajor);
        assertEquals(newMajor, course.getCourseMajor(), "Course Major should be updated by setter.");
    }

    @Test
    @DisplayName("setDescription: Should correctly update the description")
    void testSetDescription() {
        Course course = createCourse();
        String newDescription = "A different description.";
        course.setDescription(newDescription);
        assertEquals(newDescription, course.getDescription(), "Description should be updated by setter.");
    }

    @Test
    @DisplayName("setCourseCode: Should correctly update the course code")
    void testSetCourseCode() {
        Course course = createCourse();
        String newCode = "BSE";
        course.setCourseCode(newCode);
        assertEquals(newCode, course.getCourseCode(), "Course Code should be updated by setter.");
    }

    @Test
    @DisplayName("setQtacCode: Should correctly update the QTAC code")
    void testSetQtacCode() {
        Course course = createCourse();
        String newQtacCode = "654321";
        course.setQtacCode(newQtacCode);
        assertEquals(newQtacCode, course.getQtacCode(), "QTAC Code should be updated by setter.");
    }

    @Test
    @DisplayName("setCourseName: Should correctly update the course name")
    void testSetCourseName() {
        Course course = createCourse();
        String newName = "Bachelor of Engineering (Software)";
        course.setCourseName(newName);
        assertEquals(newName, course.getCourseName(), "Course Name should be updated by setter.");
    }

    // --- Edge Case Tests ---

    @Test
    @DisplayName("Setters should handle null values gracefully")
    void testSetters_NullValues() {
        Course course = createCourse();

        course.setCourseMajor(null);
        course.setDescription(null);
        course.setCourseCode(null);
        course.setQtacCode(null);
        course.setCourseName(null);

        assertNull(course.getCourseMajor(), "CourseMajor should be null.");
        assertNull(course.getDescription(), "Description should be null.");
        assertNull(course.getCourseCode(), "CourseCode should be null.");
        assertNull(course.getQtacCode(), "QtacCode should be null.");
        assertNull(course.getCourseName(), "CourseName should be null.");
    }

    // --- ToString Test ---

    @Test
    @DisplayName("toString: Should return only the courseName")
    void testToString() {
        Course course = createCourse();
        assertEquals(NAME, course.toString(), "toString() should return the courseName.");

        // Test with a different name
        String shortName = "B.IT";
        course.setCourseName(shortName);
        assertEquals(shortName, course.toString(), "toString() should reflect the updated courseName.");
    }
}
