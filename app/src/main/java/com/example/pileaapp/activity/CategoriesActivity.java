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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pileaapp.R;
import com.example.pileaapp.api.models.Category;
import com.example.pileaapp.helpers.categoryRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CategoriesActivity extends AppCompatActivity {


    List s1;
    public RecyclerView recyclerView;
    public categoryRecyclerViewAdapter recycleViewAdapter;
    public Context context = this;
    static Dialog deleteDialog;
    static Dialog editDialog;
    CompositeDisposable compositeDisposable;


    private static final String TAG = CategoriesActivity.class.getSimpleName();

    public static CategoriesActivity instance;    // Some objects require this.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_categories);


        s1 = new ArrayList();
        recyclerView = findViewById(R.id.categoriesRVCategories);
        recycleViewAdapter = new categoryRecyclerViewAdapter(this, s1);
        recyclerView.setAdapter(recycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        showCategories(findViewById(android.R.id.content).getRootView());


        deleteDialog = new Dialog(this);
        editDialog = new Dialog(this);


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        showCategories();
    }
    public void SyncLocation (View view) {
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


                        recycleViewAdapter = new categoryRecyclerViewAdapter(context, list);
                        recyclerView.setAdapter(recycleViewAdapter);

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

    public void editCategory(Category selectedCategory)
    {
        compositeDisposable = new CompositeDisposable();



        Single<Category> categorySingle = MainActivity.apiService.editCategory(selectedCategory.getCategoryID(), MainActivity.userLogin.getToken(), MainActivity.API_KEY, selectedCategory);
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
                        Log.d(TAG, "Category edited: " + category.getPlantCategory());

                        //Toast
                        Context context = getApplicationContext();
                        CharSequence text = "Category edited.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        showCategories();


                    }

                    @Override
                    public void onError(Throwable e) {
                        // oops, we best show some error message
                        Log.d(TAG, "ERROR: " + e.getMessage());
                    }


                });


    }


    public void deleteCategory(Category selectedCategory)
    {
        compositeDisposable = new CompositeDisposable();



        Single<Category> categorySingle = MainActivity.apiService.deleteCategory(selectedCategory.getCategoryID(), MainActivity.userLogin.getToken(), MainActivity.API_KEY);
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
                        Log.d(TAG, "Category deleted: " + category.getPlantCategory());

                        //Toast
                        Context context = getApplicationContext();
                        CharSequence text = "Category deleted.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        showCategories();


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

    public void ShowDeletePopup(View v, Category category) {
        Button btnClose;
        Button btnDelete;
        TextView text;
        deleteDialog.setContentView(R.layout.delete_popup);

        btnClose = (Button) deleteDialog.findViewById(R.id.deletePopUpBNo);
        btnDelete = (Button) deleteDialog.findViewById(R.id.deletePopUpBYes);
        text = (TextView) deleteDialog.findViewById(R.id.deletePopUpTVText);

        text.setText("Do you really want to delete this category?");

        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteCategory(category);
                deleteDialog.dismiss();

            }
        });


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

        deleteDialog.show();
    }

    public void ShowEditPopup(View v, Category category) {
        Button btnClose;
        Button btnEdit;
        EditText categoryField;
        editDialog.setContentView(R.layout.categories_edit_popup);

        btnEdit = (Button) editDialog.findViewById(R.id.categoriesEditPopUpBYes);
        btnClose = (Button) editDialog.findViewById(R.id.categoriesEditPopUpBNo);
        categoryField = (EditText) editDialog.findViewById(R.id.categoriesEditPopUpETInput);
        categoryField.setText(category.getPlantCategory());
        Category selectedCategory = category;
//        selectedCategory = category;


        btnEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText categoryField = (EditText) editDialog.findViewById(R.id.categoriesEditPopUpETInput);
                selectedCategory.setPlantCategory(categoryField.getText().toString());
                editCategory(selectedCategory);
                editDialog.dismiss();
            }
        });


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });
        editDialog.show();
    }

    @Override
    protected void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }

}