package com.esteka.arrosapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.esteka.arrosapp.radio.PlaybackStatus;
import com.esteka.arrosapp.radio.Station;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataOutput;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.support.v4.media.MediaMetadataCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment{

    private ActionBar actionBar;
    private ImageButton btnPlay;
    private ImageButton btnStop;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private TextView tvStationInfo;
    private TextView tvArtist;
    private TextView tvTitle;
    private ImageView ivStationImage;

    //private ArrayList<Station> RadioApp.stationList;
    //private Integer RadioApp.stationCurrent;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ItemDetailActivity activity = (ItemDetailActivity) this.getActivity();
        actionBar = activity.getSupportActionBar();

        /*if (getArguments().containsKey(RadioApp.ARG_STATION_CURRENT)) {
            RadioApp.stationList = getArguments().getParcelableArrayList(RadioApp.ARG_STATION_LIST);
            RadioApp.stationCurrent = (Integer) getArguments().get(RadioApp.ARG_STATION_CURRENT);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        btnPlay = rootView.findViewById(R.id.btn_play);
        btnStop = rootView.findViewById(R.id.btn_stop);
        btnNext = rootView.findViewById(R.id.btn_next);
        btnPrevious = rootView.findViewById(R.id.btn_previous);
        tvStationInfo = rootView.findViewById(R.id.tv_station_info);
        tvArtist = rootView.findViewById(R.id.tv_artist);
        tvTitle = rootView.findViewById(R.id.tv_title);
        ivStationImage = rootView.findViewById(R.id.iv_station_image);
        ImageButton btnShare = rootView.findViewById(R.id.btn_share);
        Button btnMoreInfo = rootView.findViewById(R.id.btn_moreinfo);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "I'm listening to "+RadioApp.stationList.get(RadioApp.stationCurrent).getName()+" via Arrosa App";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Try out ArrosApp");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Play stream
                RadioApp.getInstance().getRadioManager().playOrPause(RadioApp.stationList.get(RadioApp.stationCurrent).getStreamURL());
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stop stream
                if (RadioApp.getInstance().getRadioManager().isPlaying())
                {
                    RadioApp.getInstance().getRadioManager().stop();
                }
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadStation(RadioApp.stationCurrent-1);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadStation(RadioApp.stationCurrent+1);
            }
        });

        btnMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                Intent intent = new Intent(context, ItemInfoActivity.class);
                context.startActivity(intent);
            }
        });

        loadStation(RadioApp.stationCurrent);

        return rootView;
    }

    private void loadStation(int index)
    {
        if (index < 0)
        {
            // We are navigation back, load the last station
            index = RadioApp.stationList.size()-1;
        }
        else if (index > RadioApp.stationList.size()-1)
        {
            index = 0;
        }

        Station loadStation = RadioApp.stationList.get(index);
        if (loadStation != null) {
            tvStationInfo.setText(loadStation.getFreq() + " " + loadStation.getArea());
            ivStationImage.setImageResource(loadStation.getImageResId());
            tvArtist.setText(loadStation.getName());
            actionBar.setTitle(loadStation.getName());
        }

        if (loadStation.getStatus()!=PlaybackStatus.PLAYING && loadStation.getStatus()!=PlaybackStatus.STOPPED && loadStation.getStatus()!=PlaybackStatus.PAUSED) {
            RadioApp.getInstance().getRadioManager().stop();
            RadioApp.getInstance().getRadioManager().playOrPause(loadStation.getStreamURL());
        }

        RadioApp.stationCurrent = index;
    }

    @Override
    public void onStart() {

        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {

        EventBus.getDefault().unregister(this);

        super.onStop();
    }

    @Subscribe
    public void onEvent(String status){

        tvTitle.clearAnimation();
        Station station = RadioApp.stationList.get(RadioApp.stationCurrent);
        station.setStatus(status);

        switch (status){

            case PlaybackStatus.LOADING:
                tvTitle.setText(R.string.loading_station);
                tvTitle.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.blink));
                break;

            case PlaybackStatus.ERROR:
            case PlaybackStatus.IDLE:
                tvTitle.setText(R.string.loading_error);
                break;

            case PlaybackStatus.PAUSED:
                tvTitle.setText(R.string.station_paused);
                break;

            case PlaybackStatus.STOPPED:
                tvTitle.setText(R.string.station_stopped);
                break;

            case PlaybackStatus.PLAYING:
                tvTitle.setText(station.getDesc());
                break;

            case PlaybackStatus.METADATA:
                tvTitle.setText(RadioApp.getInstance().getRadioManager().getMetadata().toString());
                // TODO.. check for real metadata in the passed variable
                String artistName = (station != null) ? station.getName() : "...";
                String albumName = "...";
                String titleName = (station != null) ? station.getDesc() : getResources().getString(R.string.live_broadcast);
                RadioApp.getInstance().getRadioManager().getService().getMediaSession().setMetadata(new MediaMetadataCompat.Builder()
                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artistName)
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, albumName)
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, titleName)
                        .build());
                break;


        }

        btnPlay.setImageResource(status.equals(PlaybackStatus.PLAYING)
                ? R.drawable.ic_pause_black
                : R.drawable.ic_play_black);

    }
}
