package com.tungsten.fcllibrary.component.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.color.ColorPickerView;

public class FCLColorPickerDialog extends FCLDialog implements View.OnClickListener, TextView.OnEditorActionListener, ColorPickerView.OnColorChangedListener {

    private final int initColor;
    private final Listener listener;

    private ColorPickerView colorPickerView;
    private int currentColor;

    private View initColorBar;
    private View destColorBar;
    private FCLEditText editColor;
    private FCLButton positive;
    private FCLButton negative;

    public FCLColorPickerDialog(@NonNull Context context, int initColor, Listener listener) {
        super(context);
        setContentView(R.layout.dialog_color_picker);
        setCancelable(false);
        this.initColor = initColor;
        this.listener = listener;
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init(){
        colorPickerView = findViewById(R.id.color_picker);
        colorPickerView.setOnColorChangedListener(this);
        colorPickerView.setColor(initColor);
        currentColor = initColor;

        initColorBar = findViewById(R.id.init_color);
        destColorBar = findViewById(R.id.dest_color);
        initColorBar.setBackgroundColor(initColor);
        destColorBar.setBackgroundColor(initColor);
        editColor = findViewById(R.id.color_text);
        editColor.setOnEditorActionListener(this);
        editColor.setText(getHex(initColor));

        positive = findViewById(R.id.color_picker_positive);
        negative = findViewById(R.id.color_picker_negative);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void setColor(int color){
        currentColor = color;
        colorPickerView.setColor(color);
        editColor.setText(getHex(color));
        destColorBar.setBackgroundColor(color);
        if (listener != null) {
            listener.onColorChanged(color);
        }
    }

    private String getHex(int color) {
        return "#" + String.format("%08X", (color));
    }

    private int parseColorString(String colorString) throws NumberFormatException {
        int a, r, g, b = 0;
        if (colorString.startsWith("#")) {
            colorString = colorString.substring(1);
        }
        if (colorString.length() == 0) {
            r = 0;
            a = 255;
            g = 0;
        } else if (colorString.length() <= 2) {
            a = 255;
            r = 0;
            b = Integer.parseInt(colorString, 16);
            g = 0;
        } else if (colorString.length() == 3) {
            a = 255;
            r = Integer.parseInt(colorString.substring(0, 1), 16);
            g = Integer.parseInt(colorString.substring(1, 2), 16);
            b = Integer.parseInt(colorString.substring(2, 3), 16);
        } else if (colorString.length() == 4) {
            a = 255;
            r = Integer.parseInt(colorString.substring(0, 2), 16);
            g = r;
            r = 0;
            b = Integer.parseInt(colorString.substring(2, 4), 16);
        } else if (colorString.length() == 5) {
            a = 255;
            r = Integer.parseInt(colorString.substring(0, 1), 16);
            g = Integer.parseInt(colorString.substring(1, 3), 16);
            b = Integer.parseInt(colorString.substring(3, 5), 16);
        } else if (colorString.length() == 6) {
            a = 255;
            r = Integer.parseInt(colorString.substring(0, 2), 16);
            g = Integer.parseInt(colorString.substring(2, 4), 16);
            b = Integer.parseInt(colorString.substring(4, 6), 16);
        } else if (colorString.length() == 7) {
            a = Integer.parseInt(colorString.substring(0, 1), 16);
            r = Integer.parseInt(colorString.substring(1, 3), 16);
            g = Integer.parseInt(colorString.substring(3, 5), 16);
            b = Integer.parseInt(colorString.substring(5, 7), 16);
        } else if (colorString.length() == 8) {
            a = Integer.parseInt(colorString.substring(0, 2), 16);
            r = Integer.parseInt(colorString.substring(2, 4), 16);
            g = Integer.parseInt(colorString.substring(4, 6), 16);
            b = Integer.parseInt(colorString.substring(6, 8), 16);
        } else {
            b = -1;
            g = -1;
            r = -1;
            a = -1;
        }
        return Color.argb(a, r, g, b);
    }

    @Override
    public void onClick(View view) {
        if (view == positive){
            if (listener != null) {
                listener.onPositive(currentColor);
            }
            this.dismiss();
        }
        if (view == negative){
            if (listener != null) {
                listener.onNegative(initColor);
            }
            this.dismiss();
        }
    }

    @Override
    public void onColorChanged(int newColor) {
        setColor(newColor);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {
            setColor(parseColorString(textView.getText().toString()));
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), getContext().getString(R.string.color_picker_toast), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public interface Listener {
        void onColorChanged(int color);
        void onPositive(int destColor);
        void onNegative(int initColor);
    }
}
