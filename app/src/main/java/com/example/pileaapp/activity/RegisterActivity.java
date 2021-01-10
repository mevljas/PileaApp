package com.example.pileaapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pileaapp.R;
import com.example.pileaapp.api.models.Register;

import java.util.regex.Pattern;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {

    CompositeDisposable compositeDisposable;
    private static final String TAG = RegisterActivity.class.getSimpleName();

    EditText email;
    EditText password1;
    EditText password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        compositeDisposable = new CompositeDisposable();

        email = (EditText) findViewById(R.id.addPlantETName);
        password1 = (EditText) findViewById(R.id.registerETPassword);
        password2 = (EditText) findViewById(R.id.registerETPasswordConfirm);
    }

    //Register user
    public void RegisterUser (View view) {
        if(validateInput()){
            register();
        }


//        Intent intent = new Intent(this,MainActivity.class);
//        startActivity(intent);
    }

    public void register()
    {
        compositeDisposable = new CompositeDisposable();

        Register registerBody = new Register();
        registerBody.setEmail(email.getText().toString());
        registerBody.setUsername(email.getText().toString());
        registerBody.setPassword(password1.getText().toString());


        Single<Register> loginSingle = MainActivity.apiService.register(MainActivity.API_KEY, registerBody);
        loginSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Register>() {
                    @Override
                    public void onSubscribe(Disposable d) {
//                        first we create a CompositeDisposable object which acts as a container for disposables
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Register register) {
                        // data is ready and we can update the UI
                        Log.d(TAG, "SUCCESS");
                        Log.d(TAG, "Registration successful.");

                        //Toast
                        Context context = getApplicationContext();
                        CharSequence text = "User registerd";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        //To login
                        Intent intent = new Intent(context,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);


                    }



                    @Override
                    public void onError(Throwable e) {
                        // oops, we best show some error message
                        Log.d(TAG, "ERROR: " + e.getMessage());
                        //Toast
                        Context context = getApplicationContext();
                        CharSequence text = "Register failed";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    }


                });


    }


    public boolean validateInput(){
        Boolean formValid = true;


        Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");


        if(!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()){
            formValid = false;
            //Toast
            Context context = getApplicationContext();
            CharSequence text = "Wrong email format";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            System.out.println("Email wrong");
        }
        if(!specailCharPatten.matcher(password1.getText()).find()){
            formValid = false;
            //Toast
            Context context = getApplicationContext();
            CharSequence text = "Missing special characters";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            System.out.println("No special characters");
        }
        if(!UpperCasePatten.matcher(password1.getText().toString()).find()){
            formValid = false;
            //Toast
            Context context = getApplicationContext();
            CharSequence text = "Missing uppsercase characters";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            System.out.println("No uppsercase characters");
        }
        if(!lowerCasePatten.matcher(password1.getText().toString()).find()){
            formValid = false;
            //Toast
            Context context = getApplicationContext();
            CharSequence text = "Missing lowercase characters";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            System.out.println("No lowercase characters");
        }
        if(!digitCasePatten.matcher(password1.getText().toString()).find()){
            formValid = false;
            //Toast
            Context context = getApplicationContext();
            CharSequence text = "Missing digital characters";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            System.out.println("No digital characters");
        }
        if(!((password1.getText().toString()).equals(password2.getText().toString()))){
            formValid = false;
            //Toast
            Context context = getApplicationContext();
            CharSequence text = "Passwords don't match";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            System.out.println(password1.getText()+" password dont match "+password2.getText());
        }

        return formValid;
    }

    @Override
    protected void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }


}