package library.views;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import library.App;
import library.utils.sessions.LibrarianSession;
public class HomeController {
    @FXML
    private void logOut() throws Exception {
        LibrarianSession.setLibrarian(null);
        App.setRoot("login");
    }
}
