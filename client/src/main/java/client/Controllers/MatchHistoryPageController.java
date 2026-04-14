package client.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

/*
    William Vipperman, Yarrick Dillard
    CS1760 - Adv Obj OOP & Design
    Final Project
    MatchHistoryPageController.java
*/

public class MatchHistoryPageController {
    // FXML Variables.
    @FXML TableView match_history_table;
    @FXML TableColumn date_column, time_column, opponent_column, result_column;

    // FXML Methods.
    @FXML private void initialize() {
        // Populate match_history_table with values.
    }

    // Other methods.
    public void initializeData() {
        System.out.println("initalizeData()");
    }
}