package com.inplanesight.models;

import java.io.Serializable;

public class Coordinates implements Serializable {
    Double latitude;
    Double longitude;

    public Coordinates() {}

    public Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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
        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }
}
