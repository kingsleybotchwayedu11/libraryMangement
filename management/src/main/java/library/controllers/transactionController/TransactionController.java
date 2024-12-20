package library.controllers.transactionController;

import java.sql.SQLException;
import java.time.LocalDateTime;

import library.controllers.StatusReport;
import library.model.LibraryResource.Book;
import library.model.LibraryResource.LibraryResource;
import library.model.LibraryTransactions.BorrowTransaction;
import library.model.LibraryTransactions.CheckInTransaction;
import library.model.LibraryTransactions.Reservation;
import library.model.Users.Librarian;
import library.model.Users.Patron;
import library.utils.BorrowingQue.BorrowingQue;
import library.utils.sessions.LibrarianSession;

import java.util.UUID;
import java.util.List;

public class TransactionController {

    /**
     * Handles the borrowing of a library resource.
     * 
     * @param bookId            The ID of the book to be borrowed.
     * @param patronMemId       The ID of the patron borrowing the book.
     * @param libarian          The librarian authorizing the transaction.
     * @param expectedReutnrDate The expected return date for the book.
     * @return A StatusReport indicating success or failure of the transaction.
     */
    public static StatusReport borrowResource(String bookId, String patronMemId, Librarian libarian, LocalDateTime expectedReutnrDate) throws SQLException {
        // Retrieve the book by ID.
        Book book = Book.getById(bookId);
        if (book == null) {
            return new StatusReport(false, "Book not found");
        }

        // Retrieve the patron by library card ID.
        Patron patron = Patron.findOne("libraryCardId", patronMemId);
        if (patron == null) {
            return new StatusReport(false, "Patron not found, check the member ID");
        }

        // Check if the book is available.
        if (!book.isAvailable()) {
            // If unavailable, add the patron to the borrowing queue.
            return new StatusReport(false, "Book not available, request added to pending list");
        } else {
            // Create a new borrowing transaction object.
            BorrowTransaction transaction = new BorrowTransaction(
                UUID.randomUUID().toString(), book.getId(), patron.getLibraryCardId(),
                LocalDateTime.now(), expectedReutnrDate,
                libarian.getId(), "active"
            );

            // Save the transaction to the database.
            boolean savedStatus = transaction.saveToDatabase();
            if (savedStatus) {
                // Update the book's borrowed count and save it to the database.
                book.incrementTotalBorrowed();
                book.saveToDatabase();
                return new StatusReport(true, "Transaction saved", transaction);
            } else {
                return new StatusReport(false, "Couldn't save information to the database");
            }
        }
    }

    /**
     * Handles the return of a borrowed library resource.
     * 
     * @param transactionId The ID of the borrowing transaction being closed.
     * @return A StatusReport indicating success or failure of the return process.
     */
    public static StatusReport returnResource(String transactionId) throws SQLException{
        // Retrieve the borrowing transaction by ID.
        BorrowTransaction transactionBorrow = BorrowTransaction.getById(transactionId);
        if (transactionBorrow == null) {
            return new StatusReport(false, "Transaction entry not found");
        }

        // Retrieve the book associated with the transaction.
        Book book = Book.getById(transactionBorrow.getBorrowedItem());

        // Retrieve the currently logged-in librarian.
        Librarian lib = LibrarianSession.getLoggedInLibrarian();

        // Create a new check-in transaction.
        CheckInTransaction transactionCheckin = new CheckInTransaction(
            UUID.randomUUID().toString(), lib.getId(), transactionBorrow.getId(), LocalDateTime.now()
        );

        // Save the check-in transaction to the database.
        Boolean checkinDetailsSaved = transactionCheckin.saveToDatabase();
        if (checkinDetailsSaved) {
            // Close the borrowing transaction and save it to the database.
            transactionBorrow.setStatus("close");
            transactionBorrow.saveToDatabase();

            // Check if there are pending requests for the book in the queue.
            String reservationId = BorrowingQue.getFirstPerson(book.getId());
            if (reservationId != null) {
                // If a reservation exists, process borrowing for the next patron.
                Reservation reservationDetails = Reservation.getById(reservationId);
                borrowResource(
                    reservationDetails.getBookId(),
                    reservationDetails.getPatronId(),
                    LibrarianSession.getLoggedInLibrarian(),
                    LocalDateTime.now().plusDays(reservationDetails.getExpectedNumberOfDays())
                );
            } else {
                // If no reservations exist, update the book's borrowed count.
                book.decrementTotalBorrowed();
                book.saveToDatabase();
            }
            return new StatusReport(true, "Process completed", transactionCheckin);
        } else {
            return new StatusReport(false, "Can't save check-in information to the database");
        }
    }

    /**
     * Adds a reservation for a library resource.
     * 
     * @param bookId   The ID of the book to be reserved.
     * @param memberId The ID of the patron making the reservation.
     * @param days     The number of days the reservation is valid.
     * @return A StatusReport indicating success or failure of the reservation.
     */
    public static StatusReport addReservation(String bookId, String memberId, int days) throws SQLException{
        // Retrieve the book by ID.
        Book book = Book.getById(bookId);
        if (book == null) {
            return new StatusReport(false, "Can't find book");
        }

        // Retrieve the patron by library card ID.
        Patron patron = Patron.findOne("libraryCardId", memberId);
        if (patron == null) {
            return new StatusReport(false, "Can't find patron");
        }

        // Create a new reservation object.
        Reservation reserve = new Reservation(UUID.randomUUID().toString(), bookId, memberId, LocalDateTime.now(), days);

        // Save the reservation to the database.
        Boolean isSavedSuccessfully = reserve.saveToDatabase();
        if(isSavedSuccessfully) {
            BorrowingQue.addToQue(bookId, reserve.getId());
            return new StatusReport(true, "Operation successful", reserve);
        }else {
            return new StatusReport(false, "Can't save to the database");
        }
    }
}
