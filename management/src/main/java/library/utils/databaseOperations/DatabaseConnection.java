
package library.utils.databaseOperations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseConnection {
    // Logger instance

    // Database credentials and URL
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test";  // Update with your DB URL
    private static final String USER = "root";  // Update with your DB username
    private static final String PASSWORD = "9%8%7521Ko^Ko^";  // Update with your DB password
    public  static Connection connection = null;

    // Method to establish and return a database connection
    public static  Connection getConnection() {
      

        try {
            // Step 1: Load and register JDBC driver
           // Class.forName("com.mysql.cj.jdbc.Driver");  // For MySQL JDBC driver
            
            // Step 2: Establish the connection
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

        } catch (SQLException e) {
            // Step 3: Handle SQL exceptions (e.g., wrong credentials, database not available)
           System.err.println();
        } catch (Exception e) {
            // Step 5: Handle any other unexpected exceptions
            System.out.println(e);
        }
        return connection;
    }

    
}
