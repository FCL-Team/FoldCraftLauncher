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
                    String javaDir = FCLPath.RUNTIME_DIR + "/java";
                    if (cmd.contains("clear")) {
                        logWindow.cleanLog();
                        return;
                    } else if (cmd.contains("java21")) {
                        shellUtil.append("cd " + javaDir);
                        if (!new File(javaDir, "jre21").exists() && !new File(javaDir, "jre17_").exists()) {
                            shellUtil.append("cp " + new File(new File(FCLPath.SHARED_COMMON_DIR).getParentFile(), "jre21.zip").getAbsolutePath() + " ./");
                            shellUtil.append("unzip jre21.zip");
                        }
                        shellUtil.append("mv jre17 jre17_");
                        shellUtil.append("mv jre21 jre17");
                        return;
                    } else if (cmd.contains("java17")) {
                        shellUtil.append("cd " + javaDir);
                        shellUtil.append("mv jre17 jre21");
                        shellUtil.append("mv jre17_ jre17");
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
