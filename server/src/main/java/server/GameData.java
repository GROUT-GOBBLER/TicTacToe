package server;

import java.io.*;
import java.util.ArrayList;

/*
    William Vipperman, Yarrick Dillard
    CS1760 - Adv Obj OOP & Design
    Final Project
    GameData.java
*/

public class GameData implements Serializable {
    // Instance variables.
    private String winner;
    private String loser;
    private String[][] board;

    // Constructors.
    public GameData(String win, String lose, String[][] b) {
        winner = win;
        loser = lose;

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

    public GameData(String[][] b) {
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
    public String toString() { return "Winner: " + winner + " Loser: " + loser; }

    public static void saveGame(GameData game) throws ClassNotFoundException, IOException {
        ArrayList<GameData> gameList = getAllGames();

        try {
            ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream("Saved_games.dat", false));
            if (gameList.size() > 0) {
                for (GameData listedGame : gameList) { 
                    fileOut.writeObject(listedGame);
                }
            }

            fileOut.writeObject(game);
            fileOut.close();
        } 
        catch (Exception e) { e.printStackTrace(); }
    }

    public static ArrayList<GameData> getAllGames() throws ClassNotFoundException, IOException {
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