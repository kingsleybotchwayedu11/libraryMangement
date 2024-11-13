package library.model.LibraryTransactions;

import library.model.LibraryResource.LibraryResource;
import library.model.Users.*;

public class BorrowTransaction {
    // The borrowed item (book, magazine, or other resources)
    private LibraryResource borrowedItem;
    
    // The library member who borrowed the item
    private Patron borrower;
    
    // The expected return date for the borrowed item
    private String expectedReturnDate; 
    
    // The actual borrowed date (when the transaction occurred)
    private String borrowedDate; 
    
    // The librarian who issued the item
    private Librarian issuedLibrarian;
    
    // Tracks whether the transaction has been closed (e.g., item returned)
    private String status; 
    
    // Unique identifier for the borrow transaction
    

    // Getter and Setter methods

    // Getters and Setters for borrowedItem
    public LibraryResource getBorrowedItem() {
        return borrowedItem;
    }

    public void setBorrowedItem(LibraryResource borrowedItem) {
        this.borrowedItem = borrowedItem;
    }

    // Getters and Setters for borrower
    public Patron getBorrower() {
        return borrower;
    }

    public void setBorrower(Patron borrower) {
        this.borrower = borrower;
    }

    // Getters and Setters for expectedReturnDate
    public String getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(String expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    // Getters and Setters for borrowedDate
    public String getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    // Getters and Setters for issuedLibrarian
    public Librarian getIssuedLibrarian() {
        return issuedLibrarian;
    }

    public void setIssuedLibrarian(Librarian issuedLibrarian) {
        this.issuedLibrarian = issuedLibrarian;
    }

    // Getters and Setters for isTransactionClosed
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
