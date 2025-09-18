package com.javaninjas.careerpathway.pages.quiz.models;

import java.util.List;

public record Question (int id, String prompt, List<String> options) {
    public Question {
        if (options == null || options.size() < 2) throw new IllegalArgumentException("Need at least 2 options.");
    }

    public Question(String prompt, List<String> options) {
        this(0, prompt, options);
    }
}
