package com.javaninjas.careerpathway.intergration;

import com.javaninjas.careerpathway.core.integrations.openai.models.CareerPlan;
import com.javaninjas.careerpathway.core.integrations.openai.models.CareerPlan.WeekPlan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the CareerPlan and its nested class WeekPlan.
 * Verifies the correct functionality of all getters and setters.
 */
class CareerPlanTest {

    // Test Data for WeekPlan
    private static final int WEEK_ONE = 1;
    private static final List<String> WEEK_ONE_TASKS = Arrays.asList(
            "Complete basic Java tutorial",
            "Set up development environment"
    );
    private static final int WEEK_TWO = 2;
    private static final List<String> WEEK_TWO_TASKS = Collections.singletonList(
            "Build first simple application"
    );

    // Test Data for CareerPlan
    private static final String CAREER_TITLE = "Software Developer";

    // -------------------------------------------------------------------------
    // WeekPlan Tests (Nested Class)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("WeekPlan: Should correctly set and get the week number")
    void testWeekPlan_SetAndGetWeek() {
        WeekPlan plan = new WeekPlan();
        plan.setWeek(WEEK_ONE);
        assertEquals(WEEK_ONE, plan.getWeek(), "The week number should match the set value.");
    }

    @Test
    @DisplayName("WeekPlan: Should correctly set and get the tasks list")
    void testWeekPlan_SetAndGetTasks() {
        WeekPlan plan = new WeekPlan();
        plan.setTasks(WEEK_ONE_TASKS);
        
        List<String> retrievedTasks = plan.getTasks();
        
        assertNotNull(retrievedTasks, "The tasks list should not be null after setting.");
        assertEquals(WEEK_ONE_TASKS.size(), retrievedTasks.size(), "The size of the tasks list should match.");
        assertEquals(WEEK_ONE_TASKS, retrievedTasks, "The retrieved tasks list should be identical to the set list.");
    }

    @Test
    @DisplayName("WeekPlan: Should handle setting an empty tasks list")
    void testWeekPlan_SetAndGetTasks_Empty() {
        WeekPlan plan = new WeekPlan();
        List<String> emptyList = Collections.emptyList();
        
        plan.setTasks(emptyList);
        
        assertNotNull(plan.getTasks(), "The tasks list should not be null.");
        assertTrue(plan.getTasks().isEmpty(), "The retrieved tasks list should be empty.");
    }

    // -------------------------------------------------------------------------
    // CareerPlan Tests (Outer Class)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("CareerPlan: Should correctly set and get the career title")
    void testCareerPlan_SetAndGetCareer() {
        CareerPlan careerPlan = new CareerPlan();
        careerPlan.setCareer(CAREER_TITLE);
        
        String retrievedCareer = careerPlan.getCareer();
        
        assertNotNull(retrievedCareer, "The career title should not be null.");
        assertEquals(CAREER_TITLE, retrievedCareer, "The retrieved career title should match the set value.");
    }

    @Test
    @DisplayName("CareerPlan: Should correctly set and get the plan list of WeekPlan objects")
    void testCareerPlan_SetAndGetPlan() {
        // 1. Create WeekPlan objects
        WeekPlan week1 = new WeekPlan();
        week1.setWeek(WEEK_ONE);
        week1.setTasks(WEEK_ONE_TASKS);

        WeekPlan week2 = new WeekPlan();
        week2.setWeek(WEEK_TWO);
        week2.setTasks(WEEK_TWO_TASKS);
        
        List<WeekPlan> fullPlan = Arrays.asList(week1, week2);

        // 2. Set plan in CareerPlan
        CareerPlan careerPlan = new CareerPlan();
        careerPlan.setPlan(fullPlan);

        // 3. Retrieve and assert
        List<WeekPlan> retrievedPlan = careerPlan.getPlan();

        assertNotNull(retrievedPlan, "The plan list should not be null after setting.");
        assertEquals(2, retrievedPlan.size(), "The plan list should contain 2 weeks.");
        
        // Assert content of the first week
        assertEquals(WEEK_ONE, retrievedPlan.get(0).getWeek(), "First week number is correct.");
        assertEquals(WEEK_ONE_TASKS, retrievedPlan.get(0).getTasks(), "First week tasks are correct.");

        // Assert content of the second week
        assertEquals(WEEK_TWO, retrievedPlan.get(1).getWeek(), "Second week number is correct.");
        assertEquals(WEEK_TWO_TASKS, retrievedPlan.get(1).getTasks(), "Second week tasks are correct.");
    }

    // -------------------------------------------------------------------------
    // Null/Edge Case Tests
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("CareerPlan: Should handle null value for career title")
    void testCareerPlan_SetAndGetCareer_Null() {
        CareerPlan careerPlan = new CareerPlan();
        careerPlan.setCareer(null);
        assertNull(careerPlan.getCareer(), "Should return null if null was set for career.");
    }

    @Test
    @DisplayName("CareerPlan: Should handle null value for plan list")
    void testCareerPlan_SetAndGetPlan_Null() {
        CareerPlan careerPlan = new CareerPlan();
        careerPlan.setPlan(null);
        assertNull(careerPlan.getPlan(), "Should return null if null was set for plan list.");
    }
}