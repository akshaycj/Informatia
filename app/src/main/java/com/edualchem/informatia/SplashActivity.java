package com.edualchem.informatia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import java.util.ArrayList;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        View easySplashScreenView = new EasySplashScreen(SplashActivity.this)
                .withFullScreen()
                .withTargetActivity(SignUpActivity.class)
                .withSplashTimeOut(4000)
                .withBackgroundResource(android.R.color.white)
                .withFooterText("Copyright 2016")
                .withLogo(R.drawable.jjlogo)
                .withAfterLogoText("")
                .create();


        setContentView(easySplashScreenView);



    }
}
