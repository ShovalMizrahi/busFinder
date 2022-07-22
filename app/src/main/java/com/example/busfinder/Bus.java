package com.example.busfinder;

public class Bus {
    private String id;
    private String lineID;
    private String longt;
    private String lat;


    public Bus(String id, String line, String longtitude, String latitude) {
        this.id = id;
        this.lineID = line;
        this.longt = longtitude;
        this.lat = latitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLine() {
        return lineID;
    }

    public void setLine(String line) {
        this.lineID = line;
    }

    public String getLongtitude() {
        return longt;
    }

    public void setLongtitude(String longtitude) {
        this.longt = longtitude;
    }

    public String getLatitude() {
        return lat;
    }

    public void setLatitude(String latitude) {
        this.lat = latitude;
    }
}
