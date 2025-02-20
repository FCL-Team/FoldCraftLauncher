package com.tungsten.fcl.control;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.util.UUID;

public class ViewGroupDialog extends FCLDialog implements View.OnClickListener {

    private final GameMenu gameMenu;
    private final boolean select;
    private final ObservableList<ControlViewGroup> selectedGroups;
    @Nullable
    private final Callback callback;

    private FCLButton addViewGroup;
    private FCLButton positive;

    private ListView listView;

    public interface Callback {
        void onSelect(ObservableList<ControlViewGroup> viewGroup);
    }

    public ViewGroupDialog(@NonNull Context context, GameMenu gameMenu, boolean select, ObservableList<ControlViewGroup> selectedGroups, @Nullable Callback callback) {
        super(context);
        this.gameMenu = gameMenu;
        this.select = select;
        this.selectedGroups = selectedGroups;
        this.callback = callback;
        setCancelable(false);
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            dialogWindow.setLayout(ConvertUtils.dip2px(context,400), ViewGroup.LayoutParams.MATCH_PARENT);
        }
        setContentView(R.layout.dialog_manage_view_groups);

        addViewGroup = findViewById(R.id.add_view_group);
        positive = findViewById(R.id.positive);
        addViewGroup.setOnClickListener(this);
        positive.setOnClickListener(this);

        addViewGroup.setVisibility(select ? View.GONE : View.VISIBLE);

        listView = findViewById(R.id.list);
        refreshList(select);
    }

    private ViewGroupAdapter adapter;

    private void refreshList(boolean select) {
        adapter = new ViewGroupAdapter(getContext(), gameMenu.getController().viewGroups(), gameMenu, select, selectedGroups);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == addViewGroup) {
            EditViewGroupDialog dialog = new EditViewGroupDialog(getContext(), gameMenu, new ControlViewGroup(UUID.randomUUID().toString()), (name, visibility) -> {
                ControlViewGroup viewGroup = new ControlViewGroup(UUID.randomUUID().toString());
                viewGroup.setName(name);
                viewGroup.setVisibility(visibility);
                gameMenu.getController().addViewGroup(viewGroup);
                refreshList(select);
            });
            dialog.show();
        }
        if (v == positive) {
            if (callback != null) {
                callback.onSelect(adapter.getSelectedGroups());
            }
            dismiss();
        }
    }
}
