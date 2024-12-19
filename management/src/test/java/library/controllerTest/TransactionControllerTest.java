package library.controllerTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

import library.controllers.TransactionController;
import library.controllers.StatusReport;
import library.model.LibraryResource.Book;
import library.model.LibraryTransactions.BorrowTransaction;
import library.model.LibraryTransactions.CheckInTransaction;
import library.model.LibraryTransactions.Reservation;
import library.model.Users.Librarian;
import library.model.Users.Patron;
import library.utils.BorrowingQue.BorrowingQue;
import library.utils.sessions.LibrarianSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

public class TransactionControllerTest {

    @Mock private Book mockedBook;
    @Mock private Patron mockedPatron;
    @Mock private Librarian mockedLibrarian;
    @Mock private BorrowTransaction mockedTransaction;
    @Mock private CheckInTransaction mockedCheckInTransaction;
    @Mock private Reservation mockedReservation;

    @BeforeEach
    public void setup() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBorrowResource_bookNotFound() throws SQLException {
        // Arrange
        String bookId = "book123";
        String patronId = "patron456";
        LocalDateTime returnDate = LocalDateTime.now().plusDays(7);
        
        when(Book.getById(bookId)).thenReturn(null); // Book is not found

        // Act
        StatusReport status = TransactionController.borrowResource(bookId, patronId, mockedLibrarian, returnDate);

        // Assert
        assertFalse(status.getOperationStatus());
        assertEquals("Book not found", status.getMessage());
    }

    @Test
    public void testBorrowResource_patronNotFound() throws SQLException {
        // Arrange
        String bookId = "book123";
        String patronId = "patron456";
        LocalDateTime returnDate = LocalDateTime.now().plusDays(7);
        
        when(Book.getById(bookId)).thenReturn(mockedBook); // Book found
        when(Patron.findOne("libraryCardId", patronId)).thenReturn(null); // Patron not found

        // Act
        StatusReport status = TransactionController.borrowResource(bookId, patronId, mockedLibrarian, returnDate);

        // Assert
        assertFalse(status.getOperationStatus());
        assertEquals("Patron not found, check the member ID", status.getMessage());
    }

    @Test
    public void testBorrowResource_bookUnavailable() throws SQLException {
        // Arrange
        String bookId = "book123";
        String patronId = "patron456";
        LocalDateTime returnDate = LocalDateTime.now().plusDays(7);
        
        when(Book.getById(bookId)).thenReturn(mockedBook); // Book found
        when(Patron.findOne("libraryCardId", patronId)).thenReturn(mockedPatron); // Patron found
        when(mockedBook.isAvailable()).thenReturn(false); // Book is unavailable

        // Act
        StatusReport status = TransactionController.borrowResource(bookId, patronId, mockedLibrarian, returnDate);

        // Assert
        assertFalse(status.getOperationStatus());
        assertEquals("Book not available, request added to pending list", status.getMessage());
    }

    @Test
    public void testBorrowResource_successful() throws SQLException {
        // Arrange
        String bookId = "book123";
        String patronId = "patron456";
        LocalDateTime returnDate = LocalDateTime.now().plusDays(7);
        
        when(Book.getById(bookId)).thenReturn(mockedBook); // Book found
        when(Patron.findOne("libraryCardId", patronId)).thenReturn(mockedPatron); // Patron found
        when(mockedBook.isAvailable()).thenReturn(true); // Book is available
        when(mockedTransaction.saveToDatabase()).thenReturn(true); // Transaction saved successfully
        when(mockedBook.incrementTotalBorrowed()).thenReturn(true); // Book borrow count updated

        // Act
        StatusReport status = TransactionController.borrowResource(bookId, patronId, mockedLibrarian, returnDate);

        // Assert
        assertTrue(status.getOperationStatus());
        assertEquals("Transaction saved", status.getMessage());
    }

    @Test
    public void testReturnResource_transactionNotFound() throws SQLException {
        // Arrange
        String transactionId = "transaction123";
        
        when(BorrowTransaction.getById(transactionId)).thenReturn(null); // Transaction not found

        // Act
        StatusReport status = TransactionController.returnResource(transactionId);

        // Assert
        assertFalse(status.getOperationStatus());
        assertEquals("Transaction entry not found", status.getMessage());
    }

    @Test
    public void testReturnResource_successful() throws SQLException {
        // Arrange
        String transactionId = "transaction123";
        String bookId = "book123";
        String patronId = "patron456";
        
        BorrowTransaction transaction = mock(BorrowTransaction.class);
        when(BorrowTransaction.getById(transactionId)).thenReturn(transaction); // Transaction found
        when(Book.getById(bookId)).thenReturn(mockedBook); // Book found
        when(LibrarianSession.getLoggedInLibrarian()).thenReturn(mockedLibrarian); // Librarian found
        when(mockedCheckInTransaction.saveToDatabase()).thenReturn(true); // Check-in saved successfully
        when(transaction.getBorrowedItem()).thenReturn(bookId); // Transaction has a book
        when(mockedBook.decrementTotalBorrowed()).thenReturn(true); // Decrement borrow count

        // Act
        StatusReport status = TransactionController.returnResource(transactionId);

        // Assert
        assertTrue(status.getOperationStatus());
        assertEquals("Process completed", status.getMessage());
    }

    @Test
    public void testAddReservation_bookNotFound() throws SQLException {
        // Arrange
        String bookId = "book123";
        String patronId = "patron456";
        int days = 7;
        
        when(Book.getById(bookId)).thenReturn(null); // Book not found

        // Act
        StatusReport status = TransactionController.addReservation(bookId, patronId, days);

        // Assert
        assertFalse(status.getOperationStatus());
        assertEquals("Can't find book", status.getMessage());
    }

    @Test
    public void testAddReservation_patronNotFound() throws SQLException {
        // Arrange
        String bookId = "book123";
        String patronId = "patron456";
        int days = 7;
        
        when(Book.getById(bookId)).thenReturn(mockedBook); // Book found
        when(Patron.findOne("libraryCardId", patronId)).thenReturn(null); // Patron not found

        // Act
        StatusReport status = TransactionController.addReservation(bookId, patronId, days);

        // Assert
        assertFalse(status.getOperationStatus());
        assertEquals("Can't find patron", status.getMessage());
    }

    @Test
    public void testAddReservation_successful() throws SQLException {
        // Arrange
        String bookId = "book123";
        String patronId = "patron456";
        int days = 7;
        
        when(Book.getById(bookId)).thenReturn(mockedBook); // Book found
        when(Patron.findOne("libraryCardId", patronId)).thenReturn(mockedPatron); // Patron found
        when(mockedReservation.saveToDatabase()).thenReturn(true); // Reservation saved successfully
        when(BorrowingQue.addToQue(bookId, mockedReservation.getId())).thenReturn(true); // Added to the queue

        // Act
        StatusReport status = TransactionController.addReservation(bookId, patronId, days);

        // Assert
        assertTrue(status.getOperationStatus());
        assertEquals("Operation successful", status.getMessage());
    }
}
