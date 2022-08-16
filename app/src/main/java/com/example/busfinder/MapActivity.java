package com.example.busfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements Runnable {

    int a = 10;
    int sadsdasd = 343434;
    int f = 0;
    private final int TIMEREFRESHINGBUSES = 1500;

    double d = 32.31791352999457;

    private MapView map = null;

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

/*
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
             //   Log.v(TAG, "Permission is granted");
            } else {
               // Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

*/


        Context ctx = this.getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = findViewById(R.id.mapview);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getController().setZoom(18.0);

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.INTERNET
        });
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setMultiTouchControls(true);


        CompassOverlay compassOverlay = new CompassOverlay(this, map);
        compassOverlay.enableCompass();
        map.getOverlays().add(compassOverlay);
/*
        GeoPoint point = new GeoPoint(32.29132455186295, 34.84691106686912);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(point);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        map.getOverlays().add(startMarker);

        map.getController().setCenter(point);

 */


        map.getOverlays().clear(); //for clearing

        for (int i = 0; i < RestApi.stations.size(); i++) {


            map.invalidate();

            GeoPoint point = new GeoPoint(Double.parseDouble(RestApi.stations.get(i).getLat()), Double.parseDouble(RestApi.stations.get(i).getLongt()));

            Marker startMarker = new Marker(map);
            startMarker.setPosition(point);


            Drawable drawable = getResources().getDrawable(R.mipmap.busstop);
            drawable = resize(drawable);

            startMarker.setIcon(drawable);

            // startMarker.setTitle("asd");


            //  startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            // startMarker.setAnchor(0.05f, 0.5f);

            startMarker.setAnchor(Marker.ANCHOR_BOTTOM, Marker.ANCHOR_BOTTOM);


            StationInfo stationInfo = new StationInfo(R.layout.custom_info_window, map, RestApi.stations.get(i));


            startMarker.setInfoWindow(stationInfo);


            //     map.getOverlays().clear(); //for clearing
            map.getOverlays().add(startMarker);


            map.getController().setCenter(point);


        }


        Thread t = new Thread(this);
        t.start();


        String add = c(32.31805699856178, 34.85507235003996);
      //  Toast.makeText(ctx, add + "this is", Toast.LENGTH_SHORT).show();



      //  Toast.makeText(ctx,RestApi.buses.get(0).getMinStationIndex()+" ", Toast.LENGTH_SHORT).show();


        startThreads();






    }

    private void startThreads() {
        new Thread(new Th1()).start();

    }


    class Th1 implements Runnable {
        @Override
        public void run() {
            CalculateTime calculateTime = new CalculateTime();

            System.out.println(calculateTime.doInBackground());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /*
    public void next(View view) {

        map.invalidate();

        GeoPoint point = new GeoPoint(32.31791352999457, 34.856175118587956);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(point);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        map.getOverlays().add(startMarker);


        GeoPoint point1 = new GeoPoint(32.32154021041925, 34.85621803393249);

        Marker startMarker1 = new Marker(map);
        startMarker1.setPosition(point);
        startMarker1.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        map.getOverlays().add(startMarker1);

        map.getController().setCenter(point);

        //  map.invalidate();


    }
*/
    @Override
    public void run() {



        Boolean success = false;


        RestApi restApi = new RestApi();
        while (true) {

            //making the copy before the info is chaning!
            RestApi.lastBuses = new ArrayListBus(RestApi.buses );


            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            RestApi.buses.clear();
            while (!success) {


                try {
                    restApi.doInBackground("function=showBuses");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (RestApi.buses.size() > 0) {
                    success = true;

                        /*
        add the station route for each bus

         */

                    Bus.addStationsToBus();


                }


             else{

                try {
                    Thread.sleep(TIMEREFRESHINGBUSES);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        success = false;


            /*

            finding changing in the bus location
             */

            for(int i=0; i<RestApi.buses.size();i++)
            {
                Bus bus = RestApi.buses.get(i);
                for(int j=0; j<RestApi.lastBuses.size();j++)
                {
                    Bus lastBus = RestApi.lastBuses.get(j);

                    if(bus.getId().equals(lastBus.getId())
                    &&( !bus.getLatitude().equals(lastBus.getLatitude())
                        || !bus.getLongtitude() .equals( lastBus.getLongtitude())))
                    {
                        Station nextStation = ArrayListBus.findNextStation(i,j) ;
                        if(nextStation!=null)
                        RestApi.nextStation.put(RestApi.buses.get(i).getId(), nextStation);
                        //RestApi.nextStation.put(RestApi.buses.get(i).getId(), new Station (ArrayListBus.findNextStation(i,j) ));
                    }

                }

            }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                StationInfo.closeAllInfoWindowsOn(map);
                removeBuses(map);

                for (int i = 0; i < RestApi.buses.size(); i++) {
                    ArrayListLine arrayListLine = new ArrayListLine();
                    Line line = arrayListLine.findLineById(RestApi.buses.get(i).getLine());

                    //     Toast.makeText(MapActivity.this, RestApi.buses.get(i).getLine()+"cccc", Toast.LENGTH_SHORT).show();

                    map.invalidate();

                    //   Toast.makeText(MapActivity.this, RestApi.buses.get(0).getLatitude() + "", Toast.LENGTH_SHORT).show();

                    GeoPoint point = new GeoPoint(Double.parseDouble(RestApi.buses.get(i).getLatitude()), Double.parseDouble(RestApi.buses.get(i).getLongtitude()));

                    Marker startMarker = new Marker(map);
                    startMarker.setPosition(point);


                    Drawable drawable = getResources().getDrawable(R.mipmap.bus);
                    drawable = resize(drawable);
                    startMarker.setIcon(drawable);

                    startMarker.setAnchor(Marker.ANCHOR_BOTTOM, Marker.ANCHOR_BOTTOM);

                    BusInfo infoWindow = new BusInfo(R.layout.custom_bus_info_window, map, line,RestApi.buses.get(i));


                    startMarker.setInfoWindow(infoWindow);
                    startMarker.showInfoWindow();


                    //     map.getOverlays().clear(); //for clearing
                    map.getOverlays().add(startMarker);


                    //     map.getController().setCenter(point);


                }


            }
        });
    }

}
/*
    @Override
    public void run() {

        Context ctx = this.getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = findViewById(R.id.mapview);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getController().setZoom(18.0);

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.INTERNET
        });
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setMultiTouchControls(true);


        CompassOverlay compassOverlay = new CompassOverlay(this, map);
        compassOverlay.enableCompass();
        map.getOverlays().add(compassOverlay);

        //while(true)
        {

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            map.invalidate();

            GeoPoint point = new GeoPoint(32.31791352999457, 34.856175118587956);

            Marker startMarker = new Marker(map);
            startMarker.setPosition(point);
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            map.getOverlays().add(startMarker);




            map.getController().setCenter(point);


        }


    }

 */


    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 71, 100, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }


private class BusInfo extends InfoWindow {
    private Line line;
    private Bus bus;

    public BusInfo(int layoutResId, MapView mapView, Line line, Bus bus) {
        super(layoutResId, mapView);
        this.line = line;
        this.bus = bus;
    }

    public void onClose() {
    }

    public void onOpen(Object arg0) {
        // LinearLayout layout = (LinearLayout) findViewById(R);
        TextView btnMoreInfo = mView.findViewById(R.id.busInfoLine);
        TextView busInfoNextStop = mView.findViewById(R.id.busInfoNextStop);
       // if (bus.getStations() != null)
       //     btnMoreInfo.setText(line.getNumber() + " " +this.bus.getStations().get(0).getDistanceFromBus() +" " +this.bus.getStations().get(1).getDistanceFromBus()  );
       //    btnMoreInfo.setText(this.bus.getStations().get(0).getDistanceFromBus() +" " +this.bus.getStations().get(1).getDistanceFromBus()  );
        double distance= bus.getStations().get(0).getDistanceFromBus();
          btnMoreInfo.setText(line.getNumber()+" "+distance);

          Station nextStation = RestApi.nextStation.get(bus.getId());
          if(nextStation!=null)
        busInfoNextStop.setText(nextStation.getName());

        //bus.getStations().get(1).getDistanceFromBus()


/*
            layout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Override Marker's onClick behaviour here

                    Toast.makeText(MapActivity.this, "check", Toast.LENGTH_SHORT).show();
                    btnMoreInfo.setText(Html.fromHtml("<table><tr><td>30</td><td>5 minutes</td></tr></table>"));

                }
            });

            */
    }


}


private class StationInfo extends InfoWindow {
    private Station station;

    public StationInfo(int layoutResId, MapView mapView, Station station) {
        super(layoutResId, mapView);
        this.station = station;
        int x = 4;
    }

    public void onClose() {
    }

    public void onOpen(Object arg0) {
        // LinearLayout layout = (LinearLayout) findViewById(R);
        TextView btnMoreInfo = mView.findViewById(R.id.wInfoText);

        String info = "";

        for (int i = 0; i < station.getLines().size(); i++) {
            if (i + 1 == station.getLines().size())
                info += station.getLines().get(i).getNumber();
            else
                info += station.getLines().get(i).getNumber() + ", ";

        }

        btnMoreInfo.setText(info);


        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                StationInfo.closeAllInfoWindowsOn(map);

            }
        }.start();






/*
            layout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Override Marker's onClick behaviour here

                    Toast.makeText(MapActivity.this, "check", Toast.LENGTH_SHORT).show();
                    btnMoreInfo.setText(Html.fromHtml("<table><tr><td>30</td><td>5 minutes</td></tr></table>"));

                }
            });

            */
    }

}


    public void removeBuses(MapView map) {
        while (RestApi.stations.size() != map.getOverlays().size()) {
            map.getOverlays().remove(map.getOverlays().size() - 1);

        }

    }


    public String c(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        String result = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            result = address;
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

}