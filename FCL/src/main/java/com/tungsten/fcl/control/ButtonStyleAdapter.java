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
import com.tungsten.fcl.control.data.ControlButtonData;
import com.tungsten.fcl.control.data.ControlButtonStyle;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fcl.setting.Controller;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLRadioButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ButtonStyleAdapter extends FCLAdapter {

    /**
     * 样式列表条目：sourceName 为 null 表示本布局样式；否则为来源布局名（跨布局导入展示，只可选不可改删）。
     */
    public static class Entry {
        public final ControlButtonStyle style;
        public final String sourceName;

        public Entry(ControlButtonStyle style, String sourceName) {
            this.style = style;
            this.sourceName = sourceName;
        }
    }

    /**
     * 来源筛选值：null = 全部；{@link #FILTER_LOCAL} = 仅本布局样式；其他值 = 对应来源布局名。
     */
    public static final String FILTER_LOCAL = "";

    private final Controller controller;
    private final List<Entry> allEntries;
    private final List<Entry> entries = new ArrayList<>();
    private final boolean select;
    private final Runnable onStyleRemoved;
    private final Consumer<ControlButtonStyle> onStyleEdit;
    private String filter;

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

    public ButtonStyleAdapter.Entry getSelectedEntry() {
        for (ButtonStyleAdapter.Entry entry : allEntries) {
            if (entry.style == selectedStyle.get()) {
                return entry;
            }
        }
        return null;
    }

    public List<Entry> getEntries() {
        return allEntries;
    }

    public ButtonStyleAdapter(Context context, Controller controller, List<Entry> entries, boolean select, ControlButtonStyle initStyle, Runnable onStyleRemoved, Consumer<ControlButtonStyle> onStyleEdit) {
        super(context);
        this.controller = controller;
        this.allEntries = entries;
        this.select = select;
        this.onStyleRemoved = onStyleRemoved;
        this.onStyleEdit = onStyleEdit;
        this.entries.addAll(entries);

        ControlButtonStyle initial = null;
        if (initStyle != null) {
            for (Entry entry : entries) {
                if (entry.sourceName == null && entry.style.getName().equals(initStyle.getName())) {
                    initial = entry.style;
                    break;
                }
            }
        }
        if (initial == null) {
            for (Entry entry : entries) {
                if (entry.sourceName == null) {
                    initial = entry.style;
                    break;
                }
            }
        }
        if (initial == null && !entries.isEmpty()) {
            initial = entries.get(0).style;
        }
        selectedStyle.set(initial);
    }

    /**
     * 按来源筛选（null = 全部），立即刷新列表；选中项不受筛选影响。
     */
    public void setFilter(String filter) {
        this.filter = filter;
        entries.clear();
        for (Entry entry : allEntries) {
            if (filter == null
                    || (FILTER_LOCAL.equals(filter) && entry.sourceName == null)
                    || (filter.equals(entry.sourceName))) {
                entries.add(entry);
            }
        }
        notifyDataSetChanged();
    }

    public int indexOfLocal(String name) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).sourceName == null && entries.get(i).style.getName().equals(name)) {
                return i;
            }
        }
        return 0;
    }

    static class ViewHolder {
        AppCompatButton button;
        FCLTextView name;
        FCLRadioButton radioButton;
        FCLImageButton edit;
        FCLImageButton delete;
    }

    @Override
    public Object getItem(int i) {
        return entries.get(i).style;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    private int countReferences(String name) {
        int count = 0;
        for (ControlViewGroup group : controller.viewGroups()) {
            for (ControlButtonData data : group.getViewData().buttonList()) {
                if (data.getStyle().getName().equals(name)) {
                    count++;
                }
            }
        }
        return count;
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
            viewHolder.edit = view.findViewById(R.id.edit);
            viewHolder.delete = view.findViewById(R.id.delete);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ButtonStyleAdapter.Entry entry = entries.get(i);
        ControlButtonStyle style = entry.style;
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
        if (entry.sourceName == null) {
            viewHolder.name.setText(style.getName());
        } else {
            // 其他布局的样式：标注来源布局，选中确认时导入复制进当前布局
            viewHolder.name.setText(getContext().getString(R.string.style_name_from, style.getName(), entry.sourceName));
        }
        viewHolder.name.setSelected(true);
        if (select) {
            viewHolder.radioButton.setVisibility(View.VISIBLE);
        } else {
            viewHolder.radioButton.setVisibility(View.GONE);
        }
        viewHolder.radioButton.checkProperty().unbind();
        viewHolder.radioButton.checkProperty().bind(Bindings.createBooleanBinding(() -> selectedStyle.get() == style, selectedStyle));
        viewHolder.radioButton.setOnClickListener(view1 -> selectedStyle.set(style));
        if (entry.sourceName == null) {
            // 本布局样式：条目上直接编辑；其他布局的样式归其布局管理，此处只可导入
            viewHolder.edit.setVisibility(View.VISIBLE);
            viewHolder.edit.setOnClickListener(view1 -> {
                if (onStyleEdit != null) {
                    onStyleEdit.accept(style);
                }
            });
        } else {
            viewHolder.edit.setVisibility(View.GONE);
            viewHolder.edit.setOnClickListener(null);
        }
        if (entry.sourceName == null) {
            viewHolder.delete.setVisibility(View.VISIBLE);
            viewHolder.delete.setOnClickListener(view1 -> {
                controller.ensureAllLoaded();
                int references = countReferences(style.getName());
                String message = references > 0
                        ? getContext().getString(R.string.style_warning_delete_refs, references)
                        : getContext().getString(R.string.style_warning_delete_unused);
                FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                builder.setCancelable(false);
                builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
                builder.setMessage(message);
                builder.setPositiveButton(() -> {
                    controller.removeButtonStyle(style);
                    controller.saveToDisk();
                    if (onStyleRemoved != null) {
                        onStyleRemoved.run();
                    }
                });
                builder.setNegativeButton(null);
                builder.create().show();
            });
        } else {
            viewHolder.delete.setVisibility(View.GONE);
            viewHolder.delete.setOnClickListener(null);
        }
        return view;
    }
}
