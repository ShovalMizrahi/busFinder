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
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class LineAdapter extends BaseAdapter implements Filterable {

        private ArrayList<Line> mOriginalValues; // Original Values
        private ArrayList<Line> mDisplayedValues;    // Values to be displayed
        LayoutInflater inflater;
        Activity activity;


        public LineAdapter(Context context, ArrayList<Line> mLineArrayList) {
            this.mOriginalValues = mLineArrayList;
            this.mDisplayedValues = mLineArrayList;
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

        class ViewHolder {
            LinearLayout llContainer;
            TextView line,company;

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {

                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.activity_list_view, null);
                holder.llContainer = (LinearLayout)convertView.findViewById(R.id.llContainer);
                holder.line = (TextView) convertView.findViewById(R.id.line);
                holder.company = (TextView) convertView.findViewById(R.id.tVCompanySearchLine);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.line.setText(mDisplayedValues.get(position).getNumber());
            holder.company.setText(mDisplayedValues.get(position).getCompanyID());




            holder.llContainer.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Intent intent = new Intent(activity, TrackListViewActivity.class);
                    Bundle b = new Bundle();
                    b.putString("line",mDisplayedValues.get(position).getNumber());
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

                    mDisplayedValues = (ArrayList<Line>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    ArrayList<Line> FilteredArrList = new ArrayList<Line>();

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<Line>(mDisplayedValues); // saves the original data in mOriginalValues
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
                            String data = mOriginalValues.get(i).getNumber();
                            if (data.toLowerCase().startsWith(constraint.toString())) {
                                FilteredArrList.add(new Line(mOriginalValues.get(i).getId(),mOriginalValues.get(i).getNumber(),mOriginalValues.get(i).getCompanyID()));
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
