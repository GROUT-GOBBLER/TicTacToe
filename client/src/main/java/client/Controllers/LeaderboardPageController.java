package client.Controllers;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    HashMap<String, Integer> playerHashMap = null;

    // FXML Variables.
    @FXML TableView<?> leaderboard_table;
    @FXML TableColumn<?, ?> player_column, wins_column, losses_column, win_loss_ratio_column;


    // FXML Methods.
    @FXML private void initialize() {
        
    }

    // Other methods.
    public void initializeData(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        System.out.println("initalizeData()");
        this.socket = socket;
        input = in;
        output = out;

        try {
            output.writeUTF("Give leaderboard");
            output.flush();

            playerHashMap = (HashMap<String, Integer>) input.readObject();

            for (Map.Entry<String, Integer> player : playerHashMap.entrySet()) {
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}