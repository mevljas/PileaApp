package com.example.pileaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private TextView categories;
    private String url = "https://pilea-web-dev.azurewebsites.net/api/v1/Category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        categories = (TextView) findViewById(R.id.categories);
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
            ArrayList<String>  data = new ArrayList<>();

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


            for (String row: data) {
                String currentText = categories.getText().toString();
                categories.setText(currentText + "\n\n" + row);
            }
        }
    };


    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };

    public static final String EXTRA_MESSAGE = "com.example.pileaapp.MESSAGE";

    public void addCategoryActivity (View view) {
        Intent intent = new Intent(this,AddCategoryActivity.class);
        String message = "Add category to the list.";
//        Example of sending data.
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    //Open Plants Activity
    public void PlantsActivity (View view) {
        Intent intent = new Intent(this,PlantsActivity.class);
        startActivity(intent);
    }

    //Open Catagories Activity
    public void CatagoriesActivity (View view) {
        Intent intent = new Intent(this,CatagoriesActivity.class);
        startActivity(intent);
    }

    //Open Locations Activity
    public void LocationsActivity (View view) {
        Intent intent = new Intent(this,LocationsActivity.class);
        startActivity(intent);
    }

    //Open Friends Activity
    public void FriendsActivity (View view) {
        Intent intent = new Intent(this,FriendsActivity.class);
        startActivity(intent);
    }



}