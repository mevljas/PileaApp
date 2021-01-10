package com.example.pileaapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pileaapp.R;
import com.example.pileaapp.api.models.Location;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddLocationActivity extends AppCompatActivity {

    private EditText locationCreateText;
    private EditText locationCreateDescription;
    Context context;

    CompositeDisposable compositeDisposable;
    private static final String TAG = AddLocationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_add_location);

        locationCreateText = (EditText) findViewById(R.id.addLocationETName);
        locationCreateDescription = (EditText) findViewById(R.id.addLocationETDescription);


    }


    public void addLocation(View view)
    {
        compositeDisposable = new CompositeDisposable();

        Location locationBody = new Location();
        locationBody.setName(locationCreateText.getText().toString());
        locationBody.setDescription(locationCreateDescription.getText().toString());

        Single<Location> locationSingle = MainActivity.apiService.createLocation(MainActivity.userLogin.getToken(), MainActivity.API_KEY, MainActivity.userLogin.getUserID(), locationBody);
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
                        Log.d(TAG, "Location created: " + location.getName());

                        //Toast
                        Context context = getApplicationContext();
                        CharSequence text = "Location added";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        Intent intent = new Intent(context,LocationsActivity.class);
                        startActivity(intent);


                    }

                    @Override
                    public void onError(Throwable e) {
                        // oops, we best show some error message
                        Log.d(TAG, "ERROR: " + e.getMessage());
                    }


                });


    }


}