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
 * It extracts the resource `Java Ninjas Database.db` to a temporary file and opens it.
 */
public final class Database {
    private static final String RESOURCE_PATH = "/com/javaninjas/careerpathway/Java Ninjas Database.db";
    private static File dbFile;

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
     * Ensure the bundled DB is available in a persistent, writable location.
     * On Windows this uses %APPDATA%/JavaNinjasCareerPathway, otherwise uses
     * ${user.home}/.careerpathway. If the file already exists there we use it
     * (so changes persist across runs). If not, copy the bundled resource once.
     */
    private static void ensureDatabaseFile() throws IOException {
        if (dbFile != null && dbFile.exists()) return;

        String appData = System.getenv("APPDATA");
        File dir;
        if (appData != null && !appData.isBlank()) {
            dir = new File(appData, "JavaNinjasCareerPathway");
        } else {
            dir = new File(System.getProperty("user.home"), ".careerpathway");
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }

        dbFile = new File(dir, "Java Ninjas Database.db");

        // If an external DB already exists, keep using it (persisted data remains).
        if (dbFile.exists()) return;

    // Debug: print where we're placing/reading the DB from
    System.out.println("[Database] using persistent DB path: " + dbFile.getAbsolutePath());

        // Otherwise, attempt to copy the bundled resource to this location.
        try (InputStream in = Database.class.getResourceAsStream(RESOURCE_PATH)) {
            if (in == null) {
                // No resource bundled; create an empty DB file so SQLite can initialize it.
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
