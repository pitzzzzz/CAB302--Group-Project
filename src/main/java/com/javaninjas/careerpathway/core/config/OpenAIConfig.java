package com.javaninjas.careerpathway.core.config;
/**
 * Centralized config loader for API keys and environment settings.
 */
public class OpenAIConfig {
    private static String KEY="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";

    static {
        System.out.println("Loaded OpenAI key: " + KEY);
        if (KEY == null || KEY.isBlank()) {
            throw new IllegalStateException("Missing OpenAI API key. " +
                    "Set OPENAI_API_KEY in .env or system environment variables.");
        }
    }

    public static String getApiKey() {
        return KEY;
    }
}
