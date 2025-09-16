package com.javaninjas.careerpathway.Db;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class controller {

    @Test
    public void testDatabaseConnection() {
        try (Connection conn = com.javaninjas.careerpathway.app.Database.getConnection()) {
            assertNotNull(conn, "Connection should not be null");
            assertFalse(conn.isClosed(), "Connection should be open");
        } catch (SQLException e) {
            fail("Failed to connect to SQLite database: " + e.getMessage());
        }
    }
}
