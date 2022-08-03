package com.example.busfinder.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.busfinder.ArrayListStation;
import com.example.busfinder.MenuActivity;
import com.example.busfinder.R;
import com.example.busfinder.StationSearchActivity;
import com.example.busfinder.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ListView nearbyStations;
                                                                                     

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        nearbyStations = root.findViewById(R.id.nearbyStationsList);

        CustomAdapter adapter = new CustomAdapter(getActivity(),MenuActivity.getSortedStations());
        nearbyStations.setAdapter(adapter);


        return root;
    }

    class CustomAdapter extends BaseAdapter {
        ArrayListStation stations;
        LayoutInflater inflater;

        public CustomAdapter(Context context,ArrayListStation stations) {
            super();
            this.stations = stations;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return stations.size();
        }

        @Override
        public Object getItem(int i) {
            return stations.get(i);
        }

        @Override
        public long getItemId(int i) {
            return stations.get(i).hashCode();
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
            holder.station_name.setText(stations.get(position).getName());
            holder.station_id.setText(stations.get(position).getId());

            holder.llContainer.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    Toast.makeText(getContext(), stations.get(position).getName(), Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }


    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}