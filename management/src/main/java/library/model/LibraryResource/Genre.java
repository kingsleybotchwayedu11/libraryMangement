
package library.model.LibraryResource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import library.utils.databaseOperations.DatabaseConnection;
import library.utils.databaseOperations.DatabaseOperationInterface;

public class Genre  implements DatabaseOperationInterface{

    String name;

    public Genre(String name) {
        this.name  = name;
    }
    public String getName() {
        return this.name;
    }

    @Override 
    public boolean saveToDatabase()  {
        try {
        //check if 
        String insertSQL = "INSERT INTO Genre (name) VALUES (?)";
        PreparedStatement genreDatabase = this.getConnection().prepareStatement(insertSQL);
        genreDatabase.setString(1, this.name);
        int result = genreDatabase.executeUpdate();
        return true;
        }catch(SQLException ex) {
            return false;
        }
       
    }
    public Genre() {};

    @Override 
    public boolean deleteFromDatabase() throws SQLException {
            String deletQuery = "DELETE FROM Genre WHERE name = ?";
            PreparedStatement deletPreparedStatement = this.getConnection().prepareStatement(deletQuery);
            deletPreparedStatement.setString(1, this.name);
            deletPreparedStatement.executeUpdate();
            return true;

    }

    
    public static Genre getGenre(String id) throws SQLException {
            //get connectoin
               final String selectQuery = "SELECT * from Genre where name = ?";
               PreparedStatement dbEntryQuery = DatabaseConnection.getConnection().prepareStatement(selectQuery);
               dbEntryQuery.setString(1, id);
               ResultSet genreEntry = dbEntryQuery.executeQuery();
               //check if there are rows
               if(genreEntry.next()) 
                return new Genre(genreEntry.getString("name"));
               return null; 
        
    }

}