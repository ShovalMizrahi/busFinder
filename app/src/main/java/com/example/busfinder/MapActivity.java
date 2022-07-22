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
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements Runnable {

    int a = 10;

    int f=0;
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


            Drawable drawable = getResources().getDrawable(R.mipmap.bustop);
            drawable = resize(drawable);

            startMarker.setIcon(drawable);


            //  startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
           // startMarker.setAnchor(0.05f, 0.5f);

            startMarker.setAnchor(Marker.ANCHOR_BOTTOM, Marker.ANCHOR_BOTTOM);





            InfoWindow infoWindow = new MyInfoWindow(R.layout.custom_info_window, map,"pp");


            startMarker.setInfoWindow(infoWindow);


            //     map.getOverlays().clear(); //for clearing
            map.getOverlays().add(startMarker);


            map.getController().setCenter(point);


        }


        Thread t = new Thread(this);
        t.start();


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

                } else {

                    try {
                        Thread.sleep(TIMEREFRESHINGBUSES);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
            success = false;


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    removeBuses(map);

                    for (int i = 0; i < RestApi.buses.size(); i++) {
                        String infomartion = RestApi.buses.get(i).getLine()+"aa";

                        Toast.makeText(MapActivity.this, RestApi.buses.get(i).getLine()+"cccc", Toast.LENGTH_SHORT).show();

                        map.invalidate();

                        //   Toast.makeText(MapActivity.this, RestApi.buses.get(0).getLatitude() + "", Toast.LENGTH_SHORT).show();

                        GeoPoint point = new GeoPoint(Double.parseDouble(RestApi.buses.get(i).getLatitude()), Double.parseDouble(RestApi.buses.get(i).getLongtitude()));

                        Marker startMarker = new Marker(map);
                        startMarker.setPosition(point);


                        Drawable drawable = getResources().getDrawable(R.mipmap.bussymbol);
                        drawable = resize(drawable);

                      //  if(i==1) {
                            //startMarker.setIcon(drawable);
                            startMarker.setIcon(drawable);
                          //  startMarker.setIcon(startMarker.getIcon());

                      //  }


                   //     Toast.makeText(MapActivity.this, RestApi.buses.get(i).getLine() + " u" , Toast.LENGTH_SHORT).show();




                     //   startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                        startMarker.setAnchor(Marker.ANCHOR_BOTTOM, Marker.ANCHOR_BOTTOM);
                        //  startMarker.setAnchor(0.05f, 0.5f);

                        InfoWindow infoWindow = new MyInfoWindow(R.layout.custom_info_window, map,infomartion);


                        startMarker.setInfoWindow(infoWindow);


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

    int w = 0;

    private class MyInfoWindow extends InfoWindow {
        private String line;
        public MyInfoWindow(int layoutResId, MapView mapView,String line) {
            super(layoutResId, mapView);
            this.line = line;
        }

        public void onClose() {
        }

        public void onOpen(Object arg0) {
            // LinearLayout layout = (LinearLayout) findViewById(R);
            TextView btnMoreInfo = mView.findViewById(R.id.wInfoText);
            btnMoreInfo.setText("how "+line);


            new CountDownTimer(3000, 1000) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    InfoWindow.closeAllInfoWindowsOn(map);

                }
            }.start();


            w++;


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



    public void removeBuses(MapView map)
    {
        while(RestApi.stations.size() != map.getOverlays().size())
        {
            map.getOverlays().remove(map.getOverlays().size()-1);

        }

    }

}