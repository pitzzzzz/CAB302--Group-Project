package com.javaninjas.careerpathway.pages.quiz;

import com.javaninjas.careerpathway.pages.quiz.models.QuizPathwaySuggestion;

public interface QuizSuggestionRepository {
    void saveSuggestion(QuizPathwaySuggestion suggestion);
}
