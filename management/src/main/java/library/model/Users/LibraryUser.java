package library.model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import library.utils.databaseOperations.DatabaseOperationInterface;

abstract class LibraryUser implements DatabaseOperationInterface {

    protected String name; // the name of the user
    protected String id; // unique id suited for database operation(primary key)
    protected String role; // specifies the roles of the user
    protected String address; // location of the user
    protected String email; // email of the user
    protected String phoneNumber; // phone number of the user

    // Constructor for LibraryUser class
    public LibraryUser(String id, String name, String role, String address, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public LibraryUser() {}
    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and Setter for role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Getter and Setter for address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for phoneNumber
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Method to check if user exists in the database
    protected boolean checkIfUserExist()  {
        String selectQuery = "SELECT 1 FROM " + this.role + " WHERE id = ?";
        PreparedStatement checkIfExistQuery = this.getConnection().prepareStatement(selectQuery);
        checkIfExistQuery.setString(1, this.id); // Use the transactionId field of this instance
        ResultSet resultSet = checkIfExistQuery.executeQuery();
        return resultSet.next(); // Returns true if a result is found, false otherwise
    }

    // Method to delete user from the database
    @Override
    public boolean deleteFromDatabase() {
        try {
            String sql = "DELETE FROM " + this.role + " WHERE id = ?";
            PreparedStatement stmt = this.getConnection().prepareStatement(sql);
            stmt.setString(1, this.id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows >= 1; // Return true if at least one row is affected
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
