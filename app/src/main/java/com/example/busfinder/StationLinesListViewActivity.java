package com.example.busfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StationLinesListViewActivity extends AppCompatActivity {

    ImageView iVfavoriteStation;

    private Station station;
    LineAdapter adapter;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_lines_list_view);

        username = User.getCurrentUsername();

        findStation();

        initImageViews();

        //showing the lines that pass through this station
        ListView listView = findViewById(R.id.lVLinesOfStations);
        adapter = new LineAdapter(this, station.getLines());
        listView.setAdapter(adapter);

        initTextViews();

    }

    // finding the station that the user clicked on in the previous screen
    private void findStation() {
        Bundle b = getIntent().getExtras();
        String station_id = b.getString("station");
        for (int i = 0; i < RestApi.stations.size(); i++) {
            if (RestApi.stations.get(i).getId().equals(station_id))
                station = RestApi.stations.get(i);
        }
    }

    // showing the station information
    private void initTextViews() {
        TextView tVStationInfo = findViewById(R.id.tVStationInfo);
        tVStationInfo.setLines(2);
        tVStationInfo.setText(station.getId() + "\n" + station.getName());
    }

    //adding the station to favorites using the heart icon
    public void addToFavorite(View view) {


        if (isFavorite()) {
            FireBase.deleteFavoriteStation(username, station.getId());
            iVfavoriteStation.setImageResource(R.drawable.ic_baseline_favorite_border_24);

        } else {
            FireBase.addFavoriteStation(username, station.getId());
            iVfavoriteStation.setImageResource(R.drawable.ic_baseline_favorite_24);

        }


    }


    // changing to the map screen
    public void moveToMap (View view)
    {
        this.finish();
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("stationId",station.getId());
        startActivity(intent);

        Toast.makeText(this, "this is map", Toast.LENGTH_SHORT).show();
    }


    // checking if station in favorites
    boolean isFavorite() {
        for (int i = 0; i < FireBase.favoriteStations.size(); i++)
            if (FireBase.favoriteStations.get(i).getId().equals(station.getId()))
                return true;

        return false;

    }


    void initImageViews() {
        iVfavoriteStation = findViewById(R.id.iVfavoriteStation);


        if (FireBase.favoriteLines == null)
            return;

        if (isFavorite())
            iVfavoriteStation.setImageResource(R.drawable.ic_baseline_favorite_24);

        else
            iVfavoriteStation.setImageResource(R.drawable.ic_baseline_favorite_border_24);


    }


}