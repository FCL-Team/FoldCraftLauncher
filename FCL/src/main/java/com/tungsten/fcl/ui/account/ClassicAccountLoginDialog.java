package com.tungsten.fcl.ui.account;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tungsten.fclcore.auth.AuthInfo;
import com.tungsten.fclcore.auth.ClassicAccount;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;

import java.util.function.Consumer;

public class ClassicAccountLoginDialog extends FCLDialog {
    public ClassicAccountLoginDialog(@NonNull Context context, ClassicAccount oldAccount, Consumer<AuthInfo> success, Runnable failed) {
        super(context);
    }
}
