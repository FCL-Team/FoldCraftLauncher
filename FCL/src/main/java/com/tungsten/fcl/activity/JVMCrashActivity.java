package com.tungsten.fcl.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.FCLActivity;

public class JVMCrashActivity extends FCLActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jvm_crash);
    }
}
