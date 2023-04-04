package com.tungsten.fcl.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.view.LogWindow;
import com.tungsten.fcl.util.ShellUtil;
import com.tungsten.fclauncher.FCLPath;
import com.tungsten.fcllibrary.component.theme.Theme;

import java.io.File;

public class ShellActivity extends AppCompatActivity {
    private LogWindow logWindow;
    private EditText editText;
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
                String cmd = editText.getText().toString();
                if (cmd.endsWith("\n")) {
                    logWindow.appendLog("->" + cmd);
                    editText.setText("");
                    if (cmd.contains("clear")) {
                        logWindow.cleanLog();
                        return;
                    } else if (cmd.contains("启用隐藏主题")) {

                        return;
                    } else if (cmd.contains("fullscreen")) {
                        Theme theme = Theme.getTheme(ShellActivity.this);
                        if (cmd.contains("true")) {
                            theme.setFullscreen(true);
                            Theme.saveTheme(ShellActivity.this, theme);
                            logWindow.appendLog("fullscreen=true\n");
                        } else if (cmd.contains("false")) {
                            theme.setFullscreen(false);
                            Theme.saveTheme(ShellActivity.this, theme);
                            logWindow.appendLog("fullscreen=false\n");
                        }
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
