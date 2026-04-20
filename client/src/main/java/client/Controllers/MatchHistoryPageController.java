package client.Controllers;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.fxml.FXML;
import javafx.scene.control.*;

/*
    William Vipperman, Yarrick Dillard
    CS1760 - Adv Obj OOP & Design
    Final Project
    MatchHistoryPageController.java
*/

public class MatchHistoryPageController {
    // Client Variables
    Socket socket;
    ObjectInputStream input;
    ObjectOutputStream output;
    
    // FXML Variables.
    @FXML TableView match_history_table;
    @FXML TableColumn date_column, time_column, opponent_column, result_column;

    // FXML Methods.
    @FXML private void initialize() {
        // Populate match_history_table with values.
    }

    // Other methods.
    public void initializeData(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        System.out.println("initalizeData()");
        this.socket = socket;
        input = in;
        output = out;
    }
}