package com.aboelyan.sha.mshaweerak.RecyclerViewClients;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aboelyan.sha.mshaweerak.GoogleMaps.GPSTracker;
import com.aboelyan.sha.mshaweerak.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListDesplayForClient extends AppCompatActivity  {


    RecyclerView Desplay_Shofires_For_Clients;
    private ClientAdapter adapter;
    private RequestQueue mRequestQueue;
    private List<ClientsModel> clientsModels ;
    SharedPreferences pref2,pref,prefsh;
    SharedPreferences.Editor editorsh,editor,editor2;
    private JsonArrayRequest jsonArrayRequest;
    public MyLocationListener2 listener;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter  recyclerViewadapter;
    private RequestQueue requestQueue;
    String urlListDesplayShForClient = "http://devsinai.com/mashaweer/ListDesplayForClient.php";
    String car_id,cr_id,LatitudeSend,LongtudeSend;
    ProgressBar progressBar;
    private ProgressDialog pd;
    String latitude,longitude;
    SharedPreferences SharPlace;
    SharedPreferences.Editor editorSharPlace;
    double litude,longtude;
    String comminglitude,comminglongtudee;
    public LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_desplay_for_client);
        clientsModels=new ArrayList<>();
        Desplay_Shofires_For_Clients = (RecyclerView) findViewById(R.id.Desplay_Shofires_For_Clients);
        Desplay_Shofires_For_Clients.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        Desplay_Shofires_For_Clients.setLayoutManager(recyclerViewlayoutManager);
        recyclerViewadapter = new ClientAdapter(clientsModels,this);
        Desplay_Shofires_For_Clients.setAdapter(recyclerViewadapter);

        GPSTracker gps = new GPSTracker(this);
        pd = new ProgressDialog(ListDesplayForClient.this);
        pd.setMessage("Loading . . . ");

        pref2 = getSharedPreferences("myPrefs",MODE_PRIVATE);
        cr_id=pref2.getString("car_id","car_id");
        editor2=pref2.edit();


        SharPlace = getSharedPreferences("Share.Places", Context.MODE_PRIVATE);


        comminglitude =  SharPlace.getString("litude","");
        comminglongtudee=  SharPlace.getString("longtude","");
        editorSharPlace=SharPlace.edit();




        Log.d(comminglitude,"comminglitudeeeee"+"   "+comminglongtudee+"comminglongtudeeeee");
        Toast.makeText(this,comminglitude+" "+comminglongtudee,Toast.LENGTH_LONG).show();

        // Toast.makeText(this, car_id+"A"+"           "+cr_id+"B", Toast.LENGTH_SHORT).show();

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        if(gps.canGetLocation()){

            latitude = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());

        }

        else{

            gps.showSettingsAlert();

        }

        JSON_DATA_WEB_CALL();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) { // Please, use a final int instead of hardcoded int value
            if (resultCode == RESULT_OK) {
                comminglitude = (String) data.getExtras().getString("litude");
                comminglongtudee = (String) data.getExtras().getString("longtude");

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener2();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);

    }

    public void JSON_DATA_WEB_CALL() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, urlListDesplayShForClient,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            progressBar.setVisibility(View.GONE);

                            JSON_PARSE_DATA_AFTER_WEBCALL(jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params=new HashMap<String, String>();
                params.put("car_id", cr_id);
                params.put("latitude", String.valueOf(litude));
                params.put("longitude", String.valueOf(longtude));
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);

    }


    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {

            ClientsModel clientsModel2 = new ClientsModel();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                clientsModel2.setNAME(json.getString("username"));

                clientsModel2.setPHONE(json.getString("phone"));
                clientsModel2.setMODEL_CAR(json.getString("model_car"));
                clientsModel2.setLatitude(json.getString("latitude"));
                clientsModel2.setLongitude(json.getString("longitude"));

                clientsModel2.setDistance(json.getString("distance"));


                // bookModels2.setUser_id(json.getString("user_id"));

                //  bookModels2.setCar_id(json.getString("car_id"));
                Log.v(String.valueOf(json),"oioiiooo");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            clientsModels.add(clientsModel2);
        }

        recyclerViewadapter = new ClientAdapter(clientsModels,this);

        Desplay_Shofires_For_Clients.setAdapter(recyclerViewadapter);




    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(listener);

    }

    public class MyLocationListener2  implements GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
            LocationListener {

        private LocationRequest locationRequest;
        private GoogleApiClient googleApiClient;
        private Context appContext;
        private boolean currentlyProcessingLocation = false;
        private int mInterval=0;
        private final int CONNTIMEOUT=50000;
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

        @Override
        public void onLocationChanged(Location location) {
            // Log.v(Constants.BLL_LOG, "onLocationChanged position: " + location.getLatitude() + ", " + location.getLongitude() + " accuracy: " + location.getAccuracy());
            // Log.v(Constants.BLL_LOG, "onLocationChanged position: location.getAccuracy()= "+location.getAccuracy());
            litude=location.getLatitude();
            longtude=location.getLongitude();
            Log.v(String.valueOf(litude),"bnbnbnbn222");
            Log.v(String.valueOf(longtude),"fgghhghghg222");


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            locationRequest = LocationRequest.create();
            locationRequest.setInterval(mInterval * 1000); // milliseconds
            locationRequest.setFastestInterval(mInterval * 1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setSmallestDisplacement(MIN_DISTANCE_CHANGE_FOR_UPDATES);//dostance change
            int permissionCheck = ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permissionCheck!= PackageManager.PERMISSION_DENIED)
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, (com.google.android.gms.location.LocationListener) this);


        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }
    }
    public void onProviderDisabled(String provider)
    {
        Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
    }


    public void onProviderEnabled(String provider)
    {
        Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
    }


    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }


}