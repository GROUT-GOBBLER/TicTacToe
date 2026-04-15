package client.Controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
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
    LoginPageController.java
*/

public class LoginPageController {
    // FXML Variables.
    @FXML Button submit_button;
    @FXML TextField name_textfield, port_number_textfield;

    // FXML Methods.
    @FXML private void submitButtonPressed() {
        // Step 1: Verify user credentials with the server.
        String user_name = name_textfield.getText();
        String port_number = port_number_textfield.getText();

        // ...
        ObjectInputStream myInputStream;
        ObjectOutputStream myOutputStream;
        Socket socket;

        try {
            InetAddress addr = InetAddress.getByName("localhost");
            socket = new Socket(addr, Integer.parseInt(port_number));
            
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
            stage.setScene(new Scene(root));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
