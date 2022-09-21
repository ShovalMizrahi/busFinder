package com.example.busfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class TrackListViewActivity extends AppCompatActivity  {

  //  MyRecyclerViewAdapter adapter;
    ImageView iVfavoriteLine;
    private Line line;
    private ArrayListStation line_stations;
    String username;
    Company company;

    StationAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list_view);
        findLine();
        initTextView();

        initImageViews();

        username = User.getCurrentUsername();


        Log.i("linemess", line.getNumber());
        Log.i("linemess", String.valueOf(RestApi.routes.get(line.getId())));


        /*
        RecyclerView recyclerView = findViewById(R.id.lineStationsList);
        line_stations = RestApi.routes.get(line.getId());
        adapter = new MyRecyclerViewAdapter(this, line_stations);
        adapter.setClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
*/


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

        ArrayListLine lines = new ArrayListLine();
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
        // imageCompany.setMaxHeight(58);
        //imageCompany.setMaxWidth(66);
    }

}

