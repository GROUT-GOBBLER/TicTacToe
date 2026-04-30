module client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.desktop;

    opens client to javafx.fxml;
    opens client.Controllers to javafx.fxml, javafx.base;

    exports client;
}
