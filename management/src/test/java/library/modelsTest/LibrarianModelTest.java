package library.modelsTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import org.junit.jupiter.api.AfterAll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import library.model.Users.Librarian;
import library.utils.databaseOperations.DatabaseConnection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
class LibrarianModleTest {

    private static Librarian testLibrarian; 
    private static Connection databaseConnection;
    // This method will be run before any tests are executed
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        // Establish the connection before the tests run
        testLibrarian = new Librarian(
        UUID.randomUUID().toString(), 
        "test Librarian", "Ochisoa 80", "Kingsley@gmail.com", "0592960273", 
        "Test2333r",  "testpassword");
        databaseConnection = DatabaseConnection.getConnection();
    }

    // This method will be run after all tests are executed
    @AfterAll
    static void tearDownAfterClass() throws Exception {
        if (testLibrarian.getConnection() != null) {
            testLibrarian.getConnection().close();  // Close the connection after tests
        }
        //delete all test entries
        testLibrarian.deleteFromDatabase();
    }
    @Test
    void testGetters() {
        assertNotNull(testLibrarian.getPassword());
        assertNotNull(testLibrarian.getUserName());
    }

    @Test
    void testInsertIntoDatabase() throws SQLException {
        // Test to ensure the Librarian is saved in the database
        boolean libarianSavedSuccessfully = testLibrarian.saveToDatabase();
        assertTrue(libarianSavedSuccessfully, "Should insert the Librarian model into the database");
        //fetch the information from the datatabase
        String query = "SELECT * FROM Librarian where id = ?";
        PreparedStatement st = databaseConnection.prepareStatement(query);
        st.setString(1, testLibrarian.getId());
        ResultSet dataFeedback = st.executeQuery();
        assertTrue(libarianSavedSuccessfully, "The Librarian is successfully saved");
        assertTrue(dataFeedback.next());
        assertEquals(dataFeedback.getString("id"), testLibrarian.getId());
    }
    @Test
    void testUpdateInformation() throws SQLException{
        // Test to ensure the Librarian is saved in the database
        String updatedName = "Updated anem";
        testLibrarian.setName(updatedName); //update the title in Librarian object
        testLibrarian.saveToDatabase(); //save the updated information into the database
        boolean libarianUpdatedSuccessfully = testLibrarian.saveToDatabase(); // call save but expect update operation since the Librarian is already saved
        String query = "SELECT * FROM Librarian where id = ?"; //get Librarian from the database;
        PreparedStatement st = databaseConnection.prepareStatement(query);
        st.setString(1, testLibrarian.getId());
        ResultSet updatedFeedBack = st.executeQuery();
        assertTrue(updatedFeedBack.next()); //ther should be entry in the database
        assertTrue(libarianUpdatedSuccessfully);
        assertEquals(updatedFeedBack.getString("name"), updatedName);
    } 

    @Test
    void checkDeleteFromDatabae()  throws SQLException{
        testLibrarian.saveToDatabase();
        boolean successfullyDeleted = testLibrarian.deleteFromDatabase(); //delete
        String query = "SELECT * FROM Librarian where id = ?"; // try to select the deleted entry
        PreparedStatement st = databaseConnection.prepareStatement(query); 
        st.setString(1, testLibrarian.getId());
        ResultSet dataFeedback = st.executeQuery(); // there should be no rows since the entry is deleted
        assertTrue(successfullyDeleted, "The Librarian entry is deleted");
        assertFalse(dataFeedback.next(), "The should be no rows"); //ther should be no rows
    }
    @Test 
    void findByIdTest() throws SQLException {
        testLibrarian.saveToDatabase();
        Librarian librarian = Librarian.findOne("id",testLibrarian.getId());
        assertEquals(librarian.getId(), testLibrarian.getId());
    }

    @Test
    void findOneWrongColumn() throws SQLException {
        Librarian librarian = Librarian.findOne("wrong column", "name");
        assertNull(librarian);
    }
    @Test
    void doesNotExist() throws SQLException {
        Librarian librarian = Librarian.findOne("name", "name");
        assertNull(librarian);
    }
    @Test 
    void finOneWrongColumn() throws SQLException {
        assertNotNull(testLibrarian.getUserName());
        assertNotNull(testLibrarian.getPassword());
    }
}
