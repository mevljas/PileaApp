package com.example.pileaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Pattern;

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
/*
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);*/
    }


    public boolean validateInput(){
        Boolean formValid = true;
        EditText email = (EditText) findViewById(R.id.editTextEmail_register);
        EditText password1 = (EditText) findViewById(R.id.editPassword_register);
        EditText password2 = (EditText) findViewById(R.id.editPassword2_register);

        Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");


        if(!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()){
            formValid = false;
            System.out.println("Email wrong");
        }
        if(!specailCharPatten.matcher(password1.getText()).find()){
            formValid = false;
            System.out.println("No special characters");
        }
        if(!UpperCasePatten.matcher(password1.getText()).find()){
            formValid = false;
            System.out.println("No uppsercase characters");
        }
        if(!lowerCasePatten.matcher(password1.getText()).find()){
            formValid = false;
            System.out.println("No lowercase characters");
        }
        if(!digitCasePatten.matcher(password1.getText()).find()){
            formValid = false;
            System.out.println("No digital characters");
        }
        //For some reason this doesnt work
        /*
        if(!((password1.getText()).equals(password2.getText()))){
            formValid = false;
            System.out.println(password1.getText()+" password dont match "+password2.getText());
        }*/

        //Fix the password equals thing



        return formValid;
    }


}