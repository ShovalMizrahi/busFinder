package com.example.busfinder;


import android.os.AsyncTask;
import android.os.StrictMode;

import com.example.busfinder.ArrayListBus;
import com.example.busfinder.ArrayListLine;
import com.example.busfinder.ArrayListStation;
import com.example.busfinder.ArrayListTrack;
import com.example.busfinder.Json;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class CalculateTime extends AsyncTask<String, String, String> {

    private static final String URL = "https://transportation-server.almogshaby.repl.co/";

    private static HttpURLConnection con;
    public static ArrayListStation stations = new ArrayListStation();
    public static ArrayListLine lines = new ArrayListLine();
    public static ArrayListBus buses = new ArrayListBus();
    public static ArrayListTrack tracks = new ArrayListTrack();


    public CalculateTime() {
        //set context variables if required
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {


        String a = "";


        try {
            a = SocketConnection.getURLSource("https://www.google.com/maps/dir/%D7%94%D7%92%D7%99%D7%9C%D7%94+15,+%D7%A0%D7%AA%D7%A0%D7%99%D7%94%E2%80%AD/%D7%A9%D7%9C%D7%95%D7%9D+%D7%A6%D7%9C%D7%97+41,+%D7%A4%D7%AA%D7%97+%D7%AA%D7%A7%D7%95%D7%95%D7%94%E2%80%AD/@32.1909608,34.7393712,11z/data=!3m1!4b1!4m14!4m13!1m5!1m1!1s0x151d416b00544c55:0x13b492b5758ef7a5!2m2!1d34.8473822!2d32.3143144!1m5!1m1!1s0x151d36813d4bee8b:0x6ee6e52dfb428779!2m2!1d34.9101594!2d32.0743736!3e0?hl=en");
            System.out.println(a.length());

        } catch (Exception e) {
            System.out.println(e);

            //	System.out.println("Hello World");

        }

        return "works";


    }
}
