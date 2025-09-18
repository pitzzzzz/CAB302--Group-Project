package com.javaninjas.careerpathway.pages.quiz.services;

import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.pages.quiz.models.Question;
import com.javaninjas.careerpathway.pages.quiz.models.QuizData;
import com.javaninjas.careerpathway.pages.quiz.models.QuizPathwaySuggestion;

import java.util.List;
import java.util.stream.Collectors;

public class QuizService {
    private final List<List<Question>> questionSets;

    public QuizService(List<List<Question>> questionSets) {
        this.questionSets = questionSets;
    }

    public QuizService(QuizData quizData) {
        this(QuizData.getQuizQuestionSets());
    }

    public List<Question> getQuestions() {
        return questionSets.stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public List<Question> getQuestionSet(int setIndex) {
        return questionSets.get(setIndex);
    }

    public QuizPathwaySuggestion calculateResult(List<Integer> answers) {
        UserSession session = UserSession.getInstance();
        String userId = session != null ? String.valueOf(session.getUserID()) : "0";
        // TODO: Implement logic to calculate result based on answers
        return new QuizPathwaySuggestion("Sample Pathway", "This is a sample result based on your answers.", answers, userId);
    }
}
