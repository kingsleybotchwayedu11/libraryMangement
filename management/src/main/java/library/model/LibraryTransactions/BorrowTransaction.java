package library.model.LibraryTransactions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


import library.utils.databaseOperations.DatabaseConnection;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BorrowTransaction extends LibraryTransaction {
    // The borrowed item (book, magazine, or other resources)
    private String borrowedItemId;
    
    // The library member who borrowed the item
    private String borrowerId;
    
    // The expected return date for the borrowed item
    private LocalDateTime expectedReturnDate; 
    
    // The actual borrowed date (when the transaction occurred)
    private LocalDateTime borrowedDate; 
    
    // The librarian who issued the item
    private String issuedLibrarianId;
    
    // Tracks whether the transaction has been closed (e.g., item returned)
    private String status; 
    
    public BorrowTransaction(String id,String borrowedItemId, String borrowerId,LocalDateTime borrowedDate, LocalDateTime expectedReturnDate,String issuedLibrarianId, String status) {
        super(id, "BorrowingTransaction");
        this.borrowedItemId = borrowedItemId;
        this.borrowerId = borrowerId;
        this.expectedReturnDate = expectedReturnDate;
        this.borrowedDate = borrowedDate;
        this.issuedLibrarianId = issuedLibrarianId;
        this.status = status;
    }

    // Getters and Setters for borrowedItem
    public String getBorrowedItem() {
        return borrowedItemId;
    }


    //Getters and Setters for borrower
    public String getBorrower() {
        return borrowerId;
    }


    // Getters and Setters for expectedReturnDate
    public LocalDateTime getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDateTime expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    // Getters and Setters for borrowedDate
    public LocalDateTime getBorrowedDate() {
        return borrowedDate;
    }

    

    // Getters and Setters for issuedLibrarian
    public String getIssuedLibrarian() {
        return issuedLibrarianId;
    }

   

    // Getters and Setters for isTransactionClosed
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    private static BorrowTransaction formBorrowTransactionObject(ResultSet rs) throws SQLException {
        return new BorrowTransaction(
            rs.getString("id"),                              // Transaction ID
            rs.getString("borrowedResourceId"),              // Borrowed Item ID
            rs.getString("patronMemberCardId"),              // Borrower ID
            rs.getTimestamp("expectedReturnDate").toLocalDateTime(), // Expected Return Date
            rs.getTimestamp("borrowedDate").toLocalDateTime(),      // Borrowed Date
            rs.getString("issuedLibrarian"),                // Issued Librarian ID
            rs.getString("status")                          // Transaction Status
        );
    }
    

    @Override
    public boolean saveToDatabase() throws SQLException {
        PreparedStatement stmt;
        // Check if the transaction is already saved in the database
        boolean transactionExists = this.checkIfTransactionExists(); 
        System.out.println( "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println(transactionExists);
        if (transactionExists) {
            // Update existing transaction
            String updateSQL = "UPDATE BorrowingTransaction " +
                    "SET borrowedResourceId = ?, patronMemberCardId = ?, issuedLibrarian = ?, " +
                    "borrowedDate = ?, expectedReturnDate = ?, status = ? " +
                    "WHERE id = ?";
            stmt = this.getConnection().prepareStatement(updateSQL);
            stmt.setString(1, this.borrowedItemId);      // borrowedResourceId
            stmt.setString(2, this.borrowerId);     // patronMemberCardId
            stmt.setString(3, this.issuedLibrarianId);        // issuedLibrarian
            stmt.setTimestamp(4, Timestamp.valueOf(this.borrowedDate));        // borrowedDate
            stmt.setTimestamp(5, Timestamp.valueOf(this.expectedReturnDate));  // expectedReturnDate
            stmt.setString(6, this.status);    // isTransactionClosed
            stmt.setString(7, this.id);          // transactionId
        } else {
            // Insert new transaction
            String insertSQL = "INSERT INTO BorrowingTransaction (id, borrowedResourceId, patronMemberCardId, " +
                    "issuedLibrarian, borrowedDate, expectedReturnDate, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = this.getConnection().prepareStatement(insertSQL);
            stmt.setString(1, this.id);          // transactionId
            stmt.setString(2, this.borrowedItemId);     // borrowedResourceId
            stmt.setString(3, this.borrowerId);     // patronMemberCardId
            stmt.setString(4, this.issuedLibrarianId);        // issuedLibrarian
            stmt.setTimestamp(5, Timestamp.valueOf(this.borrowedDate));        // borrowedDate
            stmt.setTimestamp(6, Timestamp.valueOf(this.expectedReturnDate));  // expectedReturnDate
            stmt.setString(7, this.status);    // isTransactionClosed
        }

        // Execute the prepared statement
        stmt.executeUpdate();
        return true;
    }

    //get all transactions that all borrowed Transactions that are status
    public static List<BorrowTransaction> getAllOverdue() throws SQLException {
        List<BorrowTransaction> overdueTransactions = new ArrayList<>();

        String query =  "SELECT * FROM BorrowingTransaction WHERE expectedReturnDate < NOW() AND status = 'active'";
        PreparedStatement stm  = DatabaseConnection.getConnection().prepareStatement(query);
        ResultSet overdueEntries = stm.executeQuery();
        while (overdueEntries.next()) {
            overdueTransactions.add(formBorrowTransactionObject(overdueEntries));
        }
        
        return overdueTransactions;
    }

    public static List<BorrowTransaction> getAll() throws SQLException{
        List<BorrowTransaction> transactions = new ArrayList<>();
        String query =  "SELECT * FROM BorrowingTransaction;";
        PreparedStatement stm  = DatabaseConnection.getConnection().prepareStatement(query);
        ResultSet overdueEntries = stm.executeQuery();
        while (overdueEntries.next()) {
            transactions.add(formBorrowTransactionObject(overdueEntries));
        }
        return transactions;
    }

    public static List<BorrowTransaction> getUser(String memberCardID)  throws SQLException{
        List<BorrowTransaction> overdueTransactions = new ArrayList<>();
            // Correct the query with proper LIKE pattern
            String query =  "SELECT * FROM BorrowingTransaction WHERE patronMemberCardId LIKE ? ;";
            
            // Prepare the statement
            PreparedStatement stm = DatabaseConnection.getConnection().prepareStatement(query);
            
            // Set the parameters with the LIKE pattern
            stm.setString(1, "%" + memberCardID + "%"); // Add % before and after the value
            
            // Execute the query
            ResultSet overdueEntries = stm.executeQuery();
            
            // Loop through the results and add to the list
            while (overdueEntries.next()) {
                overdueTransactions.add(formBorrowTransactionObject(overdueEntries));
            }
        return overdueTransactions;
    }

    public static List<BorrowTransaction> getResource(String resource) {
        List<BorrowTransaction> overdueTransactions = new ArrayList<>();
        try {
            // Correct the query with proper LIKE pattern
            String query =  "SELECT * FROM BorrowingTransaction WHERE borrowedResourceId LIKE ? ;";
            
            // Prepare the statement
            PreparedStatement stm = DatabaseConnection.getConnection().prepareStatement(query);
            
            // Set the parameters with the LIKE pattern
            stm.setString(1, "%" + resource + "%"); // Add % before and after the value
            
            // Execute the query
            ResultSet overdueEntries = stm.executeQuery();
            
            // Loop through the results and add to the list
            while (overdueEntries.next()) {
                overdueTransactions.add(formBorrowTransactionObject(overdueEntries));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return overdueTransactions;
    }
    
    public boolean checkIfOverdue() {
        //check if account object is overdue
        return LocalDateTime.now().isBefore(this.expectedReturnDate);
    }

    public static BorrowTransaction getById(String id) throws SQLException {
            String sql = "SELECT * FROM BorrowingTransaction WHERE id = ?";
            PreparedStatement st = DatabaseConnection.getConnection().prepareStatement(sql);
            st.setString(1, id);
            ResultSet entry = st.executeQuery();
            if(entry.next())
                return formBorrowTransactionObject(entry);
        return null;
    }
}
 