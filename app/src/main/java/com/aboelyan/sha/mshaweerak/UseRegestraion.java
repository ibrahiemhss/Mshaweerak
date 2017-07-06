package com.aboelyan.sha.mshaweerak;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aboelyan.sha.mshaweerak.FirbaseNotfication.SharedprefManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UseRegestraion extends AppCompatActivity {

   private AppCompatButton reg_bn;
   private EditText Name, Phone, UserName, Password, ConfirmPassword;
    private String name, Phones, username, password, confirmpassword,TOKEN;
    String reg_url = "http://devsinai.com/mashaweer/register_user.php";
    private  android.app.AlertDialog.Builder builder;
    SharedPreferences prefToken;

    SharedPreferences.Editor editorToken;
    Context context;

    String Token;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vregestraion);

        reg_bn = (AppCompatButton) findViewById(R.id.btn_signup);
        Phone = (EditText) findViewById(R.id.phone);
        UserName = (EditText) findViewById(R.id.reg_usernam);
        Password = (EditText) findViewById(R.id.reg_password);
        ConfirmPassword = (EditText) findViewById(R.id.reg_confirmpass);

        builder = new android.app.AlertDialog.Builder(UseRegestraion.this);
       // FirebaseMessaging.getInstance().subscribeToTopic("tamam");
       // FirebaseInstanceId.getInstance().getToken();

        reg_bn.setOnClickListener(new View.OnClickListener() {
                                      @Override

                                      public void onClick(View v) {



                                        FirebaseMessaging.getInstance().subscribeToTopic("test");

                                         Toast.makeText(UseRegestraion.this,TOKEN,Toast.LENGTH_LONG).show();
                                          Phones = Phone.getText().toString().trim();
                                          username = UserName.getText().toString();
                                          password = Password.getText().toString();
                                          TOKEN=SharedprefManager.getIstance(getApplicationContext()).getToken();
                                          confirmpassword = ConfirmPassword.getText().toString();
                                          if ( Phones.equals("") || username.equals("") || password.equals("") || confirmpassword.equals("")) {
                                              builder.setTitle("something went wrong .....");
                                              builder.setMessage("please fill all fields");
                                              diplayAlert("input_error");
                                          } else {
                                              if (!(password.equals(confirmpassword))) {
                                                  builder.setTitle("something went wrong .....");
                                                  builder.setMessage("not correct");
                                                  diplayAlert("input_error");
                                              } else {
                                                  if(SharedprefManager.getIstance(UseRegestraion.this).getToken()!=null) {

                                                  StringRequest stringRequest = new StringRequest(Request.Method.POST, reg_url,
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
                                                          params.put("phone", Phones);
                                                          params.put("username", username);
                                                          params.put("password", password);
                                                          params.put("token",TOKEN);

                                                          return params;
                                                      }
                                                  };

                                                  Mysingletone.getInstance(UseRegestraion.this).addToRequestque(stringRequest);
                                              }
                                          }

                                      }
                                  }}
        );
    }

    protected final void diplayAlert(final String code) {


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("input_error")) {
                    Password.setText("");
                    ConfirmPassword.setText("");
                } else if (code.equals("registration_success")) {

                    finish();

                } else if (code.equals("registeration_faild")) {
                    Phone.setText("");
                    UserName.setText("");
                    Password.setText("");
                    ConfirmPassword.setText("");

                }

            }
        });

        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}