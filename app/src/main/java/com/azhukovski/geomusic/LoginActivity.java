package com.azhukovski.geomusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button b_login = (Button)findViewById(R.id.b_login);
        final TextView tv_register = (TextView)findViewById(R.id.tv_register);

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_login = (EditText)findViewById(R.id.et_login);
                EditText et_password = (EditText)findViewById(R.id.et_password);

                String login = et_login.getText().toString();
                String password = et_password.getText().toString();

                authenticateUser(login, password);
            }
        });

    }

    private void authenticateUser(final String login, final String password) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Networking.BASE_API_URL + "login.php";

        StringRequest getUsersRequest = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String >() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getString("status").equals("error")){
                                Toast.makeText(LoginActivity.this, "Возникла ошибка при входе: " + jsonResponse.get("info").toString(), Toast.LENGTH_SHORT).show();
                            } else {

                                Gson gson = new Gson();
                                User user = gson.fromJson(response, User.class);
                                Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                                mainActivityIntent.putExtra("com.azhukovski.geomusic.User", user);
                                mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                LoginActivity.this.startActivity(mainActivityIntent);
                            }
                        }catch (JSONException e){
                            Toast.makeText(LoginActivity.this, "Возникла неизвестная ошибка при входе в аккаунт.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("login", login);
                params.put("password", password);
                return params;
            }
        };
        queue.add(getUsersRequest);
    }
}
