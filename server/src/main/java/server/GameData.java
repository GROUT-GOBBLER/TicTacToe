package server;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/*
    William Vipperman, Yarrick Dillard
    CS1760 - Adv Obj OOP & Design
    Final Project
    GameData.java
*/

public class GameData implements Serializable {
    // Instance variables.
    private String[] players;
    private Date date;
    private String winner;
    private String loser;
    private String[][] board;

    public GameData(String[] ps, Date d, String w, String l, String[][] b) {
        players = ps;
        date = d;
        winner = w;
        loser = l;

        if (b.length != 3) { 
            throw new IllegalArgumentException("Board must have exactly 3 rows"); 
        }

        for (int index = 0; index < b.length; index++) {
            if (b[index].length != 3) { 
                throw new IllegalArgumentException("Each row in the board must have exactly 3 columns"); 
            }
        }

        board = b;
    }

    public GameData(String[] ps, Date d, String[][] b) {
        winner = new String("-");
        loser = new String("-");

        if (b.length != 3) { 
            throw new IllegalArgumentException("Board must have exactly 3 rows");
        }

        for (int index = 0; index < b.length; index++) {
            if (b[index].length != 3) { 
                throw new IllegalArgumentException("Each row in the board must have exactly 3 columns");
            }
        }

        board = b;
    }

    // Get methods.
    public String getWinner() { return winner; }
    public String getLoser() { return loser; }
    public String[][] getBoard() { return board; }
    public Date getDate() { return date; }
    public String[] getPlayers() { return players; }

    // Set methods.
    public void setWinner(String winner) { this.winner = winner; }
    public void setLoser(String loser) { this.loser = loser; }
    
    public void setBoard(String[][] b) {
        if (b.length != 3) {
            throw new IllegalArgumentException("Board must have exactly 3 rows");
        }

        for (int index = 0; index < b.length; index++) {
            if (b[index].length != 3) {
                throw new IllegalArgumentException("Each row in the board must have exactly 3 columns");
            }
        }

        board = b;
    }

    // Other methods.
    @Override
    public String toString() {
        return "Player 1: " + players[0] + " | Player2: " + players[1] + " | Played at: " + date.toString();
    }

    public static void SaveGame(GameData game) throws ClassNotFoundException, IOException {
        ArrayList<GameData> gameList = GetAllGames();

        ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream("Saved_games.dat", false));
        if (gameList.size() > 0) {
            for (GameData listedGame : gameList) {
                fileOut.writeObject(listedGame);
            }
        }
        
        try {
            fileOut.writeObject(game);
            fileOut.close();
        } 
        catch (Exception e) { e.printStackTrace(); }
    }

    public static ArrayList<GameData> GetAllGames() throws ClassNotFoundException, IOException {
        ObjectInputStream fileIn = null;

        try { fileIn = new ObjectInputStream(new FileInputStream("Saved_games.dat")); } 
        catch (Exception e) {
            if (fileIn != null) fileIn.close();
            return new ArrayList<GameData>();
        }

        GameData curr;
        ArrayList<GameData> gameList = new ArrayList<GameData>();

        try {
            while (true) {
                curr = (GameData) fileIn.readObject();
                gameList.add(curr);
            }
        } 
        catch (java.io.EOFException e) {
            fileIn.close();
            return gameList;
        }
        catch (StreamCorruptedException e2) {
            //likely empty so return empty
            fileIn.close();
            return new ArrayList<GameData>();
        }
        catch (Exception e3) { e3.printStackTrace(); }

        fileIn.close();
        return new ArrayList<GameData>();
    }
}