package com.tungsten.fcl.ui.account;

import static com.tungsten.fclcore.util.Logging.LOG;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.game.OAuthServer;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.FXUtils;
import com.tungsten.fcl.util.WeakListenerHolder;
import com.tungsten.fclcore.auth.AuthInfo;
import com.tungsten.fclcore.auth.OAuthAccount;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;

import java.util.function.Consumer;
import java.util.logging.Level;

public class OAuthAccountLoginDialog extends FCLDialog implements View.OnClickListener {

    private FCLButton positive;
    private FCLButton negative;

    private final OAuthAccount account;
    private final Consumer<AuthInfo> success;
    private final Runnable failed;
    private final ObjectProperty<OAuthServer.GrantDeviceCodeEvent> deviceCode = new SimpleObjectProperty<>();

    private final WeakListenerHolder holder = new WeakListenerHolder();

    public OAuthAccountLoginDialog(@NonNull Context context, OAuthAccount account, Consumer<AuthInfo> success, Runnable failed) {
        super(context);
        this.account = account;
        this.success = success;
        this.failed = failed;

        setContentView(R.layout.dialog_relogin_oauth);

        FXUtils.onChangeAndOperate(deviceCode, deviceCode -> Schedulers.androidUIThread().execute(() -> {
            if (deviceCode != null) {
                AndroidUtils.copyText(getContext(), deviceCode.getUserCode());
            }
        }));
        holder.add(Accounts.OAUTH_CALLBACK.onGrantDeviceCode.registerWeak(deviceCode::set));

        positive = findViewById(R.id.login);
        negative = findViewById(R.id.cancel);

        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == positive) {
            positive.setEnabled(false);
            negative.setEnabled(false);
            Task.supplyAsync(account::logInWhenCredentialsExpired)
                    .whenComplete(Schedulers.androidUIThread(), (authInfo, exception) -> {
                        if (exception == null) {
                            success.accept(authInfo);
                            dismiss();
                        } else {
                            LOG.log(Level.INFO, "Failed to login when credentials expired: " + account, exception);
                            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                            builder.setMessage(Accounts.localizeErrorMessage(getContext(), exception));
                            builder.setCancelable(false);
                            builder.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), null);
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
