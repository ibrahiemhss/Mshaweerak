package com.aboelyan.sha.mshaweerak;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aboelyan.sha.mshaweerak.Fragments.FragmentLog_In_Sh;
import com.aboelyan.sha.mshaweerak.Nav.DataModel;
import com.aboelyan.sha.mshaweerak.Nav.DrawerItemCustomAdapter;
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

public class Main extends AppCompatActivity {

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



    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    FragmentTransaction ft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_users);

        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header, mDrawerList, false);
        mDrawerList.addHeaderView(header, null, false);
        setupToolbar();

        DataModel[] drawerItem = new DataModel[3];

        drawerItem[0] = new DataModel(R.drawable.driver, "دخول السائقين");
        drawerItem[1] = new DataModel(R.drawable.login, "تسجيل دخول");
        drawerItem[2] = new DataModel(R.drawable.logout, "تسجبل خروج");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        setupDrawerToggle();


            editTextUsername = (EditText) findViewById(R.id.username_txt);
        editTextPassword = (EditText) findViewById(R.id.password_txt);

        buttonLogin = (Button) findViewById(R.id.login_btn);
        txtrRegister = (TextView) findViewById(R.id.register_btn);


        builder = new AlertDialog.Builder(this);
        pref = getSharedPreferences("Login2.conf", Context.MODE_PRIVATE);
        idd = pref.getString("id", "id");
        user_name=pref.getString("username","username");


        editor = pref.edit();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                super.onDrawerSlide(drawerView, 0); // this disables the arrow @ completed state
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disables the animation
            }
        };


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


    public class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        Fragment fragment = null;

        Context context;
        switch (position) {
            case 0:
                Intent intent = new Intent(Main.this, FragmentLog_In_Sh.class);
                intent .putExtra("openF2",true);
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
                break;
            case 1:

                break;
           /*
            case 2:
                fragment = new TableFragment();
                break;*/

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

}