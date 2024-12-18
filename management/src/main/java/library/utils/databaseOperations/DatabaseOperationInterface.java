/**handles database operation */
package library.utils.databaseOperations;

import java.sql.*;

public interface DatabaseOperationInterface {

     public default  Connection getConnection() throws SQLException {
        //returns database connection
        return DatabaseConnection.getConnection();
     }


     //handles database operation
     public boolean saveToDatabase() throws SQLException;

     //update entry from the database;
     public boolean deleteFromDatabase() throws SQLException;
    
}