package com.tungsten.fcl.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.FCLActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends FCLActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
}
