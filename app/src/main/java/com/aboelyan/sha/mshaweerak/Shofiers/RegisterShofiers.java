package com.aboelyan.sha.mshaweerak.Shofiers;

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
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterShofiers extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    AppCompatButton btn_signupSh;
    TextView link_loginSh;
    TextView selection;
    EditText reg_usernamSh, phoneSh, reg_passwordSh, reg_confirmpassSh,ModelCarSh;
    String name, Phones, username, password, confirmpassword,MODECAR;
    String reg_url = "http://devsinai.com/mashaweer/register_shofier.php";
    android.app.AlertDialog.Builder builder;
    SharedPreferences prefToken;
    Spinner spinSh;
    static  String carstype;
    SharedPreferences pref,pref2Sh;
    SharedPreferences.Editor editor,editor2Sh;
    SharedPreferences.Editor editorToken;
    Context context;
    String[] items = { "اختر نوع السياره", "تاكسي اجره", "ميكروباص", "نصف نقل"};
    String Token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shofiers);

        btn_signupSh = (AppCompatButton) findViewById(R.id.btn_signupSh);
        reg_usernamSh = (EditText) findViewById(R.id.reg_usernamSh);
        phoneSh = (EditText) findViewById(R.id.phoneSh);
        reg_passwordSh = (EditText) findViewById(R.id.reg_passwordSh);
        ModelCarSh= (EditText) findViewById(R.id.ModelCarSh);
        reg_confirmpassSh = (EditText) findViewById(R.id.reg_confirmpassSh);
        link_loginSh= (TextView) findViewById(R.id.link_loginSh);
        selection = (TextView) findViewById(R.id.car_kindSh);

        spinSh= (Spinner) findViewById(R.id.spinnerSh);

        spinSh.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                items);
        aa.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinSh.setAdapter(aa);


        builder = new android.app.AlertDialog.Builder(RegisterShofiers.this);

        link_loginSh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterShofiers.this, LogInShofiers.class));
            }
        });
        btn_signupSh.setOnClickListener(new View.OnClickListener() {
                                      @Override

                                      public void onClick(View v) {

                                          pref2Sh = getSharedPreferences("myPrefsShofiers", MODE_PRIVATE);
                                          final String caaaar=pref2Sh.getString("someValue",carstype);
                                          editor2Sh=pref2Sh.edit();


                                                FirebaseMessaging.getInstance().subscribeToTopic("test");
                                             final String Tokens =   FirebaseInstanceId.getInstance().getToken();

                                            Toast.makeText(RegisterShofiers.this,Tokens,Toast.LENGTH_LONG).show();
                                          Phones = phoneSh.getText().toString();
                                          username = reg_usernamSh.getText().toString();
                                          password = reg_passwordSh.getText().toString();
                                          MODECAR=ModelCarSh.getText().toString();
                                          confirmpassword = reg_confirmpassSh.getText().toString();
                                          if ( Phones.equals("") || username.equals("") || password.equals("") || confirmpassword.equals("")) {
                                              builder.setTitle("something went wrong .....");
                                              builder.setMessage("please fill all fields");
                                              diplayAlert("input_error");
                                          } else {
                                              if (!(password.equals(confirmpassword))) {
                                                  builder.setTitle("something went wrong .....");
                                                  builder.setMessage("not correct");
                                                  diplayAlert("input_error");
                                              } else {StringRequest stringRequest = new StringRequest(Request.Method.POST, reg_url,
                                                      new Response.Listener<String>() {
                                                          @Override
                                                          public void onResponse(String response) {
                                                              try {JSONArray jsonArray = new JSONArray(response);
                                                                  JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                                  String code = jsonObject.getString("code");
                                                                  String message = jsonObject.getString("message");
                                                                  builder.setTitle("التسجيل :");
                                                                  builder.setMessage(message);
                                                                  diplayAlert(code);
                                                                  builder.show();
                                                              } catch (JSONException e) {
                                                                  e.printStackTrace();}}
                                                      }, new Response.ErrorListener() {
                                                  @Override
                                                  public void onErrorResponse(VolleyError error) {
                                                  }}) {
                                                  @Override
                                                  protected Map<String, String> getParams() throws AuthFailureError {
                                                      Map<String, String> params = new HashMap<String, String>();
                                                      params.put("phone_sh", Phones);
                                                      params.put("name", username);
                                                      params.put("password", password);
                                                      params.put("model_car", MODECAR);
                                                      params.put("car_id", caaaar);

                                                       params.put("token",Tokens);

                                                      return params;
                                                  }
                                              };

                                                  Mysingletone.getInstance(RegisterShofiers.this).addToRequestque(stringRequest);
                                              }
                                          }

                                      }
                                  }
        );
    }
    public void onItemSelected(AdapterView<?> parent, View v, int position,
                               long id) {

        int num=spinSh.getSelectedItemPosition();
        carstype=Integer.toString(num);
        Log.d("uiiui", carstype);
        pref2Sh = getSharedPreferences("myPrefsShofiers", MODE_PRIVATE);
        editor2Sh = pref2Sh.edit();
        editor2Sh.putString("someValue", carstype); // add or overwrite someValue
        editor2Sh.commit();
        selection.setText(items[position]);

    }

    public void onNothingSelected(AdapterView<?> parent) {
        selection.setText("");
    }

    protected final void diplayAlert(final String code) {


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("input_error")) {
                    reg_passwordSh.setText("");
                    reg_confirmpassSh.setText("");
                } else if (code.equals("registration_success")) {

                    finish();

                } else if (code.equals("registeration_faild")) {
                    btn_signupSh.setText("");
                    reg_usernamSh.setText("");
                    reg_passwordSh.setText("");
                    reg_confirmpassSh.setText("");

                }

            }
        });

        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}