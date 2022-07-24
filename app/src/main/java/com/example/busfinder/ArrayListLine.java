package com.example.busfinder;

import java.util.ArrayList;

public class ArrayListLine extends ArrayList<Line> {

    public Line findLineById(String id)
    {
        for(int i=0;i<RestApi.lines.size();i++)
        {
            if(id.equals(RestApi.lines.get(i).getId()))
            {
                return RestApi.lines.get(i);
            }

        }
        return null;
    }
}
