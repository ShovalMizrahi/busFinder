package com.example.busfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterLines extends RecyclerView.Adapter<RecyclerViewAdapterLines.ViewHolder>{
    private ArrayListLine station_lines;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    RecyclerViewAdapterLines(Context context, ArrayListLine station_lines) {
        this.mInflater = LayoutInflater.from(context);
        this.station_lines = station_lines;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_list_view, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String lineNumber = station_lines.get(position).getNumber();
        String lineCompany = station_lines.get(position).getCompanyID();
        holder.text_line_number.setText(lineNumber);
        holder.text_company_name.setText(lineCompany);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return station_lines.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text_line_number, text_company_name;
        LinearLayout llContainer;

        ViewHolder(View itemView) {
            super(itemView);
            llContainer = itemView.findViewById(R.id.llContainer);
            text_line_number = itemView.findViewById(R.id.line);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Line getItem(int id) {
        return station_lines.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    // parent activity will implement this method to respond to click events

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
