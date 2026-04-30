package client.Controllers;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import client.App;
import javafx.event.*;

/*
    William Vipperman, Yarrick Dillard
    CS1760 - Adv Obj OOP & Design
    Final Project
    SingeplayerTictactoeBoardController.java
*/

public class SinglePlayerTictactoeBoardController {
    // FXML variables.
    @FXML Label professor_label, difficulty_label, you_are_label;
    @FXML ImageView professor_image_imageview;
    @FXML Button button_A1, button_A2, button_A3, button_B1, button_B2, button_B3, button_C1, button_C2, button_C3;
    @FXML Button submit_button;

    // Instance variables.
    private String username, difficulty, my_x_or_o, opponent_x_or_o;
    private String[][] board_arr = { {" ", " ", " "}, {" ", " ", " "}, {" ", " ", " "} };

    private Button button_selected = new Button();  // keeps track of the most recently selected button by the user.
    private ArrayList<Button> valid_buttons = new ArrayList<Button>();  // keeps track of which buttons are able to be selected.

    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

        // Constants.
        private final Image BILITSKI_IMAGE = loadImage("/client/images/bilitski.png");
        private final Image DAHIYA_IMAGE = loadImage("/client/images/dahiya.jpg");
        private final Image MARCHEGIANI_IMAGE = loadImage("/client/images/marchegiani.png");
        private final Image OHL_IMAGE = loadImage("/client/images/Ohl.png");
        private final String DEFAULT_BUTTON_COLOR = "#D4D4D4";
        private final String SELECTED_BUTTON_COLOR = "#ff0000";

        // Classes.
        private static class Move { int row, column; }

    // FXML methods.
    @FXML private void initialize() {
        // Initialize valid_buttons.
        valid_buttons.add(button_A1);
        valid_buttons.add(button_A2);
        valid_buttons.add(button_A3);
        valid_buttons.add(button_B1);
        valid_buttons.add(button_B2);
        valid_buttons.add(button_B3);
        valid_buttons.add(button_C1);
        valid_buttons.add(button_C2);
        valid_buttons.add(button_C3);

        // Disable submit button.
        submit_button.setDisable(true);
    }

    @FXML private void playButtonPressed(ActionEvent e) {
        // Reset previously selected button.
        if (valid_buttons.contains(button_selected)) button_selected.setText("");
        button_selected.setStyle("-fx-background-color: " + DEFAULT_BUTTON_COLOR);
        submit_button.setDisable(false);

        // Get currently selected button.
        button_selected = (Button) e.getSource();
        
        // Set new selected button appearance.
        button_selected.setStyle("-fx-background-color: " + SELECTED_BUTTON_COLOR);
        button_selected.setText(my_x_or_o);
    }

    @FXML private void submitButtonPressed() {
        if (!isGameOver()) { playerMove(); }
        if (!isGameOver()) { 
            opponentMove(); 
            isGameOver();
        }
    }

    // Other methods.
        // GUI Elements.
        public void initializeData(Socket s, ObjectInputStream in, ObjectOutputStream out, String diff, String name) {
            // Reading in data.
            socket = s;
            input = in;
            output = out;
            difficulty = diff;
            username = name;

            // Set professor image.
            switch(difficulty) { 
                case "easy": {
                    professor_image_imageview.setImage(BILITSKI_IMAGE);
                    professor_label.setText(professor_label.getText() + "Dr. Bilitski");
                    difficulty_label.setText(difficulty_label.getText() + "Easy");
                    break;
                }
                case "medium": {
                    professor_image_imageview.setImage(DAHIYA_IMAGE);
                    professor_label.setText(professor_label.getText() + "Dr. Dahiya");
                    difficulty_label.setText(difficulty_label.getText() + "Medium");
                    break;
                }
                case "hard": {
                    professor_image_imageview.setImage(MARCHEGIANI_IMAGE);
                    professor_label.setText(professor_label.getText() + "Dr. Marchegiani");
                    difficulty_label.setText(difficulty_label.getText() + "Hard");
                    break;
                }
                case "extreme": {
                    professor_image_imageview.setImage(OHL_IMAGE);
                    professor_label.setText(professor_label.getText() + "Dr. Ohl");
                    difficulty_label.setText(difficulty_label.getText() + "Extreme");
                    break;
                }
                default: {
                    System.out.println("Error in intializeData() in SingeplayerTictactoeBoardController.java: " + difficulty);
                    break;
                }
            }

            // Determining who is X and who is O.
            double random_num = Math.random();

            if (random_num > 0.5) { 
                my_x_or_o = "X"; 
                opponent_x_or_o = "O";
            }
            else { 
                my_x_or_o = "O"; 
                opponent_x_or_o = "X";
            }
            
            you_are_label.setText("You are: " + my_x_or_o);

            if (opponent_x_or_o.equals("X")) { opponentMove(); }    // X always goes first.
        }

        private Image loadImage(String file_path) {
            try {
                // Use getResource to get the URL from classpath
                Image img = new Image(SinglePlayerTictactoeBoardController.class.getResourceAsStream(file_path));
                return img;
            } catch (Exception e) {
                System.out.println("Error in loadImage() in SinglePlayerTictactoeBoardController.java\n" + e.getMessage());
                return null;
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

        // Gameplay.
        private void playerMove() {
            button_selected.setText(my_x_or_o);

            valid_buttons.remove(button_selected);
            button_selected.setDisable(true);
            button_selected.setStyle("-fx-background-color: " + DEFAULT_BUTTON_COLOR);

            addMoveToBoard(button_selected, my_x_or_o);

            disableButtons();
        }

        private void opponentMove() {
            Button opponent_move = calculateOpponentMove();

            opponent_move.setText(opponent_x_or_o);
            valid_buttons.remove(opponent_move);
            opponent_move.setDisable(true);

            addMoveToBoard(opponent_move, opponent_x_or_o);
            enableButtons();
        }

        private void addMoveToBoard(Button move, String symbol) {         
            // X ... stupid_compsci_major_counter_variable=0,1
            // O ... stupid_compsci_major_counter_variable=2

            // A1, B1, C1   // 0,0  0,1  0,2
            // A2, B2, C2   // 1,0  1,1  1,2
            // A3, B3, C3   // 2,0  2,1  2,2

            switch (move.getId()) {
                case "button_A1": {
                    board_arr[0][0] = symbol;
                    break;
                }
                case "button_A2": {
                    board_arr[1][0] = symbol;
                    break;
                }
                case "button_A3": {
                    board_arr[2][0] = symbol;
                    break;
                }
                case "button_B1": {
                    board_arr[0][1] = symbol;
                    break;
                }
                case "button_B2": {
                    board_arr[1][1] = symbol;
                    break;
                }
                case "button_B3": {
                    board_arr[2][1] = symbol;
                    break;
                }
                case "button_C1": {
                    board_arr[0][2] = symbol;
                    break;
                }
                case "button_C2": {
                    board_arr[1][2] = symbol;
                    break;
                }
                case "button_C3": {
                    board_arr[2][2] = symbol;
                    break;
                }
                default: {
                    System.out.println("IMPOSSIBLE move in addMoveToBoard() in Server.java: " + move);
                    break;
                }
            }
        }

        private boolean isGameOver() {
            // A1, B1, C1   // 0,0  0,1  0,2
            // A2, B2, C2   // 1,0  1,1  1,2
            // A3, B3, C3   // 2,0  2,1  2,2

            /*
                Win Conditions:
                1) A1, B1, C1 are all X/O.
                2) A2, B2, C2 are all X/O.
                3) A3, B3, C3 are all X/O.
                4) A1, A2, A3 are all X/O.
                5) B1, B2, B3 are all X/O.
                6) C1, C2, C3 are all X/O.
                7) A1, B2, C3 are all X/O.
                8) C1, B2, A3 are all X/O.
            */

            String A1 = board_arr[0][0]; String A2 = board_arr[1][0]; String A3 = board_arr[2][0];
            String B1 = board_arr[0][1]; String B2 = board_arr[1][1]; String B3 = board_arr[2][1];
            String C1 = board_arr[0][2]; String C2 = board_arr[1][2]; String C3 = board_arr[2][2];
            
            if ((A1 + B1 + C1).equals("XXX") || (A2 + B2 + C2).equals("XXX") || (A3 + B3 + C3).equals("XXX")
                || (A1 + A2 + A3).equals("XXX") || (B1 + B2 + B3).equals("XXX") || (C1 + C2 + C3).equals("XXX")
                || (A1 + B2 + C3).equals("XXX") || (C1 + B2 + A3).equals("XXX")) {
                    if (my_x_or_o.equals("X")) { launchEndScreen("Win"); }
                    else { launchEndScreen("Lose"); }
                    return true;
            }
            else if ((A1 + B1 + C1).equals("OOO") || (A2 + B2 + C2).equals("OOO") || (A3 + B3 + C3).equals("OOO")
                || (A1 + A2 + A3).equals("OOO") || (B1 + B2 + B3).equals("OOO") || (C1 + C2 + C3).equals("OOO")
                || (A1 + B2 + C3).equals("OOO") || (C1 + B2 + A3).equals("OOO")) {
                    if (my_x_or_o.equals("X")) { launchEndScreen("Lose"); }
                    else { launchEndScreen("Win"); }
                    return true;
            } 
            else if (!A1.isBlank() && !A2.isBlank() && !A3.isBlank() 
                && !B1.isBlank() && !B2.isBlank() && !B3.isBlank()
                && !C1.isBlank() && !C2.isBlank() && !C3.isBlank()) {
                    launchEndScreen("Draw");
                    return true;
            }

            return false;
        }

        private void launchEndScreen(String win_status) {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("game-over-page.fxml"));
                Parent root = loader.load();

                GameOverPageController gop_controller = loader.getController();
                gop_controller.initializeData(socket, input, output, my_x_or_o, username, win_status, board_arr);

                Stage stage = (Stage) button_A1.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setTitle("Game over");
            }
            catch (Exception e) {
                e.printStackTrace();
            }            
        }

        private Button calculateOpponentMove() {
            /* 
                Professor Movesets
                 - Bilitski/EASY: Always picks the worst move.
                 - Dahiya/MEDIUM: Picks moves at random.
                 - Marchegiani/HARD: 50% chace for random move, 50% chance for best move.
                 - Ohl/EXTREME: Always chooses the best move.
            */

            switch (difficulty) {
                case "easy": { return getWorstMove(); }
                case "medium": { return getRandomMove(); }
                case "hard": { 
                    if (Math.random() >= 0.5) return getBestMove();
                    else return getRandomMove();
                }
                case "extreme": { return getBestMove(); }
                default: {
                    System.out.println("Invalid difficulty in calculateOpponentMove() in SingeplayerTictactoeBoardController.java:" + difficulty);
                    return null;
                }
            }
        }

            // calculateOpponentMove() helper methods.
            private Button getBestMove() {                
                Move best_move = findTheBestMove();

                int row = best_move.row;
                int column = best_move.column;

                Button button_move = new Button();

                switch (row + "" + column) {
                    case "00": { button_move = button_A1; break; }
                    case "01": { button_move = button_B1; break; }
                    case "02": { button_move = button_C1; break; }
                    case "10": { button_move = button_A2; break; }
                    case "11": { button_move = button_B2; break; }
                    case "12": { button_move = button_C2; break; }
                    case "20": { button_move = button_A3; break; }
                    case "21": { button_move = button_B3; break; }
                    case "22": { button_move = button_C3; break; }
                    default: {
                        System.out.println("Error in getBestMove() in SinglePlayerTictactoeBoardController.java: " + row + "" + column);
                        break;
                    }
                }
                
                return button_move;
            }

            private Button getWorstMove() {
                Move best_move = findTheWorstMove();

                int row = best_move.row;
                int column = best_move.column;

                Button button_move = new Button();

                switch (row + "" + column) {
                    case "00": { button_move = button_A1; break; }
                    case "01": { button_move = button_B1; break; }
                    case "02": { button_move = button_C1; break; }
                    case "10": { button_move = button_A2; break; }
                    case "11": { button_move = button_B2; break; }
                    case "12": { button_move = button_C2; break; }
                    case "20": { button_move = button_A3; break; }
                    case "21": { button_move = button_B3; break; }
                    case "22": { button_move = button_C3; break; }
                    default: {
                        System.out.println("Error in getBestMove() in SinglePlayerTictactoeBoardController.java: " + row + "" + column);
                        break;
                    }
                }
                
                return button_move;
            }

            private Button getRandomMove() {
                int number_of_possible_moves = valid_buttons.size();
                int random_number = ThreadLocalRandom.current().nextInt(0, number_of_possible_moves);
                return valid_buttons.get(random_number);
            }

            private Move findTheBestMove() {
                // Initialize variables.
                int best_value = -1000;
                Move best_move = new Move();
                best_move.row = -1;
                best_move.column = -1;

                // Go through each empty space and evaluate the minimax function on it. 
                // Find the cell with the highest value.
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        if (board_arr[x][y].equals(" ")) { // If this space is empty.
                            board_arr[x][y] = opponent_x_or_o;
                            int move_value = miniMax(board_arr, 0, false);
                            board_arr[x][y] = " ";

                            if (move_value > best_value) {
                                best_move.row = x;
                                best_move.column = y;
                                best_value = move_value;
                            }
                        }
                    }
                }

                return best_move;
            }

            private Move findTheWorstMove() {
                // Initialize variables.
                int worst_value = 1000;
                Move worst_move = new Move();
                worst_move.row = -1;
                worst_move.column = -1;

                // Go through each empty space and evaluate the minimax function on it. 
                // Find the cell with the lowest value.
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        if (board_arr[x][y].equals(" ")) { // If this space is empty.
                            board_arr[x][y] = opponent_x_or_o;
                            int move_value = miniMax(board_arr, 0, false);
                            board_arr[x][y] = " ";

                            if (move_value < worst_value) {
                                worst_move.row = x;
                                worst_move.column = y;
                                worst_value = move_value;
                            }
                        }
                    }
                }

                return worst_move;
            }

            private int miniMax(String board[][], int depth, boolean is_max) {
                int score = evaluate(board);

                if (score == 10) { return score - depth; } 
                if (score == -10) { return score + depth; }
                if (!isMovesLeft(board)) { return 0; }

                if (is_max) {
                    int best = -1000;

                    // Evaluate every empty space and recurse on it.
                    for (int x = 0; x < 3; x++) {
                        for (int y = 0; y < 3; y++) {
                            if (board[x][y].equals(" ")) {
                                board[x][y] = opponent_x_or_o;
                                best = Math.max(best, miniMax(board, depth+1, !is_max));
                                board[x][y] = " ";
                            }
                        }
                    }

                    return best;
                }
                else {
                    int best = 1000;

                    // Evaluate every empty space and recurse on it.
                    for (int x = 0; x < 3; x++) {
                        for (int y = 0; y < 3; y++) {
                            if (board[x][y].equals(" ")) {
                                board[x][y] = my_x_or_o;
                                best = Math.min(best, miniMax(board, depth+1, !is_max));
                                board[x][y] = " ";
                            }
                        }
                    }

                    return best;
                }
            }

            private int evaluate(String[][] board) {
                // Check rows for a victory.
                for (int row = 0; row < 3; row++) {
                    if (!board[row][0].equals(" ") && board[row][0].equals(board[row][1]) && board[row][1].equals(board[row][2])) {
                        if (board[row][0].equals(my_x_or_o)) { return -10; }
                        else if (board[row][0].equals(opponent_x_or_o)) { return 10; }
                    }
                }

                // Check columns for a victory.
                for (int column = 0; column < 3; column++) {
                    if (!board[0][column].equals(" ") && board[0][column].equals(board[1][column]) && board[1][column].equals(board[2][column])) {
                        if (board[0][column].equals(my_x_or_o)) { return -10; }
                        else if (board[0][column].equals(opponent_x_or_o)) { return 10; }
                    }
                }

                // Check diagonals for a victory.
                if (!board[0][0].equals(" ") && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
                    if (board[0][0].equals(my_x_or_o)) { return -10; }
                    else if (board[0][0].equals(opponent_x_or_o)) { return 10; }
                }

                if (!board[0][2].equals(" ") && board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
                    if (board[0][2].equals(my_x_or_o)) { return -10; }
                    else if (board[0][2].equals(opponent_x_or_o)) { return 10; }
                }

                // If no victory yet, return a zero.
                return 0;
            }

            private Boolean isMovesLeft(String board[][]) {
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        if (board[x][y].equals(" ")) { return true; }
                    }
                }

                return false;
            }
}
