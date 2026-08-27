package com.tungsten.fcl.ui.account;

import static com.tungsten.fclcore.util.Logging.LOG;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.game.OAuthServer;
import com.tungsten.fcl.setting.Accounts;
import com.mio.util.AndroidUtilKt;
import com.mio.util.LoginStageTextBinder;
import com.tungsten.fcl.util.FXUtils;
import com.tungsten.fcl.util.WeakListenerHolder;
import com.tungsten.fclcore.auth.AuthInfo;
import com.tungsten.fclcore.auth.OAuthAccount;
import com.tungsten.fclcore.auth.microsoft.MicrosoftAccount;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;

import android.widget.TextView;

import java.util.function.Consumer;
import java.util.logging.Level;

public class OAuthAccountLoginDialog extends FCLDialog implements View.OnClickListener {

    private final FCLButton positive;
    private final FCLButton negative;

    /** 登录进度行（include view_login_progress），微软重登时显示当前阶段 */
    private final View loginProgress;
    private final TextView progressText;

    private final OAuthAccount account;
    private final Consumer<AuthInfo> success;
    private final Runnable failed;
    private final ObjectProperty<OAuthServer.GrantDeviceCodeEvent> deviceCode = new SimpleObjectProperty<>();

    private final WeakListenerHolder holder = new WeakListenerHolder();
    private boolean useExternalBrowser = false;

    public OAuthAccountLoginDialog(@NonNull Context context, OAuthAccount account, Consumer<AuthInfo> success, Runnable failed) {
        super(context);
        this.account = account;
        this.success = success;
        this.failed = failed;

        setContentView(R.layout.dialog_relogin_oauth);
        setCancelable(false);

        FXUtils.onChangeAndOperate(deviceCode, deviceCode -> Schedulers.androidUIThread().execute(() -> {
            if (deviceCode != null) {
                AndroidUtilKt.copyText(getContext(), deviceCode.getUserCode());
            }
        }));
        holder.add(Accounts.OAUTH_CALLBACK.onGrantDeviceCode.registerWeak(deviceCode::set));
        holder.add(Accounts.OAUTH_CALLBACK.onOpenBrowser.registerWeak(event -> {
            if (useExternalBrowser) {
                AndroidUtilKt.openLink(context, event.getUrl());
            } else {
                AndroidUtilKt.openLinkWithBuiltinWebView(context, event.getUrl());
            }
        }));

        positive = findViewById(R.id.login);
        negative = findViewById(R.id.cancel);
        loginProgress = findViewById(R.id.login_progress);
        progressText = findViewById(R.id.progress_text);

        positive.setOnClickListener(this);
        negative.setOnClickListener(this);

        positive.setOnLongClickListener(view -> {
            useExternalBrowser = true;
            onClick(positive);
            return true;
        });
    }

    @Override
    public void onClick(View view) {
        if (view == positive) {
            positive.setEnabled(false);
            negative.setEnabled(false);
            // 微软账户注入登录进度回调，实时显示各认证阶段
            MicrosoftAccount microsoftAccount = account instanceof MicrosoftAccount ? (MicrosoftAccount) account : null;
            if (microsoftAccount != null) {
                loginProgress.setVisibility(View.VISIBLE);
                progressText.setText(R.string.launch_state_logging_in);
                microsoftAccount.setProgressCallback(new LoginStageTextBinder(getContext(), progressText));
            }
            Task.supplyAsync(account::logInWhenCredentialsExpired)
                    .whenComplete(Schedulers.androidUIThread(), (authInfo, exception) -> {
                        if (microsoftAccount != null) {
                            microsoftAccount.setProgressCallback(null);
                            loginProgress.setVisibility(View.GONE);
                        }
                        if (exception == null) {
                            success.accept(authInfo);
                            dismiss();
                        } else {
                            LOG.log(Level.INFO, "Failed to login when credentials expired: " + account, exception);
                            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                            builder.setMessage(Accounts.localizeErrorMessage(getContext(), exception));
                            builder.setCancelable(false);
                            builder.setNegativeButton(getContext().getString(com.tungsten.fcl.R.string.dialog_positive), null);
                            builder.create().show();
                        }
                        positive.setEnabled(true);
                        negative.setEnabled(true);
                    }).start();
        }
        if (view == negative) {
            failed.run();
            dismiss();
        }
    }
}
