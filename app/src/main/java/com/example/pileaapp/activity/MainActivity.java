package com.example.pileaapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pileaapp.R;
import com.example.pileaapp.api.ApiService;
import com.example.pileaapp.api.models.Login;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

     public static final String API_KEY = "PileaSecretKey1x";
     public static Login userLogin;
     public static ApiService apiService;
    Toast loginNecesaryToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeApi();




        Button plants = findViewById(R.id.mainBPlants);

        loginNecesaryToast= Toast.makeText(getApplicationContext(),
                "User must be logged in.",
                Toast.LENGTH_SHORT);
    }

    public void AnimateButtons(){
        ImageView categories = findViewById(R.id.mainIVCategories);
        ImageView locations = findViewById(R.id.mainIVLocations);
        ImageView plant = findViewById(R.id.mainIVPlants);

        categories.setTranslationY(300);
        locations.setTranslationY(300);
        plant.setTranslationY(300);

        categories.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        locations.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        plant.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();




    }



    private void initializeApi(){

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pilea-web-dev.azurewebsites.net/api/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        // create an instance of the ApiService
        apiService = retrofit.create(ApiService.class);


    }



    //Open Plants Activity
    public void PlantsActivity (View view) {
        if (userLogin != null){
            Intent intent = new Intent(this,PlantsActivity.class);
            startActivity(intent);
        }
        else {
            loginNecesaryToast.show();
        }

    }

    //Open Locations Activity
    public void LocationsActivity (View view) {
        if (userLogin != null){
            Intent intent = new Intent(this,LocationsActivity.class);
            startActivity(intent);
        }
        else {
            loginNecesaryToast.show();
        }

    }

    //Open Friends Activity
    public void FriendsActivity (View view) {
        if (userLogin != null){
            Intent intent = new Intent(this,FriendsActivity.class);
            startActivity(intent);
        }
        else {
            loginNecesaryToast.show();
        }

    }

    //Open Friends Activity
    public void CategoriesActivity (View view) {
        if (userLogin != null){
            Intent intent = new Intent(this,CategoriesActivity.class);
            startActivity(intent);
        }
        else {
            loginNecesaryToast.show();
        }

    }

    //Open Login Activity
    public void LoginActivity (View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    public static class AddLocationActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_location);
        }


    }
}