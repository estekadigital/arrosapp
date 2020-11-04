package com.esteka.arrosapp;

import android.app.Application;

import com.esteka.arrosapp.radio.RadioManager;
import com.esteka.arrosapp.radio.Station;

import java.util.ArrayList;

public class RadioApp extends Application
{
    // Singleton instance
    private static RadioApp sInstance = null;
    public static ArrayList<Station> stationList;
    public static Integer stationCurrent = -1;
    public static Boolean useLocalStations = true;
    public static String stationDataURL   = "https://www.arrosasarea.eus/arrosapp/stations.json";

    RadioManager radioManager;

    @Override
    public void onCreate() {
        super.onCreate();
        // Setup singleton instance
        radioManager = RadioManager.with(this);
        sInstance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //clear your session variable.
        //you may want to do this quick, and on another thread
        //to prevent android from killing your app
    }

    public RadioManager getRadioManager()
    {
        return radioManager;
    }

    // Getter to access Singleton instance
    public static RadioApp getInstance() {
        return sInstance ;
    }

}