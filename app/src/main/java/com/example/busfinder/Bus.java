package com.example.busfinder;

import java.util.ArrayList;

public class Bus {
    private String id;
    private String lineID;
    private String longt;
    private String lat;

    private ArrayListStation stations;

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

    public static void addStationsToBus() {

        for (int i = 0; i < RestApi.buses.size(); i++) {


            RestApi.buses.get(i).stations = new ArrayListStation();

        for (int j = 0; j < RestApi.tracks.size(); j++) {

            if (RestApi.tracks.get(j).getLineID().equals(RestApi.buses.get(i).lineID)) {
                ArrayListStation arrayListStation = new ArrayListStation();
                Station station = arrayListStation.findStationById(RestApi.tracks.get(j).getStationID());
                RestApi.buses.get(i).stations.add(station);
                RestApi.buses.get(i).stations.get(RestApi.buses.get(i).stations.size() - 1).setDistanceFromBus(Station.getDistance(Double.parseDouble(RestApi.buses.get(i).getLatitude()), Double.parseDouble(RestApi.buses.get(i).getLongtitude()), Double.parseDouble(station.getLat()), Double.parseDouble(station.getLongt())));

            }

        }

        }


    }

    public ArrayListStation getStations() {
        return stations;
    }

    public void setStations(ArrayListStation stations) {
        this.stations = stations;
    }
}
