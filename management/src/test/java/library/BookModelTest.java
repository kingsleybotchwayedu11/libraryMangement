package library;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import library.model.LibraryResource.Book;
import library.utils.databaseOperations.DatabaseConnection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
class BookModelTest {

    private static Book testBook; 
    private static Connection databaseConnection;
    // This method will be run before any tests are executed
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        // Establish the connection before the tests run
        testBook = new Book(
        UUID.randomUUID().toString(), 
        "test Book", "shelf 44", 50, 0, 
        "Test  Uter", "uniq isb", "comedy");
        databaseConnection = DatabaseConnection.getConnection();
    }

    // This method will be run after all tests are executed
    @AfterAll
    static void tearDownAfterClass() throws Exception {
        if (testBook.getConnection() != null) {
            testBook.getConnection().close();  // Close the connection after tests
        }
    }

    @Test
    void testInsertIntoDatabase() throws SQLException {
        // Test to ensure the book is saved in the database
        boolean bookIsSavedSuccesfully = testBook.saveToDatabase();
        assertTrue(bookIsSavedSuccesfully, "Should insert the Book model into the database");
        //fetch the information from the datatabase
        String query = "SELECT * FROM Book where id = ?";
        PreparedStatement st = databaseConnection.prepareStatement(query);
        st.setString(1, testBook.getId());
        ResultSet dataFeedback = st.executeQuery();
        assertTrue(bookIsSavedSuccesfully, "The book is successfully saved");
        assertTrue(dataFeedback.next());
        assertEquals(dataFeedback.getString("title"), testBook.getTitle());
    }
    @Test
    void testUpdateInformation() throws SQLException{
        // Test to ensure the book is saved in the database
        String updatedTitle = "updated tittle";
        testBook.setTitle(updatedTitle); //update the title in book object
        testBook.saveToDatabase(); //save the updated information into the database
        boolean bookUpdatedSuccessful = testBook.saveToDatabase(); // call save but expect update operation since the book is already saved
        String query = "SELECT * FROM Book where id = ?"; //get book from the database;
        PreparedStatement st = databaseConnection.prepareStatement(query);
        st.setString(1, testBook.getId());
        ResultSet updatedFeedBack = st.executeQuery();
        assertTrue(updatedFeedBack.next());
        assertTrue(bookUpdatedSuccessful);
        assertEquals(updatedFeedBack.getString("title"), updatedTitle);
    } 

    @Test
    void checkDeleteFromDatabae()  throws SQLException{
        testBook.saveToDatabase();
        boolean successfullyDeleted = testBook.deleteFromDatabase(); //delete
        String query = "SELECT * FROM Book where id = ?"; // try to select the deleted entry
        PreparedStatement st = databaseConnection.prepareStatement(query); 
        st.setString(1, testBook.getId());
        ResultSet dataFeedback = st.executeQuery(); // there should be no rows since the entry is deleted
        assertTrue(successfullyDeleted, "The book entry is deleted");
        assertFalse(dataFeedback.next(), "The should be no rows"); //ther should be no rows
    }

   
}
