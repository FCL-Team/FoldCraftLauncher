package com.tungsten.fcl.ui.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.tungsten.fcl.R;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.util.List;

public class DocCategoryAdapter extends FCLAdapter {

    private final List<DocIndex> list;

    private final ObjectProperty<DocIndex> selectedIndexProperty = new SimpleObjectProperty<>(null);

    public ObjectProperty<DocIndex> selectedIndexProperty() {
        return selectedIndexProperty;
    }

    public DocIndex getSelectedIndex() {
        return selectedIndexProperty.get();
    }

    public DocCategoryAdapter(Context context, List<DocIndex> list) {
        super(context);
        this.list = list;

        if (!list.isEmpty()) {
            selectedIndexProperty.set(list.get(0));
        }
        selectedIndexProperty.addListener(invalidate -> notifyDataSetChanged());
    }

    private static class ViewHolder {
        FCLLinearLayout parent;
        FCLTextView name;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = new FCLLinearLayout(getContext());
            viewHolder.parent = new FCLLinearLayout(getContext());
            viewHolder.name = new FCLTextView(getContext());
            ((FCLLinearLayout) view).addView(viewHolder.parent, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int padding = ConvertUtils.dip2px(getContext(), 10);
            viewHolder.parent.setPadding(padding, padding, padding, padding);
            viewHolder.parent.setBackground(getContext().getDrawable(R.drawable.bg_container_transparent_clickable));
            viewHolder.parent.addView(viewHolder.name, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            viewHolder.name.setSingleLine(true);
            viewHolder.name.setUseThemeColor(true);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        DocIndex index = list.get(i);
        viewHolder.parent.setBackground(index == getSelectedIndex() ? getContext().getDrawable(R.drawable.bg_container_transparent_selected) : getContext().getDrawable(R.drawable.bg_container_transparent_clickable));
        viewHolder.parent.setOnClickListener(v -> selectedIndexProperty.set(index));
        viewHolder.name.setText(index.getDisplayName(getContext()));
        return view;
    }
}
