package com.tungsten.fcl.control;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.QuickInputTexts;
import com.tungsten.fclauncher.keycodes.FCLKeycodes;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;

public class QuickInputDialog extends FCLDialog implements View.OnClickListener {

    private final GameMenu menu;

    private FCLButton addText;
    private FCLButton positive;

    private ListView listView;

    public QuickInputDialog(@NonNull Context context, GameMenu menu) {
        super(context);
        this.menu = menu;
        setCancelable(false);
        setContentView(R.layout.dialog_quick_input);

        addText = findViewById(R.id.add_text);
        positive = findViewById(R.id.positive);
        addText.setOnClickListener(this);
        positive.setOnClickListener(this);

        listView = findViewById(R.id.list);
        refreshList(menu);
    }

    private void refreshList(GameMenu menu) {
        InputTextAdapter adapter = new InputTextAdapter(getContext(), QuickInputTexts.getInputTexts(), string -> {
            if (StringUtils.isNotBlank(string)) {
                if (menu.getCursorMode() == FCLBridge.CursorEnabled) {
                    for (int i = 0; i < string.length(); i++) {
                        menu.getInput().sendChar(string.charAt(i));
                    }
                } else {
                    menu.getInput().sendKeyEvent(FCLKeycodes.KEY_T, true);
                    menu.getInput().sendKeyEvent(FCLKeycodes.KEY_T, false);
                    new Handler().postDelayed(() -> {
                        for (int i = 0; i < string.length(); i++) {
                            menu.getInput().sendChar(string.charAt(i));
                        }
                        menu.getInput().sendKeyEvent(FCLKeycodes.KEY_ENTER, true);
                        menu.getInput().sendKeyEvent(FCLKeycodes.KEY_ENTER, false);
                    }, 50);
                }
            }
            dismiss();
        });
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == addText) {
            AddInputTextDialog dialog = new AddInputTextDialog(getContext(), () -> refreshList(menu));
            dialog.show();
        }
        if (v == positive) {
            dismiss();
        }
    }
}
