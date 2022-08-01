package com.example.busfinder;

public class Line {
    private String id;
    private String number;
    private String companyID;


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
}
