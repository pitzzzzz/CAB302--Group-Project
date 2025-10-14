package com.javaninjas.careerpathway.db.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaninjas.careerpathway.core.integrations.openai.models.CareerPlan;
import com.javaninjas.careerpathway.db.connection.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple DAO to persist CareerPlan objects as JSON in the database so
 * user-specific plans survive application restarts.
 */
public class CareerPlanDao {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final DateTimeFormatter TF = DateTimeFormatter.ISO_DATE_TIME;

    // Ensure table exists
    private static void ensureTable() {
        String sql = "CREATE TABLE IF NOT EXISTS CareerPlans (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER NOT NULL, " +
                "careerName TEXT NOT NULL, " +
                "planJson TEXT NOT NULL, " +
                "createdAt TEXT NOT NULL, " +
                "UNIQUE(userId, careerName)" +
                ")";
        try (Connection conn = Database.getConnection(); Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveOrUpdatePlan(int userId, String careerName, CareerPlan plan) {
        if (userId <= 0 || careerName == null || careerName.isBlank() || plan == null) return;
        ensureTable();
        try (Connection conn = Database.getConnection()) {
            String json = MAPPER.writeValueAsString(plan);
            // Try update first
            String upSql = "UPDATE CareerPlans SET planJson = ?, createdAt = ? WHERE userId = ? AND careerName = ?";
            try (PreparedStatement up = conn.prepareStatement(upSql)) {
                up.setString(1, json);
                up.setString(2, LocalDateTime.now().format(TF));
                up.setInt(3, userId);
                up.setString(4, careerName);
                int affected = up.executeUpdate();
                if (affected > 0) return;
            }

            // Insert if update didn't affect rows
            String inSql = "INSERT INTO CareerPlans(userId, careerName, planJson, createdAt) VALUES (?, ?, ?, ?)";
            try (PreparedStatement in = conn.prepareStatement(inSql)) {
                in.setInt(1, userId);
                in.setString(2, careerName);
                in.setString(3, json);
                in.setString(4, LocalDateTime.now().format(TF));
                in.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CareerPlan getPlan(int userId, String careerName) {
        ensureTable();
        String sql = "SELECT planJson FROM CareerPlans WHERE userId = ? AND careerName = ? LIMIT 1";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, careerName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String json = rs.getString("planJson");
                    return MAPPER.readValue(json, CareerPlan.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LocalDateTime getPlanTimestamp(int userId, String careerName) {
        ensureTable();
        String sql = "SELECT createdAt FROM CareerPlans WHERE userId = ? AND careerName = ? LIMIT 1";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, careerName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String s = rs.getString("createdAt");
                    return LocalDateTime.parse(s, TF);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
