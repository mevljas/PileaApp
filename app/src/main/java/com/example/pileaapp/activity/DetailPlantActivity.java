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
import com.example.pileaapp.api.models.Category;
import com.example.pileaapp.api.models.Location;
import com.example.pileaapp.api.models.Plant;
import com.example.pileaapp.helpers.RecycleViewAdapterPlants;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    TextView plantCategoryText;
    TextView plantLocationText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_plant);


        Intent i = getIntent();
        String plantName = i.getStringExtra("plantName");
        String plantDescription = i.getStringExtra("plantDescription");
        String plantNote = i.getStringExtra("plantName");
        String plantDaysBetween = i.getStringExtra("plantDaysBetween");
        String plantLastWateringDate = i.getStringExtra("plantLastWateringDate");
        String nextWateringDate = i.getStringExtra("nextWateringDate");

        int plantCategoryId = i.getIntExtra("plantCategory",0);
        int plantLocationId = i.getIntExtra("plantLocation",0);





        TextView plantNameText = (TextView) this.findViewById(R.id.detailPlantTVName);
        TextView plantDescriptionText = (TextView) this.findViewById(R.id.detailPlantTVDescription);
        TextView plantNoteText = (TextView) this.findViewById(R.id.detailPlantTVNote);
        TextView plantDaysBetweenText = (TextView) this.findViewById(R.id.detailPlantTVBetween);
        TextView plantLastWateringDateText = (TextView) this.findViewById(R.id.detailPlantTVLastWateing);
        TextView plantnextWateringDateText2 = (TextView) this.findViewById(R.id.detailPlantTVNextwatering2);
        TextView plantnextWateringDateText = (TextView) this.findViewById(R.id.detailPlantTVNextwatering);

        plantCategoryText = (TextView) this.findViewById(R.id.detailPlantTVCategory);
        plantLocationText = (TextView) this.findViewById(R.id.detailPlantTVLocation);

        getCategory(plantCategoryId);
        getLocation(plantLocationId);

        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");




        try {
            Date lastwatering = dateOnly.parse(plantLastWateringDate);
            String out = dateOnly.format(lastwatering);
            plantLastWateringDateText.setText("Last watering date : "+out);



            Date nextwatering = dateOnly.parse(nextWateringDate);
            out = dateOnly.format(nextwatering);
            plantnextWateringDateText.setText("Next watering date : "+out);
            plantnextWateringDateText2.setText("Next watering date : "+out);


        } catch (ParseException e) {
            e.printStackTrace();
        }








        //nextWateringDate.setSelected("Next watering : "+plantnextWateringDateText);
        plantNameText.setText(plantName);
        plantDescriptionText.setText("Description : "+plantDescription);
        plantNoteText.setText("Note : "+plantNote);
        plantDaysBetweenText.setText("Days between watering : "+plantDaysBetween);
        //plantCategoryText.setText("Category : "+plantCategoryId);
        //plantLocationText.setText("Location : "+plantLocationId);

    }


    public void getDetails(){

    }

    public void getCategory(int categoryId) {
        compositeDisposable = new CompositeDisposable();

        Single<Category> categorySingle = MainActivity.apiService.getCategory(categoryId, MainActivity.userLogin.getToken(), MainActivity.API_KEY);
        categorySingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Category>() {
                    @Override
                    public void onSubscribe(Disposable d) {
//                        first we create a CompositeDisposable object which acts as a container for disposables
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Category category) {
                        // data is ready and we can update the UI
                        Log.d(TAG, "SUCCESS");
                        Log.d(TAG, "Category get got: " + category.getPlantCategory());

                        plantCategoryText.setText("Category : "+category.getPlantCategory());
                    }

                    @Override
                    public void onError(Throwable e) {
                        // oops, we best show some error message
                        Log.d(TAG, "ERROR: " + e.getMessage());
                    }


                });


    }

    public void getLocation(int locationId) {
        compositeDisposable = new CompositeDisposable();

        Single<Location> locationSingle = MainActivity.apiService.getLocation(locationId, MainActivity.userLogin.getToken(), MainActivity.API_KEY);
        locationSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Location>() {
                    @Override
                    public void onSubscribe(Disposable d) {
//                        first we create a CompositeDisposable object which acts as a container for disposables
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Location location) {
                        // data is ready and we can update the UI
                        Log.d(TAG, "SUCCESS");
                        Log.d(TAG, "Location get got: " + location.getName());

                        plantLocationText.setText("Location : "+location.getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        // oops, we best show some error message
                        Log.d(TAG, "ERROR: " + e.getMessage());
                    }


                });


    }

}