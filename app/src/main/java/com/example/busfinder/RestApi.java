package com.example.busfinder;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class RestApi extends AsyncTask<String, String, String> {

    private static final String URL = "https://transportation-server.almogshaby.repl.co/";

    private static HttpURLConnection con;
    public static ArrayListStation stations= new ArrayListStation();
    public static ArrayListLine lines = new ArrayListLine();
    public static ArrayListBus buses = new ArrayListBus();
    public static ArrayListBus lastBuses = new ArrayListBus();

    public static ArrayListTrack tracks = new ArrayListTrack();
    public  static Route routes = new Route();

    public static HashMap<String, Station> nextStation = new HashMap<String, Station>();
    public static HashMap<String, Integer> minStation = new HashMap<String, Integer>(); //the index of







    public RestApi() {
        //set context variables if required
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);


        String url = URL;

        String urlParameters = params[0];

        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

        try {

            URL myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();


            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Java client");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                //System.out.println(wr.getClass());

                wr.write(postData);
            }


            StringBuilder content;

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {


                String line;
                content = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }


                String adjusted = content.toString().replaceAll("(?m)^[ \t]*\r?\n", "");

                if (params[0].equals("function=showStations"))
                    stations = Json.convertJsonToObject(ArrayListStation.class, adjusted);

                if (params[0].equals("function=showLines"))
                    lines = Json.convertJsonToObject(ArrayListLine.class, adjusted);

                if (params[0].equals("function=showBuses"))
                    buses = Json.convertJsonToObject(ArrayListBus.class, adjusted);

                if (params[0].equals("function=showTracks"))
                    tracks = Json.convertJsonToObject(ArrayListTrack.class, adjusted);


                if (params[0].equals("function=showRoutes"))
                    routes = Json.convertJsonToObject(Route.class, adjusted);


                System.out.println(adjusted);

            }


        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            con.disconnect();
        }

        return "Hhhh";


    }
}
