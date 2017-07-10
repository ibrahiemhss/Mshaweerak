package com.aboelyan.sha.mshaweerak;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.aboelyan.sha.mshaweerak.Booking.Bookings;
import com.aboelyan.sha.mshaweerak.Clients.LogInUsers;
import com.aboelyan.sha.mshaweerak.FCMH.FCMRegistrationService;
import com.aboelyan.sha.mshaweerak.SendDataInSecond.MyService;

public class Opening extends AppCompatActivity {

    String username;
    String password;
    String id, user_name;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TextView textView;
    private BroadcastReceiver broadcastReceiver;

    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Calendar calendar;
    Intent alarm;
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

        final int SDK_INT = Build.VERSION.SDK_INT;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
        }


        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm = new Intent(this, MyService.class);
        pendingIntent = PendingIntent.getService(this, 0, alarm, 0);

        if (SDK_INT < Build.VERSION_CODES.KITKAT) {
            alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+10000, pendingIntent);
            Log.d("lowerMF","hahah");
        }
        else if (Build.VERSION_CODES.KITKAT <= SDK_INT  && SDK_INT < Build.VERSION_CODES.M) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+10000,pendingIntent);
            Log.d("kitkatMF","hahah");
        }
        else if (SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+10000,pendingIntent);
            Log.d("marshmallowMF","hahah");
        }

        startService(new Intent(this,FCMRegistrationService.class));

        pref = getSharedPreferences("Login2.conf", Context.MODE_PRIVATE);
        user_name = pref.getString("username","" );
        password = pref.getString("password", "");
        id= pref.getString("id", "id");

        editor = pref.edit();
        Toast.makeText(this,user_name+" "+password+" "+id,Toast.LENGTH_LONG).show();



        if(user_name.equals("")||password.equals("")){

            Toast.makeText(this,"من فضلك قم بالتسجيل",Toast.LENGTH_LONG).show();
            startActivity(new Intent(Opening.this, LogInUsers.class));

        }else {
            Intent intent = new Intent(Opening.this, Bookings.class);
            Bundle bundle = new Bundle();
            bundle.putString("username",user_name );
          //  bundle.putString("id", id);
            intent.putExtras(bundle);
            startActivity(intent);

        }
    }
}
