
package library.controllers;

import java.time.LocalDateTime;

import library.model.LibraryResource.Book;
import library.model.LibraryResource.LibraryResource;
import library.model.LibraryTransactions.BorrowTransaction;
import library.model.LibraryTransactions.CheckInTransaction;
import library.model.Users.Librarian;
import library.model.Users.Patron;
import library.utils.BorrowingQue.BorrowingQue;

import java.util.UUID;
import java.util.List;

public class TransactionController {

     public static StatusReport borrowResource(String bookId, String patronMemId, Librarian libarian, LocalDateTime expectedReutnrDate) {
        //check book
        Book book = Book.getById(bookId);
        if(book == null) {
            return new StatusReport(false, "Book not found");
        } 
        Patron patron = Patron.findOne("libraryCardId", patronMemId);
        if(patron == null)
            return new StatusReport(false, "Patron not found, check the member id");
        
        //check if book is available
        if(!book.isAvailable()) {
            //add customer to que
            BorrowingQue.addToQue(book.getId(), patron);
            return new StatusReport(false, "book not available request is added to pending list");
        }
        else {
            //form a borrowed transaction object
            BorrowTransaction transaction = new BorrowTransaction(
                UUID.randomUUID().toString(), book.getId(), patron.getLibraryCardId(),
                LocalDateTime.now(), expectedReutnrDate,
                 libarian.getId(), "active"
                 );
                 boolean savedStatus = transaction.saveToDatabase();
                 if(savedStatus) {
                    book.incrementTotalBorrowed();
                    return new StatusReport(true, "transaction Saved", transaction);
                 }else {
                    return new StatusReport(false, "couldnt save information to database");
                 }
             }
     
        }

        static StatusReport returnResource(LibraryResource book, Patron patron, Librarian lib) {
            //check book
            //find borrwon resource
            List<BorrowTransaction> borrowTransactions = BorrowTransaction.getUserBorrowBook(book.getId(), patron.getLibraryCardId());
            if(borrowTransactions == null || borrowTransactions.size() == 0)
                return new StatusReport(false, "cant find entry for user resource");
            BorrowTransaction transactionBorrow = borrowTransactions.get(0);
            //form transaction object
            CheckInTransaction transactionCheckin = new CheckInTransaction(UUID.randomUUID().toString(), lib.getId(), transactionBorrow.getId(), LocalDateTime.now());
            Boolean checkinDetailsSaved = transactionCheckin.saveToDatabase();
            if(checkinDetailsSaved) {
                transactionBorrow.setStatus("close");
                transactionBorrow.saveToDatabase();
                //check to see if there are people who wants to borrow the resource
                Patron quedPatron = BorrowingQue.getFirstPerson(book.getId());
                if(quedPatron != null) {
                    //give the book to the person

                }
                return new StatusReport(true, "process completed",transactionCheckin);
            } else {
                return new StatusReport(false, "cant save checkin information in the database");
            }
            
            }

            
     }