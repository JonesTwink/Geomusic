package com.azhukovski.geomusic;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;

public class AudioDataFetcher {
    private AudioDataUIUpdater uiUpdater;

    AudioDataFetcher(MainActivity activity){
        uiUpdater = new AudioDataUIUpdater(activity);
    }

    public void processBroadcastRecieverIntent(Context context, Intent intent){
        showDebugInfo(intent);
        String artist = intent.getStringExtra("artist");
        String album = intent.getStringExtra("album");
        String track = intent.getStringExtra("track");
        boolean isPlaying = intent.getBooleanExtra("playing", false);

        uiUpdater.updateAudioInfo(artist, album, track, isPlaying);
    }

    public IntentFilter setAudioPlayerIntentFilters(){
        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.musicservicecommand");
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.android.music.playstatechanged");
        iF.addAction("com.android.music.updateprogress");
        iF.addAction("com.android.music.metachanged");

        iF.addAction("com.htc.music.metachanged");

        iF.addAction("fm.last.android.metachanged");
        iF.addAction("com.sec.android.app.music.metachanged");
        iF.addAction("com.nullsoft.winamp.metachanged");
        iF.addAction("com.amazon.mp3.metachanged");
        iF.addAction("com.miui.player.metachanged");
        iF.addAction("com.real.IMP.metachanged");
        iF.addAction("com.sonyericsson.music.metachanged");
        iF.addAction("com.rdio.android.metachanged");
        iF.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        iF.addAction("com.andrew.apollo.metachanged");
        iF.addAction("ru.yandex.music.metachanged");

        iF.addAction("com.vkontakte.android.audio.player.MediaButtonEventReceiver");
        iF.addAction("com.vkontakte.android.PlayerWidget");
        iF.addAction("com.vkontakte.android.PlayerBigWidget");
        iF.addAction("ru.yandex.music.ui.widget.WigdetProvider");
        iF.addAction("ru.yandex.music.common.service.player.MediaReciever");
        iF.addAction("ru.yandex.music.push.PushNotificationReciever");
        iF.addAction("ru.yandex.music.push.PushMessageReciever");
        return iF;
    }

    private void showDebugInfo(Intent intent){
        String action = intent.getAction();
        String cmd = intent.getStringExtra("command");

        String debugInfo = "COMMAND"+ cmd + "; ACTION" + action + "; EXTRAS: " + intent.getExtras().toString();

        uiUpdater.showDebugInfo(debugInfo);

    }
}
