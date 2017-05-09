package com.azhukovski.geomusic;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class RemoteUserDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private User user;
    private static final CharSequence[] MAP_TYPE_NAMES =
            {"Обычная", "Спутниковая", "Контурная", "Гибридная"};
    private ImageView mapToggler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_remote_user_details);
        Bundle bundle = getIntent().getExtras();
        try{
            user = bundle.getParcelable("com.azhukovski.geomusic.User");
            user.currentUser = bundle.getParcelable("com.azhukovski.geomusic.currentUser");
            setUIValues(user);
        }catch (NullPointerException e){
            Toast.makeText(RemoteUserDetailsActivity.this, "Возникла ошибка при считывании данных пользователя.", Toast.LENGTH_SHORT).show();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapToggler = (ImageView)this.findViewById(R.id.iv_user);
        mapToggler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMapTypeDialog();
            }
        });
    }

    private void setUIValues(User user){
        TextView tv_login = (TextView) this.findViewById(R.id.tv_login);
        tv_login.setText(user.login);
        tv_login.setSelected(true);

        TextView tv_info = (TextView) this.findViewById(R.id.tv_info);
        tv_info.setText(user.about);
        tv_info.setSelected(true);

        TextView tv_currentSong = (TextView) this.findViewById(R.id.tv_currentSong);
        tv_currentSong.setText(user.song);
        tv_currentSong.setSelected(true);

        TextView tv_currentAlbum = (TextView) this.findViewById(R.id.tv_currentAlbum);
        tv_currentAlbum.setText(user.album);

        TextView tv_distance = (TextView) this.findViewById(R.id.tv_distance);
        tv_distance.setText(user.getFormattedDistanceToCurrentUser() + " м");
        tv_distance.setSelected(true);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final LatLng loggedInUserPosition = addCurrentUserMarker(mMap);
        final LatLng remoteUserPosition = addRemoteUserMarker(mMap);
        drawLineBtweenUsers(mMap);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                setMapZoomToFitMarkers(loggedInUserPosition, remoteUserPosition, mMap);
            }
        });
    }

    private LatLng addCurrentUserMarker(GoogleMap mMap){
        LatLng loggedInUserPosition = new LatLng(Double.parseDouble(user.currentUser.latitude),Double.parseDouble(user.currentUser.longitude));
        mMap.addMarker(new MarkerOptions().position(loggedInUserPosition).title(user.currentUser.login + " (Вы)").snippet(user.currentUser.song).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loggedInUserPosition, 17));
        return loggedInUserPosition;
    }

    private LatLng addRemoteUserMarker(GoogleMap mMap){
        LatLng remoteUserPosition = new LatLng(Double.parseDouble(user.latitude),Double.parseDouble(user.longitude));
        mMap.addMarker(new MarkerOptions().position(remoteUserPosition).title(user.login).snippet(user.song).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        return remoteUserPosition;
    }

    private void drawLineBtweenUsers(GoogleMap mMap){
        PolylineOptions line=
                new PolylineOptions().add(new LatLng(Double.parseDouble(user.latitude), Double.parseDouble(user.longitude)),
                        new LatLng(Double.parseDouble(user.currentUser.latitude), Double.parseDouble(user.currentUser.longitude)))
                        .width(4).color(Color.argb(120, 101, 137, 253));

        mMap.addPolyline(line);
    }

    private void setMapZoomToFitMarkers(LatLng loggedInUserPosition, LatLng remoteUserPosition, GoogleMap mMap){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(loggedInUserPosition);
        builder.include(remoteUserPosition);
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 40));
    }

    private void showMapTypeDialog() {
        final String fDialogTitle = "Тип карты";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(fDialogTitle);

        int checkItem = mMap.getMapType() - 1;
        builder.setSingleChoiceItems(
                MAP_TYPE_NAMES,
                checkItem,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int item) {

                        switch (item) {
                            case 1:
                                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                break;
                            case 2:
                                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                break;
                            case 3:
                                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                                break;
                            default:
                                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        }
                        dialog.dismiss();
                    }
                }
        );

        AlertDialog fMapTypeDialog = builder.create();
        fMapTypeDialog.setCanceledOnTouchOutside(true);
        fMapTypeDialog.show();
    }
}
