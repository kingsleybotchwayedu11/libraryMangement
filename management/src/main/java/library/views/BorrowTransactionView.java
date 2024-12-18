
package library.views;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import library.App;
import library.model.LibraryResource.Book;
import library.model.LibraryTransactions.BorrowTransaction;
import library.utils.sessions.LibrarianSession;
import javafx.scene.control.TableView;

public class BorrowTransactionView {
    public static Stage transactionStage = new Stage();

    @FXML
    private ChoiceBox<String> attribute;

    @FXML
    private TableColumn<BorrowTransaction, String> borrowedDate;

    @FXML
    private TableColumn<BorrowTransaction, String> expectedDate;

    @FXML
    private AnchorPane formArea;

    @FXML
    private TableColumn<BorrowTransaction, String> librarianId;

    @FXML
    private TableColumn<BorrowTransaction, String> patronId;

    @FXML
    private TextField query;

    @FXML
    private TableColumn<BorrowTransaction, String> resourceId;
    @FXML
    private TableView<BorrowTransaction> table;

    @FXML
    private Button search;

    @FXML
    private TableColumn<BorrowTransaction, String> status;

    @FXML
    void search(ActionEvent event) throws SQLException {
        System.out.println("ok");
        if(attribute.getValue().equals("All") || attribute.getValue().equals("Overdue") || !query.getText().isEmpty()) {
            List<BorrowTransaction> transactions = new ArrayList<>();
            if(attribute.getValue().equals("Overdue")) {
                transactions = BorrowTransaction.getAllOverdue();
            } else if(attribute.getValue().equals("MemberId")) {
                transactions = BorrowTransaction.getUser(query.getText());
            } else if(attribute.getValue().equals("All")) {
                transactions = BorrowTransaction.getAll();
            }else if(attribute.getValue().equals("ReourceId")) {
                transactions = BorrowTransaction.getResource(query.getText());
            } else {
                transactions.add(BorrowTransaction.getById(query.getText()));
            }
            table.setItems(FXCollections.observableArrayList(transactions));
        } 
    }

     static void  displayStage() throws Exception {
        Scene borrowScene  = new Scene(App.loadFXML("borrowTransaction"));
        transactionStage.setTitle("View  Borrow Transactions");
        transactionStage.setScene(borrowScene);
        transactionStage.show();
    }

    @FXML
    void initialize() throws SQLException {
        attribute.getItems().addAll("MemberId", "ReourceId", "All", "OverDue");
        attribute.setValue("All");
        //give drop shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.GRAY);
        dropShadow.setRadius(10);
        dropShadow.setOffsetY(10);
        formArea.setEffect(dropShadow);

        //book columns
        status.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        resourceId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBorrowedItem()));
        borrowedDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBorrowedDate().toString()));
        librarianId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIssuedLibrarian()));
        patronId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBorrower()));
        expectedDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExpectedReturnDate().toString()));

        //get colulnst
        table.setItems(FXCollections.observableArrayList(BorrowTransaction.getAll()));
    }
}
