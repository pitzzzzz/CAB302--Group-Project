package com.javaninjas.careerpathway.core.integrations.openai.prompts;

public class ChatbotPromptBuilder {
    public static String build(String careerName, String question) {
        return "You are a career guidance assistant.\n" +
                "The user is pursuing a career in " + careerName + ".\n" +
                "Answer their question helpfully, concisely, and practically.\n\n" +
                "Question: " + question;
    }
}
