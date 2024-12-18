/**handles database operation */
package library.utils.databaseOperations;

import java.sql.*;

public interface DatabaseOperationInterface {

     public default  Connection getConnection()  {
        //returns database connection
        try {
         return DatabaseConnection.getConnection();
        } catch(Exception ex) {
         return null;
        }
        
     }


     //handles database operation
     public boolean saveToDatabase();

     //update entry from the database;
     public boolean deleteFromDatabase() ;
    
}