package com.javaninjas.careerpathway.core.integrations.openai.prompts;

public class PersonalityPromptBuilder {
    public static String build(String quizAnswers) {
        return "You are an expert career advisor.\n" +
                "The user gave the following answers to a career quiz:\n" +
                quizAnswers + "\n\n" +
                "Based on this, return a JSON object shaped like:\n" +
                "{\n" +
                "  \"coreTraits\": [\"meaning-seeking\", \"collaborative\", ...],\n" +
                "  \"reflections\": [\n" +
                "    \"You want to find meaning in your work\",\n" +
                "    \"You enjoy collaborating on big goals\"\n" +
                "  ]\n" +
                "}\n\n" +
                "Return only JSON. No extra commentary.";
    }
}
