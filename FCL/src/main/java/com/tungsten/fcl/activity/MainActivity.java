package com.tungsten.fcl.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class MainActivity extends FCLActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FCLSeekBar view = findViewById(R.id.test_view);
        //view.setEnabled(false);
        ThemeEngine.getInstance().applyAndSave(this, Color.parseColor("#FF5C6BC0"));


        button.setOnClickListener(null);
    }

}