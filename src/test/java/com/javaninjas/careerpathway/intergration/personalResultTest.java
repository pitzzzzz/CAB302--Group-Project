package com.javaninjas.careerpathway.intergration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.javaninjas.careerpathway.core.integrations.openai.models.PersonalityResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for PersonalityResult model.
 * Verifies the correct functionality of all getters and setters.
 */
class PersonalityResultTest {

    // Test Data
    private static final List<String> CORE_TRAITS_DATA = Arrays.asList("Extroverted", "Analytical", "Conscientious");
    private static final List<String> REFLECTIONS_DATA = Arrays.asList(
            "You thrive in social settings.",
            "You prefer data-driven decisions."
    );
    private static final String RECOMMENDED_DEGREE_DATA = "Computer Science";

    // -------------------------------------------------------------------------
    // Setters and Getters Tests
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should correctly set and get the coreTraits list")
    void testSetAndGetCoreTraits() {
        PersonalityResult result = new PersonalityResult();
        
        result.setCoreTraits(CORE_TRAITS_DATA);
        
        List<String> retrievedTraits = result.getCoreTraits();
        
        assertNotNull(retrievedTraits, "The list of core traits should not be null after setting.");
        assertEquals(CORE_TRAITS_DATA.size(), retrievedTraits.size(), "The size of the list should match the set data.");
        assertEquals(CORE_TRAITS_DATA, retrievedTraits, "The retrieved list should be identical to the set list.");
    }

    @Test
    @DisplayName("Should correctly set and get the reflections list")
    void testSetAndGetReflections() {
        PersonalityResult result = new PersonalityResult();

        result.setReflections(REFLECTIONS_DATA);

        List<String> retrievedReflections = result.getReflections();

        assertNotNull(retrievedReflections, "The list of reflections should not be null after setting.");
        assertEquals(REFLECTIONS_DATA, retrievedReflections, "The retrieved list should be identical to the set list.");
    }

    @Test
    @DisplayName("Should correctly set and get the recommendedDegree string")
    void testSetAndGetRecommendedDegree() {
        PersonalityResult result = new PersonalityResult();

        result.setRecommendedDegree(RECOMMENDED_DEGREE_DATA);

        String retrievedDegree = result.getRecommendedDegree();

        assertNotNull(retrievedDegree, "The recommended degree should not be null after setting.");
        assertEquals(RECOMMENDED_DEGREE_DATA, retrievedDegree, "The retrieved degree should match the set degree.");
    }
    
    // -------------------------------------------------------------------------
    // Edge Case Tests
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should handle null value for coreTraits list")
    void testSetAndGetCoreTraits_Null() {
        PersonalityResult result = new PersonalityResult();
        result.setCoreTraits(null);
        assertNull(result.getCoreTraits(), "Should return null if null was set for core traits.");
    }

    @Test
    @DisplayName("Should handle empty list for reflections")
    void testSetAndGetReflections_Empty() {
        PersonalityResult result = new PersonalityResult();
        List<String> emptyList = Collections.emptyList();
        
        result.setReflections(emptyList);
        
        assertEquals(0, result.getReflections().size(), "The reflections list should be empty.");
        assertEquals(emptyList, result.getReflections(), "The retrieved list should be the empty list.");
    }

    @Test
    @DisplayName("Should handle null value for recommendedDegree")
    void testSetAndGetRecommendedDegree_Null() {
        PersonalityResult result = new PersonalityResult();
        result.setRecommendedDegree(null);
        assertNull(result.getRecommendedDegree(), "Should return null if null was set for recommended degree.");
    }

    @Test
    @DisplayName("Should initialize with null lists and string by default")
    void testInitialState() {
        PersonalityResult result = new PersonalityResult();
        assertNull(result.getCoreTraits(), "Core traits should be null on object creation.");
        assertNull(result.getReflections(), "Reflections should be null on object creation.");
        assertNull(result.getRecommendedDegree(), "Recommended degree should be null on object creation.");
    }

    @Test
    @DisplayName("Should allow setting new values after initial set")
    void testReassignment() {
        PersonalityResult result = new PersonalityResult();
        
        // Initial set
        result.setRecommendedDegree("Old Degree");
        result.setCoreTraits(Arrays.asList("Old Trait"));
        
        // New set
        result.setRecommendedDegree("New Degree");
        result.setCoreTraits(CORE_TRAITS_DATA);

        assertEquals("New Degree", result.getRecommendedDegree(), "The degree should be the newly set value.");
        assertEquals(CORE_TRAITS_DATA, result.getCoreTraits(), "The traits list should be the newly set value.");
    }
}