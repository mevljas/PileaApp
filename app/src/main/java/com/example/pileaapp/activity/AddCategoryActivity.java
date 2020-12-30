package com.example.pileaapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.util.Log;

import com.example.pileaapp.R;
import com.example.pileaapp.api.models.Category;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class AddCategoryActivity extends AppCompatActivity {

    private TextView status;
    private EditText categoryCreateText;

    CompositeDisposable compositeDisposable;
    private static final String TAG = AddCategoryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        categoryCreateText = (EditText) findViewById(R.id.teCategory);
        status = (TextView) findViewById(R.id.status);
    }

    public void addCategory(View view)
    {
        compositeDisposable = new CompositeDisposable();
        this.status.setText("Posting");

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
                        status.setText("Category created: " + category.getPlantCategory());


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