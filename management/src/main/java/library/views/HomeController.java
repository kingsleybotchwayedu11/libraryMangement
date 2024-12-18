package library.views;

import java.sql.SQLException;
import java.util.List;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import library.App;
import library.model.LibraryResource.Book;
import library.utils.sessions.LibrarianSession;
import library.views.registerPatron.RegisterPatronController;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
public class HomeController {
    @FXML
    TextField query;
   
    //Book Specifi
    @FXML
    private TableColumn<Book, String> author;

    @FXML
    private TableColumn<Book, String> title;

    @FXML
    private TableColumn<Book, Boolean> status;
    @FXML
    AnchorPane formArea;
    
    
    @FXML
    private TextField error;

    @FXML
    private Label librarianName;

    @FXML
    private TableColumn<Book, String> bookLocation;

    @FXML
    private Button logOutButon;


    @FXML
    private Button registerPatron;

    @FXML
    private TableColumn<Book, String> resourceId;

    @FXML
    private Button search;

    @FXML
    private Button searchBook;

    @FXML
    private TableView<Book> table;

    @FXML
    private TableColumn<Book, Integer> totalAvailable;

    @FXML
    private TableColumn<Book, Integer> totalBorrowed;

    @FXML
    private TableColumn<Book, Integer> totalCopies;



    @FXML
    private void logOut() throws Exception {
        LibrarianSession.setLibrarian(null);
        App.setRoot("libarianLogin");
    }

    


    private void displayBook(List<Book> books) {
        if(books.size() == 0) {
            table.setVisible(false);
            error.setText("no books match query");
            error.setVisible(true);
        } else {
            table.setItems(FXCollections.observableArrayList(books));
            table.setVisible(true);
            error.setText(" ");
            error.setVisible(false);
        }
    }

    @FXML //search the query
    private void searchBook() throws SQLException{
        if(query.getText().isEmpty()) {
            error.setText("Value must be provided");
            error.setVisible(true);
        } else {
            //ensure erro is not visible
            error.setVisible(false);
            List<Book> books = Book.findByAttribute(getDatabaseColumnName(), query.getText());
            if(books.size() == 0) {
                error.setText("Book not found");
                error.setVisible(true);
            } else {
                displayBook(books);
            }
        }
    }
    //show display stage when button is clickec
    @FXML
    private void showRegisterPatronStage() throws Exception {
        RegisterPatronController.showRegisterPatronStage();
    }

    @FXML
    private ChoiceBox<String> attribute;
    @FXML
    private void viewUserDetailsStage()  throws Exception{
        System.out.println("i am working");
        UserDetailsController.displayUserDetailsPage();
    }

    @FXML
    void initialize() {
        attribute.getItems().addAll("Title", "Id", "Genre");
        attribute.setValue("Title");
        //give drop shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.GRAY);
        dropShadow.setRadius(10);
        dropShadow.setOffsetY(10);
        formArea.setEffect(dropShadow);
        librarianName.setText(LibrarianSession.getLoggedInLibrarian().getName().substring(0, 8));

        //book columns
        resourceId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        title.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        author.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
        status.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isAvailable() ? true : false));
        bookLocation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        totalAvailable.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRemainingCopies()).asObject());
        totalBorrowed.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTotalBorrowed()).asObject());
        totalCopies.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTotalCopies()).asObject());
    }

    @FXML
    void showAddBookStage() throws Exception{
        AddBookController.displayStage();
    }

    @FXML
    void viewBorrowStage() throws Exception {
       BorrowController.displayStage();
    }

    @FXML
    void displayCheckInStage() throws Exception{
        CheckInView.displayStage();
    }

    @FXML
    void showReserveStage() throws Exception {
        ReservView.displayStage();
    }
    private String getDatabaseColumnName() {
            return attribute.getSelectionModel().getSelectedItem().toLowerCase();
    }
}
