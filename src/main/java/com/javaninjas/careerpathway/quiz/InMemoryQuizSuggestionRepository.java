package com.javaninjas.careerpathway.quiz;

import com.javaninjas.careerpathway.quiz.model.QuizPathwaySuggestion;
import java.util.ArrayList;
import java.util.List;

public class InMemoryQuizSuggestionRepository implements QuizSuggestionRepository {
    private final List<QuizPathwaySuggestion> suggestions = new ArrayList<>();

    private static final InMemoryQuizSuggestionRepository INSTANCE = new InMemoryQuizSuggestionRepository();

    private InMemoryQuizSuggestionRepository() {}

    public static InMemoryQuizSuggestionRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void saveSuggestion(QuizPathwaySuggestion suggestion) {
        suggestions.add(suggestion);
    }

    public List<QuizPathwaySuggestion> getAllSuggestions() {
        return new ArrayList<>(suggestions);
    }
}
