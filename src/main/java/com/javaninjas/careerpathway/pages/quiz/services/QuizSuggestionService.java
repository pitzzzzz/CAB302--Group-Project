package com.javaninjas.careerpathway.pages.quiz.services;

import com.javaninjas.careerpathway.pages.quiz.QuizSuggestionRepository;
import com.javaninjas.careerpathway.pages.quiz.model.QuizPathwaySuggestion;

public class QuizSuggestionService {
    private final QuizSuggestionRepository repository;

    public QuizSuggestionService(QuizSuggestionRepository repository) {
        this.repository = repository;
    }

    public void saveSuggestion(QuizPathwaySuggestion suggestion) {
        repository.saveSuggestion(suggestion);
    }
}
