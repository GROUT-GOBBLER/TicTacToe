package client.Controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import client.App;
import javafx.application.Platform;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.control.*;
import javafx.stage.Stage;

/*
    William Vipperman, Yarrick Dillard
    CS1760 - Adv Obj OOP & Design
    Final Project
    MainMenuController.java
*/

public class MainMenuController {
    // Client Variables
    String client_name;
    Socket socket;
    ObjectInputStream input;
    ObjectOutputStream output;

    // FXML Variables.
    @FXML Label welcome_name_label, current_connected_label;
    @FXML Button play_button;

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

            stage.getIcons().add(
                new Image(getClass().getResourceAsStream("/client/Icon.png"))
            );

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

            stage.getIcons().add(
                new Image(getClass().getResourceAsStream("/client/Icon.png"))
            );

            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void singleplayerGameButtonPressed() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("professor-select-page.fxml"));
            Parent root = loader.load();

            ProfessorSelectController ps_controller = loader.getController();
            ps_controller.initializeData(client_name, socket, input, output);

            Stage stage = (Stage) welcome_name_label.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Professor Select");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private void multiplayerGameButtonPressed() {
        current_connected_label.setText("Waiting for other player...");
        play_button.setDisable(true);

        try {
            output.writeUTF("How many connected?");
            output.flush();
            
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        boolean active = true;
                        while (active) {
                            String response = input.readUTF();

                            if (response.equals("1")) {
                                output.writeUTF("I am connected.");
                                output.flush();
                            }
                            if (response.equals("2")) { active = false; }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    Platform.runLater(() -> {
                        openPlayScreen();
                    });
                }
            }).start();   
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Other methods.
    public void initializeData(String name, Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        welcome_name_label.setText("Welcome " + name + "!");

        client_name = name;
        this.socket = socket;
        input = in;
        output = out;
    }

    private void openPlayScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("tic-tac-toe-basic-board.fxml"));
            Parent root = loader.load();

            PlayScreenController ps_controller = loader.getController();
            ps_controller.initializeData(client_name, socket, input, output);

            Stage stage = (Stage) welcome_name_label.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Tic-Tac-Toe");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
