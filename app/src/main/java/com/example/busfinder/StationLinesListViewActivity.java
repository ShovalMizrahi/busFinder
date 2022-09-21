package com.example.busfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class StationLinesListViewActivity extends AppCompatActivity {

    private Station station;
    LineAdapter adapter;
    SharedPreferences sp1;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_lines_list_view);

        username = User.getCurrentUsername();

        Bundle b = getIntent().getExtras();
        String station_id = b.getString("station");
        for (int i=0;i < RestApi.stations.size() ; i++){
            if(RestApi.stations.get(i).getId().equals(station_id))
                station = RestApi.stations.get(i);
        }


        Toast.makeText(this, station.getLines().size()+"", Toast.LENGTH_SHORT).show();

        ListView listView = findViewById(R.id.lVLinesOfStations);
        adapter = new LineAdapter(this,station.getLines());
        listView.setAdapter(adapter);



    }

    public void AddToFavorite(View view){
        boolean flag = false;
        for (int i=0; i<FireBase.favoriteStations.size(); i++) {
            if (FireBase.favoriteStations.get(i).getId().equals(station.getId())) {
                FireBase.deleteFavoriteLine(username, station.getId());
                flag = true;
                break;
            }
        }
        if (!flag)
            FireBase.addFavoriteLines(username, station.getId());

    }



}