package com.tungsten.fcl.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.BaseInfoData;
import com.tungsten.fcl.control.data.ControlButtonStyle;
import com.tungsten.fcl.control.data.ControlDirectionStyle;
import com.tungsten.fcl.control.data.DirectionStyles;
import com.tungsten.fcl.control.view.ControlDirection;
import com.tungsten.fcllibrary.component.dialog.EditDialog;
import com.tungsten.fcl.util.FXUtils;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fcllibrary.component.dialog.FCLColorPickerDialog;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLPreciseSeekBar;
import com.tungsten.fcllibrary.component.view.FCLSpinner;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.util.ArrayList;

public class AddDirectionStyleDialog extends FCLDialog implements View.OnClickListener {

    private final Callback callback;

    private FCLButton positive;
    private FCLButton negative;

    private FCLEditText editName;
    private ControlDirection direction;
    private FCLSpinner<ControlDirectionStyle.Type> typeSpinner;

    private ScrollView container;
    private FCLLinearLayout buttonStyleLayout;
    private FCLLinearLayout rockerStyleLayout;

    private ControlDirectionStyle style;

    private ControlButtonStyle buttonStyle;
    private boolean isEdit;
    private ControlButtonStyle beforeStyle;

    public interface Callback {
        void onStyleAdd(ControlDirectionStyle style);
    }

    public AddDirectionStyleDialog(@NonNull Context context, ControlDirectionStyle beforeStyle, boolean isEdit, Callback callback) {
        super(context);
        setContentView(R.layout.dialog_add_direction_style);
        setCancelable(false);
        this.callback = callback;
        this.style = beforeStyle == null ? new ControlDirectionStyle("") : beforeStyle;
        this.isEdit = isEdit;

        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);

        editName = findViewById(R.id.name);
        direction = findViewById(R.id.direction);
        typeSpinner = findViewById(R.id.type);
        ArrayList<ControlDirectionStyle.Type> types = new ArrayList<>();
        types.add(ControlDirectionStyle.Type.BUTTON);
        types.add(ControlDirectionStyle.Type.ROCKER);
        typeSpinner.setDataList(types);
        ArrayList<String> typeString = new ArrayList<>();
        typeString.add(getContext().getString(R.string.style_direction_button));
        typeString.add(getContext().getString(R.string.style_direction_rocker));
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, typeString);
        typeAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        typeSpinner.setAdapter(typeAdapter);

        container = findViewById(R.id.container);

        buttonStyleLayout = (FCLLinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_direction_style_button, null);
        rockerStyleLayout = (FCLLinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_direction_style_rocker, null);

        editName.setText(style.getName());
        style.nameProperty().bind(editName.stringProperty());
        changeDirectionStyle();
        style.addListener(observable -> changeDirectionStyle());
        {
            FCLPreciseSeekBar interval = buttonStyleLayout.findViewById(R.id.interval);

            FCLTextView intervalText = buttonStyleLayout.findViewById(R.id.interval_text);

            intervalText.setOnClickListener(v -> openTextEditDialog(context, interval.progressProperty(), true));

            interval.setProgress(style.getButtonStyle().getInterval());
            style.getButtonStyle().intervalProperty().bindBidirectional(interval.progressProperty());

            intervalText.stringProperty().bind(Bindings.createStringBinding(() -> interval.getProgress() / 10f + " %", interval.progressProperty()));

            FCLTextView buttonStyleText = buttonStyleLayout.findViewById(R.id.button_style_text);
            FCLButton buttonStyleSet = buttonStyleLayout.findViewById(R.id.set_button_style);

            buttonStyle = new ControlButtonStyle("");
            buttonStyleText.setText(buttonStyle.getName());
            buttonStyleText.stringProperty().bind(buttonStyle.nameProperty());
            buttonStyle.setTextSize(style.getButtonStyle().getTextSize());
            buttonStyle.setTextColor(style.getButtonStyle().getTextColor());
            buttonStyle.setStrokeWidth(style.getButtonStyle().getStrokeWidth());
            buttonStyle.setStrokeColor(style.getButtonStyle().getStrokeColor());
            buttonStyle.setCornerRadius(style.getButtonStyle().getCornerRadius());
            buttonStyle.setFillColor(style.getButtonStyle().getFillColor());
            buttonStyle.setTextSizePressed(style.getButtonStyle().getTextSizePressed());
            buttonStyle.setTextColorPressed(style.getButtonStyle().getTextColorPressed());
            buttonStyle.setStrokeWidthPressed(style.getButtonStyle().getStrokeWidthPressed());
            buttonStyle.setStrokeColorPressed(style.getButtonStyle().getStrokeColorPressed());
            buttonStyle.setCornerRadiusPressed(style.getButtonStyle().getCornerRadiusPressed());
            buttonStyle.setFillColorPressed(style.getButtonStyle().getFillColorPressed());
            buttonStyle.setFillColorPressed(style.getButtonStyle().getFillColorPressed());
            this.beforeStyle = buttonStyle.clone();
            style.getButtonStyle().textSizeProperty().bind(buttonStyle.textSizeProperty());
            style.getButtonStyle().textColorProperty().bind(buttonStyle.textColorProperty());
            style.getButtonStyle().strokeWidthProperty().bind(buttonStyle.strokeWidthProperty());
            style.getButtonStyle().strokeColorProperty().bind(buttonStyle.strokeColorProperty());
            style.getButtonStyle().cornerRadiusProperty().bind(buttonStyle.cornerRadiusProperty());
            style.getButtonStyle().fillColorProperty().bind(buttonStyle.fillColorProperty());
            style.getButtonStyle().textSizePressedProperty().bind(buttonStyle.textSizePressedProperty());
            style.getButtonStyle().textColorPressedProperty().bind(buttonStyle.textColorPressedProperty());
            style.getButtonStyle().strokeWidthPressedProperty().bind(buttonStyle.strokeWidthPressedProperty());
            style.getButtonStyle().strokeColorPressedProperty().bind(buttonStyle.strokeColorPressedProperty());
            style.getButtonStyle().cornerRadiusPressedProperty().bind(buttonStyle.cornerRadiusPressedProperty());
            style.getButtonStyle().fillColorPressedProperty().bind(buttonStyle.fillColorPressedProperty());

            buttonStyleSet.setOnClickListener(v -> {
                ButtonStyleDialog dialog = new ButtonStyleDialog(getContext(), true, buttonStyle, style -> {
                    buttonStyle.setName(style.getName());
                    buttonStyle.setTextSize(style.getTextSize());
                    buttonStyle.setTextColor(style.getTextColor());
                    buttonStyle.setStrokeWidth(style.getStrokeWidth());
                    buttonStyle.setStrokeColor(style.getStrokeColor());
                    buttonStyle.setCornerRadius(style.getCornerRadius());
                    buttonStyle.setFillColor(style.getFillColor());
                    buttonStyle.setTextSizePressed(style.getTextSizePressed());
                    buttonStyle.setTextColorPressed(style.getTextColorPressed());
                    buttonStyle.setStrokeWidthPressed(style.getStrokeWidthPressed());
                    buttonStyle.setStrokeColorPressed(style.getStrokeColorPressed());
                    buttonStyle.setCornerRadiusPressed(style.getCornerRadiusPressed());
                    buttonStyle.setFillColorPressed(style.getFillColorPressed());
                    changeDirectionStyle();
                });
                dialog.show();
            });
        }

        {
            FCLPreciseSeekBar rockerSize = rockerStyleLayout.findViewById(R.id.rocker_size);
            FCLPreciseSeekBar bgStrokeWidth = rockerStyleLayout.findViewById(R.id.bg_stroke_width);
            FCLPreciseSeekBar bgCornerRadius = rockerStyleLayout.findViewById(R.id.bg_corner_radius);
            FCLPreciseSeekBar strokeWidth = rockerStyleLayout.findViewById(R.id.stroke_width);
            FCLPreciseSeekBar cornerRadius = rockerStyleLayout.findViewById(R.id.corner_radius);

            FCLTextView rockerSizeText = rockerStyleLayout.findViewById(R.id.rocker_size_text);
            FCLTextView bgStrokeWidthText = rockerStyleLayout.findViewById(R.id.bg_stroke_width_text);
            FCLTextView bgCornerRadiusText = rockerStyleLayout.findViewById(R.id.bg_corner_radius_text);
            FCLTextView strokeWidthText = rockerStyleLayout.findViewById(R.id.stroke_width_text);
            FCLTextView cornerRadiusText = rockerStyleLayout.findViewById(R.id.corner_radius_text);

            rockerSizeText.setOnClickListener(v -> openTextEditDialog(context, rockerSize.progressProperty(), true));
            bgStrokeWidthText.setOnClickListener(v -> openTextEditDialog(context, bgStrokeWidth.progressProperty(), true));
            bgCornerRadiusText.setOnClickListener(v -> openTextEditDialog(context, bgCornerRadius.progressProperty(), true));
            strokeWidthText.setOnClickListener(v -> openTextEditDialog(context, strokeWidth.progressProperty(), true));
            cornerRadiusText.setOnClickListener(v -> openTextEditDialog(context, cornerRadius.progressProperty(), true));

            rockerSize.setProgress(style.getRockerStyle().getRockerSize());
            bgStrokeWidth.setProgress(style.getRockerStyle().getBgStrokeWidth());
            bgCornerRadius.setProgress(style.getRockerStyle().getBgCornerRadius());
            strokeWidth.setProgress(style.getRockerStyle().getRockerStrokeWidth());
            cornerRadius.setProgress(style.getRockerStyle().getRockerCornerRadius());
            style.getRockerStyle().rockerSizeProperty().bindBidirectional(rockerSize.progressProperty());
            style.getRockerStyle().bgStrokeWidthProperty().bindBidirectional(bgStrokeWidth.progressProperty());
            style.getRockerStyle().bgCornerRadiusProperty().bindBidirectional(bgCornerRadius.progressProperty());
            style.getRockerStyle().rockerStrokeWidthProperty().bindBidirectional(strokeWidth.progressProperty());
            style.getRockerStyle().rockerCornerRadiusProperty().bindBidirectional(cornerRadius.progressProperty());

            rockerSizeText.stringProperty().bind(Bindings.createStringBinding(() -> rockerSize.getProgress() / 10f + " %", rockerSize.progressProperty()));
            bgStrokeWidthText.stringProperty().bind(Bindings.createStringBinding(() -> bgStrokeWidth.getProgress() / 10f + " dp", bgStrokeWidth.progressProperty()));
            bgCornerRadiusText.stringProperty().bind(Bindings.createStringBinding(() -> bgCornerRadius.getProgress() / 10f + " %", bgCornerRadius.progressProperty()));
            strokeWidthText.stringProperty().bind(Bindings.createStringBinding(() -> strokeWidth.getProgress() / 10f + " dp", strokeWidth.progressProperty()));
            cornerRadiusText.stringProperty().bind(Bindings.createStringBinding(() -> cornerRadius.getProgress() / 10f + " %", cornerRadius.progressProperty()));

            FCLTextView bgStrokeColorText = rockerStyleLayout.findViewById(R.id.bg_stroke_color_text);
            FCLTextView bgFillColorText = rockerStyleLayout.findViewById(R.id.bg_fill_color_text);
            FCLTextView strokeColorText = rockerStyleLayout.findViewById(R.id.stroke_color_text);
            FCLTextView fillColorText = rockerStyleLayout.findViewById(R.id.fill_color_text);

            View bgStrokeColorView = rockerStyleLayout.findViewById(R.id.bg_stroke_color_view);
            View bgFillColorView = rockerStyleLayout.findViewById(R.id.bg_fill_color_view);
            View strokeColorView = rockerStyleLayout.findViewById(R.id.stroke_color_view);
            View fillColorView = rockerStyleLayout.findViewById(R.id.fill_color_view);

            FCLButton bgStrokeColorSet = rockerStyleLayout.findViewById(R.id.set_bg_stroke_color);
            FCLButton bgFillColorSet = rockerStyleLayout.findViewById(R.id.set_bg_fill_color);
            FCLButton strokeColorSet = rockerStyleLayout.findViewById(R.id.set_stroke_color);
            FCLButton fillColorSet = rockerStyleLayout.findViewById(R.id.set_fill_color);

            bgStrokeColorView.setBackgroundColor(style.getRockerStyle().getBgStrokeColor());
            bgStrokeColorText.setText(getHex(style.getRockerStyle().getBgStrokeColor()));
            bgFillColorView.setBackgroundColor(style.getRockerStyle().getBgFillColor());
            bgFillColorText.setText(getHex(style.getRockerStyle().getBgFillColor()));
            strokeColorView.setBackgroundColor(style.getRockerStyle().getRockerStrokeColor());
            strokeColorText.setText(getHex(style.getRockerStyle().getRockerStrokeColor()));
            fillColorView.setBackgroundColor(style.getRockerStyle().getRockerFillColor());
            fillColorText.setText(getHex(style.getRockerStyle().getRockerFillColor()));
            style.getRockerStyle().bgStrokeColorProperty().addListener(observable -> {
                bgStrokeColorView.setBackgroundColor(style.getRockerStyle().getBgStrokeColor());
                bgStrokeColorText.setText(getHex(style.getRockerStyle().getBgStrokeColor()));
            });
            style.getRockerStyle().bgFillColorProperty().addListener(observable -> {
                bgFillColorView.setBackgroundColor(style.getRockerStyle().getBgFillColor());
                bgFillColorText.setText(getHex(style.getRockerStyle().getBgFillColor()));
            });
            style.getRockerStyle().rockerStrokeColorProperty().addListener(observable -> {
                strokeColorView.setBackgroundColor(style.getRockerStyle().getRockerStrokeColor());
                strokeColorText.setText(getHex(style.getRockerStyle().getRockerStrokeColor()));
            });
            style.getRockerStyle().rockerFillColorProperty().addListener(observable -> {
                fillColorView.setBackgroundColor(style.getRockerStyle().getRockerFillColor());
                fillColorText.setText(getHex(style.getRockerStyle().getRockerFillColor()));
            });

            bgStrokeColorSet.setOnClickListener(v -> {
                FCLColorPickerDialog dialog = new FCLColorPickerDialog(getContext(), style.getRockerStyle().getBgStrokeColor(), new FCLColorPickerDialog.Listener() {
                    @Override
                    public void onColorChanged(int color) {

                    }

                    @Override
                    public void onPositive(int destColor) {
                        style.getRockerStyle().setBgStrokeColor(destColor);
                    }

                    @Override
                    public void onNegative(int initColor) {

                    }
                });
                dialog.show();
            });
            bgFillColorSet.setOnClickListener(v -> {
                FCLColorPickerDialog dialog = new FCLColorPickerDialog(getContext(), style.getRockerStyle().getBgFillColor(), new FCLColorPickerDialog.Listener() {
                    @Override
                    public void onColorChanged(int color) {

                    }

                    @Override
                    public void onPositive(int destColor) {
                        style.getRockerStyle().setBgFillColor(destColor);
                    }

                    @Override
                    public void onNegative(int initColor) {

                    }
                });
                dialog.show();
            });
            strokeColorSet.setOnClickListener(v -> {
                FCLColorPickerDialog dialog = new FCLColorPickerDialog(getContext(), style.getRockerStyle().getRockerStrokeColor(), new FCLColorPickerDialog.Listener() {
                    @Override
                    public void onColorChanged(int color) {

                    }

                    @Override
                    public void onPositive(int destColor) {
                        style.getRockerStyle().setRockerStrokeColor(destColor);
                    }

                    @Override
                    public void onNegative(int initColor) {

                    }
                });
                dialog.show();
            });
            fillColorSet.setOnClickListener(v -> {
                FCLColorPickerDialog dialog = new FCLColorPickerDialog(getContext(), style.getRockerStyle().getRockerFillColor(), new FCLColorPickerDialog.Listener() {
                    @Override
                    public void onColorChanged(int color) {

                    }

                    @Override
                    public void onPositive(int destColor) {
                        style.getRockerStyle().setRockerFillColor(destColor);
                    }

                    @Override
                    public void onNegative(int initColor) {

                    }
                });
                dialog.show();
            });
        }

        container.addView(style.styleTypeProperty().get() == ControlDirectionStyle.Type.BUTTON ? buttonStyleLayout : rockerStyleLayout);
        typeSpinner.setSelection(style.styleTypeProperty().get() == ControlDirectionStyle.Type.BUTTON ? 0 : 1);
        FXUtils.bindSelection(typeSpinner, style.styleTypeProperty());
        style.styleTypeProperty().addListener(observable -> {
            container.removeAllViewsInLayout();
            container.addView(style.styleTypeProperty().get() == ControlDirectionStyle.Type.BUTTON ? buttonStyleLayout : rockerStyleLayout);
        });
    }

    private void changeDirectionStyle() {
        ControlDirectionStyle clone = style.clone();
        direction.getData().setStyle(clone);
        direction.getData().getBaseInfo().setSizeType(BaseInfoData.SizeType.ABSOLUTE);
        direction.getData().getBaseInfo().setAbsoluteWidth(60);
        direction.getData().getBaseInfo().setAbsoluteHeight(60);
    }

    private String getHex(int color) {
        return "#" + String.format("%08X", (color));
    }

    @Override
    public void onClick(View v) {
        if (v == positive) {
            if (!isEdit && DirectionStyles.getStyles().stream().anyMatch(it -> it.getName().equals(style.getName()))) {
                Toast.makeText(getContext(), getContext().getString(R.string.style_warning_exist), Toast.LENGTH_SHORT).show();
            } else if (StringUtils.isBlank(style.getName())) {
                Toast.makeText(getContext(), getContext().getString(R.string.style_warning_name), Toast.LENGTH_SHORT).show();
            } else {
                dismiss();
                callback.onStyleAdd(style);
            }
        }
        if (v == negative) {
            buttonStyle.setTextSize(beforeStyle.getTextSize());
            buttonStyle.setTextColor(beforeStyle.getTextColor());
            buttonStyle.setStrokeWidth(beforeStyle.getStrokeWidth());
            buttonStyle.setStrokeColor(beforeStyle.getStrokeColor());
            buttonStyle.setCornerRadius(beforeStyle.getCornerRadius());
            buttonStyle.setFillColor(beforeStyle.getFillColor());
            buttonStyle.setTextSizePressed(beforeStyle.getTextSizePressed());
            buttonStyle.setTextColorPressed(beforeStyle.getTextColorPressed());
            buttonStyle.setStrokeWidthPressed(beforeStyle.getStrokeWidthPressed());
            buttonStyle.setStrokeColorPressed(beforeStyle.getStrokeColorPressed());
            buttonStyle.setCornerRadiusPressed(beforeStyle.getCornerRadiusPressed());
            buttonStyle.setFillColorPressed(beforeStyle.getFillColorPressed());
            buttonStyle.setFillColorPressed(beforeStyle.getFillColorPressed());
            dismiss();
        }
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
