package com.javaninjas.careerpathway.pages.quiz.services;

import com.javaninjas.careerpathway.pages.quiz.model.Question;
import com.javaninjas.careerpathway.pages.quiz.model.QuizData;
import com.javaninjas.careerpathway.pages.quiz.model.QuizResult;
import java.util.List;
import java.util.stream.Collectors;

public class QuizService {
    private final List<List<Question>> questionSets;

    public QuizService(List<List<Question>> questionSets) {
        this.questionSets = questionSets;
    }

    /**
     * Convenience constructor used by UI code which creates a QuizService with a QuizData instance.
     * Delegates to the primary constructor using the static QuizData.getQuizQuestionSets().
     */
    public QuizService(QuizData quizData) {
        this(QuizData.getQuizQuestionSets());
    }

    /**
     * Return a flattened list of all questions across all sets. Used by UI controllers.
     */
    public List<Question> getQuestions() {
        return questionSets.stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public List<Question> getQuestionSet(int setIndex) {
        return questionSets.get(setIndex);
    }

    public QuizResult calculateResult(List<Integer> answers) {
        // TODO: Implement logic to calculate result based on answers
        // Return a QuizResult with a title and description based on input
        return new QuizResult("Sample Pathway", "This is a sample result based on your answers.");
    }
}
