module client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens client to javafx.fxml;
    opens client.Controllers to javafx.fxml;

    exports client;
}
