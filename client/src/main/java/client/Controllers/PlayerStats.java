package client.Controllers;

import javafx.beans.property.*;

public class PlayerStats {
    private final SimpleStringProperty player;
    private final SimpleIntegerProperty wins;
    private final SimpleIntegerProperty losses;
    private final SimpleDoubleProperty ratio;

    public PlayerStats(String player, int wins, int losses) {
        this.player = new SimpleStringProperty(player);
        this.wins = new SimpleIntegerProperty(wins);
        this.losses = new SimpleIntegerProperty(losses);

        double r = (losses == 0) ? wins : (double) wins / losses;
        double rRound = Double.parseDouble(String.format("%.5f", r));

        this.ratio = new SimpleDoubleProperty(rRound);
    }

    public String getPlayer() { return player.get(); }
    public int getWins() { return wins.get(); }
    public int getLosses() { return losses.get(); }
    public double getRatio() { return ratio.get(); }
}