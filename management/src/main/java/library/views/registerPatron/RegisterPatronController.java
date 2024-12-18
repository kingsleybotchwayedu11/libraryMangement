package library.views.registerPatron;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import library.App;
import library.controllers.StatusReport;
import library.controllers.UserController;
import library.model.Users.Patron;

public class RegisterPatronController  {
    //Patron Details
    @FXML
    TextField name;
    @FXML
    TextField email;
    @FXML
    TextField telephone;
    @FXML
    TextArea address;
    @FXML
    TextArea report;

    //Button
    @FXML
    Button register;
    @FXML
    Button clear;


    static Stage registerPatronWindow = new Stage();
    public static void showRegisterPatronStage() throws Exception {
    registerPatronWindow.setTitle("Register Patron");
    Scene registerPatronSView = new Scene(App.loadFXML("registerPatron"), 720, 480);
    registerPatronWindow.setScene(registerPatronSView);
    registerPatronWindow.show();
    registerPatronWindow.toFront();
    }
    private boolean validate() {
        //check for username
        if(name.getText().isEmpty()) {
            report.setText("name is required");
            report.setVisible(true);
            return false;
        }
        if(name.getText().length() > 180) {
            report.setText("name cant be more than 180 characters");
            return false;
        }
        //valdiate email
       if(!(email.getText() != null && email.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))) {
            report.setText("Provide a valid email");
            report.setVisible(true);
        return false;
        }
    
        //check for telephon number
        if(!(!telephone.getText().isEmpty() && telephone.getText().matches("-?\\d+")))
            return true;
        if(address.getText().isEmpty()){
            report.setText("provide a valide email");
            report.setVisible(true);
            return false;
        }
        return true;
    }

    @FXML
    private void saveUser() throws SQLException {
        if(validate()){
            StatusReport dbReport = UserController.registerPatron(name.getText(), address.getText(), email.getText(), telephone.getText());
            String message;
            if(dbReport.getOperationStatus()) {
                Patron patron = (Patron) dbReport.getOperationEntity();
                message = "Operation successfull \n\n";
                message += "Member Id Number:\t" + patron.getLibraryCardId();
            } else {
                message = "\tOperation Failed \n\n";
                message += dbReport.getMessage();
            }
            report.setText(message);
            report.setVisible(true);

    }
}

    @FXML
    private void clearInput() {
        name.setText("");
        email.setText("");
        address.setText("");
        telephone.setText("");
        report.setText("");
        report.setVisible(false);
    }

}
