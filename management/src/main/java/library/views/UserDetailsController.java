package library.views;

import java.util.List;

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
    private TextArea address;

    @FXML
    private ChoiceBox<String> attribute;

    @FXML
    private TextField email;

    @FXML
    private TextField memberId;

    @FXML
    private TextField name;

    @FXML
    private TextField report;

    @FXML
    private Button search;

    @FXML
    private TextField telephone;

    @FXML
    private AnchorPane formArea;

    @FXML
    private TextField value;

    @FXML
    private AnchorPane user;

    @FXML
    void searchUser() {
        if(validate()) {
            String columnName = getDatabaseColumnName();
            List<Patron> patrons = Patron.findByAttribute(columnName, value.getText());
            if(patrons.size() == 0){
                report.setText("No user Found");
                report.setVisible(true);
                user.setVisible(false);
            }else {
                displayUserDetails(patrons.get(0));
                user.setVisible(true);
                report.setVisible(false);
            }
        }
    }
    private void displayUserDetails(Patron patron) {
        name.setText(patron.getName());
        email.setText(patron.getEmail());
        telephone.setText(patron.getPhoneNumber());
        memberId.setText(patron.getLibraryCardId());
        address.setText(patron.getAddress());
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
