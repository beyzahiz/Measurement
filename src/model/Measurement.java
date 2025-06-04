package model;

import java.time.LocalDateTime;

public class Measurement {
    private int id;
    private String type; // "TEMPERATURE" or "HUMIDITY"
    private String location;
    private LocalDateTime date;
    private double value;
    private LocalDateTime measurementTime;

    public Measurement(int id, String type, String location, LocalDateTime date, double value, LocalDateTime measurementTime) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.date = date;
        this.value = value;
        this.measurementTime = measurementTime;
    }

    // Getters
    public int getId() { return id; }
    public String getType() { return type; }
    public String getLocation() { return location; }
    public LocalDateTime getDate() { return date; }
    public double getValue() { return value; }
    public LocalDateTime getMeasurementTime() { return measurementTime; }
}
