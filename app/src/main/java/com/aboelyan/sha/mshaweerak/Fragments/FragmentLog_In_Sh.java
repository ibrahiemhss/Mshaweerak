package com.aboelyan.sha.mshaweerak.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aboelyan.sha.mshaweerak.Mysingletone;
import com.aboelyan.sha.mshaweerak.R;
import com.aboelyan.sha.mshaweerak.RecyclerViewClients.ListDesplayForClient;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 12/07/2017.
 */

public class FragmentLog_In_Sh extends Fragment {
    private final static String LOGIN_URL = "http://devsinai.com/mashaweer/logIn_Shofier.php";
    EditText username_Sh;
    EditText password_Sh;
    TextView register_Sh;
    String username;
    String password;
    String sh_id, car_id, name, TOKEN;
    AlertDialog.Builder builder;
    SharedPreferences prefsh, NatficationName;
    Button login_Sh;
    SharedPreferences.Editor editorsh, editorNatficationName;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_log_in_sh, container, false);

        login_Sh= (Button) rootView.findViewById(R.id.login_Sh);
        username_Sh = (EditText) rootView.findViewById(R.id.username_Sh);
        password_Sh = (EditText) rootView.findViewById(R.id.password_Sh);

      //  register_Sh = (TextView) rootView.findViewById(R.id.register_Sh);
        prefsh = getActivity().getSharedPreferences("Loginsh.shofier", Context.MODE_PRIVATE);
        sh_id = prefsh.getString("sh_id", "sh_id");
        name = prefsh.getString("username", "username");
        car_id = prefsh.getString("car_id", "car_id");

        editorsh = prefsh.edit();

        builder = new AlertDialog.Builder(getContext());

        login_Sh.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View v) {
                                            LogInSh();
                                        }
                                    }

        );


        return rootView;
    }

    private void LogInSh() {

        TOKEN = FirebaseInstanceId.getInstance().getToken();
        Log.e(TOKEN, "Tokenis");
        username = username_Sh.getText().toString();
        password = password_Sh.getText().toString();
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


                                editorsh.putString("sh_id", jsonObject.getString("sh_id"));
                                editorsh.putString("car_id", jsonObject.getString("car_id"));
                                editorsh.putString("username", jsonObject.getString("username"));

                                Log.d(response, "response");
                                editorsh.commit();
                                editorsh.apply();
                                String code = jsonObject.getString("code");
                                if (code.equals("login_failed")) {
                                    builder.setTitle("Login Error");
                                    desplayalert(jsonObject.getString("message"));
                                } else {
                                    Intent intent = new Intent(getContext(), ListDesplayForClient.class);
                                    startActivity(intent);
                                    Toast.makeText(getActivity(), sh_id + " " + car_id + " " + name, Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
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
                    params.put("token", TOKEN);

                    return params;
                }
            };
            Mysingletone.getInstance(getActivity()).addToRequestque(stringRequest);
        }

    }

    private void desplayalert(String message) {

        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                username_Sh.setText("");
                password_Sh.setText("");

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void TestUpload() {
        int i = 1 + 1;
    }

}