package com.javaninjas.careerpathway.db.dao;

import com.javaninjas.careerpathway.core.models.User;
import com.javaninjas.careerpathway.pages.results.models.QuizResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final Connection conn;

    public UserDao(Connection conn) {
        this.conn = conn;
    }

    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (first_name, last_name, email, password_hash, city, age_group, profile_stage, anonymous) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPasswordHash());
            stmt.setString(5, user.getCity());
            stmt.setString(6, user.getAgeGroup());
            stmt.setString(7, user.getProfileStage());
            stmt.setInt(8, user.isAnonymous() ? 1 : 0);
            stmt.executeUpdate();
        }
    }

    public void updateRecommendedCourse(int userID, String course) throws SQLException {
        String sql = "UPDATE users SET recommendedCourse = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, course);
            stmt.setInt(2, userID);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Recommended course updated in DB for userID " + userID);
            } else {
                System.out.println("No user found with userID " + userID);
            }
        }
    }


    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("city"),
                        rs.getString("age_group"),
                        rs.getString("profile_stage"),
                        rs.getInt("anonymous") == 1
                );
                user.setQuizResults(getQuizResultsForUser(id));
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("city"),
                        rs.getString("age_group"),
                        rs.getString("profile_stage"),
                        rs.getInt("anonymous") == 1
                );
                user.setQuizResults(getQuizResultsForUser(user.getUserID()));
                users.add(user);
            }
        }
        return users;
    }

    private List<QuizResult> getQuizResultsForUser(int userId) throws SQLException {
        List<QuizResult> quizResults = new ArrayList<>();
        String sql = "SELECT * FROM quiz_results WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                quizResults.add(new QuizResult(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("question"),
                        rs.getString("answer")
                ));
            }
        }
        return quizResults;
    }
}
