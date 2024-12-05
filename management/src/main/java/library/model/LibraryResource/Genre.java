
package library.model.LibraryResource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import library.utils.databaseOperations.DatabaseOperationInterface;

public class Genre  implements DatabaseOperationInterface{

    String name;

    public Genre(String name) {
        this.name  = name;
    }
    public String getName() {
        return this.name;
    }

    boolean checkIfExists() {
        try {
            boolean checkIfSaved = this.checkIfExists();
            if(checkIfSaved) 
                return true;
            
            String query = "SELECT 1 FROM Genre WHERE name = ?";
            PreparedStatement genrePreparedStatement = this.getConnection().prepareStatement(query);
            genrePreparedStatement.setString(1, this.name);
            ResultSet checkIfRows = genrePreparedStatement.executeQuery();
            return checkIfRows.next();
        } catch(SQLException err) {
            err.printStackTrace();;
        } catch(Exception ex) {
            ex.printStackTrace();;
        }
        return false;
    }
    @Override 
    public boolean saveToDatabase()  {
        try {
        //check if 
        String insertSQL = "INSERT INTO Genre (name) VALUES (?)";
        PreparedStatement genreDatabase = this.getConnection().prepareStatement(insertSQL);
        genreDatabase.setString(1, this.name);
        int result = genreDatabase.executeUpdate();
        return result > 0;
        }catch(SQLException ex) {
            ex.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override 
    public boolean deleteFromDatabase() {
        try{
            String deletQuery = "DELETE FROM Genre WHERE name = ?";
            PreparedStatement deletPreparedStatement = this.getConnection().prepareStatement(deletQuery);
            deletPreparedStatement.setString(1, this.name);
            int affectedRows = deletPreparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

}