package com.javaninjas.careerpathway.pages.dashboard.components;

import java.util.Collections;
import java.util.List;

public class PathwayFilterState {
    private final List<String> tags;
    private final String salary;
    private final String satisfaction;

    public PathwayFilterState(List<String> tags, String salary, String satisfaction) {
        this.tags = tags == null ? Collections.emptyList() : Collections.unmodifiableList(tags);
        this.salary = salary;
        this.satisfaction = satisfaction;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getSalary() {
        return salary;
    }

    public String getSatisfaction() {
        return satisfaction;
    }

    @Override
    public String toString() {
        return "PathwayFilterState{" +
                "tags=" + tags +
                ", salary='" + salary + '\'' +
                ", satisfaction='" + satisfaction + '\'' +
                '}';
    }
}
