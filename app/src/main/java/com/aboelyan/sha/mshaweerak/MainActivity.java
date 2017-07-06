package com.aboelyan.sha.mshaweerak;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aboelyan.sha.mshaweerak.Booking.Bookings;
import com.aboelyan.sha.mshaweerak.Shofiers.LogInShofiers;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity /*implements CompoundButton.OnCheckedChangeListener*/ {

    private final static String LOGIN_URL = "http://devsinai.com/mashaweer/login.php";
    EditText editTextUsername;
    EditText editTextPassword;
    TextView txtrRegister,registerSh_btn;
    String username;
    String password;
    String id, user_name;
    AlertDialog.Builder builder;
    SharedPreferences pref;
    AppCompatButton buttonLogin;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = (EditText) findViewById(R.id.username_txt);
        editTextPassword = (EditText) findViewById(R.id.password_txt);

        buttonLogin = (AppCompatButton) findViewById(R.id.login_btn);
        txtrRegister = (TextView) findViewById(R.id.register_btn);
        registerSh_btn= (TextView) findViewById(R.id.registerSh_btn);

        builder = new AlertDialog.Builder(this);
        pref = getSharedPreferences("Login2.conf", Context.MODE_PRIVATE);
        id = pref.getString("id", "id");
        user_name=pref.getString("username","username");


        editor = pref.edit();

        txtrRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(MainActivity.this, UseRegestraion.class));
            }
        });
        registerSh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LogInShofiers.class));
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(final View v) {

                                               username = editTextUsername.getText().toString();
                                               password = editTextPassword.getText().toString();

                                               if (username.equals("") || password.equals("")) {
                                                   builder.setTitle("somthing wrong");
                                                   desplayalert("Enter a valid username and password");
                                               } else {
                                                   StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                                                           new Response.Listener<String>() {
                                                               @Override
                                                               public void onResponse(String response) {

                                                                   try {
                                                                       JSONArray jsonArray = new JSONArray(response);
                                                                       JSONObject jsonObject = jsonArray.getJSONObject(0);



                                                                       editor.putString("username", username);
                                                                       editor.putString("password", password);
                                                                       editor.putString("id", jsonObject.getString("id"));
                                                                       editor.putString("username", username);

                                                                       Log.v(response,"respons"+ "username"  + username + "password" + password);

                                                                       editor.commit();

                                                                       String code = jsonObject.getString("code");
                                                                       if (code.equals("login_failed")) {
                                                                           builder.setTitle("Login Error");
                                                                           desplayalert(jsonObject.getString("message"));
                                                                       } else {
                                                                          Intent intent = new Intent(MainActivity.this, Bookings.class);
                                                                           Bundle bundle = new Bundle();
                                                                           bundle.putString("username",user_name );
                                                                           bundle.putString("id", id);
                                                                         intent.putExtras(bundle);
                                                                          startActivity(intent);
                                                                           Toast.makeText(MainActivity.this,id,Toast.LENGTH_LONG).show();
                                                                           Toast.makeText(MainActivity.this, user_name, Toast.LENGTH_LONG).show();

                                                                       }
                                                                   } catch (JSONException e) {
                                                                       e.printStackTrace();
                                                                   }

                                                               }
                                                           }, new Response.ErrorListener() {
                                                       @Override
                                                       public void onErrorResponse(VolleyError error) {
                                                           Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                                                           VolleyLog.e("Error: ", error.getMessage());
                                                           error.printStackTrace();

                                                       }
                                                   }
                                                   ) {
                                                       @Override
                                                       protected Map<String, String> getParams() throws AuthFailureError {

                                                           Map<String, String> params = new HashMap<String, String>();
                                                           params.put("username", username);
                                                           params.put("password", password);

                                                           return params;
                                                       }
                                                   };
                                                   Mysingletone.getInstance(MainActivity.this).addToRequestque(stringRequest);
                                               }
                                           }
                                       }

        );
    }

    private void desplayalert(String message) {

        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                editTextUsername.setText("");
                editTextPassword.setText("");

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void TestUpload() {
        int i = 1 + 1;
    }
}