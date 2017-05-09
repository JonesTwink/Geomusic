package com.azhukovski.geomusic;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;

public class AudioDataFetcher {
    private AudioDataUIUpdater uiUpdater;

    AudioDataFetcher(MainActivity activity){
        uiUpdater = new AudioDataUIUpdater(activity);
    }
    private String song;
    private String album;

    public void processBroadcastRecieverIntent(Context context, Intent intent){
        showDebugInfo(intent);
        String artist = intent.getStringExtra("artist");
        String album = intent.getStringExtra("album");
        String track = intent.getStringExtra("track");
        boolean isPlaying = intent.getBooleanExtra("playing", false);

        uiUpdater.updateAudioInfo(artist, track, album, isPlaying);
        this.album = album;
        this.song = artist + " - " + track;
    }

    public String getAlbum(){
        return album;
    }
    public String getSong(){
        return song;
    }

    private void showDebugInfo(Intent intent){
        String action = intent.getAction();
        String cmd = intent.getStringExtra("command");
    }
}
