package library.modelsTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import library.utils.BorrowingQue.BorrowingQue;

class BorrowingQueTest {

    // Test data
    private final String resourceId = "resource1";
    private final String reservationId1 = "reservation1";
    private final String reservationId2 = "reservation2";

    BorrowingQue bt = new BorrowingQue();

    @BeforeEach
    void setUp() {
        // Clear the static borrowQue map before each test
        BorrowingQue.borrowQue.clear();
    }

    @Test
    void testAddToQue_QueueDoesNotExist() {
        // Arrange
        // Initial state: no queue exists for the resource.

        // Act
        BorrowingQue.addToQue(resourceId, reservationId1);

        // Assert
        assertTrue(BorrowingQue.borrowQue.containsKey(resourceId));  // Ensure the queue was created
        assertEquals(1, BorrowingQue.borrowQue.get(resourceId).size());  // Ensure the reservation was added
        assertEquals(reservationId1, BorrowingQue.borrowQue.get(resourceId).peek());  // Ensure the correct reservation is at the front
    }

    @Test
    void testAddToQue_QueueExists() {
        // Arrange
        BorrowingQue.addToQue(resourceId, reservationId1);

        // Act
        BorrowingQue.addToQue(resourceId, reservationId2);

        // Assert
        assertTrue(BorrowingQue.borrowQue.containsKey(resourceId));  // Ensure the queue still exists
        assertEquals(2, BorrowingQue.borrowQue.get(resourceId).size());  // Ensure the new reservation is added
        assertEquals(reservationId1, BorrowingQue.borrowQue.get(resourceId).peek());  // Ensure the first reservation is the same
    }

    @Test
    void testGetFirstPerson_QueueExists() {
        assertNull(BorrowingQue.getFirstPerson(resourceId));
        // Arrange
        BorrowingQue.addToQue(resourceId, reservationId1);
        BorrowingQue.addToQue(resourceId, reservationId2);

        // Act
        String firstReservation = BorrowingQue.getFirstPerson(resourceId);

        // Assert
        assertEquals(reservationId1, firstReservation);  // Ensure the first reservation is returned
        assertEquals(1, BorrowingQue.borrowQue.get(resourceId).size());  // Ensure the reservation was removed
    }

    @Test
    void testGetFirstPerson_QueueIsEmpty() {
        // Arrange
        // No reservations are added to the queue.

        // Act
        String firstReservation = BorrowingQue.getFirstPerson(resourceId);

        // Assert
        assertNull(firstReservation);  // Ensure null is returned when the queue is empty
    }

    @Test
    void testGetFirstPerson_QueueDoesNotExist() {
       
        // Act
        String firstReservation = BorrowingQue.getFirstPerson(resourceId);

        // Assert
        assertNull(firstReservation);  // Ensure null is returned when no queue exists
    }


}
