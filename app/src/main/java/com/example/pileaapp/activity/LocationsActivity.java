package com.example.pileaapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.pileaapp.R;
import com.example.pileaapp.api.models.Category;
import com.example.pileaapp.api.models.Location;
import com.example.pileaapp.helpers.RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LocationsActivity extends AppCompatActivity {

    List s1;
    public RecyclerView recyclerView;
    public RecyclerViewAdapter myRecycleViewAdapter;
    public Context context = this;

    Dialog myDeleteDialog;
    Dialog myEditDialog;
    CompositeDisposable compositeDisposable;
    private static final String TAG = CategoriesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        recyclerView = findViewById(R.id.recyclerViewLocations);
//        myRecycleViewAdapter = new RecyclerViewAdapter(this,s1);
        recyclerView.setAdapter(myRecycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myDeleteDialog = new Dialog(this);
        myEditDialog = new Dialog(this);



    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        showLocations();
    }

    public void showLocations() {


        compositeDisposable = new CompositeDisposable();
//       Make a request by calling the corresponding method
        Single<List<Location>> list = MainActivity.apiService.getUserLocations(MainActivity.userLogin.getToken(), MainActivity.API_KEY, MainActivity.userLogin.getUserID());
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
                        List<Location> locations = list;
                        Log.d(TAG, "Number of locations received: " + locations.size());
//                        for (Category category: categories) {
//                            System.out.println(category.getPlantCategory());
//                        }


                        myRecycleViewAdapter = new RecyclerViewAdapter(context, list);
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



    // Acitivity handeling functions
    public void addLocationActivity (View view) {
        Intent intent = new Intent(this,AddLocationActivity.class);
        startActivity(intent);
    }

    public void ShowDeletePopup(View v){
        Button btnClose;
        Button btnDelete;
        TextView text;
        myDeleteDialog.setContentView(R.layout.delete_popup);

        btnClose = (Button) myDeleteDialog.findViewById(R.id.btn_delete_popup_no);
        btnDelete = (Button) myDeleteDialog.findViewById(R.id.btn_delete_popup_yes);
        text = (TextView) myDeleteDialog.findViewById(R.id.textView_delete_popup_text);

        text.setText("Do you really want to delete this location?");



        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDeleteDialog.dismiss();
            }
        });

        myDeleteDialog.show();
    }

    public void ShowEditPopup(View v){
        Button btnClose;
        Button btnDelete;

        myEditDialog.setContentView(R.layout.edit_popup);

        btnClose = (Button) myEditDialog.findViewById(R.id.btn_edit_popup_no);
        btnDelete = (Button) myEditDialog.findViewById(R.id.btn_edit_popup_yes);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myEditDialog.dismiss();
            }
        });
        myEditDialog.show();
    }

    @Override
    protected void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }

}