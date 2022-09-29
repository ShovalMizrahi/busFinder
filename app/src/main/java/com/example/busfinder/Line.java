package com.example.busfinder;

public class Line {
    private String id;
    private String number;
    private String companyID;
    private Stations line_stations;

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

    public Stations getLineStations(){
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
