package com.example.busfinder;

import java.util.ArrayList;

public class ArrayListStation extends ArrayList<Station> {

    public void bindLinesToStations()
    {
        for(int i=0;i<  size();i++)
        {
            for(int j=0; j<RestApi.lines.size();j++)
            {

                if(RestApi.lines.get(j).getId().equals(get(i).getId()))
                {

                    get(i).addLine(RestApi.lines.get(j));
                }

            }
        }
    }


}
