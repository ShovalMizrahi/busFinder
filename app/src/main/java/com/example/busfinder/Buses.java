package com.example.busfinder;

import java.util.ArrayList;

public class Buses extends ArrayList<Bus> {

    public Buses() {
        super();

    }

    public Buses(Buses arrayListBus) {
        for (int i = 0; i < arrayListBus.size(); i++) {
            this.add(new Bus(arrayListBus.get(i)));

        }

    }


    //c - the index of the current info
    //l - the index of the last info
    public static Station findNextStation(int c, int l) {


        for (int i = 0; i < RestApi.buses.get(c).getStations().size(); i++) {
            Bus bus = RestApi.buses.get(c);

            Station station = RestApi.buses.get(c).getStations().get(i);
            if (station.getDistanceFromBus() < 0.01) {

                if (RestApi.minStation.get(bus.getId()) == null || (RestApi.minStation.get(bus.getId()) != null && RestApi.minStation.get(bus.getId()) < i)) {

                    RestApi.minStation.put(bus.getId(), i);
                    if(i+1<RestApi.buses.get(c).getStations().size())
                    return RestApi.buses.get(c).getStations().get(++i);

                }


            }


        }




        for (int i = 0; i < RestApi.buses.get(c).getStations().size(); i++) {
            Bus bus = RestApi.buses.get(c);

            for (int j = 0; j < RestApi.lastBuses.get(l).getStations().size(); j++) {

                if (RestApi.buses.get(c).getStations().get(i).getId().equals(RestApi.lastBuses.get(l).getStations().get(j).getId())
                        && RestApi.buses.get(c).getStations().get(i).getDistanceFromBus() < RestApi.lastBuses.get(l).getStations().get(j).getDistanceFromBus()
                        && ((RestApi.minStation.get(bus.getId()) != null && RestApi.minStation.get(bus.getId()) < i) || RestApi.minStation.get(bus.getId()) == null)
                ) {

                    return RestApi.buses.get(c).getStations().get(i);
                }

            }


        }


        return null;

    }

}
