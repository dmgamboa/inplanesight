package com.inplanesight.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class GameModel implements Serializable {

    Coordinates airport;
    ArrayList<HuntObject> scavengerHunt;
    Date startingTimestamp;
    int score;

    public GameModel(Coordinates airport) {
        this.airport = airport;
    }

    public void addHuntObject(HuntObject item) {
        scavengerHunt.add(item);
    }

    public ArrayList<HuntObject> getScavengerHunt() {
        return scavengerHunt;
    }

    public void setScore(int score) {
        this.score += score;
    }

    public Date getStartingTimestamp() {
        return startingTimestamp;
    }

    public void setStartingTimestamp(Date startingTimestamp) {
        this.startingTimestamp = startingTimestamp;
    }
}
