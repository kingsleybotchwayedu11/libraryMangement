package library.controllerTest;
import org.junit.jupiter.api.Test;

import library.controllers.StatusReport;

import static org.junit.jupiter.api.Assertions.*;

class StatusReportTest {

    @Test
    void testConstructorWithFullParameters() {
        // Arrange
        boolean status = true;
        String message = "Operation Successful";
        Object entity = new Object();

        // Act
        StatusReport report = new StatusReport(status, message, entity);

        // Assert
        assertEquals(status, report.getOperationStatus(), "The operation status should match.");
        assertEquals(message, report.getMessage(), "The message should match.");
        assertEquals(entity, report.getOperationEntity(), "The operation entity should match.");
    }

    @Test
    void testConstructorWithPartialParameters() {
        // Arrange
        boolean status = false;
        String message = "Operation Failed";

        // Act
        StatusReport report = new StatusReport(status, message);

        // Assert
        assertEquals(status, report.getOperationStatus(), "The operation status should match.");
        assertEquals(message, report.getMessage(), "The message should match.");
        assertNull(report.getOperationEntity(), "The operation entity should be null.");
    }

    @Test
    void testGetOperationStatus() {
        // Arrange
        boolean status = true;
        String message = "Status is successful";

        // Act
        StatusReport report = new StatusReport(status, message);

        // Assert
        assertTrue(report.getOperationStatus(), "The operation status should be true.");
    }

    @Test
    void testGetMessage() {
        // Arrange
        boolean status = false;
        String message = "Operation failed";

        // Act
        StatusReport report = new StatusReport(status, message);

        // Assert
        assertEquals("Operation failed", report.getMessage(), "The message should match.");
    }

    @Test
    void testGetOperationEntity() {
        // Arrange
        boolean status = true;
        String message = "Operation successful";
        Object entity = new String("TestEntity");

        // Act
        StatusReport report = new StatusReport(status, message, entity);

        // Assert
        assertEquals(entity, report.getOperationEntity(), "The operation entity should match.");
    }
}
