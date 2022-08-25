package com.example.busfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class StationLinesListViewActivity extends AppCompatActivity implements RecyclerViewAdapterLines.ItemClickListener {

    private Station station;
    RecyclerViewAdapterLines adapter;
    SharedPreferences sp1;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_lines_list_view);

        sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
        username = sp1.getString("username",null);

        Bundle b = getIntent().getExtras();
        String station_id = b.getString("station");
        for (int i=0;i < RestApi.stations.size() ; i++){
            if(RestApi.stations.get(i).getId().equals(station_id))
                station = RestApi.stations.get(i);
        }


        RecyclerView recyclerView = findViewById(R.id.stationLinesList);
        adapter = new RecyclerViewAdapterLines(this,station.getLines());
        adapter.setClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    public void AddToFavorite(View view){
        boolean flag = false;
        for (int i=0; i<FireBase.favoriteStations.size(); i++) {
            if (FireBase.favoriteStations.get(i).getId().equals(station.getId())) {
                FireBase.favoriteStations.remove(i);
                flag = true;
                break;
            }
        }
        if (!flag)
            FireBase.addFavoriteStation(username, station.getId());

    }


    @Override
    public void onItemClick(View view, int position) {

    }
}