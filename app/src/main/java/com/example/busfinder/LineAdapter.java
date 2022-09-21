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

    public LineAdapter(Context context, ArrayList<Line> mLineArrayList) {
        this.mOriginalValues = mLineArrayList;
        this.mDisplayedValues = mLineArrayList;
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
        TextView line;
        ImageView iVCompanySearch;
        TextView tVCompanySearchLine,tVSourceStationSearchLine,tVSourceStationNumberSearchLine, tVSourceStationCitySearchLine;
        TextView tVDestStationSearchLine,tVDestStationNumberSearchLine, tVDestStationCitySearchLine;
        TextView tVLineHeader,tVSourceHeader, tVDestHeader;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LineAdapter.ViewHolder holder = null;

        if (convertView == null) {

            holder = new LineAdapter.ViewHolder();

            convertView = inflater.inflate(R.layout.activity_list_view, null);
            holder.llContainer = (LinearLayout)convertView.findViewById(R.id.llContainer);
            holder.line = (TextView) convertView.findViewById(R.id.line);
            holder.tVCompanySearchLine = (TextView) convertView.findViewById(R.id.tVCompanySearchLine);
            holder.tVSourceStationSearchLine = (TextView) convertView.findViewById(R.id.tVSourceStationSearchLine);
            holder.iVCompanySearch = (ImageView)  convertView.findViewById(R.id.iVCompanySearch);
            holder.tVSourceStationNumberSearchLine = (TextView)  convertView.findViewById(R.id.tVSourceStationNumberSearchLine);
            holder.tVSourceStationCitySearchLine = (TextView)  convertView.findViewById(R.id.tVSourceStationCitySearchLine);
            holder.tVLineHeader = (TextView)  convertView.findViewById(R.id.tVLineHeader);
            holder.tVSourceHeader = (TextView)  convertView.findViewById(R.id.tVSourceHeader);
            holder.tVDestHeader = (TextView)  convertView.findViewById(R.id.tVDestHeader);

            holder.tVDestStationSearchLine = (TextView)  convertView.findViewById(R.id.tVDestStationSearchLine);
            holder.tVDestStationNumberSearchLine = (TextView)  convertView.findViewById(R.id.tVDestStationNumberSearchLine);
            holder.tVDestStationCitySearchLine = (TextView)  convertView.findViewById(R.id.tVDestStationCitySearchLine);
            convertView.setTag(holder);
        } else {
            holder = (LineAdapter.ViewHolder) convertView.getTag();
        }

        if(position==0)
        {
            holder.tVLineHeader.setText("line");
            holder.tVSourceHeader.setText("Source");
            holder.tVDestHeader.setText("Dest");


        }

        else
        {
            holder.tVLineHeader.setVisibility(View.GONE);
            holder.tVSourceHeader.setVisibility(View.GONE);
            holder.tVDestHeader.setVisibility(View.GONE);

        }


        holder.line.setText(mDisplayedValues.get(position).getNumber());


        Line line = mDisplayedValues.get(position);
        Company company = RestApi.lineToCompany.get(line.getId());
        if(company !=null)
        {
            String url = company.getLink();
            showPictureByLink(holder.iVCompanySearch, inflater.getContext(), url);
            holder.tVCompanySearchLine.setText(company.getName());

        }
        // if(line.getLineStations() !=null )

        ArrayListStation stations =   RestApi.routes.get(line.getId());
        if(stations!=null && stations.size()>0) {
            holder.tVSourceStationSearchLine.setText(stations.get(0).getName());
            holder.tVSourceStationNumberSearchLine.setText(stations.get(0).getId());
            String sourceCity =  ArrayListCity.findCityById(stations.get(0).getCityId());
            holder.tVSourceStationCitySearchLine.setText(sourceCity);


            holder.tVDestStationSearchLine.setText(stations.get(stations.size()-1).getName());
            holder.tVDestStationNumberSearchLine.setText(stations.get(stations.size()-1).getId());
            String destCity =  ArrayListCity.findCityById(stations.get(stations.size()-1).getCityId());
            holder.tVDestStationCitySearchLine.setText(destCity);

        }






        holder.llContainer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(inflater.getContext(), TrackListViewActivity.class);
                Bundle b = new Bundle();
                b.putString("line",mDisplayedValues.get(position).getId());
                intent.putExtras(b);
                inflater.getContext().startActivity(intent);
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

    public void showPictureByLink(ImageView imageCompany, Context context, String url) {
        Glide.with(context).load(url).into(imageCompany);
        imageCompany.setMaxHeight(58);
        imageCompany.setMaxWidth(66);
    }


}
