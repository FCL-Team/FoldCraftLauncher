package com.tungsten.fcl.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.view.LogWindow;
import com.tungsten.fcl.util.ShellUtil;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.theme.Theme;
import com.tungsten.fcllibrary.component.view.FCLEditText;

import java.io.File;
import java.util.Objects;

public class ShellActivity extends FCLActivity {

    private LogWindow logWindow;
    private FCLEditText editText;
    private ShellUtil shellUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shell);
        logWindow = findViewById(R.id.shell_log_window);
        editText = findViewById(R.id.shell_input);
        logWindow.appendLog("Welcome to use Fold Craft Launcher!\n");
        logWindow.appendLog("Here is the shell command line!\n");
        shellUtil = new ShellUtil(new File(FCLPath.FILES_DIR).getParent(), output -> logWindow.appendLog("\t" + output + "\n"));
        shellUtil.start();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String cmd = Objects.requireNonNull(editText.getText()).toString();
                if (cmd.endsWith("\n")) {
                    logWindow.appendLog("->" + cmd);
                    editText.setText("");
                    if (cmd.contains("clear")) {
                        logWindow.cleanLog();
                        return;
                    }
                    shellUtil.append(cmd);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shellUtil.interrupt();
    }
}
