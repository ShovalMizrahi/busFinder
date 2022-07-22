package com.example.busfinder;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
    private MyAdapter2 adapter1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_search);

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
        ivStations = (ListView)findViewById(R.id.idstations);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        mStationArrayList = RestApi.stations;


        adapter1 = new MyAdapter2(StationSearchActivity.this, mStationArrayList);
        ivStations.setAdapter(adapter1);
    }

    public class MyAdapter2 extends BaseAdapter implements Filterable {

        private ArrayList<Station> mOriginalValues; // Original Values
        private ArrayList<Station> mDisplayedValues;    // Values to be displayed
        LayoutInflater inflater;

        public MyAdapter2(Context context, ArrayList<Station> mStationArrayList) {
            this.mOriginalValues = mStationArrayList;
            this.mDisplayedValues = mStationArrayList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mDisplayedValues.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            LinearLayout llContainer;
            TextView station_name,station_id;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {

                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.activity_station_list_view, null);
                holder.llContainer = (LinearLayout)convertView.findViewById(R.id.llContainer);
                holder.station_name = (TextView) convertView.findViewById(R.id.station_name);
                holder.station_id = (TextView) convertView.findViewById(R.id.station_id);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.station_name.setText(mDisplayedValues.get(position).getName());
            holder.station_id.setText(mDisplayedValues.get(position).getId());

            holder.llContainer.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    Toast.makeText(StationSearchActivity.this, mDisplayedValues.get(position).getName(), Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint,FilterResults results) {

                    mDisplayedValues = (ArrayList<Station>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    ArrayList<Station> FilteredArrList = new ArrayList<Station>();

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<Station>(mDisplayedValues); // saves the original data in mOriginalValues
                    }

                    /********
                     *
                     *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                     *  else does the Filtering and returns FilteredArrList(Filtered)
                     *
                     ********/
                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            String data = mOriginalValues.get(i).getName();
                            if (data.toLowerCase().startsWith(constraint.toString())) {
                                FilteredArrList.add(new Station(mOriginalValues.get(i).getId(),mOriginalValues.get(i).getName(),mOriginalValues.get(i).getCity(),mOriginalValues.get(i).getLongt(),mOriginalValues.get(i).getLat()));
                            }
                        }
                        // set the Filtered result to return
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                    return results;
                }
            };
            return filter;
        }
    }


    public void back(View view){
        finish();
    }
}