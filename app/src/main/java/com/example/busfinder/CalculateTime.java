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

        String textResult = "";
        String result = "";


        String latS = params[0];
        String longS = params[1];

        String latD = params[2];
        String longD = params[3];


        String url =
                "https://www.google.com/maps/dir/" + latS + ",+" + longS + "/" + latD + ",+" + longD + "/@32.3244815,34.854499,17z/data=!3m1!4b1!4m10!4m9!1m3!2m2!1d34.8568182!2d32.3247562!1m3!2m2!1d34.8565802!2d32.3243541!3e0?hl=en\n";


        System.out.println("the url is " + url);

        //"https://www.google.com/maps/dir/" + latS + ",+" + longS + "/" + latD + ",+" + longD + "/?hl=en"

        try {
            textResult = SocketConnection
                    .getURLSource(url);


            for (int j = 0; j < 1; j++) {


                char slesh = (char) 92;
                char quotes = '"';
                String bb = "min" + slesh + quotes;

                int end = textResult.indexOf(bb) + bb.length();
                int i = end - 2;
                while (textResult.charAt(i) != quotes) {
                    i--;
                }
                int start = i;

                result = textResult.substring(start + 1, end - 2);

                if (result.indexOf(" ") == -1 || isNumeric(result.split(" ")[0]) == false)
                    j--;


                textResult = textResult.substring(end, textResult.length());

            }

        } catch (Exception e) {
            System.out.println(e);


        }


        return result;
    }


    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
