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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class LineSearchActivity extends AppCompatActivity {

    private EditText etSearch;
    private ListView ivLines;

    private ArrayList<Line> mLineArrayList = new ArrayList<Line>();
    private LineAdapter adapter1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_search);
        getSupportActionBar().hide();

        initialize();




        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                adapter1.getFilter().filter(s.toString());
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
        ivLines = (ListView)findViewById(R.id.ivLines);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        mLineArrayList = RestApi.lines;


        adapter1 = new LineAdapter(LineSearchActivity.this, mLineArrayList);
        ivLines.setAdapter(adapter1);
    }





}