package com.example.busfinder;

public class Line {
    private String id;
    private String number;
    private String companyID;
    public static ArrayListStation line_stations;

    private int order; //the order in the line list of stations

    private String arrivalTime;


    public Line(Line line) {
        this.id = line.id;
        this.number = line.number;
        this.companyID = line.companyID;

    }

    public Line(String id, String number, String companyID) {
        this.id = id;
        this.number = number;
        this.companyID = companyID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public static void addStationsOfLine() {
        for (int i = 0; i < RestApi.lines.size(); i++) {

            RestApi.lines.get(i).line_stations = new ArrayListStation();


            for (int j = 0; j < RestApi.tracks.size(); j++) {

                if (RestApi.tracks.get(j).getLineID().equals(RestApi.lines.get(i).getId())) {
                    ArrayListStation arrayListStation = new ArrayListStation();
                    Station station = new Station(arrayListStation.findStationById(RestApi.tracks.get(j).getStationID()));

                    RestApi.lines.get(i).line_stations.add(station);

                }

            }

        }
    }

    public static ArrayListStation getLine_stations() {
        return line_stations;
    }

    public static void setLine_stations(ArrayListStation line_stations) {
        Line.line_stations = line_stations;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
