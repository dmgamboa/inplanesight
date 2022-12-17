package com.inplanesight.models;

import org.json.JSONObject;

import java.io.Serializable;

public class Coordinates implements Serializable {
    Double lat;
    Double lng;

    public Coordinates() {}

    public Coordinates(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Coordinates(JSONObject coordinates) {
        try {
            this.lat = coordinates.getDouble("latitude");
            this.lng = coordinates.getDouble("longitude");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Double getLatitude() {
        return lat;
    }

    public void setLatitude(Double lat) {
        this.lat = lat;
    }

    public Double getLongitude() {
        return lng;
    }

    public void setLongitude(Double lng) {
        this.lng = lng;
    }

    /** Adapted from David George's post
     * https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude */
    public static Double getDistance(Coordinates a, Coordinates b) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(a.getLatitude() - b.getLatitude());
        double lonDistance = Math.toRadians(a.getLongitude() - b.getLongitude());
        double x = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(a.getLatitude())) * Math.cos(Math.toRadians(b.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(x), Math.sqrt(1 - x));
        double distance = R * c * 1000;

        return distance;
    }
}
