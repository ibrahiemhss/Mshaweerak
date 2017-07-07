package com.aboelyan.sha.mshaweerak;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.aboelyan.sha.mshaweerak.Booking.Bookings;
import com.aboelyan.sha.mshaweerak.FCMH.FCMRegistrationService;

public class Opening extends AppCompatActivity {

    String username;
    String password;
    String id, user_name;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TextView textView;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
        textView= (TextView) findViewById(R.id.textView3);
/*
        broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        if(SharedprefManager.getIstance(this).getToken()!=null) {
            //FirebaseMessaging.getInstance().subscribeToTopic("tamam");
           // FirebaseInstanceId.getInstance().getToken();
            Log.d("myfcmtokenshared",SharedprefManager.getIstance(this).getToken());

        }

        registerReceiver(broadcastReceiver,new IntentFilter(FCMInstanceIdService.TOKEN_BROADCAST));*/
        startService(new Intent(this,FCMRegistrationService.class));

        pref = getSharedPreferences("Login2.conf", Context.MODE_PRIVATE);
        user_name = pref.getString("username","" );
        password = pref.getString("password", "");
        editor = pref.edit();
        id= pref.getString("id", "id");
        Toast.makeText(this,user_name+" "+password+" "+id,Toast.LENGTH_LONG).show();



        if(user_name.equals("")||password.equals("")){

            Toast.makeText(this,"من فضلك قم بالتسجيل",Toast.LENGTH_LONG).show();
            startActivity(new Intent(Opening.this, MainActivity.class));

        }else {
            Intent intent = new Intent(Opening.this, Bookings.class);
            Bundle bundle = new Bundle();
            bundle.putString("username",user_name );
            bundle.putString("id", id);
            intent.putExtras(bundle);
            startActivity(intent);

        }
    }
}
