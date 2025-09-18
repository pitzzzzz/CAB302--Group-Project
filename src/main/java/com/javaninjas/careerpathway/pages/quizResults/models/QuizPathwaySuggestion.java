package com.javaninjas.careerpathway.pages.quizResults.models;

import java.util.List;

public class QuizPathwaySuggestion {
    private final String pathwayName;
    private final String description;
    private final List<Integer> answers;
    private final String userId;

    public QuizPathwaySuggestion(String pathwayName, String description, List<Integer> answers, String userId) {
        this.pathwayName = pathwayName;
        this.description = description;
        this.answers = answers;
        this.userId = userId;
    }

    public String getPathwayName() { return pathwayName; }
    public String getDescription() { return description; }
    public List<Integer> getAnswers() { return answers; }
    public String getUserId() { return userId; }
}
