package library.views;

import java.time.LocalDateTime;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import library.App;
import library.controllers.StatusReport;
import library.controllers.TransactionController;
import library.model.LibraryTransactions.BorrowTransaction;
import library.utils.sessions.LibrarianSession;
import javafx.stage.*;

public class BorrowController {

    @FXML
    private TextField borrowResource;

    @FXML
    private Button close;

    @FXML
    private DatePicker expectedReturnDate;

    @FXML
    private TextField patron;

    @FXML
    private TextArea repport;

    @FXML
    private Button submit;

    @FXML
    private Button viewTransactionButton;

    @FXML
    private static Stage borrowTransactionStage = new Stage();

    private Boolean validate() {
    // Check if the 'patron' field is empty
    if (patron.getText() == null || patron.getText().trim().isEmpty()) {
        repport.setText("Patron Member Id is requried");
        repport.setVisible(true);
        return false;
    }
    
    // Check if the 'borrowResource' field is empty
    if (borrowResource.getText() == null || borrowResource.getText().trim().isEmpty()) {
        repport.setText("Resource Id is required");
        repport.setVisible(true);
        return false;
    }
    
    // Check if the 'expectedReturnDate' is not selected
    if (expectedReturnDate.getValue() == null) {
        repport.setText("Expected Return Date is required");
        repport.setVisible(true);
        return false;
    }

    // If all fields are valid
    return true;
}

    static void  displayStage() throws Exception {
        Scene borrowScene  = new Scene(App.loadFXML("borrowResource"));
        borrowTransactionStage.setTitle("Borrow A Resource");
        borrowTransactionStage.setScene(borrowScene);
        borrowTransactionStage.show();
    }

    @FXML
    void add(ActionEvent event) {
        if(validate()) {
            LocalDateTime expectedReturn = expectedReturnDate.getValue().atStartOfDay();
            StatusReport transactionReppReport = TransactionController.borrowResource(borrowResource.getText(), patron.getText(), LibrarianSession.getLoggedInLibrarian(), expectedReturn);
            if(transactionReppReport.getOperationStatus()) {
                BorrowTransaction currentTransaction = (BorrowTransaction) transactionReppReport.getOperationEntity();
                repport.setText("Operation Successfull\nId:\t" + currentTransaction.getId());
                repport.setVisible(true);
            } else {
                repport.setText(transactionReppReport.getMessage());
                repport.setVisible(true);
            }
        }
    }

    @FXML
    void clearInput(ActionEvent event) {
        patron.setText("");
        borrowResource.setText("");
        repport.setText("");
        repport.setVisible(false);
    }

    @FXML
    void viewTransactions(ActionEvent event) {

    }

}
