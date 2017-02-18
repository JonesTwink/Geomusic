package com.azhukovski.geomusic;

import android.content.IntentFilter;

/**
 * Created by admin on 19.02.2017.
 */

public class IntentUtils {
    private static String[] audioIndentActions= {
            "com.android.music.musicservicecommand",
            "com.android.music.metachanged",
            "com.android.music.playstatechanged",
            "com.android.music.updateprogress",
            "com.android.music.metachanged",
            "com.htc.music.metachanged",
            "fm.last.android.metachanged",
            "com.sec.android.app.music.metachanged",
            "com.nullsoft.winamp.metachanged",
            "com.amazon.mp3.metachanged",
            "com.miui.player.metachanged",
            "com.real.IMP.metachanged",
            "com.sonyericsson.music.metachanged",
            "com.rdio.android.metachanged",
            "com.samsung.sec.android.MusicPlayer.metachanged",
            "com.andrew.apollo.metachanged"
    };

    public static IntentFilter setAudioPlayerIntentFilters(){
        IntentFilter audioIndentFilter = new IntentFilter();
        for (String indentAction: audioIndentActions) {
            audioIndentFilter.addAction(indentAction);
        }
        return audioIndentFilter;
    }
}
