package com.aboelyan.sha.mshaweerak.RecyclerVeiwShofiers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.aboelyan.sha.mshaweerak.R;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListOfBookings extends AppCompatActivity {

    RecyclerView RS_BookingSh;
    private ShAdapter adapter;
    private RequestQueue mRequestQueue;
    private List<BookModels> bookModelses1 ;
    private ProgressDialog pd;
    SharedPreferences prefComment,pref,prefsh;
    SharedPreferences.Editor editorsh,editor;
    private JsonArrayRequest jsonArrayRequest;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter  recyclerViewadapter;
    private RequestQueue requestQueue;
    String urlListBokings = "http://devsinai.com/mashaweer/show.php";
    String car_id;
    ProgressBar progressBar;

    final String TAAG=this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_bookings);
        bookModelses1=new ArrayList<>();
        RS_BookingSh = (RecyclerView) findViewById(R.id.show_BookingSh);
        RS_BookingSh.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        RS_BookingSh.setLayoutManager(recyclerViewlayoutManager);
        recyclerViewadapter = new ShAdapter(bookModelses1,this);
        RS_BookingSh.setAdapter(recyclerViewadapter);


        prefsh = getSharedPreferences("Loginsh.shofier", Context.MODE_PRIVATE);
        car_id = prefsh.getString("car_id","car_id");

        editorsh = prefsh.edit();



        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        progressBar.setVisibility(View.VISIBLE);

        JSON_DATA_WEB_CALL();

    }
    public void JSON_DATA_WEB_CALL(){

        jsonArrayRequest = new JsonArrayRequest(urlListBokings,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jsonObject= null;
                        try {
                            jsonObject = response.getJSONObject(0);

                          //  editorsh.putString(car_id,jsonObject.getString("post_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                        progressBar.setVisibility(View.GONE);

                        Log.v(String.valueOf(response),"reeeeeeeee");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {

            BookModels bookModels2 = new BookModels();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                bookModels2.setFace(json.getString("face"));

                bookModels2.setTraveTime(json.getString("traveTime"));

                bookModels2.setUser_id(json.getString("user_id"));

                bookModels2.setCar_id(json.getString("car_id"));
                Log.v(String.valueOf(json),"oioiiooo");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            bookModelses1.add(bookModels2);
        }

        recyclerViewadapter = new ShAdapter(bookModelses1,this);

        RS_BookingSh.setAdapter(recyclerViewadapter);




    }
}