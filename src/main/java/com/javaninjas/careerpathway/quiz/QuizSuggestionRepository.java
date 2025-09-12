package com.javaninjas.careerpathway.quiz;

import com.javaninjas.careerpathway.quiz.model.QuizPathwaySuggestion;

public interface QuizSuggestionRepository {
    void saveSuggestion(QuizPathwaySuggestion suggestion);
}
