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

}
