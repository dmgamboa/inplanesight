package com.inplanesight.models;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

public class Hunt implements Serializable {
    String airportCode;
    String name;
    String id;
    String imageUrl;
    Coordinates coordinates;
    Date timestampFound;

    public Hunt(String name, String id, String imageUrl, Coordinates coordinates, Date timestampFound) {
        this.name = name;
        this.id = id;
        this.imageUrl = imageUrl;
        this.coordinates = coordinates;
        this.timestampFound = null;
    }

    public Hunt(JSONObject hunt) {
        try {
            this.name = hunt.getString("name");
            this.id = hunt.getString("id");
            this.imageUrl = hunt.getString("imageUrl");
            this.coordinates = new Coordinates(hunt.getJSONObject("coordinates"));
            this.timestampFound = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Date getTimestampFound() {
        return timestampFound;
    }

    public void setTimestampFound(Date timestampFound) {
        this.timestampFound = timestampFound;
    }
}
