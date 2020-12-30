package com.example.pileaapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pileaapp.R;
import com.example.pileaapp.api.models.Login;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText password1;
    CompositeDisposable compositeDisposable;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.editText_login_email);
        password1 = (EditText) findViewById(R.id.editText_login_password);
    }


    public void loginUser(View view)
    {
        compositeDisposable = new CompositeDisposable();

        Login loginBody = new Login();
        loginBody.setEmail(email.getText().toString());
        loginBody.setPassword(password1.getText().toString());


        Single<Login> loginSingle = MainActivity.apiService.register(MainActivity.API_KEY, loginBody);
        loginSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Login>() {
                    @Override
                    public void onSubscribe(Disposable d) {
//                        first we create a CompositeDisposable object which acts as a container for disposables
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Login login) {
                        // data is ready and we can update the UI
                        Log.d(TAG, "SUCCESS");
                        Log.d(TAG, "Login successful.");

                        //Toast
                        Context context = getApplicationContext();
                        CharSequence text = "Login successful";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();


//                        Save currently logged user and set the token.
                        MainActivity.userLogin = login;
                        login.setToken("Bearer " + login.getToken());



                    }



                    @Override
                    public void onError(Throwable e) {
                        // oops, we best show some error message
                        Log.d(TAG, "ERROR: " + e.getMessage());

                    }


                });


    }

    //Open Register Activity
    public void RegisterActivity (View view) {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}