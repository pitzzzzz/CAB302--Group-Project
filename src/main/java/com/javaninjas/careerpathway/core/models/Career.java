package com.javaninjas.careerpathway.core.models;

import java.util.List;
import java.util.Objects;

public class Career {

    private final String id;                // unique identifier (could be UUID from DB)
    private String name;                    // e.g., "Software Engineer"
    private String description;             // high-level description
    private List<String> requirements;      // e.g., ["Bachelor’s in IT", "Problem solving"]
    private List<String> tags;              // e.g., ["technology", "teamwork", "analytical"]

    // Constructor
    public Career(String id, String name, String description,
                  List<String> requirements, List<String> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.requirements = requirements;
        this.tags = tags;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getRequirements() {
        return requirements;
    }

    public List<String> getTags() {
        return tags;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequirements(List<String> requirements) {
        this.requirements = requirements;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    // Utility Methods
    public boolean hasTag(String tag) {
        return tags != null && tags.contains(tag.toLowerCase());
    }

    @Override
    public String toString() {
        return "Career{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", requirements=" + requirements +
                ", tags=" + tags +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Career)) return false;
        Career career = (Career) o;
        return id.equals(career.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
