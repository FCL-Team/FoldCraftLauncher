package com.tungsten.fcl.control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.ButtonStyles;
import com.tungsten.fcl.control.data.ControlButtonStyle;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLRadioButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

public class ButtonStyleAdapter extends FCLAdapter {

    private final ObservableList<ControlButtonStyle> list;
    private final boolean select;

    private final ObjectProperty<ControlButtonStyle> selectedStyle = new SimpleObjectProperty<>(this, "style", null);

    public ObjectProperty<ControlButtonStyle> selectedStyleProperty() {
        return selectedStyle;
    }

    public void setSelectedStyle(ControlButtonStyle style) {
        selectedStyle.set(style);
    }

    public ControlButtonStyle getSelectedStyle() {
        return selectedStyle.get();
    }

    public ButtonStyleAdapter(Context context, ObservableList<ControlButtonStyle> list, boolean select, ControlButtonStyle initStyle) {
        super(context);
        this.list = list;
        this.select = select;

        if (ButtonStyles.getStyles().stream().anyMatch(it -> it == initStyle)) {
            selectedStyle.set(initStyle);
        } else  {
            selectedStyle.set(list.get(0));
        }
    }

    static class ViewHolder {
        AppCompatButton button;
        FCLTextView name;
        FCLRadioButton radioButton;
        FCLImageButton delete;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_button_style, null);
            viewHolder.button = view.findViewById(R.id.button);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.radioButton = view.findViewById(R.id.radio_button);
            viewHolder.delete = view.findViewById(R.id.delete);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ControlButtonStyle style = list.get(i);
        GradientDrawable drawableNormal = new GradientDrawable();
        drawableNormal.setCornerRadius(ConvertUtils.dip2px(getContext(), style.getCornerRadius() / 10f));
        drawableNormal.setStroke(ConvertUtils.dip2px(getContext(), style.getStrokeWidth() / 10f), style.getStrokeColor());
        drawableNormal.setColor(style.getFillColor());
        GradientDrawable drawablePressed = new GradientDrawable();
        drawablePressed.setCornerRadius(ConvertUtils.dip2px(getContext(), style.getCornerRadiusPressed() / 10f));
        drawablePressed.setStroke(ConvertUtils.dip2px(getContext(), style.getStrokeWidthPressed() / 10f), style.getStrokeColorPressed());
        drawablePressed.setColor(style.getFillColorPressed());
        viewHolder.button.setGravity(Gravity.CENTER);
        viewHolder.button.setPadding(0, 0, 0, 0);
        viewHolder.button.setText("S");
        viewHolder.button.setAllCaps(false);
        viewHolder.button.setTextSize(style.getTextSize());
        viewHolder.button.setTextColor(style.getTextColor());
        viewHolder.button.setBackground(drawableNormal);
        viewHolder.button.setOnTouchListener((view1, motionEvent) -> {
            if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                ((AppCompatButton) view1).setTextSize(style.getTextSizePressed());
                ((AppCompatButton) view1).setTextColor(style.getTextColorPressed());
                ((AppCompatButton) view1).setBackground(drawablePressed);
            }
            if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP || motionEvent.getActionMasked() == MotionEvent.ACTION_CANCEL) {
                ((AppCompatButton) view1).setTextSize(style.getTextSize());
                ((AppCompatButton) view1).setTextColor(style.getTextColor());
                ((AppCompatButton) view1).setBackground(drawableNormal);
            }
            return true;
        });
        viewHolder.name.setText(style.getName());
        if (select) {
            viewHolder.radioButton.setVisibility(View.VISIBLE);
            viewHolder.delete.setVisibility(View.GONE);
        } else {
            viewHolder.radioButton.setVisibility(View.GONE);
            viewHolder.delete.setVisibility(View.VISIBLE);
        }
        viewHolder.radioButton.checkProperty().unbind();
        viewHolder.radioButton.checkProperty().bind(Bindings.createBooleanBinding(() -> selectedStyle.get() == style, selectedStyle));
        viewHolder.radioButton.setOnClickListener(view1 -> selectedStyle.set(style));
        viewHolder.delete.setOnClickListener(view1 -> {
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
            builder.setCancelable(false);
            builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
            builder.setMessage(getContext().getString(R.string.style_warning_delete));
            builder.setPositiveButton(() -> {
                ButtonStyles.removeStyles(style);
                ButtonStyles.checkStyles();
                notifyDataSetChanged();
            });
            builder.setNegativeButton(null);
            builder.create().show();
        });
        return view;
    }
}
