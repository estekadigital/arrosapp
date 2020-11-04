package com.esteka.arrosapp.radio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.google.android.exoplayer2.metadata.Metadata;

import org.greenrobot.eventbus.EventBus;

public class RadioManager {

    private static RadioManager instance = null;

    private static RadioService service;

    private Context context;

    private RadioManager(Context context) {
        this.context = context;
    }

    public static RadioManager with(Context context) {

        if (instance == null)
            instance = new RadioManager(context);

        return instance;
    }

    public static RadioService getService(){
        return service;
    }

    public void playOrPause(String streamUrl){

        service.playOrPause(streamUrl);
    }

    public void stop()
    {
        service.stop();;
    }

    public boolean isPlaying() {

        return service.isPlaying();
    }

    public void bind() {

        Intent intent = new Intent(context, RadioService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        if(service != null)
            EventBus.getDefault().post(service.getStatus());
    }

    public void unbind()
    {
        context.unbindService(serviceConnection);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder binder) {

            service = ((RadioService.LocalBinder) binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };

    public Metadata getMetadata()
    {
        return service.getMetadata();
    }

}
