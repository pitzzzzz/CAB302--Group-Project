package com.javaninjas.careerpathway.db.dao;

import com.javaninjas.careerpathway.core.models.Course;
import com.javaninjas.careerpathway.db.connection.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {

    // Fetch all courses
    public static List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT CourseID, CourseMajor, Description, CourseCode, QTACcode, Course FROM Course";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int courseID = rs.getInt("CourseID");
                String courseMajor = rs.getString("CourseMajor");
                String description = rs.getString("Description");
                String courseCode = rs.getString("CourseCode");
                String qtacCode = rs.getString("QTACcode");
                String courseName = rs.getString("Course");

                courses.add(new Course(courseID, courseMajor, description, courseCode, qtacCode, courseName));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return courses;
    }

    // Fetch a single course by ID
    public static Course getCourseById(int id) {
        String sql = "SELECT CourseID, CourseMajor, Description, CourseCode, QTACcode, Course FROM Course WHERE CourseID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int courseID = rs.getInt("CourseID");
                    String courseMajor = rs.getString("CourseMajor");
                    String description = rs.getString("Description");
                    String courseCode = rs.getString("CourseCode");
                    String qtacCode = rs.getString("QTACcode");
                    String courseName = rs.getString("Course");

                    return new Course(courseID, courseMajor, description, courseCode, qtacCode, courseName);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns the selection rank for a given course from the CourseEntryRequirements table.
     * Returns null if no entry exists or on error.
     */
    public static Integer getSelectionRankForCourse(int courseId) {
        String sql = "SELECT SelectionRank FROM CourseEntryRequirements WHERE CourseID = ? LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int rank = rs.getInt("SelectionRank");
                    if (rs.wasNull()) return null;
                    return rank;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
