package com.javaninjas.careerpathway.db.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/db/JavaNinjasDatabase.db";
    private static Connection conn;

    private Database() {
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(DB_URL);
        }
        return conn;
    }
}
