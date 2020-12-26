package com.example.pileaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }





    //Open Plants Activity
    public void PlantsActivity (View view) {
        Intent intent = new Intent(this,PlantsActivity.class);
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

    //Open Friends Activity
    public void CategoriesActivity (View view) {
        Intent intent = new Intent(this,CategoriesActivity.class);
        startActivity(intent);
    }

}