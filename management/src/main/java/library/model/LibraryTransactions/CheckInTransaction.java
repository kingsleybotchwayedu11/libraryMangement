package library.model.LibraryTransactions;

import library.model.Users.Patron;

public class CheckInTransaction extends LibraryTransaction {
    // The borrowed item (book, magazine, or other resources)
    private BorrowTransaction transaction;
    
    // The library member who borrowed the item
    private Patron libraryMember;
    
    // The return date
    private String returnedDate; 
    
    
    
   
}
