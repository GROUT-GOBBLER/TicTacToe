module client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    opens client to javafx.fxml;
    opens client.Controllers to javafx.fxml;

    exports client;
}
