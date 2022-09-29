package com.example.busfinder;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class TrackListViewActivity extends AppCompatActivity  {

    ImageView iVfavoriteLine;
    private Line line;
    private Stations line_stations;
    String username;
    Company company;

    StationAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list_view);
        getSupportActionBar().hide();

        findLine();
        initTextView();

        initImageViews();

        username = User.getCurrentUsername();


        Log.i("linemess", line.getNumber());
        Log.i("linemess", String.valueOf(RestApi.routes.get(line.getId())));




        line_stations = RestApi.routes.get(line.getId());

        ListView listView = findViewById(R.id.lVTrackList);
        adapter = new StationAdapter(TrackListViewActivity.this, line_stations);
        listView.setAdapter(adapter);

    }


    public void addToFavorite(View view) {

        if (isFavorite()) {
            FireBase.deleteFavoriteLine(username, line.getId());
            iVfavoriteLine.setImageResource(R.drawable.ic_baseline_favorite_border_24);

        } else {
            FireBase.addFavoriteLines(username, line.getId());
            iVfavoriteLine.setImageResource(R.drawable.ic_baseline_favorite_24);

        }







    }



    void findLine() {

        Bundle b = getIntent().getExtras();
        String lineId = b.getString("line");

        Lines lines = new Lines();
        for (int i = 0; i < RestApi.lines.size(); i++) {
            line = lines.findLineById(lineId);
        }

        company = RestApi.lineToCompany.get(line.getId());

    }


    boolean isFavorite() {
        for (int i = 0; i < FireBase.favoriteLines.size(); i++)
            if (FireBase.favoriteLines.get(i).getId().equals(line.getId()))
                return true;

        return false;

    }


    void initTextView() {
        TextView tVInfoLine = findViewById(R.id.tVInfoLine);

        String info = line.getNumber() + "\n" + company.getName();

        tVInfoLine.setText(info);


    }

    void initImageViews() {
         iVfavoriteLine = findViewById(R.id.iVfavoriteLine);


        if (FireBase.favoriteLines == null)
            return;

        if (isFavorite())
            iVfavoriteLine.setImageResource(R.drawable.ic_baseline_favorite_24);

        else
            iVfavoriteLine.setImageResource(R.drawable.ic_baseline_favorite_border_24);

        ImageView iVCompanyAddFavorite = findViewById(R.id.iVCompanyAddFavorite);


        if (company != null) {
            String url = company.getLink();
            showPictureByLink(iVCompanyAddFavorite, this, url);

        }

    }


    public void showPictureByLink(ImageView imageCompany, Context context, String url) {
        Glide.with(context).load(url).into(imageCompany);

    }

}

