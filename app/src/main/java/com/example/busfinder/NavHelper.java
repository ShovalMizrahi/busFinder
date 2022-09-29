package com.example.busfinder;

public class NavHelper {
    private Line line; //line of one bus, or second line in two buses
    private Line firstLine; //first line in two buses
    private Station startStation;
    private Station endStation;


    private Station second_station;

    public NavHelper(Line line, Station start, Station end){
        this.line = line;
        startStation = start;
        endStation = end;
    }



    public NavHelper(Line line, Station start, Station end,Station second_station,Line firstLine){
        this.line = line;
        startStation = start;
        endStation = end;
        this.second_station = second_station;
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

    public Station getSecond_station() {
        return second_station;
    }

    public Line getFirstLine() {
        return firstLine;
    }
}