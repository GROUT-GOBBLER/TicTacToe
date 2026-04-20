package client.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/*
    William Vipperman, Yarrick Dillard
    CS1760 - Adv Obj OOP & Design
    Final Project
    ErrorPopupController.java
*/

public class ErrorPopupController {
    // FXML Variables.
    @FXML TextArea error_message_textarea;

    // FXML Methods.
    @FXML private void closeButtonPressed() {
        Stage stage = (Stage) error_message_textarea.getScene().getWindow();
        stage.close();
    }

    // Other methods.
    public void initializeData(String msg) {
        error_message_textarea.setText(msg);
    }
}
