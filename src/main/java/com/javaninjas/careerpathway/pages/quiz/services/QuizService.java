package com.javaninjas.careerpathway.pages.quiz.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.core.integrations.openai.ChatGptClient;
import com.javaninjas.careerpathway.core.integrations.openai.ChatGptService;
import com.javaninjas.careerpathway.db.connection.Database;
import com.javaninjas.careerpathway.db.dao.ResultDao;
import com.javaninjas.careerpathway.pages.quiz.models.Question;
import com.javaninjas.careerpathway.pages.quiz.models.QuizData;
import com.javaninjas.careerpathway.pages.results.models.QuizPathwaySuggestion;
import com.javaninjas.careerpathway.pages.results.models.QuizResult;
import com.javaninjas.careerpathway.pages.results.services.QuizResultService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuizService {
    private final List<List<Question>> questionSets;

    public QuizService(List<List<Question>> questionSets) {
        this.questionSets = questionSets;
    }

    public QuizService(QuizData quizData) {
        this(QuizData.getQuizQuestionSets());
    }

    public List<Question> getQuestions() {
        return questionSets.stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public List<Question> getQuestionSet(int setIndex) {
        return questionSets.get(setIndex);
    }

    public List<List<Question>> getQuestionSets() {
        return questionSets;
    }

    public QuizPathwaySuggestion calculateResult(List<String> answers) {
        UserSession session = UserSession.getInstance();
        long long_userId = session != null ? session.getUserID() : 0;
        int userId = (int) long_userId;

        List<Question> allQuestions = getQuestions();

        try (Connection conn = Database.getConnection()) {
            ResultDao resultDao = new ResultDao(conn);
            for (int i = 0; i < allQuestions.size(); i++) {
                String questionText = allQuestions.get(i).prompt();
                String answerText = answers.get(i);
                if (answerText != null) {
                    QuizResult result = new QuizResult(userId, questionText, answerText);
                    resultDao.addResult(result);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }

        // AI-powered analysis
        try {
            ChatGptClient client = new ChatGptClient("YOUR_API_KEY");
            ChatGptService chatGptService = new ChatGptService(client);

            StringBuilder userAnswersText = new StringBuilder();
            for (int i = 0; i < allQuestions.size(); i++) {
                userAnswersText.append("Q: ").append(allQuestions.get(i).prompt()).append("\n");
                userAnswersText.append("A: ").append(answers.get(i)).append("\n");
            }

            JsonNode suggestionJson = chatGptService.requestCareerSuggestion(userAnswersText.toString());
            List<String> traits = new ArrayList<>();
            if (suggestionJson.has("traits")) {
                for (JsonNode traitNode : suggestionJson.get("traits")) {
                    traits.add(traitNode.asText());
                }
            }

            QuizPathwaySuggestion suggestion = new QuizPathwaySuggestion("AI-Generated Pathway", "Based on your answers, here are some insights about you.", answers, String.valueOf(userId), traits);
            QuizResultService.setSuggestion(suggestion);
            return suggestion;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            // Handle exception
            return null;
        }
    }
}
