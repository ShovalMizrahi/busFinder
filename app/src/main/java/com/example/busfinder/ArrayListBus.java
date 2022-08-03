package com.example.busfinder;

import java.util.ArrayList;

public class ArrayListBus extends ArrayList<Bus> {

    public ArrayListBus()
    {
        super();

    }

    public ArrayListBus(ArrayListBus arrayListBus) {
        for (int i = 0; i < arrayListBus.size(); i++) {
            this.add(new Bus(arrayListBus.get(i))) ;

        }

    }


    //c - the index of the current info
    //l - the index of the last info
    public static Station findNextStation(int c, int l)
    {
        //creating list of all stations that the coming close to
        ArrayListStation stations = new ArrayListStation();

        for(int i=0; i<RestApi.buses.get(c).getStations().size();i++)
        {
            for(int j=0; j<RestApi.lastBuses.get(l).getStations().size();j++)
            {
                if(RestApi.buses.get(c).getStations().get(i).getId().equals(RestApi.lastBuses.get(l).getStations().get(j).getId())
                &&   RestApi.buses.get(c).getStations().get(i).getDistanceFromBus() <  RestApi.lastBuses.get(l).getStations().get(j).getDistanceFromBus()  )
                {

                    return RestApi.buses.get(c).getStations().get(i);
                    //stations.add(new Station( RestApi.buses.get(c).getStations().get(i)));
                }

            }


        }


return null;

    }

}
