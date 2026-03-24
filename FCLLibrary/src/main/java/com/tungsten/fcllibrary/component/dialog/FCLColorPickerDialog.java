package com.tungsten.fcllibrary.component.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;

import com.tungsten.fcllibrary.component.view.color.ColorPickerView;
import com.tungsten.fcllibrary.databinding.DialogColorPickerBinding;

public class FCLColorPickerDialog extends FCLDialog implements View.OnClickListener, ColorPickerView.OnColorChangedListener {

    private final Listener listener;
    private final int initColor;
    private int currentColor;
    private final DialogColorPickerBinding binding;

    public FCLColorPickerDialog(@NonNull Context context, int initColor, Listener listener) {
        super(context);
        binding = DialogColorPickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCancelable(false);
        this.initColor = initColor;
        this.listener = listener;
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        binding.colorPicker.setOnColorChangedListener(this);
        binding.colorPicker.setColor(initColor);
        currentColor = initColor;
        binding.initColor.setBackgroundColor(initColor);
        binding.destColor.setBackgroundColor(initColor);
        binding.editColor.setText(getHex(initColor));
        binding.editColor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String colorText = s.toString();
                    if (!colorText.startsWith("#")) {
                        colorText = "#" + colorText;
                    }
                    int parsedColor = Color.parseColor(colorText);
                    if (parsedColor != currentColor) {
                        setColor(parsedColor, true);
                    }
                } catch (Throwable ignore) {
                }
            }
        });
        binding.positive.setOnClickListener(this);
        binding.negative.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void setColor(int color, boolean isFromInput) {
        currentColor = color;
        binding.colorPicker.setColor(color);
        if (!isFromInput) {
            binding.editColor.setText(getHex(color));
        }
        binding.destColor.setBackgroundColor(color);
        if (listener != null) {
            listener.onColorChanged(color);
        }
    }

    private String getHex(int color) {
        return String.format("%08X", color);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.positive) {
            if (listener != null) {
                listener.onPositive(currentColor);
            }
            this.dismiss();
        }
        if (view == binding.negative) {
            if (listener != null) {
                listener.onNegative(initColor);
            }
            this.dismiss();
        }
    }

    @Override
    public void onColorChanged(int newColor) {
        setColor(newColor, false);
    }

    public interface Listener {
        void onColorChanged(int color);

        void onPositive(int destColor);

        void onNegative(int initColor);
    }
}
