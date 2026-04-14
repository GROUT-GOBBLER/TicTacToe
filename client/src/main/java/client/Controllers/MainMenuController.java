package client.Controllers;

import java.io.IOException;

import client.App;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/*
    William Vipperman, Yarrick Dillard
    CS1760 - Adv Obj OOP & Design
    Final Project
    MainMenuController.java
*/

public class MainMenuController {
    // FXML Variables.
    @FXML Label welcome_name_label, current_connected_label;

    // FXML Methods.
    @FXML private void matchHistoryButtonPressed() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("match-history-page.fxml"));
            Parent root = loader.load();

            MatchHistoryPageController mhp_controller = loader.getController();
            mhp_controller.initializeData();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void globalLeaderboardButtonPressed() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("leaderboard-page.fxml"));
            Parent root = loader.load();

            LeaderboardPageController lp_controller = loader.getController();
            lp_controller.initializeData();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void joinGameButtonPressed() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("tic-tac-toe-basic-board.fxml"));
            Parent root = loader.load();

            PlayScreenController ps_controller = loader.getController();
            ps_controller.initializeData();

            Stage stage = (Stage) welcome_name_label.getScene().getWindow();
            stage.setScene(new Scene(root));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Other methods.
    public void initializeData(String name) {
        welcome_name_label.setText("Welcome " + name + "!");
    }
}
