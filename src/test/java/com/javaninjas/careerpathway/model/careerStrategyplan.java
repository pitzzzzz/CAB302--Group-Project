package com.javaninjas.careerpathway.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.javaninjas.careerpathway.core.models.Career;
import com.javaninjas.careerpathway.core.models.CareerStrategyPlan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Mock class for the Career dependency to simplify testing CareerStrategyPlan.
 * Only implements the necessary getName() and equals/hashCode based on ID.
 */
class MockCareer {
    private final String id;
    private final String name;

    public MockCareer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Must implement equals/hashCode compatible with the actual Career model
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MockCareer that = (MockCareer) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


/**
 * Test class for the CareerStrategyPlan model.
 * Focuses on testing constructor, getters/setters, utility methods, and equals/hashCode.
 */
class CareerStrategyPlanTest {

    // --- Test Data ---
    private static final String PLAN_ID = "plan-001";
    private static final int TIME_MONTHS = 24;
    private static final double COST = 15000.50;
    private static final double STABILITY = 0.85;
    private static final List<String> INITIAL_STEPS = Arrays.asList("Learn basics", "Build portfolio");

    private Career mockCareer;

    @BeforeEach
    void setUp() {
        // Use the mock or the actual Career class if available
        mockCareer = new Career("career-A", "Software Developer", "Build software.", Collections.emptyList(), Collections.emptyList());
    }

    // Helper method to create a fully set-up CareerStrategyPlan
    private CareerStrategyPlan createFullPlan() {
        CareerStrategyPlan plan = new CareerStrategyPlan(PLAN_ID, mockCareer);
        plan.setEstimatedTimeMonths(TIME_MONTHS);
        plan.setEstimatedCost(COST);
        plan.setStabilityScore(STABILITY);
        plan.setSteps(INITIAL_STEPS);
        return plan;
    }

    // --- Constructor and Getters Tests ---

    @Test
    @DisplayName("Constructor: Should correctly initialize final ID and create an empty steps list")
    void testConstructorInitialization() {
        CareerStrategyPlan plan = new CareerStrategyPlan(PLAN_ID, mockCareer);

        assertEquals(PLAN_ID, plan.getId(), "ID must be set by the constructor.");
        assertEquals(mockCareer, plan.getCareer(), "Career must be set by the constructor.");
        assertNotNull(plan.getSteps(), "Steps list must be initialized (not null).");
        assertTrue(plan.getSteps().isEmpty(), "Steps list must be empty initially.");
        assertEquals(0, plan.getEstimatedTimeMonths(), "Int fields should default to 0.");
        assertEquals(0.0, plan.getEstimatedCost(), "Double fields should default to 0.0.");
    }

    // --- Setters Tests ---

    @Test
    @DisplayName("Setters: Should correctly update all mutable fields")
    void testSetters() {
        CareerStrategyPlan plan = new CareerStrategyPlan(PLAN_ID, mockCareer);

        int newTime = 36;
        double newCost = 25000.0;
        double newStability = 0.95;
        Career newCareer = new Career("career-B", "Architect", "Design systems.", Collections.emptyList(), Collections.emptyList());
        List<String> newSteps = Collections.singletonList("Get certifications");

        plan.setEstimatedTimeMonths(newTime);
        plan.setEstimatedCost(newCost);
        plan.setStabilityScore(newStability);
        plan.setCareer(newCareer);
        plan.setSteps(newSteps);

        assertEquals(newTime, plan.getEstimatedTimeMonths(), "Time should be updated.");
        assertEquals(newCost, plan.getEstimatedCost(), "Cost should be updated.");
        assertEquals(newStability, plan.getStabilityScore(), "Stability score should be updated.");
        assertEquals(newCareer, plan.getCareer(), "Career object should be updated.");
        assertEquals(newSteps, plan.getSteps(), "Steps list should be updated.");
    }

    // --- Utility Methods Tests ---

    @Test
    @DisplayName("addStep: Should append a step to the existing list")
    void testAddStep() {
        CareerStrategyPlan plan = new CareerStrategyPlan(PLAN_ID, mockCareer);
        plan.setSteps(new ArrayList<>(INITIAL_STEPS)); // ensure a mutable list is set

        String newStep = "Do an internship";
        plan.addStep(newStep);

        List<String> steps = plan.getSteps();
        assertEquals(INITIAL_STEPS.size() + 1, steps.size(), "Step count should increase.");
        assertEquals(newStep, steps.get(steps.size() - 1), "The new step should be the last element.");
    }

    @Test
    @DisplayName("summarizePlan: Should generate a correct summary string")
    void testSummarizePlan() {
        CareerStrategyPlan plan = createFullPlan();

        String expectedSummary = "Plan for " + mockCareer.getName() +
                                 ": " + INITIAL_STEPS.size() + " steps, " +
                                 TIME_MONTHS + " months, $" +
                                 COST + " cost.";

        assertEquals(expectedSummary, plan.summarizePlan(), "The summary string must match the expected format and content.");
    }

    // --- Equals and HashCode Tests ---

    @Test
    @DisplayName("equals: Two plans with the same ID should be equal")
    void testEquals_SameId() {
        CareerStrategyPlan plan1 = createFullPlan();
        
        // Create a second plan with the same ID but different data
        CareerStrategyPlan plan2 = new CareerStrategyPlan(PLAN_ID, new Career("c-id", "Some Other Career", "", Collections.emptyList(), Collections.emptyList()));
        plan2.setEstimatedCost(99999.99);

        assertTrue(plan1.equals(plan2), "Plans with the same ID should be equal regardless of other field values.");
    }

    @Test
    @DisplayName("equals: Two plans with different IDs should not be equal")
    void testEquals_DifferentId() {
        CareerStrategyPlan plan1 = createFullPlan();
        CareerStrategyPlan plan2 = new CareerStrategyPlan("plan-002", mockCareer);

        assertFalse(plan1.equals(plan2), "Plans with different IDs should not be equal.");
    }

    @Test
    @DisplayName("hashCode: Two equal plans must have the same hash code")
    void testHashCode_Equal() {
        CareerStrategyPlan plan1 = createFullPlan();
        CareerStrategyPlan plan2 = new CareerStrategyPlan(PLAN_ID, mockCareer);

        assertEquals(plan1.hashCode(), plan2.hashCode(), "Equal objects must have equal hash codes.");
    }

    @Test
    @DisplayName("toString: Should contain all major field values in its output")
    void testToString() {
        CareerStrategyPlan plan = createFullPlan();
        String result = plan.toString();

        assertTrue(result.contains("id='" + PLAN_ID + "'"), "toString should contain the ID.");
        assertTrue(result.contains("career=" + mockCareer.getName()), "toString should contain the Career Name.");
        assertTrue(result.contains("steps=" + INITIAL_STEPS), "toString should contain the steps list.");
        assertTrue(result.contains("estimatedTimeMonths=" + TIME_MONTHS), "toString should contain the estimated time.");
        assertTrue(result.contains("estimatedCost=" + COST), "toString should contain the estimated cost.");
        assertTrue(result.contains("stabilityScore=" + STABILITY), "toString should contain the stability score.");
    }
}