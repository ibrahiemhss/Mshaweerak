package com.aboelyan.sha.mshaweerak.Booking;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aboelyan.sha.mshaweerak.Mysingletone;
import com.aboelyan.sha.mshaweerak.R;
import com.aboelyan.sha.mshaweerak.RecyclerViewClients.ListDesplayForClient;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Bookings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView selection;
    String[] items = { "اختر نوع السياره", "تاكسي اجره", "ميكروباص", "نصف نقل"};
    int pos;
    Spinner spin;
    static  String carstype;
    EditText face ,Time ,phone;
    AppCompatButton btn_Book;
    SharedPreferences pref,pref2;
    SharedPreferences.Editor editor,editor2;
    private String pushNotificationsBooking = "http://devsinai.com/mashaweer/messages/pushBook.php";

    String Url_Book="http://devsinai.com/mashaweer/insert_booking.php";
    String FACE,TIME,PHONE;
    AlertDialog.Builder builder;

    String id_user,username;

    TextView viewname,vieweId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

      //  FirebaseMessaging.getInstance().subscribeToTopic("tamam");
       // FirebaseInstanceId.getInstance().getToken();

        face= (EditText) findViewById(R.id.face);
        Time= (EditText) findViewById(R.id.Time);
        phone= (EditText) findViewById(R.id.phone);
        btn_Book= (AppCompatButton) findViewById(R.id.btn_Book);
        viewname= (TextView) findViewById(R.id.viewname);
        vieweId= (TextView) findViewById(R.id.vieweId);

        builder = new AlertDialog.Builder(this);

       final Bundle bundle = getIntent().getExtras();
       viewname.setText("الاسم : " + bundle.getString("username"));
      // vieweId.setText("الرقم  : " + bundle.getString("id"));




        pref = getSharedPreferences("Login2.conf", Context.MODE_PRIVATE);
        username = pref.getString("username","" );
        id_user= pref.getString("id", "id");

        editor = pref.edit();
       // Toast.makeText(this,user_name+" "+password+" "+id,Toast.LENGTH_LONG).show();

        pref = getSharedPreferences("Login2.conf", Context.MODE_PRIVATE);
       id_user = pref.getString("id", "id");
        username= pref.getString("username", "username");
        editor = pref.edit();

        pref2 = getSharedPreferences("myPrefs", MODE_PRIVATE);
        username=pref2.getString("car_id","car_id");
        editor2=pref2.edit();

        selection = (TextView) findViewById(R.id.car_kind);
         spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                items);
        aa.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        Toast.makeText(Bookings.this,carstype+"  "+username, Toast.LENGTH_LONG).show();


        btn_Book. setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FACE=face.getText().toString();
                TIME=Time.getText().toString();


                Toast.makeText(Bookings.this,carstype, Toast.LENGTH_LONG).show();


                if(FACE.equals("")||TIME.equals("")){
                    builder.setTitle("somthing wrong");
                    desplayalert("Enter a valid username and password");
                }else {

                    builder.setTitle("Confirm Delete...");
                    builder.setMessage("هل انت فعلا تريد الحجز");
                    builder.setPositiveButton("نعم الحجز الان", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Url_Book,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);

                                                String Response = jsonObject.getString("response");


                                                Log.v(response,"responsssss");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("face", FACE);
                                    params.put("traveTime", TIME);
                                    params.put("user_id", id_user);
                                    params.put("car_id", carstype);


                                    return params;
                                }
                            };
                            Mysingletone.getInstance(Bookings.this).addToRequestque(stringRequest);
                            NotficationOfBook();

                            Intent i=new Intent(Bookings.this, ListDesplayForClient.class);
                            startActivity(i);

                        }
                    });
                    builder.setNegativeButton("لا خروج", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }

                    });
                    builder.show();
                } //=====ende else=========




            };



        });


    }

    public void onItemSelected(AdapterView<?> parent, View v, int position,
                               long id) {

        int num=spin.getSelectedItemPosition();
         carstype=Integer.toString(num);
        Log.d("uiiui", carstype);
         pref2 = getSharedPreferences("myPrefs", MODE_PRIVATE);
        editor2 = pref2.edit();
        editor2.putString("car_id",carstype); // add or overwrite someValue
        editor2.commit();
        selection.setText(items[position]);

    }

    public void onNothingSelected(AdapterView<?> parent) {
        selection.setText("");
    }
    private void desplayalert(String message) {

        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                face.setText("");
                Time.setText("");
                phone.setText("");


            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void NotficationOfBook() {



        final StringRequest stringRequest = new StringRequest(Request.Method.POST, pushNotificationsBooking,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String Response = jsonObject.getString("response");

                            Log.d("responsey", Response);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();


                params.put("username", username);            // Add this line to send USER ID to server
                params.put("car_id",carstype);


                return params;
            }
        };

        Mysingletone.getInstance(Bookings.this).addToRequestque(stringRequest);
    }

}//class

