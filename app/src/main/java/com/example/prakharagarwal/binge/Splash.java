package com.example.prakharagarwal.binge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.prakharagarwal.binge.MainScreen.MainActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs =getSharedPreferences("onboarding", Context.MODE_PRIVATE);
                String uID = prefs.getString("status", null);
                Intent intent;

             //   if(uID!=null){
               //     intent= new Intent(Splash.this, MainActivity.class);
//                }else{
                    intent=new Intent(Splash.this,OnBoardingActivity.class);
//                }
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
