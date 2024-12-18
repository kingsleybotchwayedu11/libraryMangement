package library.views;


import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import library.App;
import library.controllers.StatusReport;
import library.controllers.TransactionController;
import javafx.stage.Stage;

public class CheckInView {
    //setup the stage to view
    private static Stage checkinStage = new Stage();

    @FXML
    private TextField transactionId;

    @FXML
    private TextField report;


    /**
     * Validates the input fields.
     * 
     * @return true if all inputs are valid, otherwise false.
     */
    private boolean validate() {
        return !(transactionId.getText() == null || transactionId.getText().trim().isEmpty());
    }
    
    
    /**
     * Handles the submission of the check-in operation.
     * 
     * @param event the triggered event
     */
    @FXML
    void onSubmit() throws SQLException {
        if (validate()) {
            // Process the check-in operation
            StatusReport transactionReport = TransactionController.returnResource(transactionId.getText());
            if (transactionReport.getOperationStatus()) {
                report.setText("Check-in successful!");
            } else {
                report.setText("Check-in failed: " + transactionReport.getMessage());
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
    void clear() {
        report.setText("");
        transactionId.setText("");
        report.setVisible(false);
    }
    
    public static void displayStage() throws Exception {
        Scene checkInStage = new Scene(App.loadFXML("checkIn"));
        checkinStage.setTitle("Return a resource");
        checkinStage.setScene(checkInStage);
        checkinStage.show();
    }

    @FXML
    void  viewTransacions() {}

}
