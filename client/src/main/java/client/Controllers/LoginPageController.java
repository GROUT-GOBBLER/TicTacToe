package client.Controllers;

import java.io.*;
import java.net.*;
import java.text.*;
import client.App;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

/*
    William Vipperman, Yarrick Dillard
    CS1760 - Adv Obj OOP & Design
    Final Project
    LoginPageController.java
*/

public class LoginPageController {
    // FXML Variables.
    @FXML Button submit_button;
    @FXML TextField name_textfield, port_number_textfield;

    // Instance Variables.
    String user_name;
    String port_number;
    String error_msg = "";

    // FXML Methods.
    @FXML private void initialize() {
        name_textfield.setTextFormatter(new TextFormatter<>(change -> {
            String new_text = change.getControlNewText();

            if (new_text.length() > 14) { return null; }
            else if (!containsValidChars(new_text)) { return null; }
            
            return change;
        }));

        port_number_textfield.setTextFormatter(new TextFormatter<>(change -> {
            String new_text = change.getControlNewText();

            if (change.getControlNewText().length() > 4) { return null; }
            else if (!isNumeric(new_text)) { return null; }

            return change; 
        }));
    }

    @FXML private void submitButtonPressed() {
        // Step 1: Verify user credentials with the server.
        String user_name = name_textfield.getText();
        String port_number = port_number_textfield.getText();

        boolean invalid_input = invalidUserInput();

        if (!invalid_input) {
            ObjectInputStream myInputStream;
            ObjectOutputStream myOutputStream;
            Socket socket = new Socket();

            try {
                InetAddress addr = InetAddress.getByName("localhost");

                try { socket = new Socket(addr, Integer.parseInt(port_number)); }
                catch (IOException e) { 
                    displayErrorPopup("Server does not exist at the specified port: " + port_number + "."); 
                    return;
                }
                
                
                myOutputStream = new ObjectOutputStream(socket.getOutputStream());
                myOutputStream.flush();
                myOutputStream.writeObject(user_name);
                myOutputStream.flush();

                myInputStream = new ObjectInputStream(socket.getInputStream());

                // Step 2: Open main menu window.
                FXMLLoader loader = new FXMLLoader(App.class.getResource("main-menu.fxml"));
                Parent root = loader.load();

                MainMenuController mm_controller = loader.getController();
                mm_controller.initializeData(user_name, socket, myInputStream, myOutputStream);

                Stage stage = (Stage) name_textfield.getScene().getWindow();
                stage.setResizable(false);
                stage.setTitle("Main menu");
                stage.setScene(new Scene(root));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            displayErrorPopup(error_msg);
            error_msg = "";
        }
    }

    // Other methods.
    private boolean invalidUserInput() {
        user_name = name_textfield.getText();
        port_number = port_number_textfield.getText();

        if (user_name.isBlank() || port_number.isBlank()) { 
            System.out.println("BLANK");
            error_msg = "Fields cannot be left blank!";
            return true; 
        }
        else if (user_name.length() > 14) { 
            System.out.println("SHORT");
            error_msg = "Username must be less than 14 characters!\nCurrently " + user_name.length() + " characters.";
            return true; 
        }
        else if (!isNumeric(port_number)) { 
            System.out.println("PORT");
            error_msg = "Port Number can only contain numbers!";
            return true; 
        }
        else if (!containsValidChars(user_name)) { 
            System.out.println("INVALID CHAR");
            error_msg = "Invalid characters in username!\nValid: a-z  A-Z  -  _";
            return true; 
        }

        return false;
    }

    private boolean isNumeric(String str) {
        ParsePosition pos = new ParsePosition(0);
        NumberFormat.getInstance().parse(str, pos);
        return str.length() == pos.getIndex();
    }

    private boolean containsValidChars(String str) {
        for (int x = 0; x < str.length(); x++) {
            char c = str.charAt(x);

            // Valid username characters: a-z A-Z - _
            if (!Character.isAlphabetic(c) && !(c == '_') && !(c == '-')) { 
                return false; 
            }
        }

        return true;
    }

    private void displayErrorPopup(String error_msg) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("error-popup.fxml"));
            Parent root = loader.load();

            ErrorPopupController ep_controller = loader.getController();
            ep_controller.initializeData(error_msg);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Error Popup");
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
