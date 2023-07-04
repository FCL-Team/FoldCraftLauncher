package com.tungsten.fcl.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Controller;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.view.FCLRadioButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;

public class SelectableControllerListAdapter extends FCLAdapter {

    private final ObservableList<Controller> list;
    private final SelectControllerDialog dialog;

    public SelectableControllerListAdapter(Context context, ObservableList<Controller> list, SelectControllerDialog dialog) {
        super(context);
        this.list = list;
        this.dialog = dialog;
    }

    static class ViewHolder {
        FCLRadioButton radioButton;
        FCLTextView name;
        FCLTextView version;
        FCLTextView description;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_controller_selectable, null);
            viewHolder.radioButton = view.findViewById(R.id.radio);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.version = view.findViewById(R.id.version);
            viewHolder.description = view.findViewById(R.id.description);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Controller controller = list.get(i);
        viewHolder.radioButton.setChecked(controller == dialog.getSelectedController());
        viewHolder.name.stringProperty().bind(controller.nameProperty());
        viewHolder.version.stringProperty().bind(controller.versionProperty());
        viewHolder.description.stringProperty().bind(controller.descriptionProperty());
        viewHolder.radioButton.setOnClickListener(view1 -> {
            dialog.setSelectedController(controller);
            notifyDataSetChanged();
        });
        return view;
    }
}
