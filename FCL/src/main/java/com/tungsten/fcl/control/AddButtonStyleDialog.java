package com.tungsten.fcl.control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.tabs.TabLayout;
import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.ButtonStyles;
import com.tungsten.fcl.control.data.ControlButtonStyle;
import com.tungsten.fcllibrary.component.dialog.EditDialog;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fcllibrary.component.dialog.FCLColorPickerDialog;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLPreciseSeekBar;
import com.tungsten.fcllibrary.component.view.FCLTabLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

public class AddButtonStyleDialog extends FCLDialog implements View.OnClickListener, TabLayout.OnTabSelectedListener {

    private final Callback callback;

    private FCLButton positive;
    private FCLButton negative;

    private FCLEditText editName;
    private AppCompatButton button;
    private FCLTabLayout tabLayout;

    private ScrollView container;
    private FCLLinearLayout normalStyleLayout;
    private FCLLinearLayout pressedStyleLayout;

    private ControlButtonStyle style;
    private boolean isEdit;

    public interface Callback {
        void onStyleAdd(ControlButtonStyle style);
    }

    public AddButtonStyleDialog(@NonNull Context context, ControlButtonStyle beforeStyle, boolean isEdit, Callback callback) {
        super(context);
        setContentView(R.layout.dialog_add_button_style);
        setCancelable(false);
        this.callback = callback;
        this.style = beforeStyle == null ? new ControlButtonStyle("") : beforeStyle.clone();
        this.isEdit = isEdit;

        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);

        editName = findViewById(R.id.name);
        button = findViewById(R.id.button);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(this);

        container = findViewById(R.id.container);

        normalStyleLayout = (FCLLinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_button_style, null);
        pressedStyleLayout = (FCLLinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_button_style, null);

        editName.setText(style.getName());
        style.nameProperty().bind(editName.stringProperty());
        changeButtonStyle();
        style.addListener(observable -> changeButtonStyle());
        {
            FCLPreciseSeekBar textSize = normalStyleLayout.findViewById(R.id.text_size);
            FCLPreciseSeekBar strokeWidth = normalStyleLayout.findViewById(R.id.stroke_width);
            FCLPreciseSeekBar cornerRadius = normalStyleLayout.findViewById(R.id.corner_radius);

            FCLTextView textSizeText = normalStyleLayout.findViewById(R.id.text_size_text);
            FCLTextView strokeWidthText = normalStyleLayout.findViewById(R.id.stroke_width_text);
            FCLTextView cornerRadiusText = normalStyleLayout.findViewById(R.id.corner_radius_text);

            textSizeText.setOnClickListener(v -> openTextEditDialog(context, textSize.progressProperty(), false));
            strokeWidthText.setOnClickListener(v -> openTextEditDialog(context, strokeWidth.progressProperty(), true));
            cornerRadiusText.setOnClickListener(v -> openTextEditDialog(context, cornerRadius.progressProperty(), true));

            textSize.setProgress(style.getTextSize());
            strokeWidth.setProgress(style.getStrokeWidth());
            cornerRadius.setProgress(style.getCornerRadius());
            style.textSizeProperty().bindBidirectional(textSize.progressProperty());
            style.strokeWidthProperty().bindBidirectional(strokeWidth.progressProperty());
            style.cornerRadiusProperty().bindBidirectional(cornerRadius.progressProperty());

            textSizeText.stringProperty().bind(Bindings.createStringBinding(() -> textSize.getProgress() + " sp", textSize.progressProperty()));
            strokeWidthText.stringProperty().bind(Bindings.createStringBinding(() -> strokeWidth.getProgress() / 10f + " dp", strokeWidth.progressProperty()));
            cornerRadiusText.stringProperty().bind(Bindings.createStringBinding(() -> cornerRadius.getProgress() / 10f + " dp", cornerRadius.progressProperty()));

            FCLTextView textColorText = normalStyleLayout.findViewById(R.id.text_color_text);
            FCLTextView strokeColorText = normalStyleLayout.findViewById(R.id.stroke_color_text);
            FCLTextView fillColorText = normalStyleLayout.findViewById(R.id.fill_color_text);

            View textColorView = normalStyleLayout.findViewById(R.id.text_color_view);
            View strokeColorView = normalStyleLayout.findViewById(R.id.stroke_color_view);
            View fillColorView = normalStyleLayout.findViewById(R.id.fill_color_view);

            FCLButton textColorSet = normalStyleLayout.findViewById(R.id.set_text_color);
            FCLButton strokeColorSet = normalStyleLayout.findViewById(R.id.set_stroke_color);
            FCLButton fillColorSet = normalStyleLayout.findViewById(R.id.set_fill_color);

            textColorView.setBackgroundColor(style.getTextColor());
            textColorText.setText(getHex(style.getTextColor()));
            strokeColorView.setBackgroundColor(style.getStrokeColor());
            strokeColorText.setText(getHex(style.getStrokeColor()));
            fillColorView.setBackgroundColor(style.getFillColor());
            fillColorText.setText(getHex(style.getFillColor()));
            style.textColorProperty().addListener(observable -> {
                textColorView.setBackgroundColor(style.getTextColor());
                textColorText.setText(getHex(style.getTextColor()));
            });
            style.strokeColorProperty().addListener(observable -> {
                strokeColorView.setBackgroundColor(style.getStrokeColor());
                strokeColorText.setText(getHex(style.getStrokeColor()));
            });
            style.fillColorProperty().addListener(observable -> {
                fillColorView.setBackgroundColor(style.getFillColor());
                fillColorText.setText(getHex(style.getFillColor()));
            });

            textColorSet.setOnClickListener(v -> {
                FCLColorPickerDialog dialog = new FCLColorPickerDialog(getContext(), style.getTextColor(), new FCLColorPickerDialog.Listener() {
                    @Override
                    public void onColorChanged(int color) {

                    }

                    @Override
                    public void onPositive(int destColor) {
                        style.setTextColor(destColor);
                    }

                    @Override
                    public void onNegative(int initColor) {

                    }
                });
                dialog.show();
            });
            strokeColorSet.setOnClickListener(v -> {
                FCLColorPickerDialog dialog = new FCLColorPickerDialog(getContext(), style.getStrokeColor(), new FCLColorPickerDialog.Listener() {
                    @Override
                    public void onColorChanged(int color) {

                    }

                    @Override
                    public void onPositive(int destColor) {
                        style.setStrokeColor(destColor);
                    }

                    @Override
                    public void onNegative(int initColor) {

                    }
                });
                dialog.show();
            });
            fillColorSet.setOnClickListener(v -> {
                FCLColorPickerDialog dialog = new FCLColorPickerDialog(getContext(), style.getFillColor(), new FCLColorPickerDialog.Listener() {
                    @Override
                    public void onColorChanged(int color) {

                    }

                    @Override
                    public void onPositive(int destColor) {
                        style.setFillColor(destColor);
                    }

                    @Override
                    public void onNegative(int initColor) {

                    }
                });
                dialog.show();
            });
        }

        {
            FCLPreciseSeekBar textSize = pressedStyleLayout.findViewById(R.id.text_size);
            FCLPreciseSeekBar strokeWidth = pressedStyleLayout.findViewById(R.id.stroke_width);
            FCLPreciseSeekBar cornerRadius = pressedStyleLayout.findViewById(R.id.corner_radius);

            FCLTextView textSizeText = pressedStyleLayout.findViewById(R.id.text_size_text);
            FCLTextView strokeWidthText = pressedStyleLayout.findViewById(R.id.stroke_width_text);
            FCLTextView cornerRadiusText = pressedStyleLayout.findViewById(R.id.corner_radius_text);

            textSizeText.setOnClickListener(v -> openTextEditDialog(context, textSize.progressProperty(), false));
            strokeWidthText.setOnClickListener(v -> openTextEditDialog(context, strokeWidth.progressProperty(), true));
            cornerRadiusText.setOnClickListener(v -> openTextEditDialog(context, cornerRadius.progressProperty(), true));

            textSize.setProgress(style.getTextSizePressed());
            strokeWidth.setProgress(style.getStrokeWidthPressed());
            cornerRadius.setProgress(style.getCornerRadiusPressed());
            style.textSizePressedProperty().bindBidirectional(textSize.progressProperty());
            style.strokeWidthPressedProperty().bindBidirectional(strokeWidth.progressProperty());
            style.cornerRadiusPressedProperty().bindBidirectional(cornerRadius.progressProperty());

            textSizeText.stringProperty().bind(Bindings.createStringBinding(() -> textSize.getProgress() + " sp", textSize.progressProperty()));
            strokeWidthText.stringProperty().bind(Bindings.createStringBinding(() -> strokeWidth.getProgress() / 10f + " dp", strokeWidth.progressProperty()));
            cornerRadiusText.stringProperty().bind(Bindings.createStringBinding(() -> cornerRadius.getProgress() / 10f + " dp", cornerRadius.progressProperty()));

            FCLTextView textColorText = pressedStyleLayout.findViewById(R.id.text_color_text);
            FCLTextView strokeColorText = pressedStyleLayout.findViewById(R.id.stroke_color_text);
            FCLTextView fillColorText = pressedStyleLayout.findViewById(R.id.fill_color_text);

            View textColorView = pressedStyleLayout.findViewById(R.id.text_color_view);
            View strokeColorView = pressedStyleLayout.findViewById(R.id.stroke_color_view);
            View fillColorView = pressedStyleLayout.findViewById(R.id.fill_color_view);

            FCLButton textColorSet = pressedStyleLayout.findViewById(R.id.set_text_color);
            FCLButton strokeColorSet = pressedStyleLayout.findViewById(R.id.set_stroke_color);
            FCLButton fillColorSet = pressedStyleLayout.findViewById(R.id.set_fill_color);

            textColorView.setBackgroundColor(style.getTextColorPressed());
            textColorText.setText(getHex(style.getTextColorPressed()));
            strokeColorView.setBackgroundColor(style.getStrokeColorPressed());
            strokeColorText.setText(getHex(style.getStrokeColorPressed()));
            fillColorView.setBackgroundColor(style.getFillColorPressed());
            fillColorText.setText(getHex(style.getFillColorPressed()));
            style.textColorPressedProperty().addListener(observable -> {
                textColorView.setBackgroundColor(style.getTextColorPressed());
                textColorText.setText(getHex(style.getTextColorPressed()));
            });
            style.strokeColorPressedProperty().addListener(observable -> {
                strokeColorView.setBackgroundColor(style.getStrokeColorPressed());
                strokeColorText.setText(getHex(style.getStrokeColorPressed()));
            });
            style.fillColorPressedProperty().addListener(observable -> {
                fillColorView.setBackgroundColor(style.getFillColorPressed());
                fillColorText.setText(getHex(style.getFillColorPressed()));
            });

            textColorSet.setOnClickListener(v -> {
                FCLColorPickerDialog dialog = new FCLColorPickerDialog(getContext(), style.getTextColorPressed(), new FCLColorPickerDialog.Listener() {
                    @Override
                    public void onColorChanged(int color) {

                    }

                    @Override
                    public void onPositive(int destColor) {
                        style.setTextColorPressed(destColor);
                    }

                    @Override
                    public void onNegative(int initColor) {

                    }
                });
                dialog.show();
            });
            strokeColorSet.setOnClickListener(v -> {
                FCLColorPickerDialog dialog = new FCLColorPickerDialog(getContext(), style.getStrokeColorPressed(), new FCLColorPickerDialog.Listener() {
                    @Override
                    public void onColorChanged(int color) {

                    }

                    @Override
                    public void onPositive(int destColor) {
                        style.setStrokeColorPressed(destColor);
                    }

                    @Override
                    public void onNegative(int initColor) {

                    }
                });
                dialog.show();
            });
            fillColorSet.setOnClickListener(v -> {
                FCLColorPickerDialog dialog = new FCLColorPickerDialog(getContext(), style.getFillColorPressed(), new FCLColorPickerDialog.Listener() {
                    @Override
                    public void onColorChanged(int color) {

                    }

                    @Override
                    public void onPositive(int destColor) {
                        style.setFillColorPressed(destColor);
                    }

                    @Override
                    public void onNegative(int initColor) {

                    }
                });
                dialog.show();
            });
        }

        container.addView(normalStyleLayout);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void changeButtonStyle() {
        GradientDrawable drawableNormal = new GradientDrawable();
        drawableNormal.setCornerRadius(ConvertUtils.dip2px(getContext(), style.getCornerRadius() / 10f));
        drawableNormal.setStroke(ConvertUtils.dip2px(getContext(), style.getStrokeWidth() / 10f), style.getStrokeColor());
        drawableNormal.setColor(style.getFillColor());
        GradientDrawable drawablePressed = new GradientDrawable();
        drawablePressed.setCornerRadius(ConvertUtils.dip2px(getContext(), style.getCornerRadiusPressed() / 10f));
        drawablePressed.setStroke(ConvertUtils.dip2px(getContext(), style.getStrokeWidthPressed() / 10f), style.getStrokeColorPressed());
        drawablePressed.setColor(style.getFillColorPressed());
        button.setGravity(Gravity.CENTER);
        button.setPadding(0, 0, 0, 0);
        button.setText("S");
        button.setAllCaps(false);
        button.setTextSize(style.getTextSize());
        button.setTextColor(style.getTextColor());
        button.setBackground(drawableNormal);
        button.setOnTouchListener((view, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                ((AppCompatButton) view).setTextSize(style.getTextSizePressed());
                ((AppCompatButton) view).setTextColor(style.getTextColorPressed());
                ((AppCompatButton) view).setBackground(drawablePressed);
            }
            if (event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_CANCEL) {
                ((AppCompatButton) view).setTextSize(style.getTextSize());
                ((AppCompatButton) view).setTextColor(style.getTextColor());
                ((AppCompatButton) view).setBackground(drawableNormal);
            }
            return true;
        });
    }

    private String getHex(int color) {
        return "#" + String.format("%08X", (color));
    }

    @Override
    public void onClick(View v) {
        if (v == positive) {
            if (!isEdit && ButtonStyles.getStyles().stream().anyMatch(it -> it.getName().equals(style.getName()))) {
                Toast.makeText(getContext(), getContext().getString(R.string.style_warning_exist), Toast.LENGTH_SHORT).show();
            } else if (StringUtils.isBlank(style.getName())) {
                Toast.makeText(getContext(), getContext().getString(R.string.style_warning_name), Toast.LENGTH_SHORT).show();
            } else {
                dismiss();
                callback.onStyleAdd(style);
            }
        }
        if (v == negative) {
            dismiss();
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        container.removeAllViewsInLayout();
        if (tab.getPosition() == 0) {
            container.addView(normalStyleLayout);
        } else {
            container.addView(pressedStyleLayout);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void openTextEditDialog(Context context, IntegerProperty property, boolean isPercentage) {
        EditDialog dialog = new EditDialog(context, s -> {
            if (s.matches("\\d+(\\.\\d+)?$")) {
                float progress = Float.parseFloat(s);
                if (isPercentage) {
                    progress = progress > 100 ? 100 : progress;
                    property.set((int) (progress * 10));
                } else {
                    property.set((int) progress);
                }
            }
        });
        dialog.getEditText().setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        dialog.show();
    }
}
