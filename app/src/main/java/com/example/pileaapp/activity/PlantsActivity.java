package com.example.pileaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.pileaapp.R;
import com.example.pileaapp.addPlantActivity;
import com.example.pileaapp.api.models.Category;
import com.example.pileaapp.api.models.Plant;
import com.example.pileaapp.helpers.RecycleViewAdapterPlants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PlantsActivity extends AppCompatActivity {
    List s1;
    public RecyclerView recyclerView;
    public RecycleViewAdapterPlants myRecycleViewAdapter;
    public Context context = this;

    CompositeDisposable compositeDisposable;
    private static final String TAG = PlantsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants);

        s1 = new ArrayList();
        recyclerView = findViewById(R.id.plantsRecycleView);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(PlantsActivity.this, 2);
        recyclerView.setLayoutManager(mGridLayoutManager);
        myRecycleViewAdapter = new RecycleViewAdapterPlants(this, s1);
        recyclerView.setAdapter(myRecycleViewAdapter);

        showPlants();
    }

    public void showPlants() {
        compositeDisposable = new CompositeDisposable();
//       Make a request by calling the corresponding method
        Single<List<Plant>> list = MainActivity.apiService.getUserPlants(MainActivity.userLogin.getToken(), MainActivity.API_KEY, MainActivity.userLogin.getUserID());
        list.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List>() {
                    @Override
                    public void onSubscribe(Disposable d) {
//                        first we create a CompositeDisposable object which acts as a container for disposables
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List list) {
                        // data is ready and we can update the UI
                        Log.d(TAG, "SUCCESS");
                        List<Plant> plants = list;
                        Log.d(TAG, "Number of plants received: " + plants.size());
//                        for (Category category: categories) {
//                            System.out.println(category.getPlantCategory());
//                        }


                        myRecycleViewAdapter = new RecycleViewAdapterPlants(context, list);
                        recyclerView.setAdapter(myRecycleViewAdapter);

                        /*
                        for (String row: data) {
                            String currentText = categories.getText().toString();
                            categories.setText(currentText + "\n\n" + row);
                        }*/

                    }

                    @Override
                    public void onError(Throwable e) {
                        // oops, we best show some error message
                        Log.d(TAG, "ERROR: " + e.getMessage());
                    }


                });
    }

    //Open AddPlant Activity
    public void AddPlantAcitivty (View view) {
        Intent intent = new Intent(this, addPlantActivity.class);
        startActivity(intent);

    }
}