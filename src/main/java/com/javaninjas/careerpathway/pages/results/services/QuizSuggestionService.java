package com.javaninjas.careerpathway.pages.results.services;

import com.javaninjas.careerpathway.pages.results.QuizSuggestionRepository;
import com.javaninjas.careerpathway.pages.results.models.QuizPathwaySuggestion;

public class QuizSuggestionService {
    private final QuizSuggestionRepository repository;

    public QuizSuggestionService(QuizSuggestionRepository repository) {
        this.repository = repository;
    }

    public void saveSuggestion(QuizPathwaySuggestion suggestion) {
        repository.saveSuggestion(suggestion);
    }
}
