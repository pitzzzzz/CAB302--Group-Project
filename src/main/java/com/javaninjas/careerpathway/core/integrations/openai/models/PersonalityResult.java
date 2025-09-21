package com.javaninjas.careerpathway.core.integrations.openai.models;

import java.util.List;

public class PersonalityResult {
    private List<String> coreTraits;
    private List<String> reflections;
    private String suggestedCareer;

    public List<String> getCoreTraits() {
        return coreTraits;
    }

    public void setCoreTraits(List<String> coreTraits) {
        this.coreTraits = coreTraits;
    }

    public List<String> getReflections() {
        return reflections;
    }

    public void setReflections(List<String> reflections) {
        this.reflections = reflections;
    }

    public String getSuggestedCareer() {
        return suggestedCareer;
    }

    public void setSuggestedCareer(String suggestedCareer) {
        this.suggestedCareer = suggestedCareer;
    }
}
