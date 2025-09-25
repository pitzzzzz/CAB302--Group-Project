package com.javaninjas.careerpathway.db.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class MigrationService {
    private MigrationService() {}

    public static void runMigrations(Connection conn) throws SQLException {
        createUsersTable(conn);
        createQuizResultsTable(conn);
        createFavoritesTable(conn);
        // add other table creations here
    }

    private static void createUsersTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "first_name TEXT NOT NULL," +
                "last_name TEXT NOT NULL," +
                "email TEXT NOT NULL UNIQUE," +
                "password_hash TEXT," +
                "city TEXT," +
                "age_group TEXT," +
                "profile_stage TEXT," +
                "anonymous INTEGER DEFAULT 0," +
                "recommendedCourse TEXT," +
                "suggested_career TEXT" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }


    private static void createQuizResultsTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS quiz_results (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "question TEXT NOT NULL," +
                "answer TEXT NOT NULL," +
                "FOREIGN KEY(user_id) REFERENCES users(id)" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    private static void createFavoritesTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS user_favorites (" +
                "user_id INTEGER NOT NULL," +
                "job_id INTEGER NOT NULL," +
                "PRIMARY KEY(user_id, job_id)," +
                "FOREIGN KEY(user_id) REFERENCES users(id)," +
                "FOREIGN KEY(job_id) REFERENCES Jobs(jobID)" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
}
