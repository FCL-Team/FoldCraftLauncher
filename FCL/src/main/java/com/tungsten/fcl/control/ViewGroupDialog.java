package com.tungsten.fcl.control;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;

import java.util.UUID;

public class ViewGroupDialog extends FCLDialog implements View.OnClickListener {

    private final GameMenu gameMenu;

    private FCLButton addViewGroup;
    private FCLButton positive;

    private ListView listView;

    public ViewGroupDialog(@NonNull Context context, GameMenu gameMenu) {
        super(context);
        this.gameMenu = gameMenu;
        setCancelable(false);
        setContentView(R.layout.dialog_manage_view_groups);

        addViewGroup = findViewById(R.id.add_view_group);
        positive = findViewById(R.id.positive);
        addViewGroup.setOnClickListener(this);
        positive.setOnClickListener(this);

        listView = findViewById(R.id.list);
        refreshList();
    }

    private void refreshList() {
        ViewGroupAdapter adapter = new ViewGroupAdapter(getContext(), gameMenu.getController().viewGroups(), gameMenu);
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
                refreshList();
            });
            dialog.show();
        }
        if (v == positive) {
            dismiss();
        }
    }
}
