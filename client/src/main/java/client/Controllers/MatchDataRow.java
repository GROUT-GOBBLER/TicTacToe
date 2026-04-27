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

        int afternoon = calTime.get(Calendar.AM_PM);
        String suffix = "";
        if (afternoon == 0) suffix = "AM"; else suffix = "PM";
        String timeString = calTime.get(Calendar.HOUR) + ":" + calTime.get(Calendar.MINUTE) + ":" + calTime.get(Calendar.SECOND) + " " + suffix;

        this.date = new SimpleStringProperty(dateString);
        this.time = new SimpleStringProperty(timeString);
    }

    public String getDate() {
        return date.get();
    }

    public String getTime() {
        return time.get();
    }

    public String getPlayedWith() {
        return playedWith.get();
    }

    public String getResult() {
        return result.get();
    }
}
