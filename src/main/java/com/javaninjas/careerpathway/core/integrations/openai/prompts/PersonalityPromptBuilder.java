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
        "  ],\n" +
        "  \"recommendedDegree\": \"Bachelor of Data Science\"\n" +
        "}\n\n" +
    "Return ONLY raw JSON with no markdown, no backticks, no explanation. The field 'recommendedDegree' should be a single string with the most suitable degree for the user based on their traits.";
    }
}
