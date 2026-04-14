package client;

import javafx.fxml.*;
import javafx.scene.control.*;

/*
    William Vipperman, Yarrick Dillard
    CS1760 - Adv Obj OOP & Design
    Final Project
    PlayScreenController.java
*/

public class PlayScreenController {
    // FXML Variables.
    @FXML Button submit_button;
    @FXML Button button_A1, button_A2, button_A3, button_B1, button_B2, button_B3, button_C1, button_C2, button_C3;
    @FXML Label you_are_label;

    // Constructors.
    public PlayScreenController() {
    }

    // FXML Methods.
    @FXML public void initialize() {
    }

    @FXML private void playButtonPressed() {
        System.out.println("playButtonPressed()");
    }

    @FXML private void submitButtonPressed() {
        System.out.println("submitButtonPressed()");
    }

    // Other methods.
    public void initializeData() {
    }
}
