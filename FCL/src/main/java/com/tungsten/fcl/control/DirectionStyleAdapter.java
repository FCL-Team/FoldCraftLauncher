package com.tungsten.fcl.control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.BaseInfoData;
import com.tungsten.fcl.control.data.ControlDirectionStyle;
import com.tungsten.fcl.control.data.DirectionStyles;
import com.tungsten.fcl.control.view.ControlDirection;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLRadioButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;

public class DirectionStyleAdapter extends FCLAdapter {

    private final ObservableList<ControlDirectionStyle> list;
    private final boolean select;

    private final ObjectProperty<ControlDirectionStyle> selectedStyle = new SimpleObjectProperty<>(this, "style", null);

    public ObjectProperty<ControlDirectionStyle> selectedStyleProperty() {
        return selectedStyle;
    }

    public void setSelectedStyle(ControlDirectionStyle style) {
        selectedStyle.set(style);
    }

    public ControlDirectionStyle getSelectedStyle() {
        return selectedStyle.get();
    }

    public DirectionStyleAdapter(Context context, ObservableList<ControlDirectionStyle> list, boolean select, ControlDirectionStyle initStyle) {
        super(context);
        this.list = list;
        this.select = select;

        if (DirectionStyles.getStyles().stream().anyMatch(it -> it == initStyle)) {
            selectedStyle.set(initStyle);
        } else  {
            selectedStyle.set(list.get(0));
        }
    }

    static class ViewHolder {
        ControlDirection direction;
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
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_direction_style, null);
            viewHolder.direction = view.findViewById(R.id.direction);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.radioButton = view.findViewById(R.id.radio_button);
            viewHolder.delete = view.findViewById(R.id.delete);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ControlDirectionStyle style = list.get(i);
        viewHolder.direction.getData().setStyle(style);
        viewHolder.direction.getData().getBaseInfo().setSizeType(BaseInfoData.SizeType.ABSOLUTE);
        viewHolder.direction.getData().getBaseInfo().setAbsoluteWidth(60);
        viewHolder.direction.getData().getBaseInfo().setAbsoluteHeight(60);
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
                DirectionStyles.removeStyles(style);
                DirectionStyles.checkStyles();
                notifyDataSetChanged();
            });
            builder.setNegativeButton(null);
            builder.create().show();
        });
        return view;
    }
}
