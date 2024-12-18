/**
 * Abstract class template to handle library transactions
 */

 package library.model.LibraryTransactions;

import library.utils.databaseOperations.DatabaseOperationInterface;
import java.sql.*;

abstract class LibraryTransaction implements DatabaseOperationInterface {
 protected String id; // the id that uniquly identify transactions
 protected String type; //the type of transaction, weather borrowed or checkin

 LibraryTransaction(String id, String type){
    this.id = id; // id of the transaction
    this.type = type; // type of the transaction
 }
 // Getters and Setters for id
public String getId() {
    return id;
}

public String getType() {
    return type;
}

protected boolean checkIfTransactionExists() throws SQLException {
    String selectQuery ="SELECT 1 FROM " + this.type + " WHERE id = ?";
    PreparedStatement checkIfExistQuery = this.getConnection().prepareStatement(selectQuery);
    checkIfExistQuery.setString(1, this.id); // Use the transactionId field of this instance
    ResultSet resultSet = checkIfExistQuery.executeQuery();
    return resultSet.next(); // Returns true if a result is found, false otherwise
 }


@Override 
public boolean deleteFromDatabase() throws SQLException {

        String sql = "DELETE FROM " + this.type + " WHERE id = ?";
        PreparedStatement stmt = this.getConnection().prepareStatement(sql);
        stmt.setString(1, this.id);
        stmt.executeUpdate();
        return  true;

    }

}