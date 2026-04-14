package client.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

/*
    William Vipperman, Yarrick Dillard
    CS1760 - Adv Obj OOP & Design
    Final Project
    LeaderboardPageController.java
*/

public class LeaderboardPageController {
    // FXML Variables.
    @FXML TableView<?> leaderboard_table;
    @FXML TableColumn<?, ?> player_column, wins_column, losses_column, win_loss_ratio_column;

    // FXML Methods.
    @FXML private void initialize() {
        // Populate leaderboard_table with values.
    }

    // Other methods.
    public void initializeData() {
        System.out.println("initalizeData()");
    }
}