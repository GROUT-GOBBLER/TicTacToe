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
    HashMap<Date, String> gameHashMap = null;
    ObservableList<MatchDataRow> data = FXCollections.observableArrayList();
    
    // FXML Variables.
    @FXML TableView<MatchDataRow> match_history_table;
    @FXML TableColumn<MatchDataRow, String> date_column, time_column, opponent_column, result_column;

    // FXML Methods.
    @FXML private void initialize() {
        date_column.setCellValueFactory(new PropertyValueFactory<>("date"));
        time_column.setCellValueFactory(new PropertyValueFactory<>("time"));
        opponent_column.setCellValueFactory(new PropertyValueFactory<>("playedWith"));
        result_column.setCellValueFactory(new PropertyValueFactory<>("result"));
    }

    // Other methods.
    @SuppressWarnings("unchecked")
    public void initializeData(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        System.out.println("initalizeData()");
        this.socket = socket;
        input = in;
        output = out;

        try {
            output.writeUTF("Give my history");
            output.flush();

            gameHashMap = (HashMap<Date, String>) input.readObject();

            match_history_table.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}