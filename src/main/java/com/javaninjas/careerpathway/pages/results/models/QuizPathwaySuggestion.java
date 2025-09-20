package com.javaninjas.careerpathway.pages.results.models;

import java.util.List;

public class QuizPathwaySuggestion {
    private final String pathwayName;
    private final String description;
    private final List<String> answers;
    private final String userId;
    private final List<String> traits;

    public QuizPathwaySuggestion(String pathwayName, String description, List<String> answers, String userId, List<String> traits) {
        this.pathwayName = pathwayName;
        this.description = description;
        this.answers = answers;
        this.userId = userId;
        this.traits = traits;
    }

    public String getPathwayName() { return pathwayName; }
    public String getDescription() { return description; }
    public List<String> getAnswers() { return answers; }
    public String getUserId() { return userId; }
    public List<String> getTraits() { return traits; }
}
