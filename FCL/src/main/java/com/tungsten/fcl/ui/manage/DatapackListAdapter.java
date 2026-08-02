package com.tungsten.fcl.ui.manage;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tungsten.fcl.R;
import com.tungsten.fcl.util.FlowList;
import com.tungsten.fclcore.mod.Datapack;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLCheckBox;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;

public class DatapackListAdapter extends FCLAdapter {

    private final FlowList<DatapackListPage.DatapackInfoObject> listProperty = new FlowList<>();
    private final FlowList<DatapackListPage.DatapackInfoObject> selectedItemsProperty = new FlowList<>();

    public FlowList<DatapackListPage.DatapackInfoObject> listProperty() {
        return listProperty;
    }

    public FlowList<DatapackListPage.DatapackInfoObject> selectedItemsProperty() {
        return selectedItemsProperty;
    }

    private boolean fromSelf = false;

    public DatapackListAdapter(Context context) {
        super(context);

        Handler handler = new Handler();

        FlowSubscriptions.subscribe(this.listProperty.flow(), v -> {
            fromSelf = true;
            selectedItemsProperty.clear();
            fromSelf = false;
            handler.post(this::notifyDataSetChanged);
        });
        FlowSubscriptions.subscribe(selectedItemsProperty.flow(), v -> {
            if (!fromSelf) {
                handler.post(this::notifyDataSetChanged);
            }
        });
    }

    private static class ViewHolder {
        FCLLinearLayout parent;
        FCLCheckBox checkBox;
        FCLTextView name;
        FCLTextView description;
        FlowSubscriptions.Subscription activeSubscription;
        FlowSubscriptions.Subscription checkSubscription;
    }

    @Override
    public int getCount() {
        return listProperty.size();
    }

    @Override
    public Object getItem(int i) {
        return listProperty.get().get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_datapack, null);
            viewHolder.parent = view.findViewById(R.id.parent);
            viewHolder.checkBox = view.findViewById(R.id.check);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.description = view.findViewById(R.id.description);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        DatapackListPage.DatapackInfoObject datapackInfoObject = listProperty.get().get(i);
        viewHolder.parent.setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[] { selectedItemsProperty.contains(datapackInfoObject) ? ThemeEngine.getInstance().getTheme().getColor() : ThemeEngine.getInstance().getTheme().getLtColor() }));
        ThemeEngine.getInstance().registerEvent(viewHolder.parent, () -> viewHolder.parent.setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[] { selectedItemsProperty.contains(datapackInfoObject) ? ThemeEngine.getInstance().getTheme().getColor() : ThemeEngine.getInstance().getTheme().getLtColor() })));
        viewHolder.parent.setOnClickListener(v -> {
            if (selectedItemsProperty.contains(datapackInfoObject)) {
                fromSelf = true;
                selectedItemsProperty.remove(datapackInfoObject);
                fromSelf = false;
                viewHolder.parent.setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[] { ThemeEngine.getInstance().getTheme().getLtColor() }));
            } else {
                fromSelf = true;
                selectedItemsProperty.add(datapackInfoObject);
                fromSelf = false;
                viewHolder.parent.setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[] { ThemeEngine.getInstance().getTheme().getColor() }));
            }
        });
        viewHolder.checkBox.addCheckedChangeListener();
        if (viewHolder.activeSubscription != null) {
            viewHolder.activeSubscription.cancel();
            viewHolder.activeSubscription = null;
        }
        if (viewHolder.checkSubscription != null) {
            viewHolder.checkSubscription.cancel();
            viewHolder.checkSubscription = null;
        }
        // 手动双向（对齐原 bindBidirectional，两侧同值写入均为 no-op，天然防回环）：
        // pack → checkBox 走 activeFlow 订阅；checkBox → pack 走 checkFlow 订阅。
        Datapack.Pack pack = datapackInfoObject.getPackInfo();
        viewHolder.checkBox.checkFlow().setValue(pack.isActive());
        viewHolder.activeSubscription = FlowSubscriptions.subscribe(pack.activeFlow(), viewHolder.checkBox.checkFlow()::setValue);
        viewHolder.checkSubscription = FlowSubscriptions.subscribe(viewHolder.checkBox.checkFlow(), pack::setActive);
        viewHolder.name.setText(datapackInfoObject.getTitle());
        viewHolder.description.setText(datapackInfoObject.getSubtitle());
        return view;
    }
}
