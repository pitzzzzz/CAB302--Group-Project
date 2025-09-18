package com.javaninjas.careerpathway.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CareerStrategyPlan {

    private final String id;                       // unique identifier
    private Career career;                         // linked career
    private List<String> steps;                    // ordered roadmap steps
    private int estimatedTimeMonths;               // e.g., 24 months
    private double estimatedCost;                  // e.g., tuition/fees
    private double stabilityScore;                 // AI-generated score for reliability

    // Constructor
    public CareerStrategyPlan(String id, Career career) {
        this.id = id;
        this.career = career;
        this.steps = new ArrayList<>();
    }

    // Getters
    public String getId() {
        return id;
    }

    public Career getCareer() {
        return career;
    }

    public List<String> getSteps() {
        return steps;
    }

    public int getEstimatedTimeMonths() {
        return estimatedTimeMonths;
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }

    public double getStabilityScore() {
        return stabilityScore;
    }

    // Setters
    public void setCareer(Career career) {
        this.career = career;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public void setEstimatedTimeMonths(int estimatedTimeMonths) {
        this.estimatedTimeMonths = estimatedTimeMonths;
    }

    public void setEstimatedCost(double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public void setStabilityScore(double stabilityScore) {
        this.stabilityScore = stabilityScore;
    }

    // Utility Methods
    public void addStep(String step) {
        this.steps.add(step);
    }

    public String summarizePlan() {
        return "Plan for " + career.getName() +
                ": " + steps.size() + " steps, " +
                estimatedTimeMonths + " months, $" +
                estimatedCost + " cost.";
    }

    @Override
    public String toString() {
        return "CareerStrategyPlan{" +
                "id='" + id + '\'' +
                ", career=" + career.getName() +
                ", steps=" + steps +
                ", estimatedTimeMonths=" + estimatedTimeMonths +
                ", estimatedCost=" + estimatedCost +
                ", stabilityScore=" + stabilityScore +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CareerStrategyPlan)) return false;
        CareerStrategyPlan that = (CareerStrategyPlan) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
