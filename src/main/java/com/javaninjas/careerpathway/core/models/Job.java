package com.javaninjas.careerpathway.core.models;

public class Job {
    private final int jobID;
    private final int courseID;
    private String jobDescription;
    private String jobName;
    private int jobSalary;
    public Job(int jobID, int courseID, String jobName, String jobDescription, int jobSalary) {
        this.jobID = jobID;
        this.courseID = courseID;
        this.jobName = jobName;
        this.jobDescription = jobDescription;
        this.jobSalary = jobSalary;
    }

    // Getters
    public int getJobID() {
        return jobID;
    }

    public int getJobSalary() {
        return jobSalary;
    }

    public int get () {
        return jobID;
    }


    public int getCourseID() {
        return courseID;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getJobName() {
        return jobName;
    }

    // Setters
    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public String toString() {
        return jobName;
    }
}
