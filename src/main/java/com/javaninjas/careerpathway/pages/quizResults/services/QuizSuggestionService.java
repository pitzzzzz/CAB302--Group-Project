package com.javaninjas.careerpathway.pages.quizResults.services;

import com.javaninjas.careerpathway.pages.quizResults.QuizSuggestionRepository;
import com.javaninjas.careerpathway.pages.quizResults.models.QuizPathwaySuggestion;

public class QuizSuggestionService {
    private final QuizSuggestionRepository repository;

    public QuizSuggestionService(QuizSuggestionRepository repository) {
        this.repository = repository;
    }

    public void saveSuggestion(QuizPathwaySuggestion suggestion) {
        repository.saveSuggestion(suggestion);
    }
}
