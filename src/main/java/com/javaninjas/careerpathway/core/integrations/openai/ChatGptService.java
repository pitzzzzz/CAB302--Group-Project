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

        // Some models may wrap JSON in markdown code fences. Strip them if present.
        String sanitized = stripMarkdownCodeFences(content);

        try {
            return mapper.readTree(sanitized);
        } catch (IOException primary) {
            // Heuristic fallback: attempt to extract the first JSON object substring
            int firstBrace = sanitized.indexOf('{');
            int lastBrace = sanitized.lastIndexOf('}');
            if (firstBrace >= 0 && lastBrace > firstBrace) {
                String candidate = sanitized.substring(firstBrace, lastBrace + 1);
                try {
                    return mapper.readTree(candidate);
                } catch (IOException ignored) {
                    // fall through to rethrow original
                }
            }
            throw new IOException("Failed to parse OpenAI JSON content: " + truncateForLog(sanitized), primary);
        }
    }

    /**
     * Removes surrounding ```json / ``` fences (or ``` with any language) if they exist.
     */
    private String stripMarkdownCodeFences(String text) {
        if (text == null) return null;
        String trimmed = text.trim();
        if (trimmed.startsWith("```")) {
            // Remove initial fence line
            int firstNewline = trimmed.indexOf('\n');
            if (firstNewline > 0) {
                trimmed = trimmed.substring(firstNewline + 1); // after language hint
            } else {
                // Single-line fenced content like ```json { ... } ```
                trimmed = trimmed.substring(3);
            }
            // Remove closing fence if present
            int closingFence = trimmed.lastIndexOf("```\n");
            if (closingFence == -1) {
                closingFence = trimmed.lastIndexOf("```");
            }
            if (closingFence >= 0) {
                trimmed = trimmed.substring(0, closingFence);
            }
        }
        return trimmed.trim();
    }

    private String truncateForLog(String text) {
        if (text == null) return "null";
        int max = 300;
        if (text.length() <= max) return text;
        return text.substring(0, max) + "...";
    }
}