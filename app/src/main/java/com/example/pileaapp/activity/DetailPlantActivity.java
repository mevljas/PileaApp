package com.example.pileaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pileaapp.R;
import com.example.pileaapp.api.models.Plant;
import com.example.pileaapp.helpers.RecycleViewAdapterPlants;

import java.io.Serializable;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailPlantActivity extends AppCompatActivity {

    CompositeDisposable compositeDisposable;
    private static final String TAG = DetailPlantActivity.class.getSimpleName();


    public Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_plant);


        Intent i = getIntent();
        String plantID = i.getStringExtra("plantID");



    }

}