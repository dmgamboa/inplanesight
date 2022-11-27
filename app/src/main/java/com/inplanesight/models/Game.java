package com.inplanesight.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Game implements Serializable {

    String airportCode;
    ArrayList<Hunt> scavengerHunt;
    Date startingTimestamp;
    int score;
    Users user;

    public Game(String airportCode) {
        this.airportCode = airportCode;
        scavengerHunt = new ArrayList<>();
    }

    public void addHuntObject(Hunt item) {
        scavengerHunt.add(item);
    }

    public ArrayList<Hunt> getScavengerHunt() {
        return scavengerHunt;
    }

    public Hunt getHuntAt(int index) { return scavengerHunt.get(index); }

    public void addHunt(Hunt h) { scavengerHunt.add(h); }

    public void setScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public Date getStartingTimestamp() {
        return startingTimestamp;
    }

    public void setStartingTimestamp(Date startingTimestamp) {
        this.startingTimestamp = startingTimestamp;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
