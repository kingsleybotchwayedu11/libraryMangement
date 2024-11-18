package library.model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import library.utils.databaseOperations.DatabaseConnection;

/**
 * Defines the template for a librarian, extending from Patron.
 */
public class Librarian extends LibraryUser {
    private String userName; // Username used to log in for staff members
    protected String password;

    //Contructor 
    public Librarian(String id, String name, String address, String email, String phoneNumber, String userName, String password) {
        // Call the parent constructor to initialize the inherited fields
        super(id, name, "Librarian", address, email, phoneNumber); 
        this.userName = userName; // Initialize the specific Librarian field
        this.password = password; // Initialize the specific Librarian field
    }
    // Getter and Setter for userName
    public String getUserName() {
        return userName;
    }
    // Getter and Setter for userName
    public String getPassword() {
        return this.password;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean saveToDatabase() {
        try {
            // Check if the librarian already exists
            boolean librarianExists = this.checkIfUserExist();
            PreparedStatement stmt = null;
    
            if (librarianExists) {
                // Update existing librarian
                String updateSQL = "UPDATE Librarian SET name = ?, password = ?, address = ?, email = ?, phoneNumber = ?, userName = ? WHERE id = ?";
                stmt = this.getConnection().prepareStatement(updateSQL);
                stmt.setString(1, this.name);           // name
                stmt.setString(2, this.password);       // password (assumed to be hashed)
                stmt.setString(3, this.address);        // address
                stmt.setString(4, this.email);          // email
                stmt.setString(5, this.phoneNumber);    // phone number
                stmt.setString(6, this.userName);       // userName
                stmt.setString(7, this.id);             // id
            } else {
                // Insert new librarian
                String insertSQL = "INSERT INTO Librarian (id, name, password, address, email, phoneNumber, userName) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                stmt = this.getConnection().prepareStatement(insertSQL);
                stmt.setString(1, this.id);             // id
                stmt.setString(2, this.name);           // name
                stmt.setString(3, this.password);       // password (assumed to be hashed)
                stmt.setString(4, this.address);        // address
                stmt.setString(5, this.email);          // email
                stmt.setString(6, this.phoneNumber);    // phone number
                stmt.setString(7, this.userName);       // userName
            }
    
            // Execute the statement
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (Exception err) {
            err.printStackTrace();
        }
        return false;
    }
    static Librarian formLibrarianObject(ResultSet res)  throws SQLException{
        return new Librarian(res.getString("id"), res.getString("name"), res.getString("address"), res.getString("email"), 
        res.getString("phoneNumber"),res.getString("userName"), res.getString("password") );
    }
    
    public static Librarian findOne(String attribute, String value) {
        //get connectoin
        List<String> acceptedAttributes = Arrays.asList("id", "name", "email", "userName");
        if(!acceptedAttributes.contains(attribute))
            {
                System.out.println("Wrong attribute");
                return null;
            }
        try {
           final String selectQuery = "SELECT * FROM Librarian WHERE " + attribute + " LIKE ?";
           PreparedStatement dbEntryQuery = DatabaseConnection.getConnection().prepareStatement(selectQuery);
           dbEntryQuery.setString(1, value);
           ResultSet libarianEntry = dbEntryQuery.executeQuery();
           //check if there are rows
           if (libarianEntry.next()) {
                return formLibrarianObject(libarianEntry);
           }
        } catch(SQLException ex) {
        ex.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


}
