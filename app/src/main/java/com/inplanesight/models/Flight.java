package com.inplanesight.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Flight implements Serializable {
    private String number;
    private Airport origin;
    private Airport destination;
    private int terminal;
    private String gate; /** Not always provided */
    private LocalDateTime time;
}
