package com.example.pileaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    //Open Register Activity
    public void RegisterActivity (View view) {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}