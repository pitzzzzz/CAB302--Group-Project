package com.javaninjas.careerpathway.quiz;

import com.javaninjas.careerpathway.quiz.model.Question;
import com.javaninjas.careerpathway.quiz.model.QuizResult;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class QuizServiceTest {
    @Test
    void testCalculateResultIsReproducible() {
        List<List<Question>> sets = QuizData.getQuizQuestionSets();
        QuizService service = new QuizService(sets);
        // Simulate answers: all "Very much" (index 4)
        List<Integer> answers = Arrays.asList(4,4,4,4,4, 4,4,4,4,4, 4,4,4,4,4, 4,4,4,4,4);
        QuizResult result1 = service.calculateResult(answers);
        QuizResult result2 = service.calculateResult(answers);
        assertEquals(result1, result2, "Quiz results should be reproducible for same input");
    }
}
