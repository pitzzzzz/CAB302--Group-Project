package com.javaninjas.careerpathway.core.integrations.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Objects;

/**
 * High-level service that builds prompts and interprets OpenAI responses.
 * Responsibility: compose domain-specific requests (career suggestions) and
 * parse the returned JSON.
 */
public class ChatGptService {
    private final ChatGptClient client;
    private final ObjectMapper mapper;

    public ChatGptService(ChatGptClient client) {
        this.client = Objects.requireNonNull(client);
        this.mapper = new ObjectMapper();
    }

    /**
     * Request career suggestions from the model based on user answers text.
     * Returns the parsed JSON node that the model is asked to return.
     */
    public JsonNode requestCareerSuggestion(String userAnswersText) throws IOException, InterruptedException {
        Objects.requireNonNull(userAnswersText, "userAnswersText must not be null");

        String prompt = buildCareerPrompt(userAnswersText);

        ObjectNode root = mapper.createObjectNode();
        root.put("model", "gpt-4o-mini");
        root.set("messages", mapper.createArrayNode()
                .add(mapper.createObjectNode()
                        .put("role", "user")
                        .put("content", prompt)));

        String requestBody = mapper.writeValueAsString(root);

        // Use the lower-level client to send the HTTP request
        JsonNode response = client.sendChatCompletionRequest(requestBody);

        // Extract the model text from choices[0].message.content
        JsonNode choices = response.path("choices");
        if (!choices.isArray() || choices.size() == 0) {
            throw new IOException("No choices in OpenAI response");
        }
        String content = choices.get(0).path("message").path("content").asText();

        // The prompt instructs the model to return JSON only; parse that content.
        return mapper.readTree(content);
    }

    private String buildCareerPrompt(String userAnswersText) {
        return "You are an expert career advisor. The user answered the following quiz answers:\n" +
                userAnswersText +
                "\n\nProvide a JSON object with the following shape:\n{" +
                "\"traits\": [\"trait1\", \"trait2\", ...],\n" +
                "\"degreeRecommendations\": [{\"degree\": \"Degree Name\", \"reason\": \"Why it fits\", \"relatedTraits\": [\"traitX\"]}, ...]\n}\n"
                +
                "Return only the JSON object with no additional commentary.";
    }
}
