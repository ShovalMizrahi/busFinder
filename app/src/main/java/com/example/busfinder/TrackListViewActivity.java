package com.example.busfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TrackListViewActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;
    private Line line;
    private ArrayListStation line_stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list_view);

        Bundle b = getIntent().getExtras();
        String line_number = b.getString("line");
        Log.i("linemess", line_number);
        for (int i=0;i < RestApi.lines.size() ; i++){
            if(RestApi.lines.get(i).getNumber().equals(line_number))
                line = RestApi.lines.get(i);
        }
        Log.i("linemess", line.getNumber());
        Log.i("linemess", String.valueOf(RestApi.routes.get(line.getId())));


        RecyclerView recyclerView = findViewById(R.id.lineStationsList);
        line_stations = RestApi.routes.get(line.getId());
        adapter = new MyRecyclerViewAdapter(this, line_stations);
        adapter.setClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }


}



