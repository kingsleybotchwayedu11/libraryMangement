module library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens library to javafx.fxml;
    opens library.views to javafx.fxml;
    opens library.views.registerPatron to javafx.fxml;
    exports library;
}
