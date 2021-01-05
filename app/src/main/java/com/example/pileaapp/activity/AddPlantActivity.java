package com.example.pileaapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pileaapp.R;
import com.example.pileaapp.api.models.Category;
import com.example.pileaapp.api.models.Location;
import com.example.pileaapp.api.models.Plant;
import com.example.pileaapp.helpers.categoryRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddPlantActivity extends AppCompatActivity {

    private TextView status;
    private EditText nameInput;
    private EditText noteInput;
    private Spinner daysBetweenWateringInput;
    private EditText lastWateringDateInput;
    private EditText descriptionInput;
    private Spinner categoryInput;
    private Spinner locationInput;
    private ArrayAdapter<Category> categoriesAdapter;
    private  AddPlantActivity instance;

    CompositeDisposable compositeDisposable;
    private static final String TAG = AddPlantActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "HALO");
        setContentView(R.layout.activity_add_plant);

        nameInput = (EditText) findViewById(R.id.addPlantETName);
        noteInput = (EditText) findViewById(R.id.addPlantETNote);
        daysBetweenWateringInput = (Spinner) findViewById(R.id.addPlantSDaysBetweenWatering);
        lastWateringDateInput = (EditText) findViewById(R.id.addPlantETLastWateringDate);
        descriptionInput = (EditText) findViewById(R.id.addPlantETDescription);
        categoryInput = (Spinner) findViewById(R.id.addPlantSCategory);
        locationInput = (Spinner) findViewById(R.id.addPlantSLocation);
        status = (TextView) findViewById(R.id.addCategoryTVStatus);

        instance = this;
        Log.d(TAG, "DELA");

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getCategories();
    }

    public void getCategories() {
        Log.d(TAG, "TESTSTTST");
        compositeDisposable = new CompositeDisposable();
//       Make a request by calling the corresponding method
        Single<List<Category>> list = MainActivity.apiService.getUserCategories(MainActivity.userLogin.getToken(), MainActivity.API_KEY, MainActivity.userLogin.getUserID());
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
                        List<Category> categories =  list;
                        Log.d(TAG, "Number of categories received: " + categories.size());

                        categoriesAdapter = new ArrayAdapter<Category>(instance, android.R.layout.simple_spinner_item, categories);
                        categoryInput.setAdapter(categoriesAdapter);

                    }

                    @Override
                    public void onError(Throwable e) {
                        // oops, we best show some error message
                        Log.d(TAG, "ERROR: " + e.getMessage());
                    }


                });
    }

    public void addCategory(View view)
    {
        compositeDisposable = new CompositeDisposable();
        this.status.setText("Posting");

        Plant plantBody = new Plant();
        Category category = (Category) categoryInput.getSelectedItem();
        plantBody.setCategory(category);
        plantBody.setDaysBetweenWatering((Integer) daysBetweenWateringInput.getSelectedItem());
        plantBody.setDescription(descriptionInput.getText().toString());
//        plantBody.setImage();
        plantBody.setLastWateredDate(lastWateringDateInput.getText().toString());
        Location location = (Location) locationInput.getSelectedItem();
        plantBody.setLocation(location);
//        plantBody.setNextWateredDate(lastWateringDate.getText().toString() + (Integer) daysBetweenWatering.getSelectedItem());


        Single<Plant> plantSingle = MainActivity.apiService.createPlant(MainActivity.userLogin.getToken(), MainActivity.API_KEY, MainActivity.userLogin.getUserID(), category.getCategoryID(), location.getLocationID(), plantBody);
        plantSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Plant>() {
                    @Override
                    public void onSubscribe(Disposable d) {
//                        first we create a CompositeDisposable object which acts as a container for disposables
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Plant plant) {
                        // data is ready and we can update the UI
                        Log.d(TAG, "SUCCESS");
                        Log.d(TAG, " created: " + plant.getName());
                        status.setText("Plant created: " + plant.getName());

                        //Toast
                        Context context = getApplicationContext();
                        CharSequence text = "Plant added";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();


                    }

                    @Override
                    public void onError(Throwable e) {
                        // oops, we best show some error message
                        Log.d(TAG, "ERROR: " + e.getMessage());
                        status.setText("ERROR: " + e.getMessage());
                    }


                });


    }
}