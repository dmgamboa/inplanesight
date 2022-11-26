package com.inplanesight.models;

import org.json.JSONObject;

import java.io.Serializable;

public class Leaderboard implements Serializable {
    String user;
    int score;
    String code;

    public Leaderboard() {}

    public Leaderboard(String username, int score) {
        this.user = username;
        this.score = score;
        this.code = code;
    }

    public Leaderboard(String username, int score, String code) {
        this.user = username;
        this.score = score;
        this.code = code;
    }

    public Leaderboard(JSONObject entry) {
        try {
            this.user = entry.getString("user");
            this.score = entry.getInt("score");
            this.code = entry.getString("airportCode");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
