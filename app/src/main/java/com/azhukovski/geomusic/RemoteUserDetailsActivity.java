package com.azhukovski.geomusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class RemoteUserDetailsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_user_details);
        Bundle bundle = getIntent().getExtras();
        User user = bundle.getParcelable("com.azhukovski.geomusic.User");
        setUIValues(user);

    }

    private void setUIValues(User user){
        TextView tv_login = (TextView) this.findViewById(R.id.tv_login);
        tv_login.setText(user.login);

        TextView tv_info = (TextView) this.findViewById(R.id.tv_info);
        tv_info.setText(user.info);

        TextView tv_currentSong = (TextView) this.findViewById(R.id.tv_currentSong);
        tv_currentSong.setText(user.song);

        TextView tv_currentAlbum = (TextView) this.findViewById(R.id.tv_currentAlbum);
        tv_currentAlbum.setText(user.album);

    }
}
