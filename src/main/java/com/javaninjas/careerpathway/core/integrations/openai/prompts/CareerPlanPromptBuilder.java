package com.javaninjas.careerpathway.core.integrations.openai.prompts;

public class CareerPlanPromptBuilder {
    public static String build(String careerName) {
        return "The user selected the career: " + careerName + ".\n" +
                "Generate a structured 12-week plan to help them reach this goal.\n" +
                "Each week should have 2-3 actionable tasks.\n\n" +
                "Return JSON shaped like:\n" +
                "{\n" +
                "  \"career\": \"" + careerName + "\",\n" +
                "  \"plan\": [\n" +
                "    {\"week\": 1, \"tasks\": [\"Task A\", \"Task B\"]},\n" +
                "    {\"week\": 2, \"tasks\": [\"Task C\", \"Task D\"]}\n" +
                "  ]\n" +
                "}\n\n" +
                "Return only JSON. No explanations.";
    }
}
