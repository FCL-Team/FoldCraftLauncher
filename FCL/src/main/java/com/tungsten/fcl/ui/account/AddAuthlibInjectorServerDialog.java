package com.tungsten.fcl.ui.account;

import static com.tungsten.fcl.setting.ConfigHolder.config;
import static com.tungsten.fclcore.util.Logging.LOG;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLConstraintLayout;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

import javax.net.ssl.SSLException;

public class AddAuthlibInjectorServerDialog extends FCLDialog implements View.OnClickListener {

    private FCLConstraintLayout firstLayout;
    private FCLConstraintLayout secondLayout;

    private FCLEditText editText;
    private FCLTextView url;
    private FCLTextView name;

    private FCLButton next;
    private FCLButton back;
    private FCLButton positive;
    private FCLButton negativePri;
    private FCLButton negativeSec;

    private AuthlibInjectorServer serverBeingAdded;

    public AddAuthlibInjectorServerDialog(@NonNull Context context) {
        super(context);
        setCancelable(false);
        setContentView(R.layout.dialog_add_authlib_injector_server);

        firstLayout = findViewById(R.id.first_layout);
        secondLayout = findViewById(R.id.second_layout);

        editText = findViewById(R.id.url);
        url = findViewById(R.id.address);
        name = findViewById(R.id.name);

        next = findViewById(R.id.next);
        back = findViewById(R.id.prev);
        positive = findViewById(R.id.positive);
        negativePri = findViewById(R.id.negative_pri);
        negativeSec = findViewById(R.id.negative_sec);

        next.setOnClickListener(this);
        back.setOnClickListener(this);
        positive.setOnClickListener(this);
        negativePri.setOnClickListener(this);
        negativeSec.setOnClickListener(this);

        firstLayout.setVisibility(View.VISIBLE);
        secondLayout.setVisibility(View.GONE);
    }

    private void next() {
        next.setEnabled(false);
        negativePri.setEnabled(false);
        String url = Objects.requireNonNull(editText.getText()).toString();
        Task.runAsync(() -> {
            serverBeingAdded = AuthlibInjectorServer.locateServer(url);
        }).whenComplete(Schedulers.androidUIThread(), exception -> {
            next.setEnabled(true);
            negativePri.setEnabled(true);

            if (exception == null) {
                this.name.setText(serverBeingAdded.getName());
                this.url.setText(serverBeingAdded.getUrl());

                firstLayout.setVisibility(View.GONE);
                secondLayout.setVisibility(View.VISIBLE);
            } else {
                LOG.log(Level.WARNING, "Failed to resolve auth server: " + url, exception);
                Toast.makeText(getContext(), resolveFetchExceptionMessage(exception), Toast.LENGTH_SHORT).show();
            }
        }).start();
    }

    private String resolveFetchExceptionMessage(Throwable exception) {
        if (exception instanceof SSLException) {
            return getContext().getString(R.string.account_failed_ssl);
        } else if (exception instanceof IOException) {
            return getContext().getString(R.string.account_failed_connect_injector_server);
        } else {
            return exception.getClass().getName() + ": " + exception.getLocalizedMessage();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == next) {
            next();
        }
        if (v == back) {
            firstLayout.setVisibility(View.VISIBLE);
            secondLayout.setVisibility(View.GONE);
        }
        if (v == positive) {
            if (!config().getAuthlibInjectorServers().contains(serverBeingAdded)) {
                config().getAuthlibInjectorServers().add(serverBeingAdded);
            }
            dismiss();
        }
        if (v == negativePri || v == negativeSec) {
            dismiss();
        }
    }
}
