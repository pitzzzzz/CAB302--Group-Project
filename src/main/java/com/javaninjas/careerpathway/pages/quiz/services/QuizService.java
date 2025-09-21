package com.javaninjas.careerpathway.pages.quiz.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaninjas.careerpathway.core.auth.UserSession;
import com.javaninjas.careerpathway.core.integrations.openai.ChatGptClient;
import com.javaninjas.careerpathway.core.integrations.openai.ChatGptService;
import com.javaninjas.careerpathway.core.config.OpenAIConfig;
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

    // AI-powered analysis (optional, runs only if API key present)
    List<String> traits = new ArrayList<>();
    List<String> reflections = new ArrayList<>();
    String suggestedCareer = null;
    String description;
        try {
            String apiKey = null;
            try {
                apiKey = OpenAIConfig.getApiKey();
            } catch (IllegalStateException missing) {
                // Config throws if key absent; we'll treat as optional
            }

            if (apiKey != null && !apiKey.isBlank()) {
                ChatGptClient client = new ChatGptClient(apiKey);
                ChatGptService chatGptService = new ChatGptService(client);

                StringBuilder userAnswersText = new StringBuilder();
                for (int i = 0; i < allQuestions.size(); i++) {
                    userAnswersText.append("Q: ").append(allQuestions.get(i).prompt()).append("\n");
                    userAnswersText.append("A: ").append(answers.get(i)).append("\n");
                }

                JsonNode suggestionJson = chatGptService.requestCareerSuggestion(userAnswersText.toString());
                System.out.println("[AI RAW RESPONSE] " + suggestionJson.toPrettyString());

                // Backwards compatibility: older code expected 'traits'. New model returns coreTraits/reflections.
                if (suggestionJson.has("traits")) {
                    for (JsonNode traitNode : suggestionJson.get("traits")) {
                        traits.add(traitNode.asText());
                    }
                } else if (suggestionJson.has("coreTraits")) {
                    for (JsonNode traitNode : suggestionJson.get("coreTraits")) {
                        traits.add(traitNode.asText());
                    }
                }
                if (suggestionJson.has("reflections")) {
                    for (JsonNode reflNode : suggestionJson.get("reflections")) {
                        reflections.add(reflNode.asText());
                    }
                }
                if (suggestionJson.has("suggestedCareer")) {
                    suggestedCareer = suggestionJson.get("suggestedCareer").asText();
                }

                if (traits.isEmpty()) {
                    System.out.println("[AI NOTICE] No traits found in response.");
                } else {
                    System.out.println("[AI EXTRACTED TRAITS] " + traits);
                }
                if (!reflections.isEmpty()) {
                    System.out.println("[AI REFLECTIONS] " + reflections);
                }
                description = traits.isEmpty()
                        ? "Based on your answers. (AI response provided no trait list)"
                        : "Based on your answers, here are some insights about you.";
            } else {
                System.out.println("[AI SKIPPED] No OPENAI_API_KEY provided. Proceeding without AI traits.");
                description = "Based on your answers. (AI disabled)";
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("[AI ERROR] " + e.getMessage());
            description = "Based on your answers. (AI error)";
        }

    QuizPathwaySuggestion suggestion = new QuizPathwaySuggestion(
        "AI-Generated Pathway",
        description,
        answers,
        String.valueOf(userId),
        traits,
        reflections,
        suggestedCareer
    );

    // Persist suggested career for user if available
    if (userId > 0 && suggestedCareer != null && !suggestedCareer.isBlank()) {
        try (Connection conn = Database.getConnection()) {
            com.javaninjas.careerpathway.db.dao.UserDao userDao = new com.javaninjas.careerpathway.db.dao.UserDao(conn);
            userDao.updateSuggestedCareer(userId, suggestedCareer);
        } catch (Exception e) {
            System.out.println("[DB ERROR] Could not persist suggested career: " + e.getMessage());
        }
    }

    QuizResultService.setSuggestion(suggestion);
    return suggestion;
    }
}
