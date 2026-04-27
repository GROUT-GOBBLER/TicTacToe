package client.Controllers;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
    HashMap<Date, String> GameHashMap = null;
    ObservableList<MatchDataRow> data = FXCollections.observableArrayList();
    
    // FXML Variables.
    @FXML TableView<MatchDataRow> match_history_table;
    @FXML TableColumn<MatchDataRow, String> date_column, time_column, opponent_column, result_column;

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