package com.example.pileaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocationsActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private String url = "https://pilea-web-dev.azurewebsites.net/api/v1/Location"; //Azure

    String s1[];
    public RecyclerView recyclerView;
    public RecyclerViewAdapter myRecycleViewAdapter;
    public Context context = this;

    Dialog myDeleteDialog;
    Dialog myEditDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        s1 = getResources().getStringArray(R.array.categories);
        recyclerView = findViewById(R.id.recyclerViewLocations);
        myRecycleViewAdapter = new RecyclerViewAdapter(this,s1);
        recyclerView.setAdapter(myRecycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myDeleteDialog = new Dialog(this);
        myEditDialog = new Dialog(this);

        showLocations(findViewById(android.R.id.content).getRootView());

    }

    public void showLocations(View view){
        if (view != null){
            JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("ApiKey", "PileaSecretKey1x");
                    return params;
                }
            };
            requestQueue.add(request);
        }
    }

    private Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            ArrayList<String> data = new ArrayList<>();


            for (int i = 0; i < response.length(); i++){
                try {
                    JSONObject object = response.getJSONObject(i);
                    String name = object.getString("name");

                    data.add(name);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }

            String[] s = data.toArray(new String[data.size()]);;


            myRecycleViewAdapter = new RecyclerViewAdapter(context,s);
            recyclerView.setAdapter(myRecycleViewAdapter);


        }
    };


    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };

    // Acitivity handeling functions
    public void addLocationActivity (View view) {
        Intent intent = new Intent(this,AddLocationActivity.class);
        startActivity(intent);
    }

    public void ShowDeletePopup(View v){
        Button btnClose;
        Button btnDelete;
        TextView text;
        myDeleteDialog.setContentView(R.layout.delete_popup);

        btnClose = (Button) myDeleteDialog.findViewById(R.id.btn_delete_popup_no);
        btnDelete = (Button) myDeleteDialog.findViewById(R.id.btn_delete_popup_yes);
        text = (TextView) myDeleteDialog.findViewById(R.id.textView_delete_popup_text);

        text.setText("Do you really want to delete this location?");



        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDeleteDialog.dismiss();
            }
        });

        myDeleteDialog.show();
    }

    public void ShowEditPopup(View v){
        Button btnClose;
        Button btnDelete;

        myEditDialog.setContentView(R.layout.edit_popup);

        btnClose = (Button) myEditDialog.findViewById(R.id.btn_edit_popup_no);
        btnDelete = (Button) myEditDialog.findViewById(R.id.btn_edit_popup_yes);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myEditDialog.dismiss();
            }
        });
        myEditDialog.show();
    }

}