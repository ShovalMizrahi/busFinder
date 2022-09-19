package com.example.busfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

    public class StationAdapter extends BaseAdapter implements Filterable {

        private ArrayList<Station> mOriginalValues; // Original Values
        private ArrayList<Station> mDisplayedValues;    // Values to be displayed
        LayoutInflater inflater;
        Activity activity;

        public StationAdapter(Context context, ArrayList<Station> mStationArrayList) {
            this.mOriginalValues = mStationArrayList;
            this.mDisplayedValues = mStationArrayList;
            activity = (Activity) context;
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
                    Intent intent = new Intent( activity , StationLinesListViewActivity.class);
                    Bundle b = new Bundle();
                    b.putString("station",mDisplayedValues.get(position).getId());
                    intent.putExtras(b);
                    activity.startActivity(intent);
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
                            //   if (data.toLowerCase().startsWith(constraint.toString())) {
                            if (data.toLowerCase().contains(constraint.toString())) {
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

