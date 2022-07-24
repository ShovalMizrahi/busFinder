package com.example.busfinder;

public class Track {
    private String lineID;
    private String stationID;
    private int order;

    public Track(String lineID, String stationID, int order) {
        this.lineID = lineID;
        this.stationID = stationID;
        this.order = order;
    }


    public String getLineID() {
        return lineID;
    }

    public void setLineID(String lineID) {
        this.lineID = lineID;
    }

    public String getStationID() {
        return stationID;
    }

    public void setStationID(String stationID) {
        this.stationID = stationID;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
