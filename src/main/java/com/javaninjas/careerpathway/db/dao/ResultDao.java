package com.javaninjas.careerpathway.db.dao;

import com.javaninjas.careerpathway.pages.quizResults.models.QuizResult;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultDao {
    private final Connection conn;

    public ResultDao(Connection conn) {
        this.conn = conn;
    }

    public void addResult(QuizResult result) throws SQLException {
        String sql = "INSERT INTO quiz_results (user_id, question, answer) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, result.getUserId());
            stmt.setString(2, result.getQuestion());
            stmt.setString(3, result.getAnswer());
            stmt.executeUpdate();
        }
    }

    public List<QuizResult> getResultsForUser(int userId) throws SQLException {
        List<QuizResult> results = new ArrayList<>();
        String sql = "SELECT * FROM quiz_results WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(new QuizResult(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("question"),
                        rs.getString("answer")
                ));
            }
        }
        return results;
    }
}
