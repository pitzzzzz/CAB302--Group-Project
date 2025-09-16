package com.javaninjas.careerpathway.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Small helper to provide a Connection to the bundled SQLite DB.
 * It uses the file at:
 *   C:\QUT\CAB302\CAB302---Project\Java Ninjas Database.db
 */
public final class Database {
    private static final String RESOURCE_PATH = "/Java Ninjas Database.db";
    private static final File dbFile =
            new File("C:/QUT/CAB302/CAB302---Project/Java Ninjas Database.db");

    private Database() { }

    public static synchronized Connection getConnection() throws SQLException {
        try {
            ensureDatabaseFile();
        } catch (IOException e) {
            throw new SQLException("Failed to prepare database file", e);
        }
        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        Connection conn = DriverManager.getConnection(url);
        ensureUsersTable(conn);
        return conn;
    }

    /**
     * Ensure the DB file exists. If not, try to copy from resources.
     */
    private static void ensureDatabaseFile() throws IOException {
        if (dbFile.exists()) return;

        // Debug: print where we’re placing/reading the DB from
        System.out.println("[Database] Using DB path: " + dbFile.getAbsolutePath());

        // If DB doesn’t exist, try copying the bundled resource
        try (InputStream in = Database.class.getResourceAsStream(RESOURCE_PATH)) {
            if (in == null) {
                // No resource bundled; create an empty DB file so SQLite can initialize it
                Files.createFile(dbFile.toPath());
                return;
            }
            try (FileOutputStream out = new FileOutputStream(dbFile)) {
                byte[] buf = new byte[8192];
                int r;
                while ((r = in.read(buf)) != -1) out.write(buf, 0, r);
            }
        }
    }

    private static void ensureUsersTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "first_name TEXT NOT NULL,"
                + "last_name TEXT NOT NULL,"
                + "email TEXT NOT NULL UNIQUE,"
                + "password_hash TEXT,"
                + "city TEXT,"
                + "age_group TEXT,"
                + "profile_stage TEXT,"
                + "anonymous INTEGER DEFAULT 0"
                + ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
}
