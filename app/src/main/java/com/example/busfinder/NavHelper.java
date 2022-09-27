package com.example.busfinder;

public class NavHelper {
    private Line line;
    private Station start_station;
    private Station end_station;

    public NavHelper(Line line, Station start, Station end){
        this.line = line;
        start_station = start;
        end_station = end;
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
}
