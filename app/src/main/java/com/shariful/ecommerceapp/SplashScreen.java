package com.shariful.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        userInfo();

        //start
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (email!=null){
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashScreen.this,LoginSignUpActivity.class));
                    finish();
                }

            }
        }, 300);

        //end
    }

    public void userInfo(){
        //reading shared preferences data
        SharedPreferences sharedPreferences = getSharedPreferences("info", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("emailkey")){
            email = sharedPreferences.getString("emailkey","No Email Found !");
        }
    }

}