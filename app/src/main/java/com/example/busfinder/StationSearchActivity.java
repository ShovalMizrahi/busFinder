package com.example.busfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class StationSearchActivity extends AppCompatActivity {

    private LinearLayout llContainer;
    private EditText etSearch;
    private ListView ivStations;
    private ArrayList<Station> mStationArrayList = new ArrayList<Station>();
    private StationAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_search);
        getSupportActionBar().hide();

        initialize();




        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void initialize() {
        etSearch = (EditText) findViewById(R.id.etSearch);
        ivStations = (ListView)findViewById(R.id.idstations);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        mStationArrayList = RestApi.stations;

        adapter = new StationAdapter(this,mStationArrayList);
        ivStations.setAdapter(adapter);

    }

    public void back(View view){
        finish();
    }
}