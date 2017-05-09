package com.azhukovski.geomusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private User currentUser;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;


    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private AudioDataFetcher audioData;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            audioData.processBroadcastRecieverIntent(context, intent);
            if (currentUser == null){
                Bundle bundle = getIntent().getExtras();
                currentUser = bundle.getParcelable("com.azhukovski.geomusic.User");
            }
            currentUser.song = audioData.getSong();
            currentUser.album = audioData.getAlbum();
            updateCurrentUserData();
        }
    };


    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            System.out.println(location);
            TextView text = (TextView)findViewById(R.id.tv_intentInfo);
            text.setText(location.toString());
            if (currentUser == null){
                Bundle bundle = getIntent().getExtras();
                currentUser = bundle.getParcelable("com.azhukovski.geomusic.User");
            }
            currentUser.latitude = Double.toString(location.getLatitude());
            currentUser.longitude =  Double.toString(location.getLongitude());
            updateCurrentUserData();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {
            Toast.makeText(MainActivity.this, "GPS включен.", Toast.LENGTH_SHORT).show();
        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(MainActivity.this, "GPS выключен. Влючите его, чтобы начать использовать приложение.", Toast.LENGTH_SHORT).show();
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

        fillMenu();

        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setCurrentUserInfo();


        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(network_enabled){
            try {

                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if(location != null){
                    currentUser.longitude = Double.toString(location.getLongitude());
                    currentUser.latitude = Double.toString(location.getLatitude());
                }
            }
            catch (SecurityException e){
                Toast.makeText(MainActivity.this, "Разрешите определение местоположения.", Toast.LENGTH_SHORT).show();
            }
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        catch (SecurityException e){
            Toast.makeText(MainActivity.this, "Не удалось подписаться на обновления GPS", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(MainActivity.this, "CREATE", Toast.LENGTH_SHORT).show();
        resetUserFields();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mDrawerLayout.closeDrawers();
        reloginIfNeeded();
        resetUserFields();
        Toast.makeText(MainActivity.this, "RESUME", Toast.LENGTH_SHORT).show();
    }
    private  void setCurrentUserInfo(){
        if (currentUser == null) {
            Bundle bundle = getIntent().getExtras();
            currentUser = bundle.getParcelable("com.azhukovski.geomusic.User");
        }
    }

    private  void reloginIfNeeded(){
        if (getIntent().hasExtra("com.azhukovski.geomusic.User")){
            Bundle bundle = getIntent().getExtras();
            currentUser = bundle.getParcelable("com.azhukovski.geomusic.User");
        }
    }
    private void resetUserFields(){
        final TextView tv_username = (TextView)findViewById(R.id.tv_username);
        final TextView tv_aboutUser = (TextView)findViewById(R.id.tv_aboutUser);
        tv_username.setText(currentUser.login);
        tv_aboutUser.setText(currentUser.about);

    }
    private void updateCurrentUserData() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Networking.BASE_API_URL + "updateUserData.php";

        StringRequest  getUsersRequest = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String >() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = preparePostParams(currentUser);
                return params;
            }
        };
        queue.add(getUsersRequest);
    }

    private HashMap<String, String>  preparePostParams(User user){
        HashMap<String, String> userDataParams = new HashMap<String, String>();
        userDataParams.put("id", user.id);
        userDataParams.put("latitude", user.latitude);
        userDataParams.put("longitude", user.longitude);
        userDataParams.put("song", user.song);
        userDataParams.put("album", user.album);
        System.out.println(userDataParams);
        return userDataParams;
    }

    protected void fillMenu() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == 1){
                    Intent intent = new Intent(view.getContext(), UserListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else if (id == 0){
                    mDrawerLayout.closeDrawers();
                }
                else if (id == 2){
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    protected void addDrawerItems() {
        String[] menuArray = { "Профиль", "Лента", "Выход" };
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menuArray);
        mDrawerList.setAdapter(mAdapter);
    }

    protected void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Меню");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else return false;
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
