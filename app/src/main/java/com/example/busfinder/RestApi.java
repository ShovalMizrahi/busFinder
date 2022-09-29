package com.example.busfinder;


import android.os.AsyncTask;
import android.os.StrictMode;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import java.util.HashMap;



public class RestApi extends AsyncTask<String, String, String> {

    private static final String URL = "https://transportation-server.almogshaby.repl.co/";

    private static HttpURLConnection con;
    public static Stations stations = new Stations();
    public static Lines lines = new Lines();
    public static Buses buses = new Buses();
    public static Buses lastBuses = new Buses();

    public static Tracks tracks = new Tracks();
    public static Route routes = new Route();
    public static Companies companies = new Companies();
    public static Cities cities = new Cities();


    public static HashMap<String, Station> nextStation = new HashMap<String, Station>();
    public static HashMap<String, Integer> minStation = new HashMap<String, Integer>(); //the order number of the last station


    public static HashMap<String, Company> lineToCompany = new HashMap<String, Company>(); //Each line id has company


    public RestApi() {
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
                    stations = Serialization.convertJsonToObject(Stations.class, adjusted);

                if (params[0].equals("function=showLines"))
                    lines = Serialization.convertJsonToObject(Lines.class, adjusted);

                if (params[0].equals("function=showBuses"))
                    buses = Serialization.convertJsonToObject(Buses.class, adjusted);

                if (params[0].equals("function=showTracks"))
                    tracks = Serialization.convertJsonToObject(Tracks.class, adjusted);


                if (params[0].equals("function=showRoutes"))
                    routes = Serialization.convertJsonToObject(Route.class, adjusted);

                if (params[0].equals("function=showCompanies"))
                    companies = Serialization.convertJsonToObject(Companies.class, adjusted);


                if (params[0].equals("function=showCities"))
                    cities = Serialization.convertJsonToObject(Cities.class, adjusted);


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
