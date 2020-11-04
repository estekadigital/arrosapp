package com.esteka.arrosapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.esteka.arrosapp.radio.Station;

public class ItemInfoFragment extends Fragment {

    private ActionBar actionBar;

    private ImageView ivStationImage;
    private TextView tvStationName;
    private TextView tvStationArea;
    private TextView tvStationFreq;
    private TextView tvStationAddress;
    private TextView tvStationPhone;
    private TextView tvStationEmail;
    private TextView tvStationWeb;
    private TextView tvStationLongDesc;
    private ImageView ivPodcastButton;
    private Button btnOk;

    private Station station;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemInfoFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ItemInfoActivity activity = (ItemInfoActivity) this.getActivity();
        actionBar = activity.getSupportActionBar();
        actionBar.setTitle("");
        /*if (getArguments().containsKey(RadioApp.ARG_STATION_CURRENT)) {
            RadioApp.stationList = getArguments().getParcelableArrayList(RadioApp.ARG_STATION_LIST);
            RadioApp.stationCurrent = (Integer) getArguments().get(RadioApp.ARG_STATION_CURRENT);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_info, container, false);

        btnOk = rootView.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ivStationImage = rootView.findViewById(R.id.iv_station_image);
        tvStationName = rootView.findViewById(R.id.tv_station_name);
        tvStationArea = rootView.findViewById(R.id.tv_station_area);
        tvStationFreq = rootView.findViewById(R.id.tv_station_freq);
        tvStationAddress = rootView.findViewById(R.id.tv_station_address);
        tvStationPhone = rootView.findViewById(R.id.tv_station_phone);
        tvStationEmail = rootView.findViewById(R.id.tv_station_email);
        tvStationWeb = rootView.findViewById(R.id.tv_station_web);
        tvStationLongDesc = rootView.findViewById(R.id.tv_station_Long_desc);
        ivPodcastButton = rootView.findViewById(R.id.iv_podcast_button);
        ivPodcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (station != null)
                {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(station.getPodcastURL()));
                    startActivity(i);
                }
            }
        });


        loadStation(RadioApp.stationCurrent);
        return rootView;
    }

    private void loadStation(int index)
    {
        if (index < 0 || index > RadioApp.stationList.size()-1)
        {
            index = 0;
        }

        station = RadioApp.stationList.get(index);

        if (station != null) {
            actionBar.setTitle(station.getName());
            ivStationImage.setImageResource(station.getImageResId());
            tvStationName.setText(station.getName());
            tvStationName.setVisibility(station.getName().length()>0?View.VISIBLE:View.INVISIBLE);
            tvStationArea.setText(station.getArea());
            tvStationFreq.setText(station.getFreq());
            tvStationAddress.setText(station.getAddress());
            tvStationAddress.setVisibility(station.getAddress().length()>0?View.VISIBLE:View.INVISIBLE);
            tvStationPhone.setText(station.getPhone());
            tvStationPhone.setVisibility(station.getPhone().length()>0?View.VISIBLE:View.INVISIBLE);
            tvStationEmail.setText(station.getEmail());
            tvStationWeb.setText(station.getWeb());
            tvStationLongDesc.setText(station.getLongDesc());
            ivPodcastButton.setVisibility( (station.getPodcastURL().length() > 0) ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
