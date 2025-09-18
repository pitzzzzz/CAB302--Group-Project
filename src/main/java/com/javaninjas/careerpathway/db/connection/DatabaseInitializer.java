package com.javaninjas.careerpathway.db.connection;

import java.sql.Connection;

public final class DatabaseInitializer {
    private DatabaseInitializer() {}

    public static void initialize() {
        try (Connection conn = Database.getConnection()) {
            MigrationService.runMigrations(conn);
            System.out.println("Database connection successful!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
