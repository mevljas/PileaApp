package com.example.pileaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    //Register user
    public void RegisterUser (View view) {
        if(validateInput()){

        }

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    public boolean validateInput(){
        Boolean formValid = true;
        EditText email = (EditText) findViewById(R.id.editTextEmail_register);
        EditText password1 = (EditText) findViewById(R.id.editPassword_register);
        EditText password2 = (EditText) findViewById(R.id.editPassword2_register);


        if(!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()){
            formValid = false;
            System.out.println("Email wrong");
        }
        if(password1.getText().equals(password2.getText())){
            formValid = false;
            System.out.println("password wrong");
        }
        //Add a more strict validation
        System.out.println(formValid);
        return formValid;
    }


}