package com.example.pileaapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pileaapp.R;
import com.example.pileaapp.api.models.Category;
import com.example.pileaapp.api.models.Location;
import com.example.pileaapp.api.models.Plant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    TextView plantNameText;
    TextView plantDescriptionText;
    TextView plantNoteText;
    TextView plantDaysBetweenText;
    TextView plantLastWateringDateText;
    TextView plantnextWateringDateText2;
    TextView plantnextWateringDateText;
    TextView plantCategoryText;
    TextView plantLocationText;
    ImageView plantImage;

    Plant currentPlant;

    //    Decode Base64 image
    private Bitmap decodeImage(String image){
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_plant);


        Intent i = getIntent();
        int plantId = i.getIntExtra("plantId", 0);
        int plantCategoryId = i.getIntExtra("plantCategory",0);
        int plantLocationId = i.getIntExtra("plantLocation",0);








         plantNameText = (TextView) this.findViewById(R.id.detailPlantTVName);
         plantDescriptionText = (TextView) this.findViewById(R.id.detailPlantTVDescription);
         plantNoteText = (TextView) this.findViewById(R.id.detailPlantTVNote);
         plantDaysBetweenText = (TextView) this.findViewById(R.id.detailPlantTVBetween);
         plantLastWateringDateText = (TextView) this.findViewById(R.id.detailPlantTVLastWateing);
         plantnextWateringDateText2 = (TextView) this.findViewById(R.id.detailPlantTVNextwatering2);
         plantnextWateringDateText = (TextView) this.findViewById(R.id.detailPlantTVNextwatering);

        plantCategoryText = (TextView) this.findViewById(R.id.detailPlantTVCategory);
        plantLocationText = (TextView) this.findViewById(R.id.detailPlantTVLocation);
        plantImage = (ImageView) this.findViewById(R.id.detailPlantTVImage);

        getPlant(plantId);
        getCategory(plantCategoryId);
        getLocation(plantLocationId);


    }


    void setDetails(){


        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");




        try {
            Date lastwatering = dateOnly.parse(currentPlant.getLastWateredDate());
            String out = dateOnly.format(lastwatering);
            plantLastWateringDateText.setText("Last watering date : "+out);



            Date nextwatering = dateOnly.parse(currentPlant.getNextWateredDate());
            out = dateOnly.format(nextwatering);
            plantnextWateringDateText.setText("Next watering date : "+out);
            plantnextWateringDateText2.setText("Next watering date : "+out);


        } catch (ParseException e) {
            e.printStackTrace();
        }

//        plantnextWateringDateText.setText(currentPlant.getNextWateredDate());
        plantNameText.setText(currentPlant.getName());
        plantDescriptionText.setText("Description : "+currentPlant.getDescription());
        plantNoteText.setText("Note : "+currentPlant.getNote());
        plantDaysBetweenText.setText("Days between watering : "+currentPlant.getDaysBetweenWatering());
        if (currentPlant.getImage() != null){
            plantImage.setImageBitmap(decodeImage(currentPlant.getImage()));
        }

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

    public void getPlant(int plantId) {
        compositeDisposable = new CompositeDisposable();

        Single<Plant> locationSingle = MainActivity.apiService.getPlant(plantId, MainActivity.userLogin.getToken(), MainActivity.API_KEY);
        locationSingle.subscribeOn(Schedulers.io())
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
                        Log.d(TAG, "Plant get got: " + plant.getName());

                        currentPlant = plant;
                        setDetails();
                    }

                    @Override
                    public void onError(Throwable e) {
                        // oops, we best show some error message
                        Log.d(TAG, "ERROR: " + e.getMessage());
                    }


                });


    }

}