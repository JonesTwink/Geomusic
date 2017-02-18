package com.azhukovski.geomusic;

import android.widget.TextView;

public class AudioDataUIUpdater {

    private MainActivity currentActivity;
    private TextView tv_currentSong = null;
    private TextView tv_intentInfo = null;
    private TextView tv_currentAlbum = null;

    AudioDataUIUpdater(MainActivity activity){
        currentActivity = activity;
        defineAudioTextViews();
    }

    private void defineAudioTextViews(){
        this.tv_currentSong = (TextView) currentActivity.findViewById(R.id.tv_currentSong);
        this.tv_currentAlbum = (TextView) currentActivity.findViewById(R.id.tv_currentAlbum);
        this.tv_intentInfo = (TextView) currentActivity.findViewById(R.id.tv_intentInfo);

        this.tv_currentSong.setSelected(true);
    }

    private void setAudioTextViewsInfo(String artist, String track, String album){
        tv_currentSong.setText(artist + " - " + track);
        tv_currentAlbum.setText("Альбом: " + album);
    }

    public void updateAudioInfo(String artist, String track, String album, boolean isPlaying){
        if (isPlaying) {
            setAudioTextViewsInfo(artist, track, album);
            setUserListeningStatus(true);
        } else {
            setAudioTextViewsInfo(artist, track, album);
            setUserListeningStatus(false);
        }
    }

    private void setUserListeningStatus(boolean isCurrentlyListening){
        TextView listeningStatus = (TextView) currentActivity.findViewById(R.id.tv_nowListetingTo);
        if (isCurrentlyListening){
            listeningStatus.setText("Сейчас слушает...");
        }else {
            listeningStatus.setText("Последний прослушанный трек...");
        }
    }

    public void showDebugInfo(String debugInfo){
        tv_intentInfo.setText(debugInfo);
    }
}
