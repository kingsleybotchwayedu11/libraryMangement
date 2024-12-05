package library.views;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import library.App;
import library.controllers.StatusReport;
import library.controllers.UserController;
import library.model.Users.Librarian;
import library.utils.sessions.LibrarianSession;

public class LoginController {
    @FXML
    TextField username;
    @FXML
    TextField password;
    @FXML
    TextArea errorReport;
    

    private boolean validateInput() {
        //ensure user has given a userName
        //ensure the user has given password and it must be less than 10
        if(username.getText().isEmpty()) {
            errorReport.setText("Username must be provided");
            errorReport.setVisible(true);
            return false;
        }

        if(password.getText().isEmpty())
        {
            errorReport.setText("Password filed is required");
            errorReport.setVisible(true);
            return false;
        }
        return true;
    }
    @FXML
    private void loginAction() throws Exception{
        System.out.println("Working");
        boolean inputValidate = validateInput();
        if(inputValidate) {
            StatusReport loginReport = UserController.loginLibarian(username.getText(), password.getText());
            if(loginReport.getOperationStatus()) {
                //no error report switch scene to login
                //set librarain session
                LibrarianSession.setLibrarian((Librarian) loginReport.getOperationEntity());
                //switch scene to  home screen
                App.setRoot("home");
            } else {
                errorReport.setText(loginReport.getMessage());
                errorReport.setVisible(inputValidate);
            }

        }
    }
}
