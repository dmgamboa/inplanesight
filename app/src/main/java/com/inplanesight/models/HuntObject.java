package com.inplanesight.models;

import java.io.Serializable;
import java.util.Date;

public class HuntObject implements Serializable {
    String name;
    String id;
    String imageUrl;
    Coordinates coordinates;
    Date timestampFound;

    public HuntObject(String name, String id, String imageUrl, Coordinates coordinates, Date timestampFound) {
        this.name = name;
        this.id = id;
        this.imageUrl = imageUrl;
        this.coordinates = coordinates;
        this.timestampFound = null;
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
