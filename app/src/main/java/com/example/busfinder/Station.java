package com.example.busfinder;

public class Station {
    private String id;
    private String name;
    private String city;
    private String longt;
    private String lat;

    private ArrayListLine lines ; //the lines that going into the staions

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
}
