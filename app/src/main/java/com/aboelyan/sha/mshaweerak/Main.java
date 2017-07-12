package com.aboelyan.sha.mshaweerak;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aboelyan.sha.mshaweerak.Fragments.Log_In_Sh_Dialog;
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

public class Main extends AppCompatActivity  {

    private final static String LOGIN_URL = "http://devsinai.com/mashaweer/login.php";
    EditText editTextUsername;
    EditText editTextPassword;
    TextView txtrRegister;
        TextView    registerSh_btn;
    String username;
    String password;
    String idd, user_name;
    AlertDialog.Builder builder;
    SharedPreferences pref;
    Button buttonLogin;
    SharedPreferences.Editor editor;



    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


            editTextUsername = (EditText) findViewById(R.id.username_txt);
        editTextPassword = (EditText) findViewById(R.id.password_txt);

        buttonLogin = (Button) findViewById(R.id.login_btn);
        txtrRegister = (TextView) findViewById(R.id.register_btn);


        builder = new AlertDialog.Builder(this);
        pref = getSharedPreferences("Login2.conf", Context.MODE_PRIVATE);
        idd = pref.getString("id", "id");
        user_name=pref.getString("username","username");


        editor = pref.edit();



        txtrRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(Main.this, UseRegestraion.class));
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



                                                                       editor.putString("password", password);
                                                                       editor.putString("id", jsonObject.getString("id"));
                                                                       editor.putString("username", username);
                                                                       editor.putString("id", jsonObject.getString("id"));

                                                                       Log.v(response,"respons"+ "username"  + username + "password" + password);

                                                                       editor.commit();

                                                                       String code = jsonObject.getString("code");
                                                                       if (code.equals("login_failed")) {
                                                                           builder.setTitle("Login Error");
                                                                           desplayalert(jsonObject.getString("message"));
                                                                       } else {
                                                                           Fragment fragment = null;

                                                                           fragment = new Fragment_Booking();

                                                                           FragmentManager fragmentManager = getSupportFragmentManager();
                                                                           fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

                                                                               Toast.makeText(Main.this, idd, Toast.LENGTH_LONG).show();
                                                                               Toast.makeText(Main.this, user_name, Toast.LENGTH_LONG).show();

                                                                       }
                                                                   } catch (JSONException e) {
                                                                       e.printStackTrace();
                                                                   }

                                                               }
                                                           }, new Response.ErrorListener() {
                                                       @Override
                                                       public void onErrorResponse(VolleyError error) {
                                                           Toast.makeText(Main.this, "Error", Toast.LENGTH_LONG).show();
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
                                                   Mysingletone.getInstance(Main.this).addToRequestque(stringRequest);
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




    private void addDrawerItems() {
        String[] osArray = {"تسجيل دخول السائقين", "تسجيل دخول الزبائن", "Windows", "OS X", "Linux"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Log_In_Sh_Dialog log_in_sh_dialog = new Log_In_Sh_Dialog(Main.this);
                        log_in_sh_dialog.show();
                }
            }
        });
    }
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}