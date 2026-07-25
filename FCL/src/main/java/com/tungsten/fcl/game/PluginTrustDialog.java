package com.tungsten.fcl.game;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;

/** Adapts plugin trust decisions to FCL's standard alert dialog. */
final class PluginTrustDialog extends FCLAlertDialog {
    enum Severity {
        INFO,
        WARNING,
        ERROR
    }

    interface Action {
        void run();
    }

    private String summary;
    private String message;
    private String generalDetails;
    private String technicalDetails;
    private Action primaryAction;

    PluginTrustDialog(@NonNull Context context) {
        super(context);
    }

    void setSeverity(Severity severity) {
        setAlertLevel(severity == Severity.INFO ? AlertLevel.INFO : AlertLevel.ALERT);
    }

    void setSummary(String summary) {
        this.summary = summary;
        updateMessage();
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
        updateMessage();
    }

    void setGeneralDetails(String generalDetails) {
        this.generalDetails = generalDetails;
        updateMessage();
    }

    void setTechnicalDetails(String technicalDetails) {
        this.technicalDetails = technicalDetails;
        updateMessage();
    }

    void setPrimaryButton(String text, Action action) {
        primaryAction = action;
        setNegativeButton(text, action == null ? null : action::run);
    }

    void setPrimaryButtonEnabled(boolean enabled) {
        setNegativeButtonEnabled(enabled);
    }

    void setPrimaryButtonText(String text) {
        setNegativeButton(text, primaryAction == null ? null : primaryAction::run);
    }

    void setSecondaryButton(String text, Action action) {
        setPositiveButton(text, action == null ? null : action::run);
    }

    private void updateMessage() {
        super.setMessage(joinSections(summary, message, generalDetails, technicalDetails));
    }

    private static String joinSections(String... sections) {
        StringBuilder result = new StringBuilder();
        for (String section : sections) {
            if (TextUtils.isEmpty(section)) continue;
            if (result.length() > 0) result.append("\n\n");
            result.append(section);
        }
        return result.toString();
    }

    static final class Builder {
        private final PluginTrustDialog dialog;

        Builder(Context context) {
            dialog = new PluginTrustDialog(context);
        }

        PluginTrustDialog create() {
            return dialog;
        }

        Builder setSeverity(Severity severity) {
            dialog.setSeverity(severity);
            return this;
        }

        Builder setCancelable(boolean cancelable) {
            dialog.setCancelable(cancelable);
            return this;
        }

        Builder setTitle(String title) {
            dialog.setTitle(title);
            return this;
        }

        Builder setSummary(String summary) {
            dialog.setSummary(summary);
            return this;
        }

        Builder setMessage(String message) {
            dialog.setMessage(message);
            return this;
        }

        Builder setGeneralDetails(String details) {
            dialog.setGeneralDetails(details);
            return this;
        }

        Builder setTechnicalDetails(String details) {
            dialog.setTechnicalDetails(details);
            return this;
        }

        Builder setPrimaryButton(String text, Action action) {
            dialog.setPrimaryButton(text, action);
            return this;
        }

        Builder setSecondaryButton(String text, Action action) {
            dialog.setSecondaryButton(text, action);
            return this;
        }
    }
}
