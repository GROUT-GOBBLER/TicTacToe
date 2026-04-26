package client.Controllers;

import java.io.*;
import java.net.Socket;
import client.App;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

/*
    William Vipperman, Yarrick Dillard
    CS1760 - Adv Obj OOP & Design
    Final Project
    GameOverPageController.java
*/

public class GameOverPageController {
    // FXML variables.
    @FXML Button button_A1, button_A2, button_A3, button_B1, button_B2, button_B3, button_C1, button_C2, button_C3;
    @FXML Label you_won_lost_label;

    // Instance variables.
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private String username = "";
    private String[][] ending_board = {
        {"", "", ""},
        {"", "", ""},
        {"", "", ""}
    };

    // FXML methods.
    @FXML private void onReturnToMainMenuButtonPressed() {
        System.out.println("onReturnToMainMenuButtonPressed()");

        try {
            // Inform server1.
            output.writeUTF("Back to main menu.");
            output.flush();

            // Load main menu screen.
            FXMLLoader loader = new FXMLLoader(App.class.getResource("main-menu.fxml"));
            Parent root = loader.load();

            MainMenuController mm_controller = loader.getController();
            mm_controller.initializeData(username, socket, input, output);

            Stage stage = (Stage) button_A1.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Main menu");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Other methods.
    public void initializeData(Socket socket, ObjectInputStream in, ObjectOutputStream out, String x_or_o, String name) {
        this.socket = socket;
        input = in;
        output = out;
        username = name;

        try {
            // Get win/lose.
            output.writeUTF("Did I win?~" + x_or_o);
            output.flush();

            boolean alive = true;
            while (alive) {
                try { 
                    String win_status = input.readUTF(); 

                    if (win_status.equals("won") || win_status.equals("lost") || win_status.equals("draw")) {
                        you_won_lost_label.setText("You " + win_status + "!");
                        alive = false;
                    }
                }
                catch(Exception e) {}
            }

            // Get end board.
            output.writeUTF("Give me the ending board.");
            output.flush();

            alive = true; 
            while (alive) {
                try {
                    String ending_board_str = input.readUTF();

                    if (ending_board_str.contains("X") || ending_board_str.contains("O") || ending_board_str.contains(" ")) {
                        setEndingBoard(ending_board_str);
                        alive = false;
                    }
                }
                catch (Exception e) {}
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEndingBoard(String end_board) {     
        // Expected string format: XO XOX OX
        // ^ Means A1=X, A2=O, A3=Nothing, A4=X, etc.

        // Grab the value at each place.
        char A1_value = end_board.charAt(0);
        char A2_value = end_board.charAt(1);
        char A3_value = end_board.charAt(2);
        char B1_value = end_board.charAt(3);
        char B2_value = end_board.charAt(4);
        char B3_value = end_board.charAt(5);
        char C1_value = end_board.charAt(6);
        char C2_value = end_board.charAt(7);
        char C3_value = end_board.charAt(8);
        
        // A1, B1, C1  ::: [0][0]  [0][1]  [0][2]
        // A2, B2, C2  ::: [1][0]  [1][1]  [1][2]
        // A3, B3, C3  ::: [2][0]  [2][1]  [2][2]

        // Insert the values into the 'ending_board' array.
        ending_board[0][0] = A1_value + "";
        ending_board[1][0] = A2_value + "";
        ending_board[2][0] = A3_value + "";
        ending_board[0][1] = B1_value + "";
        ending_board[1][1] = B2_value + "";
        ending_board[2][1] = B3_value + "";
        ending_board[0][2] = C1_value + "";
        ending_board[1][2] = C2_value + "";
        ending_board[2][2] = C3_value + "";

        // Set each button with its corresponding value.
        button_A1.setText(ending_board[0][0]);
        button_A2.setText(ending_board[1][0]);
        button_A3.setText(ending_board[2][0]);
        button_B1.setText(ending_board[0][1]);
        button_B2.setText(ending_board[1][1]);
        button_B3.setText(ending_board[2][1]);
        button_C1.setText(ending_board[0][2]);
        button_C2.setText(ending_board[1][2]);
        button_C3.setText(ending_board[2][2]);
    }
}
