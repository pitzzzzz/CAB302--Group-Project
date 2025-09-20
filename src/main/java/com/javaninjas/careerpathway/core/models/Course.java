package com.javaninjas.careerpathway.core.models;

public class Course {
    private final int courseID;
    private String courseMajor;
    private String description;
    private String courseCode;
    private String qtacCode;
    private String courseName;

    public Course(int courseID, String courseMajor, String description,
                  String courseCode, String qtacCode, String courseName) {
        this.courseID = courseID;
        this.courseMajor = courseMajor;
        this.description = description;
        this.courseCode = courseCode;
        this.qtacCode = qtacCode;
        this.courseName = courseName;
    }

    // Getters
    public int getCourseID() {
        return courseID;
    }

    public String getCourseMajor() {
        return courseMajor;
    }

    public String getDescription() {
        return description;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getQtacCode() {
        return qtacCode;
    }

    public String getCourseName() {
        return courseName;
    }

    // Setters
    public void setCourseMajor(String courseMajor) {
        this.courseMajor = courseMajor;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setQtacCode(String qtacCode) {
        this.qtacCode = qtacCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return courseName;
    }
}
