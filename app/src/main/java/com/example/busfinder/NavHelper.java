package com.example.busfinder;

public class NavHelper {
    private Line line; //line of one bus, or second line in two buses
    private Line firstLine; //first line in two buses
    private Station startStation;
    private Station endStation;
    private Station secondStation;

    public NavHelper(Line line, Station start, Station end){
        this.line = line;
        startStation = start;
        endStation = end;
    }



    public NavHelper(Line line, Station start, Station end,Station second_station,Line firstLine){
        this.line = line;
        startStation = start;
        endStation = end;
        this.secondStation = second_station;
        this.firstLine = firstLine;
    }


    public Line getLine() {
        return line;
    }

    public Station getEndStation() {
        return endStation;
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getSecondStation() {
        return secondStation;
    }

    public Line getFirstLine() {
        return firstLine;
    }
}