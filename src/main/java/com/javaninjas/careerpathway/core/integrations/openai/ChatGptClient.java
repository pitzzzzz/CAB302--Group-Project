package com.javaninjas.careerpathway.core.integrations.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;

public class ChatGptClient {
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private final String apiKey;
    private final HttpClient client;
    private final ObjectMapper mapper;

    public ChatGptClient(String apiKey) {
        this(apiKey, HttpClient.newHttpClient(), new ObjectMapper());
    }

    // Constructor for testing or customizing HttpClient/ObjectMapper
    public ChatGptClient(String apiKey, HttpClient client, ObjectMapper mapper) {
        this.apiKey = Objects.requireNonNull(apiKey, "OPENAI_API_KEY must be provided");
        this.client = Objects.requireNonNull(client);
        this.mapper = Objects.requireNonNull(mapper);
    }

    /**
     * Sends a raw chat completion request body (already a JSON string) to OpenAI
     * and returns the parsed JSON response.
     * This client is intentionally small and only handles HTTP-level concerns.
     */
    public JsonNode sendChatCompletionRequest(String requestBodyJson) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_URL))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() / 100 != 2) {
            throw new IOException("OpenAI API returned " + resp.statusCode() + ": " + resp.body());
        }

        return mapper.readTree(resp.body());
    }

    /**
     * Backwards-compatible helper that composes a career prompt and returns the
     * parsed JSON.
     * Delegates the domain logic to {@link ChatGptService} so callers that still
     * use the
     * old client API will continue to work.
     */
    public JsonNode requestCareerSuggestion(String userAnswersText) throws IOException, InterruptedException {
        // Delegate to service to keep single-responsibility while providing
        // compatibility
        ChatGptService svc = new ChatGptService(this);
        return svc.requestCareerSuggestion(userAnswersText);
    }
}
