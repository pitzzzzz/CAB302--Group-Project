package com.javaninjas.careerpathway.intergration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.javaninjas.careerpathway.core.integrations.openai.models.ChatResponse;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the ChatResponse model.
 * Verifies the correct behavior of the setter and getter for the 'answer' field.
 */
public class chatResponeTest {

    // Test Data
    private static final String VALID_ANSWER = "This is a test response from the chat model.";
    private static final String EMPTY_ANSWER = "";
    private static final String NEW_ANSWER = "This is the updated response.";

    // -------------------------------------------------------------------------
    // Setters and Getters Tests
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Should correctly set and get a valid string answer")
    void testSetAndGetAnswer_Valid() {
        ChatResponse response = new ChatResponse();
        
        response.setAnswer(VALID_ANSWER);
        
        String retrievedAnswer = response.getAnswer();
        
        assertNotNull(retrievedAnswer, "The retrieved answer should not be null.");
        assertEquals(VALID_ANSWER, retrievedAnswer, "The retrieved answer must match the set value.");
    }

    @Test
    @DisplayName("Should return null when no answer is set upon instantiation")
    void testGetAnswer_InitialNull() {
        ChatResponse response = new ChatResponse();
        assertNull(response.getAnswer(), "The answer should be null initially.");
    }

    @Test
    @DisplayName("Should handle setting the answer to null")
    void testSetAndGetAnswer_Null() {
        ChatResponse response = new ChatResponse();
        
        // Set an initial value
        response.setAnswer(VALID_ANSWER);
        
        // Set to null
        response.setAnswer(null);
        
        assertNull(response.getAnswer(), "The answer should be null after setting it to null.");
    }

    @Test
    @DisplayName("Should handle setting an empty string answer")
    void testSetAndGetAnswer_Empty() {
        ChatResponse response = new ChatResponse();
        
        response.setAnswer(EMPTY_ANSWER);
        
        assertNotNull(response.getAnswer(), "The answer should not be null, it should be an empty string.");
        assertTrue(response.getAnswer().isEmpty(), "The answer should be an empty string.");
        assertEquals(EMPTY_ANSWER, response.getAnswer());
    }

    @Test
    @DisplayName("Should allow reassigning the answer")
    void testSetAndGetAnswer_Reassignment() {
        ChatResponse response = new ChatResponse();
        
        // Initial set
        response.setAnswer(VALID_ANSWER);
        
        // Reassignment
        response.setAnswer(NEW_ANSWER);
        
        assertEquals(NEW_ANSWER, response.getAnswer(), "The retrieved answer should be the value set on reassignment.");
    }
}