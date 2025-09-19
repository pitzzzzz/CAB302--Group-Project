package com.javaninjas.careerpathway.core.integrations.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaninjas.careerpathway.core.integrations.openai.prompts.*;
import com.javaninjas.careerpathway.core.integrations.openai.models.*;

import java.io.IOException;

public class ChatGptService {
    private final ChatGptClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public ChatGptService(ChatGptClient client) {
        this.client = client;
    }

    // 1. Quiz → Traits + Reflections
    public PersonalityResult requestPersonalityInsights(String quizAnswers) throws IOException, InterruptedException {
        String prompt = PersonalityPromptBuilder.build(quizAnswers);
        JsonNode node = sendPrompt(prompt);
        return mapper.treeToValue(node, PersonalityResult.class);
    }

    // 2. Career → Week by Week Plan
    public CareerPlan requestCareerPlan(String careerName) throws IOException, InterruptedException {
        String prompt = CareerPlanPromptBuilder.build(careerName);
        JsonNode node = sendPrompt(prompt);
        return mapper.treeToValue(node, CareerPlan.class);
    }

    // 3. Career Chatbot → Answer
    public ChatResponse requestChatResponse(String careerName, String question) throws IOException, InterruptedException {
        String prompt = ChatbotPromptBuilder.build(careerName, question);
        JsonNode node = sendPrompt(prompt);

        ChatResponse response = new ChatResponse();
        response.setAnswer(node.asText());
        return response;
    }

    // 4. Backwards-compatible method (legacy code support)
    public JsonNode requestCareerSuggestion(String userAnswersText) throws IOException, InterruptedException {
        PersonalityResult result = requestPersonalityInsights(userAnswersText);
        return mapper.valueToTree(result); // convert POJO -> JsonNode
    }

    // Shared request logic
    private JsonNode sendPrompt(String prompt) throws IOException, InterruptedException {
        JsonNode root = mapper.createObjectNode()
                .put("model", "gpt-4o-mini")
                .set("messages", mapper.createArrayNode()
                        .add(mapper.createObjectNode()
                                .put("role", "user")
                                .put("content", prompt)));

        String requestBody = mapper.writeValueAsString(root);
        JsonNode response = client.sendChatCompletionRequest(requestBody);

        JsonNode choices = response.path("choices");
        if (!choices.isArray() || choices.isEmpty()) {
            throw new IOException("No choices in OpenAI response");
        }

        String content = choices.get(0).path("message").path("content").asText();
        return mapper.readTree(content);
    }
}