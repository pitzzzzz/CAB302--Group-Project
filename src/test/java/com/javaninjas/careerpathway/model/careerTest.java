package com.javaninjas.careerpathway.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.javaninjas.careerpathway.core.models.Career;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Career model.
 * Focuses on testing constructor, getters/setters, equals/hashCode, and utility methods.
 */
public class careerTest {

    // --- Test Data ---
    private static final String ID = "uuid-12345";
    private static final String NAME = "Data Scientist";
    private static final String DESCRIPTION = "Analyze and interpret complex data sets.";
    private static final List<String> REQUIREMENTS = Arrays.asList("Master's Degree", "Python", "Statistics");
    private static final List<String> TAGS = Arrays.asList("data", "analytical", "research");

    // Helper method to create a standard Career object
    private Career createCareer() {
        return new Career(ID, NAME, DESCRIPTION, REQUIREMENTS, TAGS);
    }

    // --- Constructor and Getters Tests ---

    @Test
    @DisplayName("Constructor and Getters: Should correctly initialize and retrieve all fields")
    void testConstructorAndGetters() {
        Career career = createCareer();

        assertEquals(ID, career.getId(), "ID must match the value passed to the constructor.");
        assertEquals(NAME, career.getName(), "Name must match the value passed to the constructor.");
        assertEquals(DESCRIPTION, career.getDescription(), "Description must match the value passed to the constructor.");
        assertEquals(REQUIREMENTS, career.getRequirements(), "Requirements list must match the set list.");
        assertEquals(TAGS, career.getTags(), "Tags list must match the set list.");
    }

    @Test
    @DisplayName("ID field should be immutable (only a getter exists for it)")
    void testIdImmutability() {
        // Since there is no setId, we only ensure getId works, which is covered above.
        // This test serves as documentation for the design constraint.
        Career career = createCareer();
        assertThrows(NoSuchMethodException.class, () -> Career.class.getMethod("setId", String.class));
        assertEquals(ID, career.getId());
    }

    // --- Setters Tests ---

    @Test
    @DisplayName("Setters: Should correctly update mutable fields")
    void testSetters() {
        Career career = createCareer();

        String newName = "Senior Data Scientist";
        String newDescription = "Lead data science projects.";
        List<String> newRequirements = Collections.singletonList("8 years experience");
        List<String> newTags = Arrays.asList("management", "big-data");

        career.setName(newName);
        career.setDescription(newDescription);
        career.setRequirements(newRequirements);
        career.setTags(newTags);

        assertEquals(newName, career.getName(), "Name should be updated by setter.");
        assertEquals(newDescription, career.getDescription(), "Description should be updated by setter.");
        assertEquals(newRequirements, career.getRequirements(), "Requirements should be updated by setter.");
        assertEquals(newTags, career.getTags(), "Tags should be updated by setter.");
    }

    // --- Utility Method Tests (hasTag) ---

    @Test
    @DisplayName("hasTag: Should return true for an existing tag (case-insensitive check)")
    void testHasTag_ExistsCaseInsensitive() {
        Career career = createCareer(); // Tags: ["data", "analytical", "research"]
        
        // Test lower-case, upper-case, and mixed-case input
        assertTrue(career.hasTag("data"), "Should find tag with exact case.");
        assertTrue(career.hasTag("ANALYTICAL"), "Should find tag with upper case input.");
        assertTrue(career.hasTag("Research"), "Should find tag with mixed case input.");
    }

    @Test
    @DisplayName("hasTag: Should return false for a non-existing tag")
    void testHasTag_DoesNotExist() {
        Career career = createCareer();
        assertFalse(career.hasTag("finance"), "Should not find non-existing tag.");
    }

    @Test
    @DisplayName("hasTag: Should return false when the tags list is null")
    void testHasTag_NullTagsList() {
        Career career = createCareer();
        career.setTags(null);
        assertFalse(career.hasTag("analytical"), "Should return false when the tags list is null.");
    }
    
    @Test
    @DisplayName("hasTag: Should return false when the tags list is empty")
    void testHasTag_EmptyTagsList() {
        Career career = createCareer();
        career.setTags(Collections.emptyList());
        assertFalse(career.hasTag("analytical"), "Should return false when the tags list is empty.");
    }
    
    @Test
    @DisplayName("hasTag: Should handle null input tag")
    void testHasTag_NullInput() {
        Career career = createCareer();
        assertFalse(career.hasTag(null), "Should return false for null input tag.");
    }

    // --- Equals and HashCode Tests ---

    @Test
    @DisplayName("equals: Two Careers with the same ID should be equal")
    void testEquals_SameId() {
        Career career1 = createCareer();
        
        // Create a second career with the same ID but different data
        Career career2 = new Career(ID, "Different Name", "Different Desc", Collections.emptyList(), Collections.emptyList());

        assertTrue(career1.equals(career2), "Careers with the same ID should be equal.");
    }

    @Test
    @DisplayName("equals: Two Careers with different IDs should not be equal")
    void testEquals_DifferentId() {
        Career career1 = createCareer();
        Career career2 = new Career("uuid-67890", NAME, DESCRIPTION, REQUIREMENTS, TAGS);

        assertFalse(career1.equals(career2), "Careers with different IDs should not be equal.");
    }

    @Test
    @DisplayName("hashCode: Two equal Careers must have the same hash code")
    void testHashCode_Equal() {
        Career career1 = createCareer();
        Career career2 = new Career(ID, "Different Name", "Different Desc", Collections.emptyList(), Collections.emptyList());

        assertEquals(career1.hashCode(), career2.hashCode(), "Equal objects must have equal hash codes.");
    }

    @Test
    @DisplayName("hashCode: Two unequal Careers should ideally have different hash codes")
    void testHashCode_Unequal() {
        Career career1 = createCareer();
        Career career2 = new Career("uuid-67890", NAME, DESCRIPTION, REQUIREMENTS, TAGS);

        // While not strictly required to be unequal, they usually are for different IDs
        assertNotEquals(career1.hashCode(), career2.hashCode(), "Unequal objects should typically have unequal hash codes.");
    }
    
    @Test
    @DisplayName("equals: Should return false when comparing against null or different object type")
    void testEquals_NullOrDifferentType() {
        Career career = createCareer();
        assertFalse(career.equals(null), "Should be false when comparing against null.");
        assertFalse(career.equals("A string object"), "Should be false when comparing against different type.");
    }

    // --- ToString Test ---

    @Test
    @DisplayName("toString: Should contain all major field values in its output")
    void testToString() {
        Career career = createCareer();
        String result = career.toString();

        assertTrue(result.contains("id='" + ID + "'"), "toString should contain the ID.");
        assertTrue(result.contains("name='" + NAME + "'"), "toString should contain the Name.");
        assertTrue(result.contains("description='" + DESCRIPTION + "'"), "toString should contain the Description.");
        assertTrue(result.contains("requirements=" + REQUIREMENTS), "toString should contain the Requirements list.");
        assertTrue(result.contains("tags=" + TAGS), "toString should contain the Tags list.");
    }
}