package com.javaninjas.careerpathway.db;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DbVerificationController {

    @Test
    public void testDatabaseConnection() {
        try (Connection conn = com.javaninjas.careerpathway.db.connection.Database.getConnection()) {
            assertNotNull(conn, "Connection should not be null");
            assertFalse(conn.isClosed(), "Connection should be open");
        } catch (SQLException e) {
            fail("Failed to connect to SQLite database: " + e.getMessage());
        }
    }
}
