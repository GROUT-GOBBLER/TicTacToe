package server;
import java.io.*;

public class GameData implements Serializable {
    private String winner;
    private String loser;
    private String[][] board;

    public GameData(String w, String l, String[][] b) {
        winner = w;
        loser = l;

        if (b.length != 3) throw new IllegalArgumentException("Board must have exactly 3 rows");

        for (int index = 0; index < b.length; index++) {
            if (b[index].length != 3) throw new IllegalArgumentException("Each row in the board must have exactly 3 columns");
        }

        board = b;
    }

    public GameData(String[][] b) {
        winner = new String("-");
        loser = new String("-");

        if (b.length != 3) throw new IllegalArgumentException("Board must have exactly 3 rows");

        for (int index = 0; index < b.length; index++) {
            if (b[index].length != 3) throw new IllegalArgumentException("Each row in the board must have exactly 3 columns");
        }

        board = b;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }

    public void setLoser(String loser) {
        this.loser = loser;
    }

    public String getLoser() {
        return loser;
    }

    public void setBoard(String[][] b) {
        if (b.length != 3) throw new IllegalArgumentException("Board must have exactly 3 rows");

        for (int index = 0; index < b.length; index++) {
            if (b[index].length != 3) throw new IllegalArgumentException("Each row in the board must have exactly 3 columns");
        }

        board = b;
    }

    public String[][] getBoard() {
        return board;
    }
}
