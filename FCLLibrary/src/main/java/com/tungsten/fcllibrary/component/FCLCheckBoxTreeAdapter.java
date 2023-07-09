package com.tungsten.fcllibrary.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.view.FCLCheckBox;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;

public class FCLCheckBoxTreeAdapter<T> extends FCLAdapter {

    private final ObservableList<FCLCheckBoxTreeItem<T>> list;

    private final SimpleBooleanProperty checkHeightProperty = new SimpleBooleanProperty(false);

    public SimpleBooleanProperty checkHeightProperty() {
        return checkHeightProperty;
    }

    public void setCheckHeight(boolean checkHeight) {
        checkHeightProperty.set(checkHeight);
    }

    public boolean isCheckHeight() {
        return checkHeightProperty.get();
    }

    public FCLCheckBoxTreeAdapter(Context context, ObservableList<FCLCheckBoxTreeItem<T>> list) {
        super(context);
        this.list = list;
    }

    private static class ViewHolder {
        FCLLinearLayout main;
        FCLImageButton switchView;
        FCLCheckBox checkBox;
        FCLTextView textView;
        FCLTextView comment;
        ListView listView;
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
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_check_box_tree, null);
            viewHolder.main = view.findViewById(R.id.main);
            viewHolder.switchView = view.findViewById(R.id.switch_view);
            viewHolder.checkBox = view.findViewById(R.id.check);
            viewHolder.textView = view.findViewById(R.id.text);
            viewHolder.comment = view.findViewById(R.id.comment);
            viewHolder.listView = view.findViewById(R.id.list);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        FCLCheckBoxTreeItem<T> item = list.get(i);
        viewHolder.switchView.setVisibility(item.getSubItem().size() == 0 ? View.INVISIBLE : View.VISIBLE);
        viewHolder.switchView.imageProperty().bind(Bindings.createObjectBinding(() -> item.isExpanded() ? getContext().getDrawable(R.drawable.ic_baseline_arrow_drop_down_24) : getContext().getDrawable(R.drawable.ic_baseline_arrow_right_24), item.expandedProperty()));
        viewHolder.switchView.setOnClickListener(v -> item.setExpanded(!item.isExpanded()));
        viewHolder.checkBox.addCheckedChangeListener();
        viewHolder.checkBox.checkProperty().bindBidirectional(item.selectedProperty());
        viewHolder.checkBox.indeterminateProperty().bindBidirectional(item.indeterminateProperty());
        viewHolder.textView.setText(item.getText());
        viewHolder.comment.setVisibility(item.getComment() == null ? View.GONE : View.VISIBLE);
        if (item.getComment() != null)
            viewHolder.comment.setText(item.getComment());
        viewHolder.listView.setVisibility((item.isExpanded() && item.getSubItem().size() > 0) ? View.VISIBLE : View.GONE);
        item.expandedProperty().addListener(observable -> viewHolder.listView.setVisibility((item.isExpanded() && item.getSubItem().size() > 0) ? View.VISIBLE : View.GONE));
        if (item.getSubItem().size() > 0) {
            viewHolder.main.post(() -> {
                viewHolder.listView.setAdapter(new FCLCheckBoxTreeAdapter<>(getContext(), item.getSubItem()));
                ViewGroup.LayoutParams layoutParams = viewHolder.listView.getLayoutParams();
                new Thread(() -> {
                    layoutParams.height = getListViewHeight(item, viewHolder.listView.getDividerHeight(), viewHolder.main.getMeasuredHeight());
                    Schedulers.androidUIThread().execute(() -> viewHolder.listView.setLayoutParams(layoutParams));
                }).start();
                item.expandedProperty().addListener(observable -> setCheckHeight(true));
                ((FCLCheckBoxTreeAdapter<?>) viewHolder.listView.getAdapter()).checkHeightProperty().addListener(observable -> {
                    if (((FCLCheckBoxTreeAdapter<?>) viewHolder.listView.getAdapter()).isCheckHeight()) {
                        ViewGroup.LayoutParams lp = viewHolder.listView.getLayoutParams();
                        new Thread(() -> {
                            lp.height = getListViewHeight(item, viewHolder.listView.getDividerHeight(), viewHolder.main.getMeasuredHeight());
                            Schedulers.androidUIThread().execute(() -> viewHolder.listView.setLayoutParams(lp));
                        }).start();
                        ((FCLCheckBoxTreeAdapter<?>) viewHolder.listView.getAdapter()).setCheckHeight(false);
                        setCheckHeight(true);
                    }
                });
            });
        }
        return view;
    }

    public static int getListViewHeight(FCLCheckBoxTreeItem<?> item, int splitSize, int baseHeight) {
        int count = getSubItemCount(item);
        return (baseHeight * count) + (splitSize * (count - 1));
    }

    public static int getSubItemCount(FCLCheckBoxTreeItem<?> item) {
        int count = item.isExpanded() ? item.getSubItem().size() : 0;
        if (item.isExpanded()) {
            for (FCLCheckBoxTreeItem<?> subItem : item.getSubItem()) {
                count += getSubItemCount(subItem);
            }
        }
        return count;
    }
}
