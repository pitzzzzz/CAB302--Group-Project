package com.javaninjas.careerpathway.quiz.model;

import java.util.List;

public record Question (String prompt, List<String> options) {
    public Question {
        if (options == null || options.size() < 2) throw new IllegalArgumentException("Need at least 2 options.");
    }
}
