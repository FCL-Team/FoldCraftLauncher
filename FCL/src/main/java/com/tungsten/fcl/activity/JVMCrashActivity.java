package com.tungsten.fcl.activity;

import android.content.Context;
import android.content.Intent;
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

    public static void startCrashActivity(Context context, int exitCode) {
        Intent intent = new Intent(context, JVMCrashActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("exitCode", exitCode);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
