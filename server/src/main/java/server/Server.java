package server;

/*
    William Vipperman, Yarrick Dillard
    CS1760 - Adv Obj OOP & Design
    Final Project
    Server.java
*/

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    // Constants.
    public static final int PORT = 5000;

    // Server variables.
    private int MaxUsers;
    private Socket[] users;
    private UserThread [] threads;
    private int numUsers;

    // Instance variables.
    private int stupid_compsci_major_counter_variable = 0;
    private String last_move = "";
    private String[][] board_arr = {
        {" ", " ", " "},   // A1, B1, C1  ::: [0][0]  [0][1]  [0][2]
        {" ", " ", " "},   // A2, B2, C2  ::: [1][0]  [1][1]  [1][2]
        {" ", " ", " "}    // A3, B3, C3  ::: [2][0]  [2][1]  [2][2]
    };
    private WinState win_state = WinState.Ongoing;

    // Enums.
    private enum WinState {
        Ongoing,
        Draw,
        XWin,
        OWin,
    };

    // SERVER METHODS.
    public Server(int m) {
        MaxUsers = m;
        users = new Socket[MaxUsers];
        threads = new UserThread[MaxUsers];   // Set things up and start.
        numUsers = 0; 
        
        try { runServer(); }
        catch (Exception e) { System.out.println("Problem with server"); }
    }

    private void runServer() throws IOException
    {
       ServerSocket s = new ServerSocket(PORT);
	   System.out.println("Started: " + s);
          
	   try {
            while(true) {
                if (numUsers < MaxUsers) {
                    try {
                        // wait for client
                        Socket newSocket = s.accept();

                        // set up streams and thread for client    
                        synchronized (this) {
     	                users[numUsers] = newSocket;
                        ObjectInputStream in = new ObjectInputStream(newSocket.getInputStream());
                        String newName = (String)in.readObject();

                        threads[numUsers] = new UserThread(newSocket, newName, numUsers, in);
                        threads[numUsers].start();
                        System.out.println("Connection " + newName + ", " + numUsers + users[numUsers]);
                        numUsers++;
                        }
                    }
                    catch (Exception e) {
                        System.out.println("Problem with connection...terminating");
                    }
                } 
            }  
        }      
        finally {
            System.out.println("Server shutting down");  
            s.close();     
        }
    }

    public synchronized void removeClient(int id) {
        try                          
        {                             
            users[id].close();    
        }                            
        catch (IOException e)
        {
            System.out.println("Already closed");
        }

        System.out.println("removing: " + threads[id].username);

        users[id] = null;
        threads[id] = null;
        // fill up "gap" in arrays
        for (int i = id; i < numUsers-1; i++)           
        {                             
            users[i] = users[i+1];
            threads[i] = threads[i+1];
            threads[i].setId(i);
        }
        numUsers--;  
    }

    // UserThread.
    private class UserThread extends Thread {
        @SuppressWarnings("unused")
        private Socket mySocket;
        private ObjectInputStream myInputStream;
        private ObjectOutputStream myOutputStream;
        private int myId;
        public String username = "none";
         
        private UserThread(Socket newSocket, String user, int id, ObjectInputStream in) throws IOException {
            mySocket = newSocket;
            myId = id;
            username = user;
            myInputStream = in;
            myOutputStream = new ObjectOutputStream(newSocket.getOutputStream());
        }

        @SuppressWarnings("unused") public ObjectInputStream getInputStream() { return myInputStream; }
        @SuppressWarnings("unused") public ObjectOutputStream getOutputStream() { return myOutputStream; }

        public synchronized void setId(int newId) { myId = newId; }

        private void respond(String msg) { // Used for sending confirmation and/or error messages.
            try {
                myOutputStream.writeUTF(msg);
                myOutputStream.flush();
            } 
            catch (Exception e) { e.printStackTrace(); }
        }

        public void run() {  
            boolean alive = true;

            while (alive) // Where you want to track client data
            {
                String action = "";

                try {
                    action = myInputStream.readUTF();

                    if (action.equals("Is other player connected?")) {
                        if (numUsers == 1) { respond("NO"); }
                        else { respond("YES"); }
                    }
                    else if (action.equals("How many connected?")) {
                        stupid_compsci_major_counter_variable++;
                        respond(stupid_compsci_major_counter_variable + "");
                    }
                    else if (action.equals("I am connected.")) { 
                        respond(stupid_compsci_major_counter_variable + ""); 
                    }
                    else if (action.equals("What am I?")) { 
                        respond(stupid_compsci_major_counter_variable + ""); 
                        stupid_compsci_major_counter_variable--;
                    }
                    else if (action.equals("I made my move.")) {
                        respond("What was the move?");
                    }
                    else if (action.contains("MOVE~")) {
                        // Get move.
                        last_move = action.split("~")[1];
                        addMoveToBoard(last_move);
                        
                        // Check to see if the move was a winning move.
                        win_state = didWeWinYet();
                        switch (win_state) {
                            case Ongoing: {
                                respond("Move received.");
                                break;
                            }
                            default: {
                                respond("Game over.");
                                stupid_compsci_major_counter_variable = 0;
                                break;
                            }
                        }

                        // Switch whose turn it is.
                        if (stupid_compsci_major_counter_variable == 2) stupid_compsci_major_counter_variable = 1;
                        else stupid_compsci_major_counter_variable = 2;
                    }
                    else if (action.equals("Is it my turn?")) { 
                        win_state = didWeWinYet();
                        switch (win_state) {
                            case Ongoing: {
                                respond(stupid_compsci_major_counter_variable + "");
                                break;
                            }
                            default: {
                                respond("Game over.");
                                stupid_compsci_major_counter_variable = 0;
                                break;
                            }
                        }
                    }
                    else if (action.equals("What was my opponent's move?")) {
                        respond(last_move);
                    }
                    else if (action.contains("Did I win?")) {
                        String parsed_string[] = action.split("~");
                        String user_letter = parsed_string[1];

                        switch (win_state) {
                            case XWin: {
                                if (user_letter.equals("X")) { respond("won"); }
                                else { respond("lost"); }
                                break;
                            }
                            case OWin: {
                                if (user_letter.equals("O")) { respond("won"); }
                                else { respond("lost"); }
                                break;
                            }
                            case Draw: {
                                respond("draw");
                                break;
                            }
                            default: {
                                System.out.println("Impossible win state in run() of UserThread in Server.java: " + win_state);
                                return;
                            }
                        }
                    }
                    else if (action.equals("Give me the ending board.")) {
                        respond(board_arr[0][0] + board_arr[1][0] + board_arr[2][0] + board_arr[0][1] + board_arr[1][1] + board_arr[2][1] + board_arr[0][2] + board_arr[1][2] + board_arr[2][2]);
                    }
                    else if (action.equals("Back to main menu.")) {
                        System.out.println("Back to main menu.\t" + stupid_compsci_major_counter_variable);

                        if (stupid_compsci_major_counter_variable == 1) {
                            //saveResultsToJSON(); Yar needs tp replace with method from GameData.java
                            resetVariables();
                        }
                        else { stupid_compsci_major_counter_variable = 1; }
                    }

                    else if (action.equals("Give leaderboard")) {
                        HashMap<String, Integer> playerHashMap = new HashMap<>();
                        ArrayList<GameData> list = GameData.GetAllGames();

                        if (list.size() > 0) {
                            for (GameData g : list) {
                                if (playerHashMap.containsKey(g.getWinner())) {
                                    int currVal = playerHashMap.get(g.getWinner());
                                    playerHashMap.replace(g.getWinner(), currVal + 1);
                                }
                                else {
                                    playerHashMap.put(g.getWinner(), 1);
                                }
                            }
                        }

                        myOutputStream.writeObject(playerHashMap);
                        myOutputStream.flush();
                    }
                    else if (action.equals("Give my history")) {

                    }
                    else {
                        System.out.println("INVALID INPUT: " + action);
                        alive = false;
                    }
                }
                catch (Exception e) {
                    System.out.println("Error listening to client: "+ e.getMessage());
                    alive = false;
                }
            }

            removeClient(myId);
        }
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        // Init a save file
        Path path = Path.of("Saved_games.dat");

        try {
            if (!Files.exists(path)) Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Server launch
        System.out.println("\n\nStarting server...\n\n");

        Server s = new Server(2);
    }
    
    // Helper methods.
    private void addMoveToBoard(String move) {         
        // X ... stupid_compsci_major_counter_variable=0,1
        // Y ... stupid_compsci_major_counter_variable=2

        // A1, B1, C1   // 0,0  0,1  0,2
        // A2, B2, C2   // 1,0  1,1  1,2
        // A3, B3, C3   // 2,0  2,1  2,2

        switch (move) {
            case "button_A1": {
                if (stupid_compsci_major_counter_variable == 2) { board_arr[0][0] = "O"; }
                else { board_arr[0][0] = "X"; }
                break;
            }
            case "button_A2": {
                if (stupid_compsci_major_counter_variable == 2) { board_arr[1][0] = "O"; }
                else { board_arr[1][0] = "X"; }
                break;
            }
            case "button_A3": {
                if (stupid_compsci_major_counter_variable == 2) { board_arr[2][0] = "O"; }
                else { board_arr[2][0] = "X"; }
                break;
            }
            case "button_B1": {
                if (stupid_compsci_major_counter_variable == 2) { board_arr[0][1] = "O"; }
                else { board_arr[0][1] = "X"; }
                break;
            }
            case "button_B2": {
                if (stupid_compsci_major_counter_variable == 2) { board_arr[1][1] = "O"; }
                else { board_arr[1][1] = "X"; }
                break;
            }
            case "button_B3": {
                if (stupid_compsci_major_counter_variable == 2) { board_arr[2][1] = "O"; }
                else { board_arr[2][1] = "X"; }
                break;
            }
            case "button_C1": {
                if (stupid_compsci_major_counter_variable == 2) { board_arr[0][2] = "O"; }
                else { board_arr[0][2] = "X"; }
                break;
            }
            case "button_C2": {
                if (stupid_compsci_major_counter_variable == 2) { board_arr[1][2] = "O"; }
                else { board_arr[1][2] = "X"; }
                break;
            }
            case "button_C3": {
                if (stupid_compsci_major_counter_variable == 2) { board_arr[2][2] = "O"; }
                else { board_arr[2][2] = "X"; }
                break;
            }
            default: {
                System.out.println("IMPOSSIBLE move in addMoveToBoard() in Server.java: " + move);
                break;
            }
        }
    }

    private WinState didWeWinYet() {
        // X ... stupid_compsci_major_counter_variable=0,1
        // Y ... stupid_compsci_major_counter_variable=2

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
                return WinState.XWin;
        }
        else if ((A1 + B1 + C1).equals("OOO") || (A2 + B2 + C2).equals("OOO") || (A3 + B3 + C3).equals("OOO")
            || (A1 + A2 + A3).equals("OOO") || (B1 + B2 + B3).equals("OOO") || (C1 + C2 + C3).equals("OOO")
            || (A1 + B2 + C3).equals("OOO") || (C1 + B2 + A3).equals("OOO")) {
                return WinState.OWin;
        } 
        else if (!A1.isBlank() && !A2.isBlank() && !A3.isBlank() 
            && !B1.isBlank() && !B2.isBlank() && !B3.isBlank()
            && !C1.isBlank() && !C2.isBlank() && !C3.isBlank()) {
                return WinState.Draw;
        }
        
        return WinState.Ongoing;
    }

    private void resetVariables()  {
        System.out.println("resetVariables()");

        stupid_compsci_major_counter_variable = 0;
        last_move = "";
        win_state = WinState.Ongoing;

        board_arr[0][0] = " "; board_arr[0][1] = " "; board_arr[0][2] = " ";
        board_arr[1][0] = " "; board_arr[1][1] = " "; board_arr[1][2] = " ";
        board_arr[2][0] = " "; board_arr[2][1] = " "; board_arr[2][2] = " ";
    }

    private void testSave() { // THIS IS ONLY FOR TESTING SAVING, use the "GameData.SaveGame();" method instead
        String[][] example_board = {{"x", "o", "x"}, {"-", "o", "-"}, {"x", "o", "-"}};
        GameData exampleGame = new GameData("John", "Jane", example_board);

        try {
            GameData.SaveGame(exampleGame);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testLoad() {
        try {
            ArrayList<GameData> list = GameData.GetAllGames();
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}