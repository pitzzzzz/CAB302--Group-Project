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
        String sql = "SELECT jobID, courseID, jobName, jobDescription FROM jobs";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int jobID = rs.getInt("jobID");
                int courseID = rs.getInt("courseID");
                String jobName = rs.getString("jobName");
                String jobDescription = rs.getString("jobDescription");
                jobs.add(new Job(jobID, courseID, jobName, jobDescription));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jobs;
    }
}
