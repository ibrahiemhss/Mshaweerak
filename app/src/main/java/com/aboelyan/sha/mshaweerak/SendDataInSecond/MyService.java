package com.aboelyan.sha.mshaweerak.SendDataInSecond;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.widget.Toast;

import com.aboelyan.sha.mshaweerak.Mysingletone;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {


private GoogleApiClient mGoogleApiClient;
private Location mLocation;
private LocationManager mLocationManager;
private LocationRequest mLocationRequest;

    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;
    SharedPreferences prefsh, SharPlace;
    AppCompatButton login_Sh;
    SharedPreferences.Editor editorsh, editorSharPlace;
    double litude,longtude;
    public static final String TAG = "WEAVER_";
    String getlatitude,getlongitude;;

    private Activity mActivity;


    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    int updates;

    Intent intent;
    int counter = 0;
    private Timer timer = new Timer();


    @Override
    public void onCreate() {
        super.onCreate();

        ScheduledExecutorService executor =
                Executors.newSingleThreadScheduledExecutor();

        Runnable periodicTask = new Runnable() {
            public void run() {
                // Invoke method(s) to do the work
                sendRequestToServer();



            }
        };
        executor.scheduleAtFixedRate(periodicTask, 0, 1, TimeUnit.MINUTES);

    }

    private void sendRequestToServer() {

        String UBDATE_LOCATION_URL = "http://devsinai.com/mashaweer/GoogleMaps/UbdateLocation.php";

        prefsh = getSharedPreferences("Loginsh.shofier", Context.MODE_PRIVATE);
        final String sh_id = prefsh.getString("sh_id", "sh_id");

        Log.i(sh_id, "loacattio_user_id");
        Log.i(String.valueOf(litude), "litudeeeeeeeeee");
        Log.i(String.valueOf(longtude), "longtudeeeeeeee");

        editorsh = prefsh.edit();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UBDATE_LOCATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("sh_id", sh_id);
                params.put("latitude", String.valueOf(litude));
                params.put("longitude", String.valueOf(longtude));

                return params;
            }
        };
        Mysingletone.getInstance(MyService.this).addToRequestque(stringRequest);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
        return START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }


    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
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

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }


    public class MyLocationListener  implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener{

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
            Log.v(String.valueOf(litude),"bnbnbnbn");
            Log.v(String.valueOf(longtude),"fgghhghghg");
            SharPlace = getSharedPreferences("Share.Places", Context.MODE_PRIVATE);
            editorSharPlace.putFloat("litude", (float)(litude));
            editorSharPlace.putFloat("longtude", (float) longtude);

            editorSharPlace.commit();

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