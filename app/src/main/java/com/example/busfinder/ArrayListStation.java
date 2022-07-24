package com.example.busfinder;

import java.util.ArrayList;

public class ArrayListStation extends ArrayList<Station> {

    public void bindLinesToStations() {
        for (int i = 0; i < RestApi.stations.size(); i++) {
            RestApi.stations.get(i).setLines(new ArrayListLine());
            for (int j = 0; j < RestApi.tracks.size(); j++) {

                if (RestApi.tracks.get(j).getStationID().equals(RestApi.stations.get(i).getId())) {


                    Line l = RestApi.lines.findLineById(RestApi.tracks.get(j).getLineID());
                    if(l!=null)
                    RestApi.stations.get(i).addLine(l);
                }

            }
        }
    }


}
