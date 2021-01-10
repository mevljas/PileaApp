package com.example.pileaapp.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.icu.util.GregorianCalendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pileaapp.R;
import com.example.pileaapp.api.models.Category;
import com.example.pileaapp.api.models.Location;
import com.example.pileaapp.api.models.Plant;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddPlantActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText noteInput;
    private Spinner daysBetweenWateringInput;
    private EditText lastWateringDateInput;
    private EditText descriptionInput;
    private Spinner categoryInput;
    private Spinner locationInput;
    private ArrayAdapter<Category> categoriesAdapter;
    private ArrayAdapter<Location> locationsAdapter;
    private  AddPlantActivity instance;
    private int mYear, mMonth, mDay;
    private Date selectedDate;
    Calendar c;
    private ImageView plantImage;



    CompositeDisposable compositeDisposable;
    private static final String TAG = AddPlantActivity.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        instance = this;

        nameInput = (EditText) findViewById(R.id.addPlantETName);
        noteInput = (EditText) findViewById(R.id.addPlantETNote);
        daysBetweenWateringInput = (Spinner) findViewById(R.id.addPlantSDaysBetweenWatering);
        lastWateringDateInput = (EditText) findViewById(R.id.addPlantETLastWateringDate);
        descriptionInput = (EditText) findViewById(R.id.addPlantETDescription);
        categoryInput = (Spinner) findViewById(R.id.addPlantSCategory);
        locationInput = (Spinner) findViewById(R.id.addPlantSLocation);
        plantImage = (ImageView) findViewById(R.id.addPlantImage);

        instance = this;
        List<Integer> numbers = Stream.iterate(1, n -> n + 1)
                .limit(30)
                .collect(Collectors.toList());
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, numbers);
        daysBetweenWateringInput.setAdapter(adapter);

        // Get Current Date
        c = Calendar.getInstance();
        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
        selectedDate = c.getTime();

        lastWateringDateInput.setText(dateOnly.format(c.getTime()));

        lastWateringDateInput.setInputType(InputType.TYPE_NULL);
        lastWateringDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        lastWateringDateInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker();
                }
            }
        });





    }


    public void showDatePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(instance,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        try {
                            selectedDate = new SimpleDateFormat("yyyy-M-d").parse(year +"-" +(monthOfYear+1) + "-" + dayOfMonth);
                            System.out.println(year +"-" +(monthOfYear+1) + "-" + dayOfMonth);
                            SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
                            lastWateringDateInput.setText(dateOnly.format(selectedDate.getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMinDate(selectedDate.getTime() - 10000000000l);
        datePickerDialog.getDatePicker().setMaxDate(selectedDate.getTime());
        datePickerDialog.show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getCategories();
        getLocations();
    }

    public void getCategories() {
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

    public void getLocations() {

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


                        locationsAdapter = new ArrayAdapter<Location>(instance, android.R.layout.simple_spinner_item, locations);
                        locationInput.setAdapter(locationsAdapter);

                    }

                    @Override
                    public void onError(Throwable e) {
                        // oops, we best show some error message
                        Log.d(TAG, "ERROR: " + e.getMessage());
                    }


                });
    }

    public void addPlant(View view)
    {
        compositeDisposable = new CompositeDisposable();

        Plant plantBody = new Plant();
        plantBody.setName(nameInput.getText().toString());
        plantBody.setNote(noteInput.getText().toString());
        Category category = (Category) categoryInput.getSelectedItem();
        plantBody.setDescription(descriptionInput.getText().toString());
////        plantBody.setImage();
        plantBody.setLastWateredDate(lastWateringDateInput.getText().toString());
        plantBody.setDaysBetweenWatering((Integer) daysBetweenWateringInput.getSelectedItem());

        Location location = (Location) locationInput.getSelectedItem();
//
        c = Calendar.getInstance();
        c.setTime(selectedDate);

        // manipulate date
        c.add(Calendar.DAY_OF_MONTH, (Integer) daysBetweenWateringInput.getSelectedItem());

        // convert calendar to date
        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
        selectedDate = c.getTime();
        plantBody.setNextWateredDate(dateOnly.format(selectedDate.getTime()));


//        Read user image
        BitmapDrawable drawable = (BitmapDrawable) plantImage.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);
        plantBody.setImage(encoded);


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

                        //Toast
                        Context context = getApplicationContext();
                        CharSequence text = "Plant added";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        Intent intent = new Intent(context,PlantsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }

                    @Override
                    public void onError(Throwable e) {
                        // oops, we best show some error message
                        Log.d(TAG, "ERROR: " + e.getMessage());
                    }


                });


    }
    public void addPlantOnImageClick(View view) {
        selectImage(instance);
    }


    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose plant image");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        plantImage.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                plantImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }




}