package com.example.busfinder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.Arrays;
import java.util.Collections;

public class MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding binding;

    FusedLocationProviderClient mFusedLocationClient;
    private static double latitude, longtitude;
    ArrayList<Double> dis_stations = new ArrayList<Double>();
    private static ArrayListStation sortedStations = new ArrayListStation();
    private final int PERMISSION_ID = 44;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "place";
    private static String apiKey = "AIzaSyD8LIod8GgrDssZ3WA-SLAsrs3iM-BihvI";
    public Place startPlace = null, endPlace = null;
    private int id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        Places.initialize(getApplicationContext(), apiKey);
        PlacesClient placesclient = Places.createClient(this);

        // method to get the location
        getLastLocation();

        getStationDistances();



    }


    public void lineSearch(View view){
        Intent intent = new Intent (this, LineSearchActivity.class);
        startActivity(intent);
    }

    public void stationSearch(View view){
        Intent intent = new Intent (this, StationSearchActivity.class);
        startActivity(intent);
    }

    public void favoriteStations(View view){
        Intent intent = new Intent (this, FavoriteStationsActivity.class);
        startActivity(intent);
    }

    public void favoriteLines(View view){
        Intent intent = new Intent (this, FavoriteLinesActivity.class);
        startActivity(intent);
    }

    public void navigate(View view){
        Intent intent = new Intent (this, NavigateActivity.class);
        startActivity(intent);
    }

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

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
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

    public void getStationDistances() {

        sortedStations.clear();

        ArrayList<Double> sortedDis= new ArrayList<Double>();
        for (int i = 0; i< RestApi.stations.size(); i++){
            dis_stations.add(RestApi.stations.get(i).getDistance());
            sortedDis.add(RestApi.stations.get(i).getDistance());

        }
        Collections.sort(sortedDis);
        for (int i = 0; i< sortedDis.size(); i++){
            for (int j=0; j< dis_stations.size();j++){
                if(sortedDis.get(i).equals(dis_stations.get(j)))
                    sortedStations.add(RestApi.stations.get(j));
            }
        }


        Log.d("arraytag", String.valueOf(dis_stations));
    }

    public static ArrayListStation getSortedStations(){
        return sortedStations;
    }



    public void startAutoCompleteActivity(View view) {
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, Arrays.asList(Place.Field.ID, Place.Field.NAME))
                .build(this);
        id = view.getId();
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }


    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data){
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            if (id == R.id.button6)
                startPlace = place;
            if (id == R.id.button8)
                endPlace = place;
            putPlace(place, id);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void putPlace(Place place, int id){
        if (id == R.id.button6) {
            TextView start = findViewById(R.id.start_station);
            start.setText(place.getName());
        }
        else {
            TextView end = findViewById(R.id.end_station);
            end.setText(place.getName());
        }
    }

    public void findRoute(View view){
        ArrayList<Station> nearDes = new ArrayList<>();
        ArrayList<Station> nearStart = new ArrayList<>();
        double lngStart = startPlace.getLatLng().longitude;
        double latStart = startPlace.getLatLng().latitude;
        double lngEnd = endPlace.getLatLng().longitude;
        double latEnd = endPlace.getLatLng().latitude;
        double dis_from_des, dis_from_start;
        ArrayList<Line> routes = new ArrayList<>();
        for (int i=0;i<RestApi.stations.size();i++){
            dis_from_des = Station.getDistance(Double.parseDouble(RestApi.stations.get(i).getLat()), Double.parseDouble(RestApi.stations.get(i).getLongt()), latEnd, lngEnd);
            dis_from_start = Station.getDistance(Double.parseDouble(RestApi.stations.get(i).getLat()), Double.parseDouble(RestApi.stations.get(i).getLongt()), latStart, lngStart);
            if(dis_from_des < 200)
                nearDes.add(RestApi.stations.get(i));
            if (dis_from_start < 200)
                nearStart.add(RestApi.stations.get(i));

        }

        for (int i=0;i<nearDes.size();i++){
            for (int j=0;j<nearStart.size();j++){
                for (int k=0;k<RestApi.lines.size();k++){
                    if (RestApi.lines.get(k).existStation(nearDes.get(i)) && RestApi.lines.get(k).existStation(nearStart.get(j))){
                        routes.add(RestApi.lines.get(k));
                    }
                }
            }
        }


    }


}