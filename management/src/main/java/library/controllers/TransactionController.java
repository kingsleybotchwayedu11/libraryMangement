
package library.controllers;

import java.time.LocalDateTime;

import library.model.LibraryResource.LibraryResource;
import library.model.LibraryTransactions.BorrowTransaction;
import library.model.LibraryTransactions.CheckInTransaction;
import library.model.Users.Librarian;
import library.model.Users.Patron;
import library.utils.BorrowingQue.BorrowingQue;

import java.util.UUID;
import java.util.List;

public class TransactionController {

     static StatusReport borrowResource(LibraryResource book, Patron patron, Librarian libarian, LocalDateTime expectedReutnrDate) {
        //check book
        
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