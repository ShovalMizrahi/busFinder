package com.example.busfinder;

import java.util.ArrayList;

public class Lines extends ArrayList<Line> {


    //find line by his id
    public Line findLineById(String id) {
        for (int i = 0; i < RestApi.lines.size(); i++) {
            if (id.equals(RestApi.lines.get(i).getId())) {
                return RestApi.lines.get(i);
            }

        }
        return null;
    }


    //any line has company
    public static void bindLineToCompany() {
        for (int i = 0; i < RestApi.lines.size(); i++) {
            Line line = RestApi.lines.get(i);

            for (int j = 0; j < RestApi.companies.size(); j++) {
                Company company = RestApi.companies.get(j);

                if (company.getId().equals(line.getCompanyID())) {

                    RestApi.lineToCompany.put(line.getId(), company);
                }

            }

        }

    }



    public static boolean isBusContainedInLines(Bus bus, Lines lines) {

        for (int i = 0; i < lines.size(); i++) {
            if (bus.getLine().equals(lines.get(i).getId()))
                return true;

        }

        return false;
    }


}
