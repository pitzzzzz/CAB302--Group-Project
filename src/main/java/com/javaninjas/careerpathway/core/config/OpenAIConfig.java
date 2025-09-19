package com.javaninjas.careerpathway.core.config;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Centralized config loader for API keys and environment settings.
 */
public class OpenAIConfig {
    private static final String KEY;

    static {
        // Try loading from .env
        Dotenv dotenv = Dotenv.configure()
                              .ignoreIfMissing() // safe if no .env present
                              .load();
        String keyFromEnv = dotenv.get("OPENAI_API_KEY");
        KEY = (keyFromEnv != null && !keyFromEnv.isBlank())
                ? keyFromEnv
                : System.getenv("OPENAI_API_KEY");

        if (KEY == null || KEY.isBlank()) {
            throw new IllegalStateException("Missing OpenAI API key. " +
                    "Set OPENAI_API_KEY in .env or system environment variables.");
        }
    }

    public static String getApiKey() {
        return KEY;
    }
}
