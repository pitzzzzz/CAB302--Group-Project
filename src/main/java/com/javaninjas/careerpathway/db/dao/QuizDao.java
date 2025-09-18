package com.javaninjas.careerpathway.db.dao;

import com.javaninjas.careerpathway.pages.quiz.models.Question;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDao {
    private final Connection conn;

    public QuizDao(Connection conn) {
        this.conn = conn;
    }

    public void addQuestion(Question question) throws SQLException {
        String sql = "INSERT INTO quiz_questions (question_text, answer_options) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, question.prompt());
            stmt.setString(2, String.join(",", question.options())); // store as CSV
            stmt.executeUpdate();
        }
    }

    public List<Question> getAllQuestions() throws SQLException {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM quiz_questions";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String[] answers = rs.getString("answer_options").split(",");
                questions.add(new Question(
                        rs.getInt("id"),
                        rs.getString("question_text"),
                        List.of(answers)
                ));
            }
        }
        return questions;
    }
}
