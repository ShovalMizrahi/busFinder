package com.example.busfinder;

import java.util.ArrayList;

public class Cities extends ArrayList<City> {

    public static String findCityById(String id) {
        for (int i = 0; i < RestApi.cities.size(); i++) {
            City city = RestApi.cities.get(i);
            if (city.getId().equals(id)) {
                return city.getName();

            }

        }
        return "";

    }
}
