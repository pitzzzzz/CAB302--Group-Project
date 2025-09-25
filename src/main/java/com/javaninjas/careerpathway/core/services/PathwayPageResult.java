package com.javaninjas.careerpathway.core.services;

import com.javaninjas.careerpathway.core.models.Career;
import com.javaninjas.careerpathway.core.models.CareerStrategyPlan;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Simple wrapper for the result of rendering the Pathway page.
 * Either `recommendedCareers` is present (top-5 recommendations) or
 * `strategyPlan` is present (user selected career plan).
 */
public class PathwayPageResult {

    private final List<Career> recommendedCareers;
    private final CareerStrategyPlan strategyPlan;

    private PathwayPageResult(List<Career> recommendedCareers, CareerStrategyPlan strategyPlan) {
        this.recommendedCareers = recommendedCareers == null ? Collections.emptyList() : recommendedCareers;
        this.strategyPlan = strategyPlan;
    }

    public static PathwayPageResult forRecommendations(List<Career> careers) {
        return new PathwayPageResult(careers, null);
    }

    public static PathwayPageResult forStrategyPlan(CareerStrategyPlan plan) {
        return new PathwayPageResult(null, plan);
    }

    public Optional<List<Career>> getRecommendedCareers() {
        return recommendedCareers == null || recommendedCareers.isEmpty()
                ? Optional.empty()
                : Optional.of(recommendedCareers);
    }

    public Optional<CareerStrategyPlan> getStrategyPlan() {
        return Optional.ofNullable(strategyPlan);
    }

    public boolean isRecommendations() {
        return getRecommendedCareers().isPresent();
    }

    public boolean isStrategyPlan() {
        return getStrategyPlan().isPresent();
    }
}
