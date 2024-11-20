package library.views;

import java.util.List;
import javafx.fxml.FXML;
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

public class HomeController {
    @FXML
    TextField query;
    @FXML
    TextField error;
    //Book Specific
    @FXML
    AnchorPane book;
    @FXML 
    TextField resourceId;
    @FXML
    TextField title;
    @FXML
    TextField author;
    @FXML
    TextField totalCoppies;
    @FXML
    TextField shelfLocation;
    @FXML
    TextField borrowed;
    @FXML
    TextField status;
    @FXML
    Label librarianName;
    @FXML
    AnchorPane formArea;
    

    @FXML
    private void logOut() throws Exception {
        LibrarianSession.setLibrarian(null);
        App.setRoot("libarianLogin");
    }

    


    private void displayBook(Book retrievedBook) {
        resourceId.setText(retrievedBook.getId());
        title.setText(retrievedBook.getTitle());
        author.setText(retrievedBook.getAuthor());
        totalCoppies.setText("" + retrievedBook.getTotalCopies());
        shelfLocation.setText(retrievedBook.getLocation());
        borrowed.setText("" + retrievedBook.getTotalBorrowed());
        status.setText(retrievedBook.getTotalBorrowed() < retrievedBook.getTotalCopies() ? "availabe" : "not available");
        book.setVisible(true);
        error.setVisible(false);
    }
    @FXML //search the query
    private void searchBook(){
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
                displayBook(books.get(0));
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
        attribute.getItems().addAll("Title", "Id");
        attribute.setValue("Title");
        //give drop shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.GRAY);
        dropShadow.setRadius(10);
        dropShadow.setOffsetY(10);
        formArea.setEffect(dropShadow);
        librarianName.setText(LibrarianSession.getLoggedInLibrarian().getName().substring(0, 8));

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
