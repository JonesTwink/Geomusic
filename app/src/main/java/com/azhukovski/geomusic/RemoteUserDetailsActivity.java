package com.azhukovski.geomusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RemoteUserDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_user_details);
        Bundle bundle = getIntent().getExtras();
        user = bundle.getParcelable("com.azhukovski.geomusic.User");
        setUIValues(user);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng userPosition = new LatLng(Double.parseDouble(user.latitude),Double.parseDouble(user.longitude));
        mMap.addMarker(new MarkerOptions().position(userPosition).title(user.login).snippet(user.song).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition, 17));
    }
}
