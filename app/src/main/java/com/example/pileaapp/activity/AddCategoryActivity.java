package com.example.pileaapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.util.Log;
import android.widget.Toast;

import com.example.pileaapp.R;
import com.example.pileaapp.api.models.Category;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddCategoryActivity extends AppCompatActivity {

    private EditText categoryCreateText;

    CompositeDisposable compositeDisposable;
    private static final String TAG = AddCategoryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        categoryCreateText = (EditText) findViewById(R.id.addCategoryETInput);
    }

    public void addCategory(View view)
    {
        compositeDisposable = new CompositeDisposable();

        Category categoryBody = new Category();
        categoryBody.setPlantCategory(categoryCreateText.getText().toString());


        Single<Category> categorySingle = MainActivity.apiService.createCategory(MainActivity.userLogin.getToken(), MainActivity.API_KEY, MainActivity.userLogin.getUserID(), categoryBody);
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
                        Log.d(TAG, "Category created: " + category.getPlantCategory());
                        //Toast
                        Context context = getApplicationContext();
                        CharSequence text = "Category added";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        Intent intent = new Intent(context,CategoriesActivity.class);
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