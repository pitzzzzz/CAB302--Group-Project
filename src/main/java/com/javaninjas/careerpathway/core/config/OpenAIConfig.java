package com.javaninjas.careerpathway.core.config;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Centralized config loader for API keys and environment settings.
 * Only loads values from the .env file, not from system environment variables.
 */
public class OpenAIConfig {
    private static final String KEY;

    static {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        KEY = dotenv.get("OPENAI_API_KEY"); // only from .env

        System.out.println("Loaded OpenAI key: " + (KEY != null && !KEY.isBlank() ? "[REDACTED]" : "MISSING"));

        if (KEY == null || KEY.isBlank()) {
            throw new IllegalStateException("Missing OpenAI API key in .env file (OPENAI_API_KEY).");
        }
    }

    public static String getApiKey() {
        return KEY;
    }
}
