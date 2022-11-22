package com.inplanesight.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Flight implements Serializable {
    private String airline;
    private String number;
    private String status;
    private String destination;
    private String terminal;
    private String gate;
    private LocalDateTime time;

    public Flight(String airline, String number, String status, String destination, String terminal, String gate, LocalDateTime time) {
        this.airline = airline;
        this.number = number;
        this.status = status;
        this.destination = destination;
        this.terminal = terminal;
        this.gate = gate;
        this.time = time;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String number) {
        this.airline = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
