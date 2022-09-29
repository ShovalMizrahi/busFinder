package com.example.busfinder;

import java.util.ArrayList;
import java.util.HashMap;

public class Stations extends ArrayList<Station> {

    private HashMap<String, Stations> busToLines = new HashMap<String, Stations>(); //any bus has list of lines


    public Stations() {

    }


    public Stations(Stations arrayListBus) {
        for (int i = 0; i < arrayListBus.size(); i++) {

            this.add(new Station(arrayListBus.get(i)));

        }

    }


    public HashMap<String, Stations> getBusToLines() {
        return busToLines;
    }

    public void setBusToLines(HashMap<String, Stations> busToLines) {
        this.busToLines = busToLines;
    }

    public void bindLinesToStations() {
        for (int i = 0; i < RestApi.stations.size(); i++) {
            RestApi.stations.get(i).setLines(new Lines());
            for (int j = 0; j < RestApi.tracks.size(); j++) {

                if (RestApi.tracks.get(j).getStationID().equals(RestApi.stations.get(i).getId())) {


                    Line l = new Line(RestApi.lines.findLineById(RestApi.tracks.get(j).getLineID()));
                    if (l != null) {
                        l.setOrder(RestApi.tracks.get(j).getStationOrder());
                        RestApi.stations.get(i).addLine(l);


                    }
                }

            }
        }
    }


    public Station findStationById(String id) {

        for (int i = 0; i < RestApi.stations.size(); i++) {

            if (id.equals(RestApi.stations.get(i).getId())) {

                return RestApi.stations.get(i);
            }
        }
        return null;
    }

    public static boolean isStationConsistList(Station station, Stations stations) {

        for (int i = 0; i < stations.size(); i++) {
            if (station.getId().equals(stations.get(i).getId()))
                return true;

        }

        return false;
    }

    public static void bindStationToCity() {


        for (int i = 0; i < RestApi.stations.size(); i++) {
            Station station = RestApi.stations.get(i);
            station.setCity(Cities.findCityById(station.getCityId()));


        }


    }
}
