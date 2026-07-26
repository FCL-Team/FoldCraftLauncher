package com.tungsten.fcl.ui.setting;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.tungsten.fcl.R;
import com.tungsten.fcl.game.PluginTrustManager;
import com.tungsten.fcl.ui.setting.PluginTrustListAdapter.Row;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;
import com.vpl.verifiedpluginload.model.TrustActionResult;
import com.vpl.verifiedpluginload.model.TrustActionStatus;
import com.vpl.verifiedpluginload.model.TrustedAuthorInfo;

import java.util.List;

public final class PluginTrustManagementPage extends FCLTempPage
        implements View.OnClickListener, PluginTrustListAdapter.Listener {
    private ListView authorsList;
    private ListView keysList;
    private FCLTextView authorsTitle;
    private FCLTextView keysTitle;
    private FCLTextView authorsEmpty;
    private FCLTextView keysEmpty;
    private FCLProgressBar progress;
    private FCLImageButton refresh;
    private boolean recoveryWarningShown;
    private int operationGeneration;

    public PluginTrustManagementPage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        authorsList = findViewById(R.id.authors_list);
        keysList = findViewById(R.id.keys_list);
        authorsTitle = findViewById(R.id.authors_title);
        keysTitle = findViewById(R.id.keys_title);
        authorsEmpty = findViewById(R.id.authors_empty);
        keysEmpty = findViewById(R.id.keys_empty);
        progress = findViewById(R.id.progress);
        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    public void onRestart() {
        loadData();
    }

    @Override
    public Task<?> refresh(Object... param) {
        loadData();
        return null;
    }

    @Override
    public void onClick(View view) {
        if (view == refresh) loadData();
    }

    @Override
    public void onDetails(Row row) {
        String message;
        if (row.getKind() == Row.Kind.AUTHOR) {
            PluginTrustManager.AuthorTrustEntry entry = row.getAuthorEntry();
            TrustedAuthorInfo author = entry.getAuthor();
            String description = author == null || author.getDescription() == null ? "-" : author.getDescription();
            String website = author == null || author.getWeb() == null ? "-" : author.getWeb();
            message = getContext().getString(
                    R.string.plugin_trust_management_author_details,
                    entry.getAuthorUuid(),
                    description,
                    website,
                    affectedDetails(entry.getAffectedPlugins())
            );
        } else {
            PluginTrustManager.KeyTrustEntry entry = row.getKeyEntry();
            message = getContext().getString(
                    R.string.plugin_trust_management_key_details,
                    entry.getKeyHash().getSha256(),
                    entry.getKeyHash().getSha1(),
                    affectedDetails(entry.getAffectedPlugins())
            );
        }
        new FCLAlertDialog.Builder(getContext())
                .setAlertLevel(FCLAlertDialog.AlertLevel.INFO)
                .setTitle(row.getTitle())
                .setMessage(message)
                .setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), null)
                .create()
                .show();
    }

    @Override
    public void onRevoke(Row row) {
        String message = getContext().getString(
                row.getKind() == Row.Kind.AUTHOR
                        ? R.string.plugin_trust_management_revoke_author_confirm
                        : R.string.plugin_trust_management_revoke_key_confirm,
                row.getTitle()
        );
        new FCLAlertDialog.Builder(getContext())
                .setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                .setTitle(getContext().getString(R.string.plugin_trust_management_revoke))
                .setMessage(message)
                .setPositiveButton(getContext().getString(R.string.plugin_trust_management_revoke), () -> revoke(row))
                .setNegativeButton(null)
                .create()
                .show();
    }

    private void loadData() {
        int generation = ++operationGeneration;
        setLoading(true);
        Task.supplyAsync(() -> PluginTrustManager.load(getContext()))
                .thenAcceptAsync(Schedulers.androidUIThread(), data -> {
                    if (isCurrentOperation(generation)) showData(data);
                })
                .whenComplete(Schedulers.androidUIThread(), error -> {
                    if (!isCurrentOperation(generation)) return;
                    setLoading(false);
                    if (error != null) showError(error);
                })
                .start();
    }

    private void showData(PluginTrustManager.TrustManagementData data) {
        authorsList.setAdapter(PluginTrustListAdapter.forAuthors(getContext(), data.getAuthorEntries(), this));
        keysList.setAdapter(PluginTrustListAdapter.forKeys(getContext(), data.getKeyEntries(), this));
        authorsTitle.setText(getContext().getString(
                R.string.plugin_trust_management_authors_count,
                data.getAuthorEntries().size()
        ));
        keysTitle.setText(getContext().getString(
                R.string.plugin_trust_management_keys_count,
                data.getKeyEntries().size()
        ));
        authorsEmpty.setVisibility(data.getAuthorEntries().isEmpty() ? View.VISIBLE : View.GONE);
        keysEmpty.setVisibility(data.getKeyEntries().isEmpty() ? View.VISIBLE : View.GONE);
        if (data.isRecoveredFromCorruption() && !recoveryWarningShown) {
            recoveryWarningShown = true;
            new FCLAlertDialog.Builder(getContext())
                    .setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                    .setTitle(getContext().getString(R.string.plugin_trust_management_recovered_title))
                    .setMessage(getContext().getString(R.string.plugin_trust_management_recovered_message))
                    .setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), null)
                    .create()
                    .show();
        }
    }

    private void revoke(Row row) {
        int generation = ++operationGeneration;
        setLoading(true);
        Task.supplyAsync(() -> row.getKind() == Row.Kind.AUTHOR
                        ? PluginTrustManager.revokeAuthor(getContext(), row.getAuthorEntry().getAuthorUuid())
                        : PluginTrustManager.revokeKey(getContext(), row.getKeyEntry().getKeyHash()))
                .thenAcceptAsync(Schedulers.androidUIThread(), result -> {
                    if (isCurrentOperation(generation)) handleRevokeResult(result);
                })
                .whenComplete(Schedulers.androidUIThread(), error -> {
                    if (!isCurrentOperation(generation)) return;
                    if (error != null) {
                        setLoading(false);
                        showError(error);
                    }
                })
                .start();
    }

    @Override
    public void onStop() {
        ++operationGeneration;
        super.onStop();
    }

    private boolean isCurrentOperation(int generation) {
        return operationGeneration == generation && isShowing();
    }

    private void handleRevokeResult(TrustActionResult result) {
        if (result.getStatus() == TrustActionStatus.SUCCESS || result.getStatus() == TrustActionStatus.NOT_FOUND) {
            loadData();
        } else {
            setLoading(false);
            showError(new IllegalStateException(result.getStatus().name()));
        }
    }

    private void setLoading(boolean loading) {
        progress.setVisibility(loading ? View.VISIBLE : View.GONE);
        refresh.setEnabled(!loading);
        authorsList.setEnabled(!loading);
        keysList.setEnabled(!loading);
    }

    private String affectedDetails(List<PluginTrustManager.InstalledPlugin> plugins) {
        if (plugins.isEmpty()) return getContext().getString(R.string.plugin_trust_management_no_installed_impact);
        StringBuilder details = new StringBuilder();
        for (PluginTrustManager.InstalledPlugin plugin : plugins) {
            if (details.length() > 0) details.append('\n');
            details.append(getContext().getString(
                    R.string.plugin_trust_management_plugin_detail,
                    plugin.getLabel(),
                    plugin.getPackageName(),
                    getContext().getString(plugin.getTypeNameRes())
            ));
        }
        return details.toString();
    }

    private void showError(Throwable error) {
        String detail = error.getMessage() == null ? error.getClass().getSimpleName() : error.getMessage();
        new FCLAlertDialog.Builder(getContext())
                .setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
                .setTitle(getContext().getString(R.string.message_failed))
                .setMessage(getContext().getString(R.string.plugin_trust_management_error, detail))
                .setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), null)
                .create()
                .show();
    }
}
