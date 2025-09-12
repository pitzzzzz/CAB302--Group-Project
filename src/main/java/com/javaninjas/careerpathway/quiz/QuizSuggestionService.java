package com.javaninjas.careerpathway.quiz;

import com.javaninjas.careerpathway.quiz.model.QuizPathwaySuggestion;

public class QuizSuggestionService {
    private final QuizSuggestionRepository repository;

    public QuizSuggestionService(QuizSuggestionRepository repository) {
        this.repository = repository;
    }

    public void saveSuggestion(QuizPathwaySuggestion suggestion) {
        repository.saveSuggestion(suggestion);
    }
}
