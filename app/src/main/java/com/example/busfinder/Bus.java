package com.example.busfinder;

public class Bus {
    private String id;
    private String lineID;
    private String longt;
    private String lat;

    private Stations stations;

    //the next station can be mnore the minStationIndex
    private int minStationIndex;


    public Bus(Bus bus) {

        this.id = bus.id;
        this.lineID = bus.lineID;
        this.longt = bus.longt;
        this.lat = bus.lat;

        stations = new Stations();

        for (int i = 0; i < bus.getStations().size(); i++) {

            stations.add(new Station(bus.getStations().get(i)));

        }


    }

    public Bus(String id, String line, String longtitude, String latitude) {
        this.id = id;
        this.lineID = line;
        this.longt = longtitude;
        this.lat = latitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLine() {
        return lineID;
    }

    public void setLine(String line) {
        this.lineID = line;
    }

    public String getLongtitude() {
        return longt;
    }

    public void setLongtitude(String longtitude) {
        this.longt = longtitude;
    }

    public String getLatitude() {
        return lat;
    }

    public void setLatitude(String latitude) {
        this.lat = latitude;
    }



    public static void addStationsToBus() {


        for (int i = 0; i < RestApi.buses.size(); i++) {

          //  RestApi.buses.get(i).stations = new ArrayListStation();

            RestApi.buses.get(i).stations  = new Stations(RestApi.routes.get(RestApi.buses.get(i).getLine()));


            for(int j=0; j< RestApi.buses.get(i).stations.size(); j++  )
            {
                Bus bus = RestApi.buses.get(i);
                Station station = bus.stations.get(j);
                double distance = Station.getDistance(Double.parseDouble(bus.getLatitude()), Double.parseDouble(bus.getLongtitude()), Double.parseDouble(station.getLat()), Double.parseDouble(station.getLongt()));
                bus.stations.get(j).setDistanceFromBus(distance);


            }


        }


    }



    public static Bus findBusById(String busId)
    {

        for(int i=0; i<RestApi.buses.size();i++)
        {
            Bus bus = RestApi.buses.get(i);
            if(bus.getId().equals(busId))
                return bus;

        }

        return null;
    }

    public Stations getStations() {
        return stations;
    }

    public void setStations(Stations stations) {
        this.stations = stations;
    }

    public int getMinStationIndex() {
        return minStationIndex;
    }

    public void setMinStationIndex(int minStationIndex) {
        this.minStationIndex = minStationIndex;
    }
}
