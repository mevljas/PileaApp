package com.example.pileaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private TextView categories;
    private String url = "https://pilea-web-dev.azurewebsites.net/api/v1/Category";

    String s1[];
    public static RecyclerView recyclerView;
    public static RecyclerViewAdapter myRecycleViewAdapter;
    public Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        categories = (TextView) findViewById(R.id.textView_categories);

        s1 = getResources().getStringArray(R.array.categories);
        recyclerView = findViewById(R.id.recyclerViewCategories);
        myRecycleViewAdapter = new RecyclerViewAdapter(this,s1);
        recyclerView.setAdapter(myRecycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        showCategories(findViewById(android.R.id.content).getRootView());
    }

    public void showCategories(View view){
        if (view != null){
            JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener);
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
                    String name = object.getString("plantCategory");

                    data.add(name);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }

            categories.setText("");

            String[] s = data.toArray(new String[data.size()]);;


            myRecycleViewAdapter = new RecyclerViewAdapter(context,s);
            recyclerView.setAdapter(myRecycleViewAdapter);

            /*
            for (String row: data) {
                String currentText = categories.getText().toString();
                categories.setText(currentText + "\n\n" + row);
            }*/

        }
    };


    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };

    // Acitivity handeling functions
    public void addCategoryActivity (View view) {
        Intent intent = new Intent(this,AddCategoryActivity.class);

        startActivity(intent);
    }

}