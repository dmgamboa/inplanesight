package com.inplanesight.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class Hunt implements Serializable {
    String airportCode;
    String name;
    String id;
    String imageRef;
    Coordinates coordinates;
    Date timestampFound;

    public Hunt(String name, String id, String imageRef, Coordinates coordinates, String airportCode) {
        this.name = name;
        this.id = id;
        this.imageRef = imageRef;
        this.coordinates = coordinates;
        this.airportCode = airportCode;
        this.timestampFound = null;
    }

    public Hunt(JSONObject hunt) {
        try {
            this.name = hunt.getString("name");
            this.id = hunt.getString("id");
            this.imageRef = hunt.getString("imageRef");
            this.coordinates = new Coordinates(hunt.getJSONObject("coordinates"));
            this.timestampFound = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getImageRef() { return imageRef; }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Date getTimestampFound() {
        return timestampFound;
    }

    public void setTimestampFound(Date timestampFound) {
        this.timestampFound = timestampFound;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
