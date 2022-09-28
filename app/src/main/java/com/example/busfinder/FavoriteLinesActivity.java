package com.example.busfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class FavoriteLinesActivity extends AppCompatActivity {

    SharedPreferences sp1;
    String username;
    LineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_lines);
        getSupportActionBar().hide();

        username = User.getCurrentUsername();

        ListView listView = findViewById(R.id.favoriteLinesList);
        adapter = new LineAdapter(FavoriteLinesActivity.this, FireBase.favoriteLines);
        listView.setAdapter(adapter);
    }

    public void back(View view){
        finish();
    }
}