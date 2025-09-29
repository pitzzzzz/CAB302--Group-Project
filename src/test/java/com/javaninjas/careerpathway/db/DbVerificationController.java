package com.javaninjas.careerpathway.db;

import org.junit.jupiter.api.Test;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class DbVerificationController {

    private boolean tableExists(String tableName) throws SQLException {
        try (Connection conn = com.javaninjas.careerpathway.db.connection.Database.getConnection();
             ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null)) {
            return rs.next();
        }
    }

    @Test
    public void testDatabaseConnection() {
        try (Connection conn = com.javaninjas.careerpathway.db.connection.Database.getConnection()) {
            assertNotNull(conn, "Connection should not be null");
            assertFalse(conn.isClosed(), "Connection should be open");
        } catch (SQLException e) {
            fail("Failed to connect to SQLite database: " + e.getMessage());
        }
    }

    @Test
    public void testUsersTableExists() {
        try {
            assertTrue(tableExists("users"), "Table 'users' should exist");
        } catch (SQLException e) {
            fail("Error checking table 'users': " + e.getMessage());
        }
    }

    @Test
    public void testCourseTableExists() {
        try {
            assertTrue(tableExists("Course"), "Table 'Course' should exist");
        } catch (SQLException e) {
            fail("Error checking table 'Course': " + e.getMessage());
        }
    }

    @Test
    public void testJobsTableExists() {
        try {
            assertTrue(tableExists("Jobs"), "Table 'Jobs' should exist");
        } catch (SQLException e) {
            fail("Error checking table 'Jobs': " + e.getMessage());
        }
    }

    @Test
    public void testQuestionsTableExists() {
        try {
            assertTrue(tableExists("Questions"), "Table 'Questions' should exist");
        } catch (SQLException e) {
            fail("Error checking table 'Questions': " + e.getMessage());
        }
    }
}
