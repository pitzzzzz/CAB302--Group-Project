package com.javaninjas.careerpathway.pages.results.services;

import com.javaninjas.careerpathway.pages.results.models.QuizPathwaySuggestion;

public class QuizResultService {
    private static QuizPathwaySuggestion suggestion;

    public static QuizPathwaySuggestion getSuggestion() {
        return suggestion;
    }

    public static void setSuggestion(QuizPathwaySuggestion suggestion) {
        QuizResultService.suggestion = suggestion;
    }
}
