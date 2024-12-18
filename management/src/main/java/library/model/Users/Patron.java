package library.model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import library.utils.databaseOperations.DatabaseConnection;

/**
 * Defines the template for a library member, extending from Patron.
 */
public class Patron extends LibraryUser {
    private String libraryCardId; // Unique identifier for the library member (library card)
    
    public Patron(String id, String name,  String address, String email, String phoneNumber, String libraryCardId) {
        super(id, name, "Patron", address, email, phoneNumber);
        this.libraryCardId = libraryCardId;
    }
    // Getter and Setter for libraryCardId
    public String getLibraryCardId() {
        return libraryCardId;
    }

    //Book
    private static Patron formPatronObject(ResultSet res){
        return new Patron(res.getString("id"), res.getString("name"), res.getString("address"), res.getString("email"),
                          res.getString("phoneNumber"), res.getString("libraryCardId")
                         );
    }
    public void setLibraryCardId(String libraryCardId) {
        this.libraryCardId = libraryCardId;
    }

    public static List<Patron> findByAttribute(String attribute, String value) {
        //get connectoin
        List<Patron> patrons = new ArrayList<>();
        List<String> acceptedAttributes = Arrays.asList("id", "name", "email", "address", "libraryCardId", "phoneNumber");
        if(!acceptedAttributes.contains(attribute))
            {
                System.out.println("Wrong attribute");
                return null;
            }
      
           final String selectQuery = "SELECT * FROM Patron WHERE " + attribute + " LIKE ?";
           PreparedStatement dbEntryQuery = DatabaseConnection.getConnection().prepareStatement(selectQuery);
           dbEntryQuery.setString(1, "%" + value + "%");
           ResultSet patronEntries = dbEntryQuery.executeQuery();
           //check if there are rows
           while (patronEntries.next()) {
                patrons.add(formPatronObject(patronEntries));
           }
        
        return patrons;
    }

   public static Patron findOne(String attribute, String value)  {
        //get connectoin
        List<String> acceptedAttributes = Arrays.asList("id", "name", "email", "libraryCardId");
        if(!acceptedAttributes.contains(attribute))
            {
                System.out.println("Wrong attribute");
                return null;
            }
        
           final String selectQuery = "SELECT * FROM Patron WHERE " + attribute + " LIKE ?";
           PreparedStatement dbEntryQuery = DatabaseConnection.getConnection().prepareStatement(selectQuery);
           dbEntryQuery.setString(1, value);
           ResultSet patronEntries = dbEntryQuery.executeQuery();
           //check if there are rows
           if (patronEntries.next()) 
                return formPatronObject(patronEntries);
          
        return null;
    }
 

    @Override
    public boolean saveToDatabase()  {
            // Check if the Patron already exists
            boolean patronExists = this.checkIfUserExist();
            PreparedStatement stmt = null;
            if (patronExists) {
                // Update existing patron
                String updateSQL = "UPDATE Patron SET name = ?, address = ?, email = ?, phoneNumber = ?, libraryCardId = ? WHERE id = ?";
                stmt = this.getConnection().prepareStatement(updateSQL);
                stmt.setString(1, this.name);
                stmt.setString(2, this.address);
                stmt.setString(3, this.email);
                stmt.setString(4, this.phoneNumber);
                stmt.setString(5, this.libraryCardId);
                stmt.setString(6, this.id);
            } else {
                // Insert new patron
                String insertSQL = "INSERT INTO Patron (id, name, address, email, phoneNumber, libraryCardId) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
                stmt = this.getConnection().prepareStatement(insertSQL);
                stmt.setString(1, this.id);
                stmt.setString(2, this.name);
                stmt.setString(3, this.address);
                stmt.setString(4, this.email);
                stmt.setString(5, this.phoneNumber);
                stmt.setString(6, this.libraryCardId);
            }
            // Execute the statement
            stmt.executeUpdate();
            return true;
    }


}
