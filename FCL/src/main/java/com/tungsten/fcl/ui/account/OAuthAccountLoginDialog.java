package com.tungsten.fcl.ui.account;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tungsten.fclcore.auth.AuthInfo;
import com.tungsten.fclcore.auth.OAuthAccount;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;

import java.util.function.Consumer;

public class OAuthAccountLoginDialog extends FCLDialog {
    public OAuthAccountLoginDialog(@NonNull Context context, OAuthAccount account, Consumer<AuthInfo> success, Runnable failed) {
        super(context);
    }
}
