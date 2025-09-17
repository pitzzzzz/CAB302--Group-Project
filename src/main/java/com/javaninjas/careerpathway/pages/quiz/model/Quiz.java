package com.javaninjas.careerpathway.pages.quiz.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Quiz {
    private final List<Question> questions;
    private final Map<Question, AnswerScale> answers;
    private int currentQuestionIndex;

    public Quiz(List<Question> questions) {
        this.questions = questions;
        this.answers = new HashMap<>();
        this.currentQuestionIndex = 0;
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public Question getQuestion(int index) {
        if (index >= 0 && index < questions.size()) return questions.get(index);
        return null;
    }

    public void answerCurrentQuestion(AnswerScale answer) {
        if (getCurrentQuestion() != null) {
            answers.put(getCurrentQuestion(), answer);
            currentQuestionIndex++;
        }
    }

    public void answerQuestionAt(int index, AnswerScale answer) {
        if (index >= 0 && index < questions.size()) {
            Question q = questions.get(index);
            answers.put(q, answer);
            if (index >= currentQuestionIndex) {
                currentQuestionIndex = index + 1;
            }
        }
    }

    public boolean isFinished() {
        return currentQuestionIndex >= questions.size();
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }
    
    public int getTotalQuestions() {
        return questions.size();
    }
}
