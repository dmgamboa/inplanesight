package com.inplanesight.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Flight implements Serializable {
    private String number;
    private String status;
    /**
     * TODO: Implement origin / destination as Airport
     */
//    private Airport origin;
//    private Airport destination;
    private String origin;
    private String destination;
    private String terminal;
    private String gate;
    /**
     * Not always provided
     */
    private LocalDateTime time;

    public Flight(String number, String status, String origin, String destination, String terminal, String gate, LocalDateTime time) {
        this.number = number;
        this.status = status;
        this.origin = origin;
        this.destination = destination;
        this.terminal = terminal;
        this.gate = gate;
        this.time = time;
    }

    /**
     * TODO: Implement origin / destination as Airport
     */
//    public Flight(String number, Airport origin, Airport destination, int terminal, String gate, LocalDateTime time) {
//        this.number = number;
//        this.origin = origin;
//        this.destination = destination;
//        this.terminal = terminal;
//        this.gate = gate;
//        this.time = time;
//    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    //    public Airport getOrigin() {
//        return origin;
//    }
//
//    public void setOrigin(Airport origin) {
//        this.origin = origin;
//    }
//
//    public Airport getDestination() {
//        return destination;
//    }
//
//    public void setDestination(Airport destination) {
//        this.destination = destination;
//    }

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
