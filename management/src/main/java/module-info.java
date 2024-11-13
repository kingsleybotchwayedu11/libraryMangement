module library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens library to javafx.fxml;
    exports library;
}
