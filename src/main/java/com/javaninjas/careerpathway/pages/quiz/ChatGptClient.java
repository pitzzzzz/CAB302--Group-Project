package com.javaninjas.careerpathway.pages.quiz;

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
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public ChatGptClient(String apiKey) {
        this.apiKey = Objects.requireNonNull(apiKey, "OPENAI_API_KEY must be provided");
    }

    public JsonNode requestCareerSuggestion(String userAnswersText) throws IOException, InterruptedException {
        // Build a simple prompt that asks for traits and top 5 degrees mapping
        String prompt = "You are an expert career advisor. The user answered the following quiz answers:\n" +
                userAnswersText +
                "\n\nProvide a JSON object with the following shape:\n{" +
                "\"traits\": [\"trait1\", \"trait2\", ...],\n" +
                "\"degreeRecommendations\": [{\"degree\": \"Degree Name\", \"reason\": \"Why it fits\", \"relatedTraits\": [\"traitX\"]}, ...]\n}\n" +
                "Return only the JSON object with no additional commentary.";

        // Build request body
        String body = mapper.writeValueAsString(
                mapper.createObjectNode()
                        .put("model", "gpt-4o-mini")
                        .set("messages", mapper.createArrayNode()
                                .add(mapper.createObjectNode()
                                        .put("role", "user")
                                        .put("content", prompt)
                                )
                        )
        );

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_URL))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() / 100 != 2) {
            throw new IOException("OpenAI API returned " + resp.statusCode() + ": " + resp.body());
        }

        JsonNode root = mapper.readTree(resp.body());
        // Extract the model output text (choices[0].message.content)
        JsonNode choices = root.path("choices");
        if (!choices.isArray() || choices.size() == 0) {
            throw new IOException("No choices in OpenAI response");
        }
        String content = choices.get(0).path("message").path("content").asText();
        // Parse that content as JSON (user asked model to return raw JSON)
        return mapper.readTree(content);
    }
}
