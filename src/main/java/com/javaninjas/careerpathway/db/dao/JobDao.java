package com.javaninjas.careerpathway.db.dao;

import com.javaninjas.careerpathway.core.models.Job;
import com.javaninjas.careerpathway.db.connection.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JobDao {

    public static List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT jobID, courseID, JobName, JobDescription, ExpectedSalary  FROM Jobs";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int jobID = rs.getInt("jobID");
                int courseID = rs.getInt("courseID");
                String jobName = rs.getString("JobName");
                String jobDescription = rs.getString("JobDescription");
                int expectedSalary = rs.getInt("ExpectedSalary");
                jobs.add(new Job(jobID, courseID, jobName, jobDescription, expectedSalary));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jobs;
    }

    public static Job getJobById(int jobId) {
        String sql = "SELECT jobID, courseID, JobName, JobDescription, ExpectedSalary FROM Jobs WHERE jobID = ?";
        try (java.sql.Connection conn = com.javaninjas.careerpathway.db.connection.Database.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, jobId);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Job(rs.getInt("jobID"), rs.getInt("courseID"), rs.getString("JobName"), rs.getString("JobDescription"), rs.getInt("ExpectedSalary"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
