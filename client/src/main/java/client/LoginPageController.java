package client;

import javafx.fxml.FXML;
import javafx.scene.control.*;

/*
    William Vipperman, Yarrick Dillard
    CS1760 - Adv Obj OOP & Design
    Final Project
    LoginPageController.java
*/

public class LoginPageController {
    // FXML Variables.
    @FXML Button submit_button;
    @FXML TextField name_textfield, port_number_textfield;

    // FXML Methods.
    @FXML private void submitButtonPressed() {
        System.out.println("submitButtonPressed()");
    }
}
