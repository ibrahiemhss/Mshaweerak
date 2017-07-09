package com.aboelyan.sha.mshaweerak.RecyclerViewClients;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.aboelyan.sha.mshaweerak.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
    private ProgressDialog pd;
    SharedPreferences pref2,pref,prefsh;
    SharedPreferences.Editor editorsh,editor,editor2;
    private JsonArrayRequest jsonArrayRequest;

    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter  recyclerViewadapter;
    private RequestQueue requestQueue;
    String urlListDesplayShForClient = "http://devsinai.com/mashaweer/ListDesplayForClient.php";
    String car_id,cr_id;
    ProgressBar progressBar;

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



        pref2 = getSharedPreferences("myPrefs", MODE_PRIVATE);
        cr_id=pref2.getString("car_id","car_id");
        editor2=pref2.edit();

       // Toast.makeText(this, car_id+"A"+"           "+cr_id+"B", Toast.LENGTH_SHORT).show();

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);



        JSON_DATA_WEB_CALL();

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
}