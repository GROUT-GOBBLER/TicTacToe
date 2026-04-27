package client.Controllers;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.*; 
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/*
    William Vipperman, Yarrick Dillard
    CS1760 - Adv Obj OOP & Design
    Final Project
    LeaderboardPageController.java
*/

public class LeaderboardPageController {
    // Client Variables
    Socket socket;
    ObjectInputStream input;
    ObjectOutputStream output;
    HashMap<String, String> playerHashMap = null;
    ObservableList<PlayerStats> data = FXCollections.observableArrayList();

    // FXML Variables.
    @FXML TableView<PlayerStats> leaderboard_table;

    @FXML private TableColumn<PlayerStats, String> player_column;
    @FXML private TableColumn<PlayerStats, Integer> wins_column;
    @FXML private TableColumn<PlayerStats, Integer> losses_column;
    @FXML private TableColumn<PlayerStats, Double> win_loss_ratio_column;


    // FXML Methods.
    @FXML private void initialize() {
        player_column.setCellValueFactory(new PropertyValueFactory<>("player"));
        wins_column.setCellValueFactory(new PropertyValueFactory<>("wins"));
        losses_column.setCellValueFactory(new PropertyValueFactory<>("losses"));
        win_loss_ratio_column.setCellValueFactory(new PropertyValueFactory<>("ratio"));
    }

    // Other methods.
    @SuppressWarnings("unchecked")
    public void initializeData(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        System.out.println("initalizeData()");
        this.socket = socket;
        input = in;
        output = out;

        try {
            output.writeUTF("Give leaderboard");
            output.flush();

            playerHashMap = (HashMap<String, String>) input.readObject();

            for (Map.Entry<String, String> player : playerHashMap.entrySet()) {
                String[] temp = player.getValue().split(", ");

                int w = Integer.parseInt(temp[0]);
                int l = Integer.parseInt(temp[1]);

                data.add(new PlayerStats(player.getKey(), w, l));
            }

            leaderboard_table.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}