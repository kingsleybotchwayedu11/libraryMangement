package library.views;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import library.App;
import library.model.Users.Patron;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class UserDetailsController {
    //hanling scenes
    private static Stage userDetalsStage = new Stage();
    public static void displayUserDetailsPage() throws Exception {
        Scene userDetailsScene = new Scene(App.loadFXML("userDetails"), 720, 500);
        userDetalsStage.setTitle("User Details");
        userDetalsStage.setScene(userDetailsScene);
        userDetalsStage.show();
        
    }

   
    
    @FXML
    private TextField report;

    @FXML
    private Button search;


    @FXML
    private TextField value;

    @FXML
    private AnchorPane user;
    @FXML
    private TableColumn<Patron, String> address;

    @FXML
    private ChoiceBox<String> attribute;

    @FXML
    private TableColumn<Patron, String> email;

    @FXML
    private AnchorPane formArea;

    @FXML
    private TableColumn<Patron, String> memberCardId;

    @FXML
    private TableColumn<Patron, String> name;

    @FXML
    private TableView<Patron> table;

    
    @FXML
    private TableColumn<Patron, String> telephone;

    @FXML
    void searchUser() {
        if(validate()) {
            String columnName = getDatabaseColumnName();
            List<Patron> patrons = Patron.findByAttribute(columnName, value.getText());
            if(patrons.size() == 0){
                report.setText("No user Found");
                report.setVisible(true);
                user.setVisible(false);
                table.setVisible(false);
            }else {
                displayUserDetails(patrons);
                report.setVisible(false);
            }
        }
    }
    private void displayUserDetails(List<Patron> patron) {
        table.setItems(FXCollections.observableArrayList(patron));
        table.setVisible(true);
    }
    private boolean validate() {
        String query = value.getText();
        if(query.isEmpty())
        {
            report.setText("Value is required");
            report.setVisible(true);
            return false;
        }
        return true;
    }

    @FXML
    void initialize() {
        attribute.getItems().addAll("Name", "Member Card Id", "Address", "Email", "Telephone");
        attribute.setValue("Member Card Id");
        //give drop shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.GRAY);
        dropShadow.setRadius(10);
        dropShadow.setOffsetY(10);
        formArea.setEffect(dropShadow);
        //for user details
        address.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        memberCardId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLibraryCardId()));
        email.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLibraryCardId()));
        telephone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhoneNumber()));

    }
    private String getDatabaseColumnName() {
        if(attribute.getSelectionModel().getSelectedItem().equals("Member Card Id"))
            return "libraryCardId";
        else if(attribute.getSelectionModel().getSelectedItem().equals("Telephone"))
            return "phoneNumber";
        else 
            return attribute.getSelectionModel().getSelectedItem().toLowerCase();
    }
}
