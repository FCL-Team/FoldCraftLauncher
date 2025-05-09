package com.tungsten.fcl.ui.account;

import static com.tungsten.fcl.setting.ConfigHolder.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;

public class ServerListAdapter extends FCLAdapter {

    private final ObservableList<AuthlibInjectorServer> list;

    public ServerListAdapter(Context context) {
        super(context);
        list = config().getAuthlibInjectorServers();

        list.addListener((InvalidationListener) i -> Schedulers.androidUIThread().execute(this::notifyDataSetChanged));
    }

    static class ViewHolder {
        ConstraintLayout parent;
        FCLTextView name;
        FCLTextView url;
        FCLImageButton delete;
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
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_authlib_injector_server, null);
            viewHolder.parent = view.findViewById(R.id.parent);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.url = view.findViewById(R.id.url);
            viewHolder.delete = view.findViewById(R.id.delete);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        AuthlibInjectorServer server = list.get(i);
        viewHolder.name.setText(server.getName());
        viewHolder.url.setText(server.getUrl());
        viewHolder.url.setSelected(true);
        viewHolder.parent.setOnClickListener(v -> {
            CreateAccountDialog dialog = new CreateAccountDialog(getContext(), server);
            dialog.show();
        });
        viewHolder.delete.setOnClickListener(v -> {
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
            builder.setMessage(String.format(getContext().getString(R.string.version_manage_remove_confirm), server.getName()));
            builder.setPositiveButton(() -> config().getAuthlibInjectorServers().remove(server));
            builder.setNegativeButton(null);
            builder.create().show();
        });
        return view;
    }
}
