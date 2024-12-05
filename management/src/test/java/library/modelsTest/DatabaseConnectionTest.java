


package library.modelsTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;

import java.sql.Connection;
import java.sql.SQLException;
import library.utils.databaseOperations.DatabaseConnection;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    private static Connection connection;

    // This method will be run before any tests are executed
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        // Establish the connection before the tests run
        connection = DatabaseConnection.getConnection();
    }

    // This method will be run after all tests are executed
    @AfterAll
    static void tearDownAfterClass() throws Exception {
        if (connection != null) {
            connection.close();  // Close the connection after tests
        }
    }

    @Test
    void testInsert() {
        // Test to ensure that the connection is not null (i.e., successfully connected)
        assertNotNull(connection, "The connection should be established successfully.");
    }

    @Test
    void testConnectionIsValid() throws SQLException {
        // Test to ensure that the connection is valid
        assertTrue(connection.isValid(2), "The connection should be valid.");
    }

   
}
