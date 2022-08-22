package com.example.busfinder;

public class Track {
    private String lineID;
    private String stationID;
    private int stationOrder;

    public Track(String lineID, String stationID, int order) {
        this.lineID = lineID;
        this.stationID = stationID;
        this.stationOrder = order;
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

    public int getStationOrder() {
        return stationOrder;
    }

    public void setStationOrder(int stationOrder) {
        this.stationOrder = stationOrder;
    }
}
