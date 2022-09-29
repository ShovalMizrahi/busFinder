package com.example.busfinder;

public class NavHelper {
    private Line line; //line of one bus, or second line in two buses
    private Line firstLine; //first line in two buses
    private Station start_station;
    private Station end_station;


    private Station second_station;

    public NavHelper(Line line, Station start, Station end){
        this.line = line;
        start_station = start;
        end_station = end;
    }



    public NavHelper(Line line, Station start, Station end,Station second_station,Line firstLine){
        this.line = line;
        start_station = start;
        end_station = end;
        this.second_station = second_station;
        this.firstLine = firstLine;
    }


    public Line getLine() {
        return line;
    }

    public Station getEnd_station() {
        return end_station;
    }

    public Station getStart_station() {
        return start_station;
    }

    public Station getSecond_station() {
        return second_station;
    }

    public Line getFirstLine() {
        return firstLine;
    }
}