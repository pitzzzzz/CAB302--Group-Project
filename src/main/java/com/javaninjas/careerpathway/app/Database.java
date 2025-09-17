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

public final class Database {
    private static final String DB_NAME = "Java Ninjas Database.db";
    private static final File dbFile =
            new File(System.getProperty("user.dir"), DB_NAME); // project root

    private static final String RESOURCE_PATH = "/" + DB_NAME;

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

    private static void ensureDatabaseFile() throws IOException {
        if (dbFile.exists()) return;

        System.out.println("[Database] Using DB path: " + dbFile.getAbsolutePath());

        try (InputStream in = Database.class.getResourceAsStream(RESOURCE_PATH)) {
            if (in == null) {
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
