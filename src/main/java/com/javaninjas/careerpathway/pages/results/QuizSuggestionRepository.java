package com.javaninjas.careerpathway.pages.results;

import com.javaninjas.careerpathway.pages.results.models.QuizPathwaySuggestion;

public interface QuizSuggestionRepository {
    void saveSuggestion(QuizPathwaySuggestion suggestion);
}
