package com.azhukovski.geomusic;

import android.content.Intent;
import android.os.StrictMode;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText et_login = (EditText)findViewById(R.id.et_login);
        final EditText et_email = (EditText)findViewById(R.id.et_email);
        final EditText et_about = (EditText)findViewById(R.id.et_about);
        final EditText et_password = (EditText)findViewById(R.id.et_password);
        final Button b_register = (Button)findViewById(R.id.b_register);

        final TextView tv_login = (TextView)findViewById(R.id.tv_login);

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(loginIntent);
            }
        });

        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String login = et_login.getText().toString();
                final String password = et_password.getText().toString();
                final String email = et_email.getText().toString();
                final String about = et_about.getText().toString();
                registerUser(login, password, email, about);
            }
        });
    }

    private void registerUser(final String login, final String password, final String email, final String about) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Networking.BASE_API_URL + "register.php";

        StringRequest getUsersRequest = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String >() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getString("status").equals("error")){
                                Toast.makeText(RegisterActivity.this, "Возникла ошибка при регистрации: " + jsonResponse.get("info").toString(), Toast.LENGTH_SHORT).show();
                            } else if (jsonResponse.getString("status").equals("success")){
                                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                RegisterActivity.this.startActivity(loginIntent);
                            }
                        }catch (JSONException e){
                            Toast.makeText(RegisterActivity.this, "Возникла неизвестная ошибка при регистрации.", Toast.LENGTH_SHORT).show();
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
                params.put("email", email);
                params.put("about", about);
                return params;
            }
        };
        queue.add(getUsersRequest);
    }
}
