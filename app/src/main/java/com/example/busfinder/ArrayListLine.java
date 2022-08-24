package com.example.busfinder;

import java.util.ArrayList;

public class ArrayListLine extends ArrayList<Line> {

    public Line findLineById(String id) {
        for (int i = 0; i < RestApi.lines.size(); i++) {
            if (id.equals(RestApi.lines.get(i).getId())) {
                return RestApi.lines.get(i);
            }

        }
        return null;
    }


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
}
