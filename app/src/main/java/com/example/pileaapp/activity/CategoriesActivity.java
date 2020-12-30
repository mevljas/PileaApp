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

import com.example.pileaapp.R;
import com.example.pileaapp.api.models.Category;
import com.example.pileaapp.helpers.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class CategoriesActivity extends AppCompatActivity {


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
        setContentView(R.layout.activity_categories);


        s1 = new ArrayList();
        recyclerView = findViewById(R.id.recyclerViewCategories);
        myRecycleViewAdapter = new RecyclerViewAdapter(this, s1);
        recyclerView.setAdapter(myRecycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        showCategories(findViewById(android.R.id.content).getRootView());


        myDeleteDialog = new Dialog(this);
        myEditDialog = new Dialog(this);


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        showCategories();
    }

    public void showCategories() {
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
                        List<Category> categories = list;
                        Log.d(TAG, "Number of categories received: " + categories.size());
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
    public void addCategoryActivity(View view) {
        Intent intent = new Intent(this, AddCategoryActivity.class);

        startActivity(intent);
    }

    public void ShowDeletePopup(View v) {
        Button btnClose;
        Button btnDelete;
        TextView text;
        TextView category;
        myDeleteDialog.setContentView(R.layout.delete_popup);

        btnClose = (Button) myDeleteDialog.findViewById(R.id.btn_delete_popup_no);
        btnDelete = (Button) myDeleteDialog.findViewById(R.id.btn_delete_popup_yes);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDeleteDialog.dismiss();
            }
        });

        myDeleteDialog.show();
    }

    public void ShowEditPopup(View v) {
        Button btnClose;
        Button btnDelete;
        TextView text;
        TextView category;
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