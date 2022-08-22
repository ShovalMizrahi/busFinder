package com.example.busfinder;

import java.util.ArrayList;

public class ArrayListStation extends ArrayList<Station> {

    public ArrayListStation() {

    }


    public ArrayListStation(ArrayListStation arrayListBus) {
        for (int i = 0; i < arrayListBus.size(); i++) {

            this.add(new Station(arrayListBus.get(i)));

        }

    }


    public void bindLinesToStations() {
        for (int i = 0; i < RestApi.stations.size(); i++) {
            RestApi.stations.get(i).setLines(new ArrayListLine());
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

}
