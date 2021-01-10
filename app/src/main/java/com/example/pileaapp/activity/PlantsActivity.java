package com.example.pileaapp.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pileaapp.R;

import com.example.pileaapp.api.models.Category;
import com.example.pileaapp.api.models.Location;
import com.example.pileaapp.api.models.Plant;
import com.example.pileaapp.helpers.RecycleViewAdapterPlants;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class PlantsActivity extends AppCompatActivity {
    List s1;
    public RecyclerView recyclerView;
    public RecycleViewAdapterPlants myRecycleViewAdapter;
    public Context context = this;
    private ArrayAdapter<Category> categoriesAdapter;
    private ArrayAdapter<Location> locationsAdapter;


    CompositeDisposable compositeDisposable;
    private static final String TAG = PlantsActivity.class.getSimpleName();

    static Dialog deleteDialog;
    static Dialog myEditDialog;

    public static PlantsActivity instance;    // Some objects require this.

    //Edit popup itemslocationsField
    private Spinner categoriesField;
    private Spinner locationsField;

    //Date
    private Date selectedDate;
    Calendar c;
    private EditText lastWateringDateInput;
    private int mYear, mMonth, mDay;

    ImageView plantEditImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;


        setContentView(R.layout.activity_plants);


        s1 = new ArrayList();
        recyclerView = findViewById(R.id.plantsRecycleView);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(PlantsActivity.this, 2);
        recyclerView.setLayoutManager(mGridLayoutManager);
        myRecycleViewAdapter = new RecycleViewAdapterPlants(this, s1);
        recyclerView.setAdapter(myRecycleViewAdapter);


        deleteDialog = new Dialog(this);
        myEditDialog = new Dialog(this);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        showPlants();
    }

    public void showPlants() {
        compositeDisposable = new CompositeDisposable();
//       Make a request by calling the corresponding method
        Single<List<Plant>> list = MainActivity.apiService.getUserPlants(MainActivity.userLogin.getToken(), MainActivity.API_KEY, MainActivity.userLogin.getUserID());
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
                        List<Plant> plants = list;
                        Log.d(TAG, "Number of plants received: " + plants.size());


                        myRecycleViewAdapter = new RecycleViewAdapterPlants(context, list);
                        recyclerView.setAdapter(myRecycleViewAdapter);


                    }

                    @Override
                    public void onError(Throwable e) {
                        // oops, we best show some error message
                        Log.d(TAG, "ERROR: " + e.getMessage());
                    }


                });
    }

    //Open AddPlant Activity
    public void AddPlantAcitivty (View view) {
        Intent intent = new Intent(this, AddPlantActivity.class);
        startActivity(intent);

    }

    //Open Detail Activity
    public void DetailPlantAcitivty (View view, Plant plant) {
        Intent intent = new Intent(this, DetailPlantActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("plantId", plant.getPlantID());
        extras.putInt("plantCategory", plant.getCategoryID());
        extras.putInt("plantLocation", plant.getLocationID());


        intent.putExtras(extras);
        startActivity(intent);

    }

    //Delete plant
    public void ShowDeletePopup(View v, Plant plant) {
        Button btnClose;
        Button btnDelete;
        TextView text;
        deleteDialog.setContentView(R.layout.delete_popup);

        btnClose = (Button) deleteDialog.findViewById(R.id.deletePopUpBNo);
        btnDelete = (Button) deleteDialog.findViewById(R.id.deletePopUpBYes);
        text = (TextView) deleteDialog.findViewById(R.id.deletePopUpTVText);

        text.setText("Do you really want to delete this plant?");

        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deletePlant(plant);
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


    public void deletePlant(Plant selectedPlant) {
        compositeDisposable = new CompositeDisposable();


        Single<Plant> plantSingle = MainActivity.apiService.deletePlant(selectedPlant.getPlantID(), MainActivity.userLogin.getToken(), MainActivity.API_KEY);
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
                        Log.d(TAG, "Plant deleted: " + plant.getPlantID());

                        //Toast
                        Context context = getApplicationContext();
                        CharSequence text = "Plant deleted.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        showPlants();


                    }

                    @Override
                    public void onError(Throwable e) {
                        // oops, we best show some error message
                        Log.d(TAG, "ERROR: " + e.getMessage());
                    }


                });


    }

    public void waterPlant(Plant selectedPlant){
        try{

            c = Calendar.getInstance();
            SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");

            Date lastWatering = new Date();

            c.setTime(lastWatering);

            // manipulate date
            c.add(Calendar.DAY_OF_MONTH, (Integer) selectedPlant.getDaysBetweenWatering());

            // convert calendar to date
            selectedDate = c.getTime();
            selectedPlant.setNextWateredDate(dateOnly.format(selectedDate.getTime()));

            Date nowDate = new Date();

            selectedPlant.setLastWateredDate(dateOnly.format(nowDate));

            /*
            System.out.println("Next watering date "+selectedPlant.getNextWateredDate());
            System.out.println("Last  watering date "+selectedPlant.getLastWateredDate());*/

            editPlant(selectedPlant);
            //Toast
            Context context = getApplicationContext();
            CharSequence text = selectedPlant.getName()+" waterd";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            showPlants();

            showPlants();

        }catch (Exception e){
            System.out.println(e.toString());

        }

    }


    public void editPlant(Plant selectedPlant) {



        compositeDisposable = new CompositeDisposable();


        Single<Plant> plantSingle = MainActivity.apiService.editPlant(selectedPlant.getPlantID(), MainActivity.userLogin.getToken(), MainActivity.API_KEY, selectedPlant);
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
                        Log.d(TAG, "Plant edited: " + plant.getName());

                        //Toast
                        Context context = getApplicationContext();
                        CharSequence text = "Plant edited.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        showPlants();

                    }

                    @Override
                    public void onError(Throwable e) {
                        // oops, we best show some error message
                        Log.d(TAG, "ERROR: " + e.getMessage());
                    }

                });


    }


    //    Decode Base64 image
    private Bitmap decodeImage(String image){
        byte[] decodedString = Base64.decode(image, Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void ShowEditPopup(View v, Plant plant) {
        Button btnClose;
        Button btnEdit;
        EditText nameField, descriptionField, noteField, dateField;
        Spinner daysBetweenWateringField;



        myEditDialog.setContentView(R.layout.plant_edit_popup);

        btnEdit = (Button) myEditDialog.findViewById(R.id.editPlantUpBYes);
        btnClose = (Button) myEditDialog.findViewById(R.id.editPlantUpBNo);


        nameField = (EditText) myEditDialog.findViewById(R.id.editPlantETName);
        noteField = (EditText) myEditDialog.findViewById(R.id.editPlantETNote);
        descriptionField = (EditText) myEditDialog.findViewById(R.id.editPlantETDescription);

        daysBetweenWateringField = (Spinner) myEditDialog.findViewById(R.id.editPlantSDaysBetween);
        locationsField = (Spinner) myEditDialog.findViewById(R.id.editPlantSLocation);
        categoriesField = (Spinner) myEditDialog.findViewById(R.id.editPlantSCategories);

        plantEditImage = (ImageView) myEditDialog.findViewById(R.id.editPlantImage);

        lastWateringDateInput = (EditText) myEditDialog.findViewById(R.id.editPlantETDate);


        //Setup data to be presented
        if (plant.getName() != null)
            nameField.setText(plant.getName().toString());
        if (plant.getDescription() != null)
            descriptionField.setText(plant.getDescription().toString());
        if (plant.getNote() != null)
            noteField.setText(plant.getNote().toString());

        if (plant.getImage() != null){
            plantEditImage.setImageBitmap(decodeImage(plant.getImage()));
        }

        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");

        if (plant.getLastWateredDate() != null){
            try {
                Date lastwatering = dateOnly.parse(plant.getLastWateredDate());
                lastWateringDateInput.setText(dateOnly.format(lastwatering));


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }









        List<Integer> numbers = Stream.iterate(1, n -> n + 1)
                .limit(30)
                .collect(Collectors.toList());
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, numbers);
        daysBetweenWateringField.setAdapter(adapter);
        daysBetweenWateringField.setSelection((Integer) plant.getDaysBetweenWatering()-1);

        getCategories(plant);
        getLocations(plant);











        btnEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                plant.setName(nameField.getText().toString());
                plant.setDescription(descriptionField.getText().toString());
                plant.setNote(noteField.getText().toString());


                plant.setLastWateredDate(lastWateringDateInput.getText().toString());
                plant.setDaysBetweenWatering((Integer) daysBetweenWateringField.getSelectedItem());

                c = Calendar.getInstance();
                c.setTime(selectedDate);

                // manipulate date
                c.add(Calendar.DAY_OF_MONTH, (Integer) daysBetweenWateringField.getSelectedItem());

                // convert calendar to date
                SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
                selectedDate = c.getTime();
                plant.setNextWateredDate(dateOnly.format(selectedDate.getTime()));

                Location location = (Location) locationsField.getSelectedItem();
                Category category = (Category) categoriesField.getSelectedItem();

                plant.setCategoryID(category.getCategoryID());
                plant.setCategory(category);


                plant.setLocationID(location.getLocationID());
                plant.setLocation(location);




                //        Read user image
                BitmapDrawable drawable = (BitmapDrawable) plantEditImage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                plant.setImage(encoded);


                editPlant(plant);
                myEditDialog.dismiss();

            }
        });


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myEditDialog.dismiss();
            }
        });



        // Get Current Date
        c = Calendar.getInstance();
        dateOnly = new SimpleDateFormat("yyyy-MM-dd");
        selectedDate = c.getTime();

//        lastWateringDateInput.setText(dateOnly.format(c.getTime()));


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




        myEditDialog.show();
    }




    public void getCategories(Plant selectedPlant) {
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
                        categoriesField.setAdapter(categoriesAdapter);


                        //TODO there has to be a better way to do this
                        for(int i = 0;i <list.size(); i++){
                            if(categories.get(i).getCategoryID().equals(selectedPlant.getCategoryID())){
                                categoriesField.setSelection(i);
                                break;
                            }
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        // oops, we best show some error message
                        Log.d(TAG, "ERROR: " + e.getMessage());
                    }


                });
    }

    public void getLocations(Plant selectedPlant) {

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
                        locationsField.setAdapter(locationsAdapter);

                        //TODO there has to be a better way to do this
                        for(int i = 0;i <list.size(); i++){
                            if(locations.get(i).getLocationID().equals(selectedPlant.getLocationID())){
                                locationsField.setSelection(i);
                                break;
                            }
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        // oops, we best show some error message
                        Log.d(TAG, "ERROR: " + e.getMessage());
                    }


                });
    }

    public void showDatePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();


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


    public void editPlantOnImageClick(View view) {
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
                        plantEditImage.setImageBitmap(selectedImage);
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
                                plantEditImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

}