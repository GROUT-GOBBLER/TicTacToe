package client.Controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
    // Client Variables
    Socket socket;
    ObjectInputStream input;
    ObjectOutputStream output;

    // FXML Variables.
    @FXML Label welcome_name_label, current_connected_label;

    // FXML Methods.
    @FXML private void matchHistoryButtonPressed() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("match-history-page.fxml"));
            Parent root = loader.load();

            MatchHistoryPageController mhp_controller = loader.getController();
            mhp_controller.initializeData(socket, input, output);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Match History");
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
            lp_controller.initializeData(socket, input, output);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Leaderboard");
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
            ps_controller.initializeData(socket, input, output);

            Stage stage = (Stage) welcome_name_label.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tic-Tac-Toe");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Other methods.
    public void initializeData(String name, Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        welcome_name_label.setText("Welcome " + name + "!");
        this.socket = socket;
        input = in;
        output = out;
    }
}
