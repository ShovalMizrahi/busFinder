package com.example.busfinder;

import com.example.busfinder.MenuActivity;

public class Station {
    private String id;
    private String name;
    private String city;
    private String longt;
    private String lat;


    private double distanceFromBus;



    private ArrayListLine lines ; //the lines that going into the staions
    private double distance;
    private final int earth_radius = 6371000;

    public Station(Station station)
    {
        this.id = station.id;

        this.name = station.name;
        this.city = station.city;
        this.longt = station.longt;
        this.lat = station.lat;
        this.lines = station.lines;
        this.distanceFromBus = station.distanceFromBus;

    }

    public Station(String name, String city, String longt, String lat, ArrayListLine lines) {
        this.name = name;
        this.city = city;
        this.longt = longt;
        this.lat = lat;
        this.lines = lines;
    }

    public Station(String id, String name, String city, String longt, String lat) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.longt = longt;
        this.lat = lat;
    }


    public Station(String id, String name, String longt, String lat) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.longt = longt;
        this.lat = lat;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setLongtitude(String longtitude) {

    }

    public String getLongt() {
        return longt;
    }

    public void setLongt(String longt) {
        this.longt = longt;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public ArrayListLine getLines() {

        return lines;
    }

    public void setLines(ArrayListLine lines) {
        this.lines = lines;
    }

    public void addLine(Line line) {
        lines.add(line);
    }


    public double getDistanceFromBus() {
        return distanceFromBus;
    }

    public void setDistanceFromBus(double distanceFromBus) {
        this.distanceFromBus = distanceFromBus;
    }


    public static double getDistance(double latS, double longS, double latD, double longD) {
        if ((longS == latD) && (longS == longD)) {
            return 0.0;
        }
        else {
            double theta = longS - longD;
            double dist = Math.sin(Math.toRadians(latS)) * Math.sin(Math.toRadians(latD)) + Math.cos(Math.toRadians(latS)) * Math.cos(Math.toRadians(latD)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
                dist = dist * 1.609344;


          //  System.out.println("\n\n\n"+dist +"\n\n\n");

           // return Math.random();
            return (dist);
        }
    }


    public Double getDistance() {
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(MenuActivity.getLatitude() - Double.parseDouble(getLat()));
        double dLon = Math.toRadians(MenuActivity.getLongtitude() - Double.parseDouble(getLongt()));

        // convert to radians
        double user_lat = Math.toRadians(MenuActivity.getLatitude());
        double station_lat = Math.toRadians(Double.parseDouble(getLat()));

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(user_lat) *
                        Math.cos(station_lat);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }


}
