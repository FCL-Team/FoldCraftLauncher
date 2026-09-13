package com.tungsten.fcl.ui.account;

import static com.tungsten.fclcore.util.Logging.LOG;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fclcore.auth.AuthInfo;
import com.tungsten.fclcore.auth.ClassicAccount;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * 外置账户（authlib-injector / Yggdrasil）凭据过期时的重新登录对话框：
 * 展示账号名并让用户重新输入密码，通过 {@link ClassicAccount#logInWithPassword(String)} 刷新凭据。
 */
public class ClassicAccountLoginDialog extends FCLDialog implements View.OnClickListener {

    private final FCLButton positive;
    private final FCLButton negative;
    private final FCLTextView username;
    private final FCLEditText password;

    /** 登录进度行（include view_login_progress） */
    private final View loginProgress;
    private final FCLTextView progressText;

    private final ClassicAccount oldAccount;
    private final Consumer<AuthInfo> success;
    private final Runnable failed;

    public ClassicAccountLoginDialog(@NonNull Context context, ClassicAccount oldAccount, Consumer<AuthInfo> success, Runnable failed) {
        super(context);
        this.oldAccount = oldAccount;
        this.success = success;
        this.failed = failed;

        setContentView(R.layout.dialog_relogin_classic);
        setCancelable(false);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        positive = findViewById(R.id.login);
        negative = findViewById(R.id.cancel);
        loginProgress = findViewById(R.id.login_progress);
        progressText = findViewById(R.id.progress_text);

        username.setText(oldAccount.getUsername());

        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == positive) {
            String passwordText = password.getText() == null ? "" : password.getText().toString();
            if (StringUtils.isBlank(passwordText)) {
                Toast.makeText(getContext(), R.string.account_create_alert, Toast.LENGTH_SHORT).show();
                return;
            }
            positive.setEnabled(false);
            negative.setEnabled(false);
            loginProgress.setVisibility(View.VISIBLE);
            progressText.setText(R.string.launch_state_logging_in);
            Task.supplyAsync(() -> oldAccount.logInWithPassword(passwordText))
                    .whenComplete(Schedulers.androidUIThread(), (authInfo, exception) -> {
                        loginProgress.setVisibility(View.GONE);
                        positive.setEnabled(true);
                        negative.setEnabled(true);
                        if (exception == null) {
                            success.accept(authInfo);
                            dismiss();
                        } else {
                            LOG.log(Level.INFO, "Failed to login when credentials expired: " + oldAccount, exception);
                            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                            builder.setMessage(Accounts.localizeErrorMessage(getContext(), exception));
                            builder.setCancelable(false);
                            builder.setNegativeButton(getContext().getString(R.string.dialog_positive), null);
                            builder.create().show();
                        }
                    }).start();
        }
        if (view == negative) {
            failed.run();
            dismiss();
        }
    }
}
