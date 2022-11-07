package com.inplanesight.models;

import java.io.Serializable;

public class Airport implements Serializable {
    String name;
    String city;
    String country;
    String code;
    Coordinates coordinates;

    public Airport() {}

    public Airport(String name, String city, String country, String code, Coordinates coordinates) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.code = code;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String toString() {
        return name + " (" + code + ")";
    }
}
