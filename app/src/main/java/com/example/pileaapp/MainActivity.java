package com.example.pileaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnimateButtons();

        Button plants = findViewById(R.id.PlantsIndex);



    }

    public void AnimateButtons(){
        ImageView categories = findViewById(R.id.imageView_categories);
        ImageView home = findViewById(R.id.imageView_home);
        ImageView social = findViewById(R.id.imageView_social);
        ImageView locations = findViewById(R.id.imageView_locations);
        ImageView plant = findViewById(R.id.imageView_plants);

        categories.setTranslationY(300);
        home.setTranslationY(300);
        social.setTranslationY(300);
        locations.setTranslationY(300);
        plant.setTranslationY(300);

        categories.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        home.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        social.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        locations.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        plant.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();




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

    //Open Login Activity
    public void LoginActivity (View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

}