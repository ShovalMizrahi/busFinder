package com.example.busfinder;


import com.google.gson.Gson;

/*

add
implementation 'com.google.code.gson:gson:2.9.0'

to dependencies in gradle Moudle

and then: Sync now!
 */
public class Json {

    public static <T>  String convertObjectToJson( T object) {

        Gson gson = new Gson();
        return gson.toJson(object);

    }


    public static <T>  T convertJsonToObject( Class<T> clas , String json) {


        Gson gson = new Gson();

        return gson.fromJson(json, clas);

    }


}