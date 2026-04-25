package client.Controllers;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import client.App;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/*
    William Vipperman, Yarrick Dillard
    CS1760 - Adv Obj OOP & Design
    Final Project
    PlayScreenController.java
*/


// CURRENT PROGRESS: 
//  - Clients can communicate in the tic-tac-toe game.
//  - The server knows when the game is won and which person won.
//  - The client who sent the winning move will know that the game is over.
//  - The other client does not. NEED TO FIX THIS.


public class PlayScreenController {
    // Client Variables
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private String x_or_o = "";
    private String user_num = "";
    private Button button_selected = new Button();  // keeps track of the most recently selected button by the user.
    private ArrayList<Button> valid_buttons = new ArrayList<Button>();  // keeps track of which buttons are able to be selected.
    private String opponent_move = "";
    private String username = "";

    private final String DEFAULT_BUTTON_COLOR = "#D4D4D4";
    private final String SELECTED_BUTTON_COLOR = "#ff0000";

    // FXML Variables.
    @FXML Button submit_button;
    @FXML Button button_A1, button_A2, button_A3, button_B1, button_B2, button_B3, button_C1, button_C2, button_C3;
    @FXML Label you_are_label;

    // FXML Methods.
    @FXML private void playButtonPressed(Event e) {
        // Reset previously selected button.
        if (valid_buttons.contains(button_selected)) button_selected.setText("");
        button_selected.setStyle("-fx-background-color: " + DEFAULT_BUTTON_COLOR);

        // Get currently selected button.
        button_selected = (Button) e.getSource();
        
        // Set new selected button appearance.
        button_selected.setStyle("-fx-background-color: " + SELECTED_BUTTON_COLOR);
        button_selected.setText(x_or_o);
    }

    @FXML private void submitButtonPressed() {
        System.out.println("submitButtonPressed()");

        try {
            output.writeUTF("I made my move.");
            output.flush();

            Platform.runLater(() -> {
                disableMyMove(button_selected);
            });

            // Tell server the move.
            boolean alive = true;
            while (alive) {
                String response = input.readUTF();

                if (response.equals("What was the move?")) {
                    System.out.println("What was the move?");
                    output.writeUTF("MOVE~" + button_selected.getId());
                    output.flush();
                }
                else if (response.equals("Move received.")) {
                    System.out.println("Move received.");
                    disableButtons();
                    alive = false;
                } 
                else if (response.equals("Game over.")) {
                    launchEndScreen();
                    return;
                }
                else {
                    System.out.println("Unexpected data from server in submitButtonPressed() in PlayScreenController.java: " + response);
                    return;
                }
            }

            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    // Wait for server response.
                    boolean alive = true;
                    while (alive) {
                        try {
                            output.writeUTF("Is it my turn?");
                            output.flush();
                            String response = input.readUTF();

                            if (response.equals(user_num)) {
                                output.writeUTF("What was my opponent's move?");
                                output.flush();

                                opponent_move = input.readUTF();

                                Platform.runLater(() -> {
                                    disableOpponentMove(opponent_move);
                                    enableButtons();
                                });

                                alive = false;
                            }
                            else if (response.equals("Game over.")) {
                                Platform.runLater(() -> {
                                    disableButtons();
                                    launchEndScreen();
                                });

                                alive = false;
                            }
                            else {
                                Platform.runLater(() -> {
                                    disableButtons();
                                });
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        } 
                    }
                }
            }).start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        
    }

    // Other methods.
    public void initializeData(String name, Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        // Set variables.
        this.socket = socket;
        input = in;
        output = out;
        username = name;
        
            // Add all the buttons. 
            valid_buttons.add(button_A1);
            valid_buttons.add(button_A2);
            valid_buttons.add(button_A3);
            valid_buttons.add(button_B1);
            valid_buttons.add(button_B2);
            valid_buttons.add(button_B3);
            valid_buttons.add(button_C1);
            valid_buttons.add(button_C2);
            valid_buttons.add(button_C3);
            valid_buttons.add(submit_button);

        // Determine 'X' or 'O' status.
        try {
            System.out.println("What am I?");
            out.writeUTF("What am I?");
            output.flush();
            
            user_num = in.readUTF();

            System.out.println("User number: " + user_num); 

            if (user_num.equals("1")) { 
                x_or_o = "X"; 
                enableButtons();
            }
            else { 
                x_or_o = "O"; 
                disableButtons();

                new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
                        // Wait for server response.
                        boolean alive = true;
                        while (alive) {
                            try {
                                output.writeUTF("Is it my turn?");
                                output.flush();
                                String response = input.readUTF();

                                if (response.equals(user_num)) {
                                    output.writeUTF("What was my opponent's move?");
                                    output.flush();

                                    opponent_move = input.readUTF();

                                    Platform.runLater(() -> {
                                        disableOpponentMove(opponent_move);
                                        enableButtons();
                                    });

                                    alive = false;
                                }
                                else if (response.equals("Game over.")) {
                                    Platform.runLater(() -> {
                                        launchEndScreen();
                                    });

                                    alive = false;
                                }
                                else {
                                    Platform.runLater(() -> {
                                        disableButtons();
                                    });
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            } 
                        }
                    }
                }).start();                
            }

            you_are_label.setText("You are " + x_or_o);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disableButtons() {
        submit_button.setDisable(true);
        button_A1.setDisable(true);
        button_A2.setDisable(true);
        button_A3.setDisable(true);
        button_B1.setDisable(true);
        button_B2.setDisable(true);
        button_B3.setDisable(true);
        button_C1.setDisable(true);
        button_C2.setDisable(true);
        button_C3.setDisable(true);
    }

    private void enableButtons() {
        for (Button b: valid_buttons) {
            b.setDisable(false);    
        }
    }

    private void disableMyMove(Button my_move) {
        System.out.println("disableMyMove()");
        System.out.println("Disabling " + my_move.getId() + "...");

        if (x_or_o.equals("X")) my_move.setText("X");
        else my_move.setText("O");

        valid_buttons.remove(my_move);
        my_move.setDisable(true);
    }

    private void disableOpponentMove(String op_move) {    
        System.out.println("disableOpponentMove()");

        Iterator<Button> iter = valid_buttons.iterator();

        while (iter.hasNext()) {
            Button button = iter.next();

            if (button.getId().equals(op_move)) {
                System.out.println("Disabling " + button.getId() + "...");

                if (x_or_o.equals("X")) button.setText("O");
                else button.setText("X");

                iter.remove();
                button.setDisable(true);
                break;
            }
        }
    }

    private void launchEndScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("game-over-page.fxml"));
            Parent root = loader.load();

            GameOverPageController gop_controller = loader.getController();
            gop_controller.initializeData(socket, input, output, x_or_o, username);

            Stage stage = (Stage) submit_button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Game over");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
