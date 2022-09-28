package com.example.busfinder;

public class Line {
    private String id;
    private String number;
    private String companyID;
    private ArrayListStation line_stations;

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

    public ArrayListStation getLineStations(){
        return line_stations;
    }

    public boolean existStation(Station s){
        line_stations =  RestApi.routes.get(getId());
        for (int i=0;i<line_stations.size();i++){
            if(Integer.parseInt(s.getId()) == Integer.parseInt(line_stations.get(i).getId()))
                return true;
        }
        return false;
    }
}
