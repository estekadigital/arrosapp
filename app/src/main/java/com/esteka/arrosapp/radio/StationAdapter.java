package com.esteka.arrosapp.radio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.esteka.arrosapp.ItemDetailActivity;
import com.esteka.arrosapp.ItemDetailFragment;
import com.esteka.arrosapp.ItemListActivity;
import com.esteka.arrosapp.R;
import com.esteka.arrosapp.RadioApp;

import java.util.ArrayList;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder>
{
    private final ItemListActivity mParentActivity;
    private final ArrayList<Station> mStations;
    private final boolean mTwoPane;

    public StationAdapter(ItemListActivity parent,
                   ArrayList<Station> items,
                   boolean twoPane) {
        mStations = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
        notifyDataSetChanged();
    }

    @Override
    public StationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        return new StationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StationAdapter.ViewHolder holder, int position) {
        Station station;
        try {
            if(position %2 == 1)
            {
                holder.itemView.setBackgroundColor(Color.parseColor("#CCCCCC"));
            }
            else
            {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            station = mStations.get(position);
            holder.mStationImage.setImageResource(station.getImageResId());
            holder.mStationName.setText(station.getName());
            holder.mStationFreq.setText(station.getFreq());
            holder.mStationArea.setText(station.getArea());

            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            RadioApp.stationCurrent = (Integer) view.getTag();
                            if (mTwoPane) {
                                Bundle arguments = new Bundle();
                                ItemDetailFragment fragment = new ItemDetailFragment();
                                fragment.setArguments(arguments);
                                mParentActivity.getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.item_detail_container, fragment)
                                        .commit();
                            } else {
                                Context context = view.getContext();
                                Intent intent = new Intent(context, ItemDetailActivity.class);
                                context.startActivity(intent);
                            }
                        }
                    }
            );
        }catch (Exception e)
        {
            holder.mStationName.setText("ERROR");
        }
    }

    @Override
    public int getItemCount() {
        return mStations.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mStationName;
        final TextView mStationFreq;
        final TextView mStationArea;
        final ImageView mStationImage;

        ViewHolder(View view) {
            super(view);
            mStationName = (TextView) view.findViewById(R.id.station_name);
            mStationFreq = (TextView) view.findViewById(R.id.station_freq);
            mStationArea = (TextView) view.findViewById(R.id.station_area);
            mStationImage = (ImageView) view.findViewById(R.id.station_image);
        }
    }
}
