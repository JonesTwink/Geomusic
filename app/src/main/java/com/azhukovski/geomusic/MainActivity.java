package com.azhukovski.geomusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Window;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private AudioDataFetcher audioData;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            audioData.processBroadcastRecieverIntent(context, intent);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);

        audioData = new AudioDataFetcher(this);

        IntentFilter audioPlayerIntentFilter = IntentUtils.setAudioPlayerIntentFilters();
        registerReceiver(mReceiver, audioPlayerIntentFilter);

    }

}
