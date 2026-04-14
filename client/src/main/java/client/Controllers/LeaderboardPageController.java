package client.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class LeaderboardPageController {
    // FXML Variables.
    @FXML TableView<?> leaderboard_table;
    @FXML TableColumn<?, ?> player_column, wins_column, losses_column, win_loss_ratio_column;

    // FXML Methods.
    @FXML private void joinGameButtonPressed() {
        System.out.println("joinGameButtonPressed()");
    }

}
