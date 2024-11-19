package library.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import library.App;
import library.controllers.BooksController;
import library.controllers.StatusReport;
import library.model.LibraryResource.*;
import javafx.scene.Scene;

public class AddBookController {

    int toalCop;
    @FXML
    private Button Add;
    

    @FXML
    private ImageView bookPic;

    @FXML
    private Button clear;

    @FXML
    private TextField author;

    @FXML
    private TextField locationBook;

    @FXML
    private TextArea report;

    @FXML
    private TextField title;

    @FXML
    private TextField totalCoppies;

    @FXML
    private ChoiceBox<String> genre;

    private static Stage addBookStage  = new Stage();
    
    static void displayStage() throws Exception {
        Scene addBookScene = new Scene(App.loadFXML("addBook"), 720, 600);
        addBookStage.setTitle("Add Book Stage");
        addBookStage.setScene(addBookScene);
        addBookStage.show();
    }

    @FXML
    void addBook() {
        if(validate()) {
            
            StatusReport bookReport = BooksController.uploadBook(title.getText(),locationBook.getText(), toalCop, author.getText(), genre.getSelectionModel().getSelectedItem());
            if(bookReport.getOperationStatus()) {
                Book book = (Book) bookReport.getOperationEntity();
                report.setText("Resource Saved Successfully\nId :" + book.getId());
                report.setVisible(true);
            }else {
            report.setText(bookReport.getMessage());
            report.setVisible(true);
            }
        }
    }

    @FXML
    void clearInput(ActionEvent event) {
        report.setText("");
        title.setText("");
        totalCoppies.setText("");
        locationBook.setText("");
        genre.setValue("");
        author.setText("");
        report.setVisible(false);
    }

    @FXML
    void initialize() {
         DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.GRAY);
        dropShadow.setRadius(10);
        dropShadow.setOffsetY(10);
        bookPic.setEffect(dropShadow);
        genre.getItems().addAll("Comedy", "Sci-fi", "Fiction", "Non-Fiction", "Mystery", "Fantasy", "Crime");
    }

    private boolean validate() {
        //check for username
        if(title.getText().isEmpty()) {
            report.setText("name is required");
            report.setVisible(true);
            return false;
        }
        //valdiate author
        if(author.getText().isEmpty()) {
            report.setText("Author is required");
            report.setVisible(true);
            return false;
        }
        if(locationBook.getText().isEmpty()) {
            report.setText("locationBook is required");
            report.setVisible(true);
            return false;
        }
        
        if(genre.getSelectionModel().getSelectedItem() == null) {
            report.setText("Please select Genre of book");
                report.setVisible(true);
                return false;
        }
        //check for toatal number of coppies number
        try {
            if(totalCoppies.getText().isEmpty()){
                report.setText("total coppies of book is required");
                report.setVisible(true);
                return false;
            }
            toalCop = Integer.parseInt(totalCoppies.getText());
            if(toalCop < 1) {
                report.setText("Total coppies of books cant be less than 1");
                report.setVisible(true);
                return false;
            } else {
                return true;
            }
        } catch(NumberFormatException ex) {
            report.setText("Only Numberic Postive values are required");
            report.setVisible(true);
            return false;
        }
    }

}
