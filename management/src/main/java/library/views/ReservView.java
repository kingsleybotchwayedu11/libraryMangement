package library.views;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import library.App;
import library.controllers.StatusReport;
import library.controllers.transactionController.TransactionController;
import javafx.stage.Stage;

public class ReservView {

    private static Stage reserveStage;

    @FXML
    private TextField memberID;

    @FXML
    private TextField report;

    @FXML
    private TextField resourceId;

    @FXML
    private TextField daysOfBorrow;

    /**
     * Validates the input fields.
     * 
     * @return true if all inputs are valid, otherwise false.
     */
    private boolean validate() {
        // Check if memberID is empty
        if (memberID.getText() == null || memberID.getText().trim().isEmpty()) {
            report.setText("Member ID is required.");
            report.setVisible(true);
            return false;
        }

        // Check if resourceId is empty
        if (resourceId.getText() == null || resourceId.getText().trim().isEmpty()) {
            report.setText("Resource ID is required.");
            report.setVisible(true);
            return false;
        }

        // Check if daysOfBorrow is empty or not a valid positive integer
        try {
            int days = Integer.parseInt(daysOfBorrow.getText());
            if (days <= 0) {
                report.setText("Number of days must be greater than 0.");
                report.setVisible(true);
                return false;
            }
        } catch (NumberFormatException e) {
            report.setText("Number of days must be a valid number.");
            report.setVisible(true);
            return false;
        }

        return true;
    }

    /**
     * Handles the submission of the reservation operation.
     * 
     * @param event the triggered event
     */
    @FXML
    void onSubmit(ActionEvent event) throws SQLException {
        if (validate()) {
            int days = Integer.parseInt(daysOfBorrow.getText());
            
            // Process the reservation operation
            StatusReport transactionReport = TransactionController.addReservation(
                resourceId.getText(),
                memberID.getText(),
                days
            );

            if (transactionReport.getOperationStatus()) {
                report.setText("Reservation successful!");
            } else {
                report.setText("Reservation failed: " + transactionReport.getMessage());
            }

            report.setVisible(true);
        }
    }

    /**
     * Clears all input fields and resets the report field.
     * 
     * @param event the triggered event
     */
    @FXML
    void clear(ActionEvent event) {
        memberID.setText("");
        resourceId.setText("");
        daysOfBorrow.setText("");
        report.setText("");
        report.setVisible(false);
    }

    /**
     * Handles the display of reservation transactions.
     * 
     * @param event the triggered event
     */
    @FXML
    void viewTransacions(ActionEvent event) {
        // Implement the logic for viewing transactions if required
    }

    /**
     * Displays the stage for the reservation view.
     * 
     * @throws Exception if loading the FXML fails
     */
    public static void displayStage() throws Exception {
        if(reserveStage == null || !reserveStage.isShowing()) {
        reserveStage =  new Stage();
        Scene reserveScene = new Scene(App.loadFXML("reserve"));
        reserveStage.setTitle("Reserve a Resource");
        reserveStage.setScene(reserveScene);
        reserveStage.show();
    } else {
        reserveStage.toFront();
    }
    }
}
