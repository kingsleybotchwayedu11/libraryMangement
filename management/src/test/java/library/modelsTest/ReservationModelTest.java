package library.modelsTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import library.model.Users.*;
import library.model.LibraryResource.*;

import library.model.LibraryTransactions.Reservation;
import library.utils.databaseOperations.DatabaseConnection;

import static org.junit.jupiter.api.Assertions.*;

class ReservationModelTest {

    private static Reservation testReservation;
    private static Connection databaseConnection;
    private static Book textBook;
    private static Patron textPatron;
    private static Genre genre;
    // This method will be run before any tests are executed
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        genre = new Genre("Thriler");
        genre.saveToDatabase();
        textPatron = new Patron(UUID.randomUUID().toString(), "Test BorrowingTransaction",  "Home xxx", "test@gmail.com", "2333333333", UUID.randomUUID().toString());
        textBook = new Book(
            UUID.randomUUID().toString(), 
            "test Book", "shelf 44", 50, 0, 
            "Test  Uter",  genre.getName());
        textBook.saveToDatabase();
        textPatron.saveToDatabase();
        // Establish the connection before the tests run
        testReservation = new Reservation(
                UUID.randomUUID().toString(), // id
                textBook.getId(), // bookId
                textPatron.getLibraryCardId(), // patronId
                LocalDateTime.now().plusDays(1), // reservedDate
                5 // expectedNumberOfDays
        );

        databaseConnection = DatabaseConnection.getConnection();
    }

    // This method will be run after all tests are executed
    @AfterAll
    static void tearDownAfterClass() throws Exception {
        if (testReservation != null && testReservation.getConnection() != null) {
            testReservation.getConnection().close();  // Close the connection after tests
        }
        // Delete all test entries
        testReservation.deleteFromDatabase();
        textBook.deleteFromDatabase();
        textPatron.deleteFromDatabase();
        genre.deleteFromDatabase();
    }

    // Test to ensure the Reservation is saved into the database
    @Test
    void testInsertIntoDatabase() throws SQLException {
        // Save the reservation into the database
        boolean reservationSavedSuccessfully = testReservation.saveToDatabase();
        assertTrue(reservationSavedSuccessfully, "Should insert the Reservation model into the database");

        // Fetch the information from the database to confirm it was saved
        String query = "SELECT * FROM Reservation WHERE id = ?";
        PreparedStatement stmt = databaseConnection.prepareStatement(query);
        stmt.setString(1, testReservation.getId());
        ResultSet resultSet = stmt.executeQuery();

        assertTrue(resultSet.next(), "Reservation should exist in the database");
        assertEquals(resultSet.getString("id"), testReservation.getId());
        assertEquals(resultSet.getString("bookId"), testReservation.getBookId());
        assertEquals(resultSet.getString("patronId"), testReservation.getPatronId());
        assertEquals(resultSet.getInt("expectedNumberOfDays"), testReservation.getExpectedNumberOfDays()); 
    }

    // Test to find a reservation by its attributes (e.g., "bookId")
     @Test
    void checkFindReservationsByBookId() throws SQLException {
        testReservation.saveToDatabase();
        List<Reservation> reservations = Reservation.findByAttribute("bookId", testReservation.getBookId());
        assertEquals(reservations.size(), 1, "There should be one reservation with the given bookId");
    }

    @Test
    void wrongAttribute() throws SQLException {
        testReservation.saveToDatabase();
        List<Reservation> reservations = Reservation.findByAttribute("wrongAttribute", testReservation.getBookId());
        assertNull(reservations);
    }

    // Test to check if a reservation can be updated in the database
    

    // Test to check if all active reservations can be fetched
    @Test
    void checkGetAllActiveReservations() throws SQLException {
        testReservation.saveToDatabase();
        List<Reservation> activeReservations = Reservation.getAllActiveReservations();
        assertTrue(activeReservations.size() > 0, "There should be at least one active reservation in the database");
    }

    // Test to check if reservation can be found by its ID
    @Test
    void checkGetReservationById() throws SQLException {
        testReservation.saveToDatabase();
        Reservation foundReservation = Reservation.getById(testReservation.getId());
        assertNotNull(foundReservation, "Reservation should be found by ID");
        assertEquals(testReservation.getId(), foundReservation.getId(), "Found reservation ID should match the test reservation ID");
    }
    
     // Test to check if reservation can be found by its ID
     @Test
     void checkReservationWithWrongId() throws SQLException {
         testReservation.saveToDatabase();
         Reservation notFound = Reservation.getById("wrong id");
         assertNull(notFound);
     }

    @Test
    void getSetters() {
        testReservation.setExpectedNumberOfDays(4);
        var datenow = LocalDateTime.now().plusDays(20);
        testReservation.setReservedDate(datenow);
        testReservation.getReservedDate();
        assertEquals(datenow, testReservation.getReservedDate());
    }
}
