package com.tungsten.fcl.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.fragment.splash.EulaFragment;
import com.tungsten.fcl.fragment.splash.RuntimeFragment;
import com.tungsten.fcllibrary.component.FCLActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends FCLActivity {

    private EulaFragment eulaFragment;
    private RuntimeFragment runtimeFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initFragments();

        start();
    }

    private void initFragments() {
        eulaFragment = new EulaFragment();
        runtimeFragment = new RuntimeFragment();
    }

    public void start() {
        SharedPreferences sharedPreferences = getSharedPreferences("launcher", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isFirstLaunch", true)) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.norm_start_anim, R.anim.norm_stop_anim).replace(R.id.fragment, eulaFragment).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.norm_start_anim, R.anim.norm_stop_anim).replace(R.id.fragment, runtimeFragment).commit();
        }
    }

    public void enterLauncher() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
