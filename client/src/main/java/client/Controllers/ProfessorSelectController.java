package client.Controllers;

import java.io.*;
import java.net.Socket;
import client.App;
import javafx.event.Event;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

/*
    William Vipperman, Yarrick Dillard
    CS1760 - Adv Obj OOP & Design
    Final Project
    ProfessorSelectController.java
*/

public class ProfessorSelectController {
    // FXML variables.
    @FXML Button easy, medium, hard, extreme;

    // Other variables.
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String username;

    private Button button_selected = new Button();  // keeps track of the most recently selected button by the user.

    // FXML methods.
    @FXML private void backButtonPressed() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("main-menu.fxml"));
            Parent root = loader.load();

            MainMenuController mm_controller = loader.getController();
            mm_controller.initializeData(username, socket, input, output);

            Stage stage = (Stage) easy.getScene().getWindow();
            stage.setResizable(false);
            stage.setTitle("Main menu");
            stage.setScene(new Scene(root));
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void difficultyButtonPressed(Event event) {
        // Get currently selected button.
        button_selected = (Button) event.getSource();

        // Load new page.
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("single-player-tictactoe-board.fxml"));
            Parent root = loader.load();

            SinglePlayerTictactoeBoardController sptttbc_controller = loader.getController();
            sptttbc_controller.initializeData(socket, input, output, button_selected.getId(), username);

            Stage stage = (Stage) easy.getScene().getWindow();
            stage.setResizable(false);
            stage.setTitle("Main menu");
            stage.setScene(new Scene(root));
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    // Other methods.
    public void initializeData(String name, Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        // Set variables.
        username = name;
        this.socket = socket;
        input = in;
        output = out;

        System.out.println("\n\nProfessorSelectController.java\tName = " + username + "\n\n");
    }
}
