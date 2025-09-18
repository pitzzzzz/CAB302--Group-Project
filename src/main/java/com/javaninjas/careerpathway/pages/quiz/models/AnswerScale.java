package com.javaninjas.careerpathway.pages.quiz.models;

public enum AnswerScale {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);
    public final int value;
    AnswerScale(int v) { this.value = v; }
    public static AnswerScale fromIndex(int i) { return values()[i]; } // 0..4
    
    /** Human-readable label for UI radio buttons. */
    public String getLabel() {
        return switch (this) {
            case ONE -> "Strongly disagree";
            case TWO -> "Disagree";
            case THREE -> "Neutral";
            case FOUR -> "Agree";
            case FIVE -> "Strongly agree";
        };
    }
}
