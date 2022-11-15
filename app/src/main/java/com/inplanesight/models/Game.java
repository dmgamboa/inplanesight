package com.inplanesight.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Game implements Serializable {

    String airportCode;
    ArrayList<Hunt> scavengerHunt;
    Date startingTimestamp;
    int score;

    public Game(String airportCode) {
        this.airportCode = airportCode;
    }

    public void addHuntObject(Hunt item) {
        scavengerHunt.add(item);
    }

    public ArrayList<Hunt> getScavengerHunt() {
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
