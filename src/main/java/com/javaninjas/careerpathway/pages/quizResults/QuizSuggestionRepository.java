package com.javaninjas.careerpathway.pages.quizResults;

import com.javaninjas.careerpathway.pages.quizResults.models.QuizPathwaySuggestion;

public interface QuizSuggestionRepository {
    void saveSuggestion(QuizPathwaySuggestion suggestion);
}
