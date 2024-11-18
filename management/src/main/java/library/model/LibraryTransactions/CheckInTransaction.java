package library.model.LibraryTransactions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;


public class CheckInTransaction extends LibraryTransaction {
    // The borrowed item (book, magazine, or other resources)
    private String borrowedTransactionId;
    
    // The day the book wast returned
    private LocalDateTime checkInDate;
    
    //The patron who accepted the book
    private String acceptedBy;
    
    public CheckInTransaction(String id, String acceptedBy, String borrowedTranactionId, LocalDateTime checkInDate) {
            super(id, "CheckInTransaction");
            this.checkInDate =  checkInDate;
            this.borrowedTransactionId = borrowedTranactionId;
            this.acceptedBy = acceptedBy;
    }
    @Override 
    public boolean saveToDatabase() {
        PreparedStatement stmt;
        try {
            // Check if the check-in transaction already exists
            boolean transactionExists = this.checkIfTransactionExists();
            
            if (transactionExists) {
                // Update existing check-in transaction
                String updateSQL = "UPDATE CheckInTransaction SET borrowedTransactionId = ?, checkInDate = ?, acceptedBy = ? WHERE id = ?";
                stmt = this.getConnection().prepareStatement(updateSQL);
                stmt.setString(1, this.borrowedTransactionId);
                stmt.setTimestamp(2, Timestamp.valueOf(this.checkInDate));
                stmt.setString(3, this.acceptedBy);
                stmt.setString(4, this.id);
            } else {
                // Insert new check-in transaction
                String insertSQL = "INSERT INTO CheckInTransaction (id, borrowedTransactionId, checkInDate, acceptedBy) " +
                        "VALUES (?, ?, ?, ?)";
                stmt = this.getConnection().prepareStatement(insertSQL);
                stmt.setString(1, this.id);
                stmt.setString(2, this.borrowedTransactionId);
                stmt.setTimestamp(3, Timestamp.valueOf(this.checkInDate));
                stmt.setString(4, this.acceptedBy);
            }
            // Execute the statement
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    
    
   
}
