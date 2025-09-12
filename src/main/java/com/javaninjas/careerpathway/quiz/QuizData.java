package com.javaninjas.careerpathway.quiz;

import com.javaninjas.careerpathway.quiz.model.Question;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizData {
    public static List<List<Question>> getQuizQuestionSets() {
        List<List<Question>> sets = new ArrayList<>();
        sets.add(Arrays.asList(
            new Question("Do you enjoy working with people?", List.of("Not at all", "A little", "Somewhat", "Quite a bit", "Very much")),
            new Question("Do you like solving problems?", List.of("Not at all", "A little", "Somewhat", "Quite a bit", "Very much")),
            new Question("Are you interested in technology?", List.of("Not at all", "A little", "Somewhat", "Quite a bit", "Very much")),
            new Question("Do you prefer working alone or in a team?", List.of("Alone", "Mostly alone", "Either", "Mostly team", "Team")),
            new Question("Do you enjoy creative tasks?", List.of("Not at all", "A little", "Somewhat", "Quite a bit", "Very much"))
        ));
        sets.add(Arrays.asList(
            new Question("Do you like helping others learn?", List.of("Not at all", "A little", "Somewhat", "Quite a bit", "Very much")),
            new Question("Are you comfortable with public speaking?", List.of("Not at all", "A little", "Somewhat", "Quite a bit", "Very much")),
            new Question("Do you enjoy working with numbers?", List.of("Not at all", "A little", "Somewhat", "Quite a bit", "Very much")),
            new Question("Do you like organizing events?", List.of("Not at all", "A little", "Somewhat", "Quite a bit", "Very much")),
            new Question("Do you enjoy outdoor activities?", List.of("Not at all", "A little", "Somewhat", "Quite a bit", "Very much"))
        ));
        sets.add(Arrays.asList(
            new Question("Do you like working with your hands?", List.of("Not at all", "A little", "Somewhat", "Quite a bit", "Very much")),
            new Question("Are you interested in science?", List.of("Not at all", "A little", "Somewhat", "Quite a bit", "Very much")),
            new Question("Do you enjoy reading?", List.of("Not at all", "A little", "Somewhat", "Quite a bit", "Very much")),
            new Question("Do you like planning ahead?", List.of("Not at all", "A little", "Somewhat", "Quite a bit", "Very much")),
            new Question("Do you enjoy working with data?", List.of("Not at all", "A little", "Somewhat", "Quite a bit", "Very much"))
        ));
        sets.add(Arrays.asList(
            new Question("Do you like teaching others?", List.of("Not at all", "A little", "Somewhat", "Quite a bit", "Very much")),
            new Question("Are you interested in healthcare?", List.of("Not at all", "A little", "Somewhat", "Quite a bit", "Very much")),
            new Question("Do you enjoy leadership roles?", List.of("Not at all", "A little", "Somewhat", "Quite a bit", "Very much")),
            new Question("Do you like working with computers?", List.of("Not at all", "A little", "Somewhat", "Quite a bit", "Very much")),
            new Question("Do you enjoy research?", List.of("Not at all", "A little", "Somewhat", "Quite a bit", "Very much"))
        ));
        return sets;
    }
}
