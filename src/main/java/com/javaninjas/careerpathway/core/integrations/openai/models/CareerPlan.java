package com.javaninjas.careerpathway.core.integrations.openai.models;

import java.util.List;

public class CareerPlan {
    private String career;
    private List<WeekPlan> plan;

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public List<WeekPlan> getPlan() {
        return plan;
    }

    public void setPlan(List<WeekPlan> plan) {
        this.plan = plan;
    }

    public static class WeekPlan {
        private int week;
        private List<String> tasks;

        public int getWeek() {
            return week;
        }

        public void setWeek(int week) {
            this.week = week;
        }

        public List<String> getTasks() {
            return tasks;
        }

        public void setTasks(List<String> tasks) {
            this.tasks = tasks;
        }
    }
}
