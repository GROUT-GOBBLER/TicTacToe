package client.Controllers;

import javafx.beans.property.*;

import java.util.*;
import java.io.*;
import java.lang.*;

public class MatchDataRow {
    private final SimpleStringProperty date;
    private final SimpleStringProperty time;
    private final SimpleStringProperty playedWith;
    private final SimpleStringProperty result;

    public MatchDataRow(String OtherPlayer, Date playedAt, String result) {
        this.playedWith = new SimpleStringProperty(OtherPlayer);
        this.result = new SimpleStringProperty(result);

        Calendar calTime = Calendar.getInstance();
        calTime.setTime(playedAt);

        String dateString = calTime.get(Calendar.MONTH) + "/" + calTime.get(Calendar.DAY_OF_MONTH) + "/" + calTime.get(Calendar.YEAR);
        String timeString = calTime.get(Calendar.HOUR) + ":" + calTime.get(Calendar.MINUTE) + ":" + calTime.get(Calendar.SECOND) + " " + calTime.get(Calendar.AM_PM);

        this.date = new SimpleStringProperty(dateString);
        this.time = new SimpleStringProperty(timeString);
    }

    public SimpleStringProperty getDate() {
        return date;
    }

    public SimpleStringProperty getTime() {
        return time;
    }

    public SimpleStringProperty getPlayedWith() {
        return playedWith;
    }

    public SimpleStringProperty getResult() {
        return result;
    }
}
