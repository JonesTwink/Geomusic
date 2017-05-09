package com.azhukovski.geomusic;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

public class UserListActivity extends AppCompatActivity {
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private ListView usersList;
    private ArrayAdapter<User> usersAdapter;
    private User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        fillMenu();
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (getIntent().hasExtra("com.azhukovski.geomusic.User")){
            Bundle bundle = getIntent().getExtras();
            currentUser = bundle.getParcelable("com.azhukovski.geomusic.User");
        }

        usersList = (ListView)findViewById(R.id.usersList);
        fillUserList();
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) parent.getItemAtPosition(position);
                Intent intent = new Intent(view.getContext(), RemoteUserDetailsActivity.class);
                intent.putExtra("com.azhukovski.geomusic.User", user);
                intent.putExtra("com.azhukovski.geomusic.currentUser", currentUser);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        mDrawerLayout.closeDrawers();

        if (getIntent().hasExtra("com.azhukovski.geomusic.User")){
            Bundle bundle = getIntent().getExtras();
            currentUser = bundle.getParcelable("com.azhukovski.geomusic.User");
        }
        fillUserList();
    }

    private void fillUserList() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Networking.BASE_API_URL + "geoget.php";

        final Context activityContext = this.getBaseContext();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        User[] users = gson.fromJson(response.toString(), User[].class);
                        users = setCurrentUserForOtherUsers(users, currentUser);
                        users = removeCurrentUserFromList(users, currentUser);
                        Arrays.sort(users);
                        usersAdapter = new ArrayAdapter<User>(activityContext, android.R.layout.simple_list_item_1, users);
                        usersList.setAdapter(usersAdapter);
                        System.out.println(users);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(stringRequest);
    }
    private User[] setCurrentUserForOtherUsers(User[] users, User currentUser){
        for (int i = 0; i < users.length; i++) {
            users[i].currentUser = currentUser;
        }
        return users;
    }

    private User[] removeCurrentUserFromList(User[] users, final User currentUser){
        ArrayList<User> list = new ArrayList<>(Arrays.asList(users));
        for (int i = 0; i < users.length; i++) {
            if(users[i].id.equals(currentUser.id)){
                list.remove(i);
                users = new User[list.size()];
                return list.toArray(users);

            }
        }
        return users;
    }

    protected void fillMenu() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0){
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else if (id == 1){
                    mDrawerLayout.closeDrawers();
                }
                else if (id == 2){
                    Intent intent = new Intent(UserListActivity.this, LoginActivity.class);
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
