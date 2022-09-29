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
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.ArrayList;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements Runnable {


    Thread busLocationRefreshThread;
    Thread calculateArrivalTimeThread;
    static boolean exit = false;

    private final int TIMEREFRESHINGBUSES = 1500;


    private MapView map = null;
    private int heightMap;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    Context context;


    TextView tVNumberStationMap;
    TextView tVNameStationMap;
    TextView tVHeaderlistLineMap;
    TextView tvListLinesMap;
    TextView tVComingBuses;

    ImageView imageCompany;

    TextView tVCompany;
    TextView tVNumberLine;
    TextView tVBusId;
    TextView tVSource;
    TextView tVDest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().hide();

        exit = false;
        context = this;

        initTextViews();
        initImageViews();
        initMap();

        focusStation() ; //if user chose station from list

        busLocationRefreshThread = new Thread(this);
        busLocationRefreshThread.start();

        gettingScreenHeight();

        startThreads();

    }

    private void gettingScreenHeight() {
        /*getting the height of the screen */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightMap = displayMetrics.heightPixels;
    }

    private void focusStation() {


        Station specificStation = findSpecificStation();

        if (specificStation != null) {
            GeoPoint point = new GeoPoint(Double.parseDouble(specificStation.getLat()), Double.parseDouble(specificStation.getLongt()));
            map.getController().setCenter(point);
        }


    }


    private Station findSpecificStation() {

        Bundle b = getIntent().getExtras();
        if (b == null)
            return null;

        String stationId = b.getString("stationId");

        if (stationId != null)
            for (int i = 0; i < RestApi.stations.size(); i++) {
                if (RestApi.stations.get(i).getId().equals(stationId))
                    return RestApi.stations.get(i);
            }

        return null;
    }

    private void startThreads() {
        calculateArrivalTimeThread = new Thread(new CalculateArrivlTime());
        calculateArrivalTimeThread.start();

    }


    class CalculateArrivlTime implements Runnable {
        @Override
        public void run() {

            while (true && exit == false) {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                CalculateTime calculateTime = new CalculateTime();


                createLinesToBus();

                // Iterating HashMap through for loop
                for (Map.Entry<String, ArrayListStation> set :
                        RestApi.stations.getBusToLines().entrySet()) {


                    Bus bus = Bus.findBusById(set.getKey());

                    if (bus == null)
                        continue;
                    ;

                    ArrayListStation stations = set.getValue();
                    for (int i = 0; i < stations.size(); i++) {

                        Station station = stations.get(i);

                        if (RestApi.minStation.get(bus.getId()) != null && RestApi.minStation.get(bus.getId()) < i) {
                            int min = RestApi.minStation.get(bus.getId());

                            int minutes;

                            String arrivalTime;
                            if (min + 1 == i) {
                                arrivalTime = calculateTime.doInBackground(bus.getLatitude(), bus.getLongtitude(), station.getLat(), station.getLongt());
                                minutes = Integer.parseInt(arrivalTime.split(" ")[0]);

                            } else {
                                if (i - 1 < 0)
                                    return;

                                Station previousStatopn = stations.get(i - 1);
                                arrivalTime = calculateTime.doInBackground(previousStatopn.getLat(), previousStatopn.getLongt(), station.getLat(), station.getLongt());

                                minutes = Integer.parseInt(arrivalTime.split(" ")[0]);

                                minutes += stations.get(i - 1).getMintues();

                            }


                            station.setMintues(minutes);


                        }

                    }
                }

            }
        }
    }

    private void createLinesToBus() {
        for (int i = 0; i < RestApi.buses.size(); i++) {
            Bus bus = RestApi.buses.get(i);
            if (bus == null)
                return;
            if (RestApi.stations.getBusToLines().get(bus.getId()) == null) {

                duplicateLinesForBus(bus);
            }


        }
    }

    private void duplicateLinesForBus(Bus bus) {
        ArrayListStation stations = new ArrayListStation(RestApi.routes.get(bus.getLine()));

        RestApi.stations.getBusToLines().put(bus.getId(), stations);


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

    @Override
    public void run() {


        Boolean success = false;


        RestApi restApi = new RestApi();
        while (true && exit == false) {

            //making the copy before the info is changing!
            RestApi.lastBuses = new ArrayListBus(RestApi.buses);


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


                } else {

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

            for (int i = 0; i < RestApi.buses.size(); i++) {
                Bus bus = RestApi.buses.get(i);
                for (int j = 0; j < RestApi.lastBuses.size(); j++) {
                    Bus lastBus = RestApi.lastBuses.get(j);

                    if (bus.getId().equals(lastBus.getId())
                            && (!bus.getLatitude().equals(lastBus.getLatitude())
                            || !bus.getLongtitude().equals(lastBus.getLongtitude()))) {
                        Station nextStation = ArrayListBus.findNextStation(i, j);
                        if (nextStation != null)
                            RestApi.nextStation.put(RestApi.buses.get(i).getId(), nextStation);
                        //RestApi.nextStation.put(RestApi.buses.get(i).getId(), new Station (ArrayListBus.findNextStation(i,j) ));
                    }

                }

            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {



                    BusInfo.closeAllInfoWindowsOn(map);
                    removeBuses(map);


                    for (int i = 0; i < RestApi.buses.size(); i++) {
                        Bus bus = RestApi.buses.get(i);
                        ArrayListLine arrayListLine = new ArrayListLine();
                        Line line = arrayListLine.findLineById(RestApi.buses.get(i).getLine());


                        map.invalidate();


                        GeoPoint point = new GeoPoint(Double.parseDouble(RestApi.buses.get(i).getLatitude()), Double.parseDouble(RestApi.buses.get(i).getLongtitude()));


                        if (exit == true)
                            continue;

                        Marker startMarker = new Marker(map);
                        startMarker.setPosition(point);


                        startMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker, MapView mapView) {

                                exposeBusInfo();
                                Company company = RestApi.lineToCompany.get(line.getId());


                                if (company != null) {
                                    tVCompany.setText(company.getName());


                                    String url = RestApi.lineToCompany.get(line.getId()).getLink();
                                    showPictureByLink(context, url);

                                } else {
                                    tVCompany.setVisibility(View.GONE);
                                    imageCompany.setVisibility(View.GONE);
                                }

                                tVNumberLine.setText("line number:" + line.getNumber());
                                tVBusId.setText("bus number:" + bus.getId());
                                tVSource.setText("starting stop:" + bus.getStations().get(0).getName());

                                int lastIndex = bus.getStations().size() - 1;
                                tVDest.setText("final stop:" + bus.getStations().get(lastIndex).getName());


                                return false;
                            }
                        });


                        int iconSource;
                        if (ArrayListLine.isBusContainedInLines(bus, FireBase.favoriteLines))
                            iconSource = R.mipmap.busstar;
                        else
                            iconSource = R.mipmap.bus;


                        Drawable drawable = getResources().getDrawable(iconSource);
                        drawable = resize(drawable);
                        startMarker.setIcon(drawable);

                        startMarker.setAnchor(Marker.ANCHOR_BOTTOM, Marker.ANCHOR_BOTTOM);

                        BusInfo infoWindow = new BusInfo(R.layout.custom_bus_info_window, map, line, RestApi.buses.get(i), context);


                        startMarker.setInfoWindow(infoWindow);
                        startMarker.showInfoWindow();


                        map.getOverlays().add(startMarker);

                    }

                }
            });
        }

    }


    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 71, 100, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }


    private class BusInfo extends InfoWindow {
        private Line line;
        private Bus bus;
        private Context context;

        public BusInfo(int layoutResId, MapView mapView, Line line, Bus bus, Context context) {
            super(layoutResId, mapView);
            this.line = line;
            this.bus = bus;
            this.context = context;
        }

        public void onClose() {
        }

        public void onOpen(Object arg0) {

            TextView btnMoreInfo = mView.findViewById(R.id.busInfoLine);
            TextView busInfoNextStop = mView.findViewById(R.id.busInfoNextStop);
            btnMoreInfo.setText(line.getNumber());

            busInfoNextStop.setText("");
            busInfoNextStop.setVisibility(View.GONE);
            Station nextStation = RestApi.nextStation.get(bus.getId());
            if (nextStation != null) {
                busInfoNextStop.setText("next:" + nextStation.getName());
                busInfoNextStop.setVisibility(View.VISIBLE);
            }

        }


    }


    private class StationInfo extends InfoWindow {
        private Station station;
        private Context context;

        public StationInfo(int layoutResId, MapView mapView, Station station, Context context) {
            super(layoutResId, mapView);
            this.station = station;
            this.context = context;
        }

        public void onClose() {


        }

        public void onOpen(Object arg0) {

            exposeStationInfo();

            TextView tVLineInfo = mView.findViewById(R.id.wInfoText);
            TextView tVNumberStaion = mView.findViewById(R.id.tVNumberStaion);
            TextView tVNameStaion = mView.findViewById(R.id.tVNameStaion);


            String info = "";

            for (int i = 0; i < station.getLines().size(); i++) {
                if (i + 1 == station.getLines().size())
                    info += station.getLines().get(i).getNumber();
                else
                    info += station.getLines().get(i).getNumber() + ", ";

            }


            tVComingBuses.setText("");


            String timeArrival = findTimeArrivalByStationId(station.getId());


            tVComingBuses.setText(timeArrival);

            tVNumberStaion.setText("code station:" + station.getId());
            tVNameStaion.setText("name station:" + station.getName());
            tVHeaderlistLineMap.setText("line list:");
            tVNumberStationMap.setText(station.getId());
            tVNameStationMap.setText(station.getName());
            tvListLinesMap.setText(info);
            tVLineInfo.setText(info);


        }

    }

    private String findTimeArrivalByStationId(String stationId) {

        String result = "";

        // Iterating HashMap through for loop
        for (Map.Entry<String, ArrayListStation> set :
                RestApi.stations.getBusToLines().entrySet()) {


            Bus bus = Bus.findBusById(set.getKey());

            if (bus == null)
                continue;


            ArrayListStation stations = set.getValue();
            for (int i = 0; i < stations.size(); i++) {

                Station station = stations.get(i);

                ArrayListLine lines = new ArrayListLine();

                if (station.getId().equals(stationId) && RestApi.minStation.get(bus.getId()) != null && RestApi.minStation.get(bus.getId()) < i) {

                    result += "\n" + "line " + lines.findLineById(bus.getLine()).getNumber() + "(" + bus.getId() + ")" + " coming in " + station.getMintues();

                }
            }


        }
        return result;
    }

    /*
    expose bus info and hide station info
     */
    public void exposeBusInfo() {
        map.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (0.6 * heightMap)));

        tVHeaderlistLineMap.setVisibility(View.GONE);
        tVNumberStationMap.setVisibility(View.GONE);
        tVNameStationMap.setVisibility(View.GONE);
        tvListLinesMap.setVisibility(View.GONE);


        imageCompany.setVisibility(View.VISIBLE);

        tVCompany.setVisibility(View.VISIBLE);
        tVNumberLine.setVisibility(View.VISIBLE);
        tVBusId.setVisibility(View.VISIBLE);
        tVSource.setVisibility(View.VISIBLE);
        tVDest.setVisibility(View.VISIBLE);
    }


    public void exposeStationInfo() {
        map.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (0.6 * heightMap)));


        tVHeaderlistLineMap.setVisibility(View.VISIBLE);
        tVNumberStationMap.setVisibility(View.VISIBLE);
        tVNameStationMap.setVisibility(View.VISIBLE);
        tvListLinesMap.setVisibility(View.VISIBLE);


        imageCompany.setVisibility(View.GONE);

        tVCompany.setVisibility(View.GONE);
        tVNumberLine.setVisibility(View.GONE);
        tVBusId.setVisibility(View.GONE);
        tVSource.setVisibility(View.GONE);
        tVDest.setVisibility(View.GONE);
    }

    public void showPictureByLink(Context context, String url) {

        Glide.with(context).load(url).into(imageCompany);
        imageCompany.setMaxHeight(200);
        imageCompany.setMaxWidth(200);
    }

    public void removeBuses(MapView map) {

        while (RestApi.stations.size() < map.getOverlays().size()) {
            map.getOverlays().remove(map.getOverlays().size() - 1);

        }

    }


    @Override
    public void onBackPressed() {
        exit = true;
        finish();
    }




    void initTextViews()
    {
        tVNumberStationMap = findViewById(R.id.tVNumberStationMap);
        tVNameStationMap = findViewById(R.id.tVNameStationMap);
        tVHeaderlistLineMap = findViewById(R.id.tVHeaderlistLineMap);
        tvListLinesMap = findViewById(R.id.tVListLinesMap);
        tVComingBuses = findViewById(R.id.tVComingBuses);


        tVCompany = findViewById(R.id.tVCompany);
        tVNumberLine = findViewById(R.id.tVNumberLine);
        tVBusId = findViewById(R.id.tVBusId);
        tVSource = findViewById(R.id.tVSource);
        tVDest = findViewById(R.id.tVDest);

    }

    private void initImageViews() {

        imageCompany = findViewById(R.id.imageCompany);
        imageCompany.setVisibility(View.GONE);
    }


    void initMap()
    {

        Context ctx = this.getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = findViewById(R.id.mapview);
        map.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));


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

        map.getOverlays().clear(); //for clearing

       placeStationsInMap();

    }

    private void placeStationsInMap() {

        for (int i = 0; i < RestApi.stations.size(); i++) {

            Station station = RestApi.stations.get(i);
            map.invalidate();

            GeoPoint point = new GeoPoint(Double.parseDouble(RestApi.stations.get(i).getLat()), Double.parseDouble(RestApi.stations.get(i).getLongt()));

            Marker startMarker = new Marker(map);
            startMarker.setPosition(point);

            int iconSource;
            if (ArrayListStation.isStationConsistList(station, FireBase.favoriteStations))
                iconSource = R.mipmap.busstopstar;
            else
                iconSource = R.mipmap.busstop;

            Drawable drawable = getResources().getDrawable(iconSource);
            drawable = resize(drawable);

            startMarker.setIcon(drawable);


            startMarker.setAnchor(Marker.ANCHOR_BOTTOM, Marker.ANCHOR_BOTTOM);


            StationInfo stationInfo = new StationInfo(R.layout.custom_info_window, map, RestApi.stations.get(i), this);

            startMarker.setInfoWindow(stationInfo);

            map.getOverlays().add(startMarker);


            map.getController().setCenter(point);

        }

    }


}