package com.javaninjas.careerpathway.quiz.model;

public enum AnswerScale {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);
    public final int value;
    AnswerScale(int v) { this.value = v; }
    public static AnswerScale fromIndex(int i) { return values()[i]; } // 0..4
}
