package com.example.busfinder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.busfinder.databinding.ActivityMenuBinding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding binding;

    FusedLocationProviderClient mFusedLocationClient;
    private static double latitude, longtitude;
    ArrayList<Double> dis_stations = new ArrayList<Double>();
    private static Stations sortedStations = new Stations();
    private final int PERMISSION_ID = 44;
    TextView route_text;

    ArrayList<NavHelper> routes;


    Stations nearDes, nearStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        // method to get the location
        getLastLocation();

        //getting station distances from user
        getStationDistances();


    }


    public void lineSearch(View view) {
        Intent intent = new Intent(this, LineSearchActivity.class);
        startActivity(intent);
    }

    public void stationSearch(View view) {
        Intent intent = new Intent(this, StationSearchActivity.class);
        startActivity(intent);
    }

    public void favoriteStations(View view) {
        Intent intent = new Intent(this, FavoriteStationsActivity.class);
        startActivity(intent);
    }

    public void favoriteLines(View view) {
        Intent intent = new Intent(this, FavoriteLinesActivity.class);
        startActivity(intent);
    }


    // getting the user's location
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitude = location.getLatitude();
                            longtitude = location.getLongitude();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    // updationg the user location every few seconds
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude();
            longtitude = mLastLocation.getLongitude();
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;


    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }

    public static double getLatitude() {
        return latitude;
    }

    public static double getLongtitude() {
        return longtitude;
    }

    //finding station distances from user
    public void getStationDistances() {

        sortedStations.clear();

        ArrayList<Double> sortedDis = new ArrayList<Double>();
        for (int i = 0; i < RestApi.stations.size(); i++) {
            dis_stations.add(RestApi.stations.get(i).getDistance());
            sortedDis.add(RestApi.stations.get(i).getDistance());

        }
        Collections.sort(sortedDis);
        for (int i = 0; i < sortedDis.size(); i++) {
            for (int j = 0; j < dis_stations.size(); j++) {
                if (sortedDis.get(i).equals(dis_stations.get(j)))
                    sortedStations.add(RestApi.stations.get(j));
            }
        }


        Log.d("arraytag", String.valueOf(dis_stations));
    }

    // returns the stations in order of proxmity
    public static Stations getSortedStations() {
        return sortedStations;
    }


    // finding a route between two locations the user put as start and end
    public void findRoute(View view) {
        nearDes = new Stations();
        nearStart = new Stations();

        TextView start = findViewById(R.id.start_station);
        TextView end = findViewById(R.id.end_station);

        LatLng startPoint = getLatLngFromAddress(start.getText().toString());
        if (startPoint == null) {
            Toast.makeText(this, "can't find start location", Toast.LENGTH_SHORT).show();
            return;
        }


        LatLng endPoint = getLatLngFromAddress(end.getText().toString());
        if (endPoint == null) {
            Toast.makeText(this, "can't find end location", Toast.LENGTH_SHORT).show();
            return;
        }

        double lngStart = startPoint.longitude;
        double latStart = startPoint.latitude;


        double lngEnd = endPoint.longitude;
        double latEnd = endPoint.latitude;

        double dis_from_des, dis_from_start;
        routes = new ArrayList<>();
        for (int i = 0; i < RestApi.stations.size(); i++) {
            dis_from_des = Station.getDistance(Double.parseDouble(RestApi.stations.get(i).getLat()), Double.parseDouble(RestApi.stations.get(i).getLongt()), latEnd, lngEnd);
            dis_from_start = Station.getDistance(Double.parseDouble(RestApi.stations.get(i).getLat()), Double.parseDouble(RestApi.stations.get(i).getLongt()), latStart, lngStart);
            if (dis_from_des < 0.4)
                nearDes.add(RestApi.stations.get(i));
            if (dis_from_start < 0.4) {
                nearStart.add(RestApi.stations.get(i));
            }

        }

        findOneBus();


        route_text = findViewById(R.id.routeText);
        route_text.setText("");


        if (routes.size() == 0) {
            findTwoBus();

        } else {
            showOneBusWays();
            return;
        }


        if (routes.size() == 0) {
            route_text.setLines(1);
            route_text.setText("No suitable route was found for the requested destination ");
        } else {

            route_text.setLines(2);


            showTwoBusWays();

        }


    }

    //show all the ways of one buse
    private void showOneBusWays() {
        route_text.setText(routes.size() + " routes were found:\n");

        for (int i = 0; i < routes.size(); i++) {

            if (i != 0) {
                route_text.setLines(route_text.getMaxLines() + 1);
                route_text.append("\n");

            }


            NavHelper route = routes.get(i);

            route_text.setLines(route_text.getMaxLines() + 5);


            route_text.append("\n" + (i + 1) + ")boarding station:\n    " + route.getStartStation().getName());
            route_text.append("\nfinal station:\n   " + route.getEndStation().getName());
            route_text.append("\nline number: " + route.getLine().getNumber());
        }

    }

    //show all the ways of two buses
    private void showTwoBusWays() {
        route_text.setText(routes.size() + " routes were found:\n");

        for (int i = 0; i < routes.size(); i++) {

            if (i != 0) {
                route_text.setLines(route_text.getMaxLines() + 1);
                route_text.append("\n");

            }


            NavHelper route = routes.get(i);

            route_text.setLines(route_text.getMaxLines() + 8);

            route_text.append("\n" + (i + 1) + ")boarding station:\n    " + route.getStartStation().getName());
            route_text.append("\nsecond station:\n   " + route.getSecondStation().getName());
            route_text.append("\nline number: " + route.getFirstLine().getNumber());
            route_text.append("\nfinal station:\n   " + route.getEndStation().getName());
            route_text.append("\nline number: " + route.getLine().getNumber());
        }

    }

    //finding ways of two buses only
    private void findTwoBus() {
        for (int i = 0; i < nearDes.size(); i++) {
            for (int j = 0; j < nearStart.size(); j++) {
                for (int x = 0; x < RestApi.stations.size(); x++) {
                    for (int k = 0; k < RestApi.lines.size(); k++) {
                        for (int w = 0; w < RestApi.lines.size(); w++) {
                            Station secondStation = RestApi.stations.get(x);
                            if (RestApi.lines.get(k).existStation(nearDes.get(i)) && RestApi.lines.get(k).existStation(secondStation)
                                    && RestApi.lines.get(w).existStation(nearStart.get(i)) && RestApi.lines.get(w).existStation(secondStation)) {
                                routes.add(new NavHelper(RestApi.lines.get(k), nearStart.get(j), nearDes.get(i), secondStation,RestApi.lines.get(w)));

                            }
                        }
                    }
                }
            }
        }

    }

    //finding ways of one bus only
    private void findOneBus() {
        for (int i = 0; i < nearDes.size(); i++) {
            for (int j = 0; j < nearStart.size(); j++) {
                for (int k = 0; k < RestApi.lines.size(); k++) {
                    if (RestApi.lines.get(k).existStation(nearDes.get(i)) && RestApi.lines.get(k).existStation(nearStart.get(j))) {
                        routes.add(new NavHelper(RestApi.lines.get(k), nearStart.get(j), nearDes.get(i)));
                    }
                }
            }
        }

    }


    private LatLng getLatLngFromAddress(String address) {

        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(address, 1);
            if (addressList != null) {
                Address singleaddress = addressList.get(0);
                LatLng latLng = new LatLng(singleaddress.getLatitude(), singleaddress.getLongitude());
                return latLng;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


}