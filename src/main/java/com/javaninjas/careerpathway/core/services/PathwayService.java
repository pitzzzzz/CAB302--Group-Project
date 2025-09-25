package com.javaninjas.careerpathway.core.services;

import com.javaninjas.careerpathway.core.models.Career;
import com.javaninjas.careerpathway.core.models.CareerStrategyPlan;
import com.javaninjas.careerpathway.core.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service responsible for preparing data for the Pathway page.
 *
 * Usage (from UI layer):
 * PathwayPageResult result = new PathwayService().renderPathwayPage(user);
 * if (result.isRecommendations()) show recommended careers; else show plan
 * details.
 */
public class PathwayService {

    /**
     * Render the pathway page for the provided user.
     * If the user has not selected a career, returns the top-5 recommended careers.
     * If the user has a selected career, returns the user's CareerStrategyPlan.
     *
     * This method is UI-agnostic: it only prepares domain data. The JavaFX layer
     * should call this and render accordingly.
     */
    public PathwayPageResult renderPathwayPage(User user) {
        Objects.requireNonNull(user, "user must not be null");

        if (user.getSelectedCareer() == null) {
            // No career chosen: fetch top-5 recommendations
            List<Career> top5 = fetchTop5RecommendedCareers(user);
            // Placeholder hook for rendering quiz-based recommendations in the UI layer
            // e.g., renderQuizBasedRecommendations(user.getQuizResults(), top5);
            return PathwayPageResult.forRecommendations(top5);
        } else {
            // Career chosen: return the existing or generate a strategy plan
            CareerStrategyPlan plan = user.getCareerStrategyPlan();
            if (plan == null) {
                // If no plan exists, generate a placeholder/default plan
                plan = generateStrategyPlanForCareer(user.getSelectedCareer(), user);
                // Optionally link plan back to user (domain behavior)
                user.setCareerStrategyPlan(plan);
            }
            // Placeholder hook for UI to render plan details
            // e.g., renderStrategyPlanDetails(plan);
            return PathwayPageResult.forStrategyPlan(plan);
        }
    }

    // ---------------------- Placeholder / helper methods ----------------------

    /**
     * Fetch the top 5 recommended careers for the given user.
     *
     * TODO: Replace this placeholder with the real algorithm that ranks jobs based
     * on quiz results and database selections. This method should query the DAO
     * or use the ranking service.
     */
    protected List<Career> fetchTop5RecommendedCareers(User user) {
        // Placeholder implementation: return an empty list or mock data.
        // Keep implementation small and side-effect free so the UI can consume results.
        return new ArrayList<>();
    }

    /**
     * Generate a basic CareerStrategyPlan for a chosen career. This is a fallback
     * when the user doesn't already have a plan stored. The real implementation
     * should consult learning providers, cost estimates, and compute a stability
     * score from labour-market data.
     */
    protected CareerStrategyPlan generateStrategyPlanForCareer(Career career, User user) {
        CareerStrategyPlan plan = new CareerStrategyPlan("plan-" + career.getId(), career);
        plan.addStep("Assess current skills and gap analysis");
        plan.addStep("Complete recommended training or certification");
        plan.addStep("Gain relevant project experience / internship");
        plan.addStep("Prepare CV and apply to entry-level roles");
        plan.setEstimatedTimeMonths(12);
        plan.setEstimatedCost(3000.0);
        plan.setStabilityScore(0.75);
        return plan;
    }

    // Placeholder comments to indicate where UI rendering helpers could live.
    // The actual JavaFX controllers should implement the rendering. We keep
    // these as comments to satisfy the requirement for placeholders.
    //
    // private void renderQuizBasedRecommendations(List<QuizResult> quizResults,
    // List<Career> recommendations) { ... }
    // private void renderStrategyPlanDetails(CareerStrategyPlan plan) { ... }
}
