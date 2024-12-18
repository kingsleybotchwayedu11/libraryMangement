package library.modelsTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import library.model.LibraryResource.Genre;
import library.utils.databaseOperations.DatabaseConnection;

public class GenreTest {

    public static Connection connection;
    static String genreName = "Text Genre";
    static Genre genre;

    @BeforeAll
    static void setUpBeforeClass() throws SQLException {
        // Establish the connection before the tests run
        DatabaseConnection db = new DatabaseConnection();
        connection = db.getConnection();
        genre = new Genre(genreName);
    }

    @AfterAll
    static void tearDownAfterClass() throws SQLException{
        genre.deleteFromDatabase();
        if (connection != null) {
            connection.close();  // Close the connection after tests
        }
    }

    @Test
    void saveToDatabase()  throws SQLException{
        // Ensure the genre does not already exist
        assertNull(Genre.getGenre(genreName));
        
        // Save the genre to the database
        assertTrue(genre.saveToDatabase());
        assertFalse(genre.saveToDatabase());
        
        // Ensure the genre is now saved in the database
        assertNotNull(Genre.getGenre(genreName));

    }

}
