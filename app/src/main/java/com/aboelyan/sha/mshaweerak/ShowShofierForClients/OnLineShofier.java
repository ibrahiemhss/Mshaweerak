package com.aboelyan.sha.mshaweerak.ShowShofierForClients;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aboelyan.sha.mshaweerak.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OnLineShofier  extends FragmentActivity implements
        OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {


    TextView SHNAME, CARMODEL, PHONE, LOCATIONCHANGED;
    Button callShofier, ViewMapShofier;
    MapView mapViewShofier;
    String Phone;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    String NameMape;
    String sh_id;
    double latitude,longitude;
    SharedPreferences prefsh, SharPlace;
    AppCompatButton login_Sh;
    SharedPreferences.Editor editorsh, editorSharPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_line_shofier);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        String LOCATION_URL = "http://devsinai.com/mashaweer/GoogleMaps/Location.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,LOCATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(OnLineShofier.this, response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject  jsonRootObject = new JSONObject(response);

                            //Get the instance of JSONArray that contains JSONObjects
                            JSONArray jsonArray = jsonRootObject.optJSONArray("");

                            //Iterate the jsonArray and print the info of JSONObjects
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                 latitude = Double.parseDouble(jsonObject.optString("latitude").toString());
                                 longitude = Double.parseDouble(jsonObject.optString("longitude").toString());

                            }

                        } catch (JSONException e) {e.printStackTrace();}

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OnLineShofier.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                })


        {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();

                map.put("sh_id", sh_id);



                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        mapFragment.getMapAsync(this);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }


        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        prefsh = getSharedPreferences("Loginsh.shofier", Context.MODE_PRIVATE);


        prefsh = getSharedPreferences("Loginsh.shofier", Context.MODE_PRIVATE);

            sh_id = prefsh.getString("sh_id", "sh_id");
           // lat = prefsh.getString("latitude", "latitude");
           // lang = prefsh.getString("longitude", "longitude");


        SHNAME = (TextView) findViewById(R.id.SHNAME);
        CARMODEL = (TextView) findViewById(R.id.CARMODEL);
        PHONE = (TextView) findViewById(R.id.PHONE);
        LOCATIONCHANGED = (TextView) findViewById(R.id.LOCATIONCHANGED);

        final Bundle bundle = getIntent().getExtras();
        SHNAME.setText("الاسم         : " + bundle.getString("name"));
        CARMODEL.setText("الهاتف       : " + bundle.getString("phone"));
        PHONE.setText("موديل السياره : " + bundle.getString("model_car"));
        NameMape=bundle.getString("name").trim();



        callShofier = (Button) findViewById(R.id.callShofier);

        Phone = bundle.getString("phone");
        callShofier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialContactPhone(Phone);
              //  Toast.makeText(OnLineShofier.this,lat+"   "+lang+sh_id,Toast.LENGTH_LONG).show();

            }
        });

    }



    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }
    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }


    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            Location userCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (userCurrentLocation != null) {
                LatLng sydney = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(sydney).title(NameMape));
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,7));
                //=============================

                // Starting locations retrieve task
            }
//====================================
            }
        }



    @Override
    public void onConnectionSuspended(int i) {

    }  @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onConnected(null);
        } else {
            Toast.makeText(OnLineShofier.this, "No Permitions Granted", Toast.LENGTH_SHORT).show();
        }
    }

//=============================================
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }



}
