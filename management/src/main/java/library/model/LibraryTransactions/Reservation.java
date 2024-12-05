package library.model.LibraryTransactions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.time.LocalDateTime;
import library.utils.databaseOperations.DatabaseConnection;
import java.util.ArrayList;
import java.util.Arrays;

public class Reservation extends LibraryTransaction {
    private String bookId;
    private String patronId;
    private LocalDateTime reservedDate;
    private int expectedNumberOfDays;
    
    public Reservation(String id, String bookId, String patronId, LocalDateTime reservedDate, int expectedNumberOfDays) {
        super(id, "Reservation");
        this.bookId = bookId;
        this.patronId = patronId;
        this.reservedDate = reservedDate;
        this.expectedNumberOfDays = expectedNumberOfDays;
    }

    // Getters and Setters
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public LocalDateTime getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(LocalDateTime reservedDate) {
        this.reservedDate = reservedDate;
    }

    public int getExpectedNumberOfDays() {
        return expectedNumberOfDays;
    }

    public void setExpectedNumberOfDays(int expectedNumberOfDays) {
        this.expectedNumberOfDays = expectedNumberOfDays;
    }

    // Helper method to create a Reservation object from a ResultSet
    private static Reservation formReservationObject(ResultSet rs) throws SQLException {
        return new Reservation(
            rs.getString("id"), // Reservation ID
            rs.getString("bookId"), // Book ID
            rs.getString("patronId"), // Patron ID
            rs.getTimestamp("reservedDate").toLocalDateTime(), // Reserved Date
            rs.getInt("expectedNumberOfDays") // Expected Number of Days
        );
    }

    @Override
    public boolean saveToDatabase() {
        try {
            PreparedStatement stmt;
            boolean transactionExists = this.checkIfTransactionExists();
            
            if (transactionExists) {
                // Update existing reservation
                String updateSQL = "UPDATE Reservation SET bookId = ?, patronId = ?, reservedDate = ?, expectedNumberOfDays = ? WHERE id = ?";
                stmt = this.getConnection().prepareStatement(updateSQL);
                stmt.setString(1, this.bookId);
                stmt.setString(2, this.patronId);
                stmt.setTimestamp(3, Timestamp.valueOf(this.reservedDate));
                stmt.setInt(4, this.expectedNumberOfDays);
                stmt.setString(5, this.id);
            } else {
                // Insert new reservation
                String insertSQL = "INSERT INTO Reservation (id, bookId, patronId, reservedDate, expectedNumberOfDays) VALUES (?, ?, ?, ?, ?)";
                stmt = this.getConnection().prepareStatement(insertSQL);
                stmt.setString(1, this.id);
                stmt.setString(2, this.bookId);
                stmt.setString(3, this.patronId);
                stmt.setTimestamp(4, Timestamp.valueOf(this.reservedDate));
                stmt.setInt(5, this.expectedNumberOfDays);
            }

            // Execute the prepared statement
            int affectedRows = stmt.executeUpdate();
            return affectedRows >= 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } catch(Exception ex){
            return false;
        }
    }

    // Method to get all active reservations
    public static List<Reservation> getAllActiveReservations() {
        List<Reservation> activeReservations = new ArrayList<>();
        try {
            String query = "SELECT * FROM Reservation WHERE reservedDate > NOW()";
            PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                activeReservations.add(formReservationObject(resultSet));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return activeReservations;
    }

    // Method to get a reservation by ID
    public static Reservation getById(String id) {
        try {
            String query = "SELECT * FROM Reservation WHERE id = ?";
            PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(query);
            stmt.setString(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return formReservationObject(resultSet);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //Reservation
    public static List<Reservation> findByAttribute(String attribute, String value) {
        // Define a list of accepted attributes that can be searched in the database
        List<String> acceptedAttributes = Arrays.asList("id", "bookId", "patronId", "reservedDate");

        // Check if the provided attribute is valid
        if (!acceptedAttributes.contains(attribute)) {
            System.out.println("Invalid attribute: " + attribute);
            return null;  // Return null or handle error as needed
        }

        // Initialize a list to hold the matching reservations
        List<Reservation> reservations = new ArrayList<>();

        try {
            // Construct the SQL query using the provided attribute and value
            String selectQuery = "SELECT * FROM Reservation WHERE " + attribute + " LIKE ?";
            PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(selectQuery);
            stmt.setString(1, "%" + value + "%");  // Use LIKE to allow partial matches

            // Execute the query and process the result set
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                // Create a Reservation object from the result set
                Reservation reservation = formReservationObject(resultSet);
                // Add the reservation to the list
                reservations.add(reservation);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();  // Handle SQL errors
        } catch (Exception ex) {
            ex.printStackTrace();  // Handle other exceptions
        }

        // Return the list of reservations found
        return reservations;
    }


}
