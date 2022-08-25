package com.example.busfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteActivity extends AppCompatActivity {

    SharedPreferences sp1;
    String username;
    StationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
        username = sp1.getString("username",null);

        ListView listView = findViewById(R.id.favoriteList);
        adapter = new StationAdapter(FavoriteActivity.this, FireBase.favoriteStations);
        listView.setAdapter(adapter);

    }

    public void back(View view){
        finish();
    }
}