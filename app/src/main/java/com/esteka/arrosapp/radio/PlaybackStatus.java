package com.esteka.arrosapp.radio;

import java.io.Serializable;

public class PlaybackStatus implements Serializable {

    public static final String IDLE = "PlaybackStatus_IDLE";

    public static final String LOADING = "PlaybackStatus_LOADING";

    public static final String PLAYING = "PlaybackStatus_PLAYING";

    public static final String PAUSED = "PlaybackStatus_PAUSED";

    public static final String STOPPED = "PlaybackStatus_STOPPED";

    public static final String ERROR = "PlaybackStatus_ERROR";

    public static final String METADATA = "PlaybackStatus_METADATA";

}