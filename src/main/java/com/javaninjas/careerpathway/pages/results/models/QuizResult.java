package com.javaninjas.careerpathway.pages.results.models;

public class QuizResult {
    private int id;
    private int userId;
    private String question;
    private String answer;

    public QuizResult(int id, int userId, String question, String answer) {
        this.id = id;
        this.userId = userId;
        this.question = question;
        this.answer = answer;
    }

    public QuizResult(int userId, String question, String answer) {
        this.userId = userId;
        this.question = question;
        this.answer = answer;
    }

    public QuizResult(String id2, Object object, Object question2, Object answer2) {
        //TODO Auto-generated constructor stub
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
