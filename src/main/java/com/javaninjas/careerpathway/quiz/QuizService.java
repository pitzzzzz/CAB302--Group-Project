package com.javaninjas.careerpathway.quiz;

import com.javaninjas.careerpathway.quiz.model.Question;
import com.javaninjas.careerpathway.quiz.model.QuizResult;
import java.util.List;

public class QuizService {
    private final List<List<Question>> questionSets;

    public QuizService(List<List<Question>> questionSets) {
        this.questionSets = questionSets;
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
