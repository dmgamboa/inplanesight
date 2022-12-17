package com.inplanesight.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Users {
    private String nickname;
    private String email;
    private String id;

    //Required default constructor for getting data from firestore
    public Users() {}

    //Parameterized constructor for other uses
    public Users(String nickname, String email, String id) {
        this.nickname = nickname;
        this.email = email;
        this.id = id;
    }

    //Getters to get data obtained from firestore
    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }
}