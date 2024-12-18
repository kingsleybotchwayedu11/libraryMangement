package library.modelsTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import org.junit.jupiter.api.AfterAll;
import library.model.LibraryResource.*;
import library.model.LibraryTransactions.BorrowTransaction;
import library.model.LibraryTransactions.CheckInTransaction;
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
class CheckInTransactionTest {
    private static BorrowTransaction testBorrowTransaction;
    private static Patron bowrower;
    private static Librarian issuedLibrarian;
    private static Book testBook;
    private static Connection databaseConnection;
    private static Genre comedy;
    private static CheckInTransaction checkst;
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
        //form check in transaction object
        testBorrowTransaction.saveToDatabase();
        checkst = new CheckInTransaction(UUID.randomUUID().toString(), issuedLibrarian.getId(), testBorrowTransaction.getId(), LocalDateTime.now());
        databaseConnection = DatabaseConnection.getConnection();
    }

    // This method will be run after all tests are executed
    @AfterAll
    static void tearDownAfterClass() throws Exception {
        if (checkst.getConnection() != null) {
            checkst.getConnection().close();  // Close the connection after tests
        }
        //delete all test entries
      //
       checkst.deleteFromDatabase();
       testBorrowTransaction.deleteFromDatabase();
       testBook.deleteFromDatabase();
       bowrower.deleteFromDatabase();
       issuedLibrarian.deleteFromDatabase();
       comedy.deleteFromDatabase();
     ;
    }

    @Test
    void testInsertIntoDatabase() throws SQLException {
        //Test to ensure the book is saved in the database
        boolean checinTransactionSavedSuccessfully = checkst.saveToDatabase();
       assertTrue(checinTransactionSavedSuccessfully, "Should insert the Transaction Model into the database");
        //fetch the information from the datatabase
        String query = "SELECT * FROM CheckInTransaction where id = ?";
        PreparedStatement st = databaseConnection.prepareStatement(query);
        st.setString(1, checkst.getId());
        ResultSet dataFeedback = st.executeQuery();
        assertTrue(dataFeedback.next());
        assertEquals(dataFeedback.getString("id"), checkst.getId()); 
    } 
    @Test
    void checkUpdate() throws SQLException {
        // Test to ensure the book is saved in the database
        boolean checinTransactionSavedSuccessfully = checkst.saveToDatabase();
        assertTrue(checinTransactionSavedSuccessfully, "Should insert the Transaction Model into the database");
        checkst.setAcceptedBy("strong id");
        checkst.saveToDatabase();
        assertEquals(checkst.getAcceptedBy(), "strong id");
    }


    @Test
    void checkType () {
        assertEquals(checkst.getType(), "CheckInTransaction");
    }
} 

    

