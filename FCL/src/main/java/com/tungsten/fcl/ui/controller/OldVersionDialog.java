package com.tungsten.fcl.ui.controller;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.download.ControllerVersion;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;

import java.util.ArrayList;

public class OldVersionDialog extends FCLDialog implements View.OnClickListener {

    private ListView listView;
    private FCLButton cancel;

    public OldVersionDialog(@NonNull Context context, ArrayList<ControllerVersion.VersionInfo> versionInfos, Callback callback) {
        super(context);
        setCancelable(false);
        setContentView(R.layout.dialog_download_controllor);

        listView = findViewById(R.id.list);
        HistoricalListAdapter adapter = new HistoricalListAdapter(getContext(), versionInfos, versionInfo -> {
            callback.download(versionInfo.getVersionCode());
            dismiss();
        });
        listView.setAdapter(adapter);

        cancel = findViewById(R.id.negative);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == cancel) {
            dismiss();
        }
    }

    public interface Callback {
        void download(int versionCode);
    }
}
