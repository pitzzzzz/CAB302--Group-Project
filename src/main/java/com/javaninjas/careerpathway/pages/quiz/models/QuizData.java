package com.javaninjas.careerpathway.pages.quiz.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizData {
    public static List<List<Question>> getQuizQuestionSets() {
        List<List<Question>> sets = new ArrayList<>();
        // FIRST 5 QUESTIONS (two options each)
        sets.add(Arrays.asList(
            new Question("Do you prefer working in a team or independently?", List.of("In a team", "Independently")),
            new Question("Do you enjoy working more with people or with data/information?", List.of("People", "Data/information")),
            new Question("Which appeals to you more: creating new ideas or following structured processes?", List.of("Creating new ideas", "Following structured processes")),
            new Question("Would you rather work in an office/desk environment or in a more hands-on/field environment?", List.of("Office/desk", "Hands-on/field")),
            new Question("Do you prefer having a consistent daily routine or variety in your day-to-day tasks?", List.of("Consistent routine", "Variety"))
        ));

        // SECOND 5 QUESTIONS (three options each)
        sets.add(Arrays.asList(
            new Question("How important is a predictable schedule (e.g., 9–5) to you?", List.of("Very important", "Somewhat important", "Not important")),
            new Question("How willing are you to complete further study/training for your career?", List.of("Very willing", "Somewhat willing", "Not willing")),
            new Question("Which motivates you more in a career: salary or meaningful impact?", List.of("Salary", "Balance of both", "Meaningful impact")),
            new Question("How comfortable are you working in high-pressure or stressful environments?", List.of("Very comfortable", "Somewhat comfortable", "Not comfortable")),
            new Question("How comfortable are you with taking risks in your work?", List.of("Very comfortable", "Somewhat comfortable", "Not comfortable"))
        ));

        // THIRD 5 QUESTIONS (four options each)
        sets.add(Arrays.asList(
            new Question("Which of these sounds most enjoyable to you?", List.of("Building or coding software", "Designing visuals, products, or spaces", "Helping people with health or wellbeing", "Working with tools and building things")),
            new Question("Which type of problem do you most enjoy solving?", List.of("Logical or technical problems", "Creative or design challenges", "Human or social issues", "Business or financial challenges")),
            new Question("Which kind of project excites you most?", List.of("Analyzing a dataset to uncover insights", "Leading a team to deliver a new service", "Crafting a marketing campaign or pitch", "Repairing or creating something tangible")),
            new Question("What type of environment do you see yourself thriving in?", List.of("Corporate/structured organization", "Entrepreneurial/start-up setting", "Community/education environment", "Trade/technical workshop")),
            new Question("Which statement resonates most with you?", List.of("I want to innovate and push boundaries", "I want to help and serve others", "I want to organize and manage processes", "I want to craft or build with my hands"))
        ));

        // FOURTH 5 QUESTIONS (mixed option counts)
        sets.add(Arrays.asList(
            new Question("How comfortable are you in leadership roles?", List.of("Very comfortable", "Somewhat comfortable", "Not comfortable")),
            new Question("Do you enjoy explaining ideas and teaching others?", List.of("Yes, very much", "Sometimes", "Not really")),
            new Question("When faced with details, do you prefer: focusing on fine accuracy or big-picture outcomes?", List.of("Fine accuracy", "Big-picture outcomes")),
            new Question("How important is helping others and making a social difference to you?", List.of("Very important", "Somewhat important", "Not important")),
            new Question("Which value is most important to you in a career?", List.of("Creativity and innovation", "Stability and security", "Service and making a difference", "Growth and achievement"))
        ));
        return sets;
    }
}
