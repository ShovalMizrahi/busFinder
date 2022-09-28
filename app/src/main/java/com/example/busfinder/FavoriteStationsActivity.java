package com.example.busfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class FavoriteStationsActivity extends AppCompatActivity {

    SharedPreferences sp1;
    String username;
    StationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        getSupportActionBar().hide();

        username = User.getCurrentUsername();

        ListView listView = findViewById(R.id.favoriteList);
        adapter = new StationAdapter(FavoriteStationsActivity.this, FireBase.favoriteStations);
        listView.setAdapter(adapter);

    }

    public void back(View view){
        finish();
    }
}