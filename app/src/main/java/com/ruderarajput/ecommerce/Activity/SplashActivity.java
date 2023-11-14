package com.ruderarajput.ecommerce.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.ruderarajput.ecommerce.R;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        auth=FirebaseAuth.getInstance();

        getSupportActionBar().hide();

     new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {
             if (auth.getCurrentUser()!=null){
                 Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                 startActivity(intent);
                 finish();
             }else {
                 Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                 startActivity(intent);
                 finish();
             }
         }
     },5000);
    }
}