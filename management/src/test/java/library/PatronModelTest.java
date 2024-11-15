package library;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.mysql.cj.result.SqlDateValueFactory;

import org.junit.jupiter.api.AfterAll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


import library.model.LibraryResource.Book;
import library.model.Users.Patron;
import library.utils.databaseOperations.DatabaseConnection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
class PatronModelTest {

    private static Patron testPatron; 
    private static Connection databaseConnection;
    // This method will be run before any tests are executed
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        // Establish the connection before the tests run
        testPatron = new Patron(UUID.randomUUID().toString(), "Test Pathron",  "Home xxx", "test@gmail.com", "2333333333", UUID.randomUUID().toString());
        databaseConnection = DatabaseConnection.getConnection();
    }

    // This method will be run after all tests are executed
    @AfterAll
    static void tearDownAfterClass() throws Exception {
        if (testPatron.getConnection() != null) {
            testPatron.getConnection().close();  // Close the connection after tests
        }
        //delete all test entries
      testPatron.deleteFromDatabase();
    }

    @Test
    void testInsertIntoDatabase() throws SQLException {
        // Test to ensure the book is saved in the database
        boolean patronSavedSuccesfully = testPatron.saveToDatabase();
        assertTrue(patronSavedSuccesfully, "Should insert the Book model into the database");
        //fetch the information from the datatabase
        String query = "SELECT * FROM Patron where id = ?";
        PreparedStatement st = databaseConnection.prepareStatement(query);
        st.setString(1, testPatron.getId());
        ResultSet dataFeedback = st.executeQuery();
        assertTrue(patronSavedSuccesfully, "The pathron is successfully saved");
        assertTrue(dataFeedback.next());
        assertEquals(dataFeedback.getString("name"), testPatron.getName());
    }

    @Test 
    void checkFindPathrons() throws SQLException {
        testPatron.saveToDatabase();
        List<Patron> patrons = Patron.findByAttribute("name", testPatron.getName());
        assertEquals(patrons.size(), 1);   
    }
    
   /*  @Test
    void checkUpdate() throws SQLException {

        // Test to ensure the book is saved in the database
        String updatedName = "Kingsley Test";
        testPatron.saveToDatabase();
        String query = "SELECT * FROM Patron WHERE id = ?";
        PreparedStatement pr = databaseConnection.prepareStatement(query);
        pr.setString(1, testPatron.getId());
        ResultSet st = pr.executeQuery();
        st.next();
        assertEquals(updatedName, st.getString("name"));
        testPatron.setName(updatedName);
        boolean isUPdated = testPatron.saveToDatabase();
        assertTrue(isUPdated); //information must exist
    }
    
    */
}