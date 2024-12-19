package library.modelsTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.junit.jupiter.api.AfterAll;
import library.model.LibraryResource.*;
import library.model.LibraryTransactions.BorrowTransaction;
import library.model.Users.Librarian;
import library.model.Users.Patron;
import java.time.LocalDateTime;
import java.util.List;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import library.utils.databaseOperations.DatabaseConnection;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
class BorrowingTransactionTest {
    private static BorrowTransaction testBorrowTransaction;
    private static Patron bowrower;
    private static Librarian issuedLibrarian;
    private static Book testBook;
    private static Connection databaseConnection;
    private static Genre comedy;
    // This method will be run before any tests are executed
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        // Establish the connection before the tests run
        comedy = new Genre("comedy");
        comedy.saveToDatabase();
        bowrower = new Patron(UUID.randomUUID().toString(), "Test BorrowingTransaction",  "Home xxx", "test@gmail.com", "2333333333", UUID.randomUUID().toString());
        issuedLibrarian = new Librarian(
            UUID.randomUUID().toString(), 
            "test Librarian", "Ochisoa 80", "Kingsley@gmail.com", "0592960273", 
            "Test  Uter",  "testpassword");
        testBook = new Book(
            UUID.randomUUID().toString(), 
            "test Book", "shelf 44", 50, 0, 
            "Test  Uter",  comedy.getName());
        testBook.saveToDatabase();
        bowrower.saveToDatabase();
        issuedLibrarian.saveToDatabase();
        testBorrowTransaction = new BorrowTransaction(UUID.randomUUID().toString(), testBook.getId(), bowrower.getLibraryCardId(),
        LocalDateTime.now(), LocalDateTime.now().plusDays(3), issuedLibrarian.getId(), "active");
        databaseConnection = DatabaseConnection.getConnection();
    }

    // This method will be run after all tests are executed
    @AfterAll
    static void tearDownAfterClass() throws Exception {
        if (testBorrowTransaction.getConnection() != null) {
            testBorrowTransaction.getConnection().close();  // Close the connection after tests
        }
        //delete all test entries
      //
       testBorrowTransaction.deleteFromDatabase();
       testBook.deleteFromDatabase();
       bowrower.deleteFromDatabase();
       issuedLibrarian.deleteFromDatabase();
       comedy.deleteFromDatabase();
    }

    @Test
    void testInsertIntoDatabase() throws SQLException {
        // Test to ensure the book is saved in the database
        boolean transactionSavedSuccessfully = testBorrowTransaction.saveToDatabase();
        assertTrue(transactionSavedSuccessfully, "Should insert the Transaction Model into the database");
        //fetch the information from the datatabase
        String query = "SELECT * FROM BorrowingTransaction where id = ?";
        PreparedStatement st = databaseConnection.prepareStatement(query);
        st.setString(1, testBorrowTransaction.getId());
        ResultSet dataFeedback = st.executeQuery();
        assertTrue(transactionSavedSuccessfully, "The BorrowingTransaction is successfully saved");
        assertTrue(dataFeedback.next());
        assertEquals(dataFeedback.getString("id"), testBorrowTransaction.getId());
    }

  @Test
    void checkUpdate() throws SQLException {
        // Test to ensure the book is saved in the database
        String updatedStatus = "closed";
        testBorrowTransaction.saveToDatabase(); //ensure information is saved before
        testBorrowTransaction.setStatus(updatedStatus); //update information in object
        boolean isUPdated = testBorrowTransaction.saveToDatabase(); //save changes to database;
        String query = "SELECT * FROM BorrowingTransaction WHERE id = ?";
        PreparedStatement pr = databaseConnection.prepareStatement(query);
        pr.setString(1, testBorrowTransaction.getId());
        ResultSet st = pr.executeQuery();
        st.next();
        assertEquals(updatedStatus, st.getString("status"));
        assertTrue(isUPdated); //information must exist
    } 

    @Test
    void checkAllOverdue() throws SQLException {
        testBorrowTransaction.setExpectedReturnDate(LocalDateTime.now().minusDays(5)); //update return date
        testBorrowTransaction.setStatus("active");
        boolean updated = testBorrowTransaction.saveToDatabase(); //save changes
        List<BorrowTransaction> overDue = BorrowTransaction.getAllOverdue();
        assertTrue(updated);
        assertEquals(1, overDue.size());
        }

    @Test
    void getUserBorrowBook() throws SQLException {
        testBorrowTransaction.setExpectedReturnDate(LocalDateTime.now().minusDays(5)); //update return date
        testBorrowTransaction.setStatus("active");
        assertEquals("active", testBorrowTransaction.getStatus());
        boolean updated = testBorrowTransaction.saveToDatabase(); //save changes
        List<BorrowTransaction> overDue = BorrowTransaction.getAllOverdue();
        assertTrue(updated);
        assertEquals(1, overDue.size());
        }

    @Test
    void gettersSetters() throws SQLException {
        testBorrowTransaction.saveToDatabase();
        assertEquals("active", testBorrowTransaction.getStatus());
        assertNotNull(testBorrowTransaction.getExpectedReturnDate());
        assertNotNull(testBorrowTransaction.getBorrowedDate());
        assertNotNull(testBorrowTransaction.checkIfOverdue());
        assertNotNull(testBorrowTransaction.getBorrower());
        assertNotNull(testBorrowTransaction.getIssuedLibrarian());
        assertNotNull(testBorrowTransaction.getBorrowedItem());
    } 
    
    @Test
    void getAllTransactions() throws SQLException {
        testBorrowTransaction.saveToDatabase();
        var transactions = BorrowTransaction.getAll();
        assertEquals(1, transactions.size(), "expect empty transactions");
    }

    @Test
    void getUserTransactions() throws SQLException {
        var transaction = BorrowTransaction.getUser("wrong user");
        assertEquals(0, transaction.size());
    }

    @Test
    void getUserExist() throws SQLException {
        testBorrowTransaction.saveToDatabase();
        var transaction = BorrowTransaction.getUser(bowrower.getLibraryCardId());
        assertEquals(1, transaction.size());
    }

    @Test
    void getResourceEmpty() throws SQLException {
        testBorrowTransaction.saveToDatabase();
        var transaction = BorrowTransaction.getResource("wrong id");
        assertEquals(0, transaction.size());
    }

    @Test
    void getResource() throws SQLException {
        testBorrowTransaction.saveToDatabase();
        var transaction = BorrowTransaction.getResource(testBook.getId());
        assertEquals(1, transaction.size());
    }

    @Test
    void getIdEmpty() throws SQLException {
        testBorrowTransaction.saveToDatabase();
        var transaction = BorrowTransaction.getById("wrong id");
        assertNull(transaction);
    }

    @Test
    void getIdExist() throws SQLException {
        testBorrowTransaction.saveToDatabase();
        var transaction = BorrowTransaction.getById(testBorrowTransaction.getId());
        assertNotNull(transaction);
    }

    @Test
    void mockException() throws SQLException{
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(new SQLException("database error"));
        try(MockedStatic<DatabaseConnection> mockedDatabaseConnection = mockStatic(DatabaseConnection.class)) {
            mockedDatabaseConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            List<BorrowTransaction> result = BorrowTransaction.getResource("Some Resource");
            assertTrue(result.isEmpty(), "Expected empty result due to SQLException");
        }
    }
    
} 

    

