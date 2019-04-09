package com.example.usermanagement.module;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.example.usermanagement.R;
import com.example.usermanagement.utils.CommanUtility;

public class SplashActivity extends AppCompatActivity {
        private Handler handler;
        private int  DELAY_TIME =3000;

        private CommanUtility commanUtility;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        commanUtility = new CommanUtility(this);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(commanUtility.getLoginStatus()){
                    Intent intent=new Intent(SplashActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent=new Intent(SplashActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }

            }
        },DELAY_TIME);
    }
}