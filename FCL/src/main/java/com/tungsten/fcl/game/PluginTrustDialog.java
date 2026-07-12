package com.tungsten.fcl.game;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.core.graphics.ColorUtils;

import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

/** A scoped, high-signal prompt used only for plugin trust decisions. */
final class PluginTrustDialog extends FCLDialog implements View.OnClickListener {
    enum Severity {
        INFO,
        WARNING,
        ERROR
    }

    interface Action {
        void run();
    }

    private final View root;
    private final View header;
    private final FrameLayout severityIconContainer;
    private final ImageFilterView severityIcon;
    private final FCLTextView severity;
    private final FCLTextView title;
    private final FCLTextView summary;
    private final ScrollView contentScroll;
    private final View content;
    private final FCLTextView message;
    private final FCLTextView generalDetails;
    private final FCLTextView technicalDetails;
    private final View actionDivider;
    private final LinearLayoutCompat actions;
    private final FCLButton secondary;
    private final FCLButton primary;

    private Action secondaryAction;
    private Action primaryAction;
    private boolean heightCheckQueued;

    PluginTrustDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_plugin_trust);

        root = findViewById(R.id.plugin_trust_dialog_root);
        header = findViewById(R.id.plugin_trust_header);
        severityIconContainer = findViewById(R.id.plugin_trust_severity_icon_container);
        severityIcon = findViewById(R.id.plugin_trust_severity_icon);
        severity = findViewById(R.id.plugin_trust_severity);
        title = findViewById(R.id.plugin_trust_title);
        summary = findViewById(R.id.plugin_trust_summary);
        contentScroll = findViewById(R.id.plugin_trust_content_scroll);
        content = findViewById(R.id.plugin_trust_content);
        message = findViewById(R.id.plugin_trust_message);
        generalDetails = findViewById(R.id.plugin_trust_general_details);
        technicalDetails = findViewById(R.id.plugin_trust_technical_details);
        actionDivider = findViewById(R.id.plugin_trust_action_divider);
        actions = findViewById(R.id.plugin_trust_actions);
        secondary = findViewById(R.id.plugin_trust_secondary);
        primary = findViewById(R.id.plugin_trust_primary);

        secondary.setVisibility(View.GONE);
        secondary.setOnClickListener(this);
        primary.setOnClickListener(this);
        configureButton(secondary);
        configureButton(primary);
        setSeverity(Severity.INFO);
        updateActionLayout();
        setOnShowListener(ignored -> checkHeight());
        checkHeight();
    }

    void setSeverity(Severity value) {
        if (value == null) value = Severity.INFO;
        switch (value) {
            case ERROR:
                applySeverity(
                        R.string.plugin_trust_level_error,
                        R.drawable.ic_plugin_trust_error_24,
                        0xFFB42318
                );
                break;
            case WARNING:
                applySeverity(
                        R.string.plugin_trust_level_warning,
                        com.tungsten.fcllibrary.R.drawable.ic_baseline_warning_24,
                        0xFFB54708
                );
                break;
            case INFO:
            default:
                applySeverity(
                        R.string.plugin_trust_level_info,
                        com.tungsten.fcllibrary.R.drawable.ic_baseline_info_24,
                        0xFF2563EB
                );
                break;
        }
    }

    private void applySeverity(int labelRes, int iconRes, int accentColor) {
        severity.setText(labelRes);
        severity.setTextColor(accentColor);
        severityIcon.setImageDrawable(getContext().getDrawable(iconRes));
        severityIcon.setImageTintList(ColorStateList.valueOf(accentColor));
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.OVAL);
        background.setColor(ColorUtils.setAlphaComponent(accentColor, 36));
        severityIconContainer.setBackground(background);
    }

    void setTitle(String value) {
        title.setText(value);
        checkHeight();
    }

    void setSummary(String value) {
        summary.setText(value);
        summary.setVisibility(TextUtils.isEmpty(value) ? View.GONE : View.VISIBLE);
        checkHeight();
    }

    void setMessage(String value) {
        message.setText(value);
        message.setVisibility(TextUtils.isEmpty(value) ? View.GONE : View.VISIBLE);
        checkHeight();
    }

    void setGeneralDetails(String value) {
        generalDetails.setText(value);
        generalDetails.setVisibility(TextUtils.isEmpty(value) ? View.GONE : View.VISIBLE);
        checkHeight();
    }

    void setTechnicalDetails(String value) {
        technicalDetails.setText(value);
        checkHeight();
    }

    void setPrimaryButton(String text, Action action) {
        primary.setText(text);
        primaryAction = action;
        updateActionLayout();
        checkHeight();
    }

    void setPrimaryButtonEnabled(boolean enabled) {
        primary.setEnabled(enabled);
    }

    void setPrimaryButtonText(String text) {
        primary.setText(text);
        checkHeight();
    }

    void setSecondaryButton(String text, Action action) {
        secondary.setVisibility(View.VISIBLE);
        secondary.setText(text);
        secondaryAction = action;
        updateActionLayout();
        checkHeight();
    }

    @Override
    public void onClick(View view) {
        Action action = view == primary ? primaryAction : secondaryAction;
        if (action != null) action.run();
        dismiss();
    }

    private void configureButton(FCLButton button) {
        button.setSingleLine(false);
        button.setMaxLines(2);
        button.setEllipsize(TextUtils.TruncateAt.END);
        int minimumHeight = ConvertUtils.dip2px(getContext(), 52);
        button.setMinHeight(minimumHeight);
        button.setMinimumHeight(minimumHeight);
    }

    private void updateActionLayout() {
        actions.setOrientation(LinearLayoutCompat.HORIZONTAL);
        if (secondary.getVisibility() == View.VISIBLE) {
            LinearLayoutCompat.LayoutParams secondaryParams = new LinearLayoutCompat.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
            );
            secondary.setLayoutParams(secondaryParams);
            LinearLayoutCompat.LayoutParams primaryParams = new LinearLayoutCompat.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
            );
            primaryParams.setMarginStart(ConvertUtils.dip2px(getContext(), 8));
            primary.setLayoutParams(primaryParams);
        } else {
            LinearLayoutCompat.LayoutParams primaryParams = new LinearLayoutCompat.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            primary.setLayoutParams(primaryParams);
        }
    }

    private void checkHeight() {
        if (heightCheckQueued) return;
        heightCheckQueued = true;
        root.post(() -> {
            Window window = getWindow();
            if (window == null) {
                heightCheckQueued = false;
                return;
            }
            ViewGroup.LayoutParams layoutParams = contentScroll.getLayoutParams();
            if (layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                contentScroll.setLayoutParams(layoutParams);
            }
            WindowManager wm = window.getWindowManager();
            Point point = new Point();
            wm.getDefaultDisplay().getSize(point);
            int dialogWidth = calculateDialogWidth(point.x);
            window.setLayout(dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT);
            root.post(() -> content.post(() -> {
                heightCheckQueued = false;
                Window currentWindow = getWindow();
                if (currentWindow == null || root.getMeasuredHeight() == 0) return;
                WindowManager currentWm = currentWindow.getWindowManager();
                Point currentPoint = new Point();
                currentWm.getDefaultDisplay().getSize(currentPoint);
                int maxHeight = currentPoint.y - ConvertUtils.dip2px(getContext(), 48);
                int decorHeight = currentWindow.getDecorView().getMeasuredHeight();
                int decorOverhead = Math.max(0, decorHeight - root.getMeasuredHeight());
                int maxRootHeight = Math.max(0, maxHeight - decorOverhead);
                int fixedHeight = fixedHeightWithoutContent();
                int availableContentHeight = Math.max(0, maxRootHeight - fixedHeight);
                int naturalContentHeight = content.getMeasuredHeight();
                if (naturalContentHeight <= availableContentHeight) return;

                ViewGroup.LayoutParams currentLayoutParams = contentScroll.getLayoutParams();
                currentLayoutParams.height = availableContentHeight;
                contentScroll.setLayoutParams(currentLayoutParams);
                currentWindow.setLayout(
                        calculateDialogWidth(currentPoint.x),
                        Math.min(maxHeight, decorOverhead + fixedHeight + availableContentHeight)
                );
            }));
        });
    }

    private int calculateDialogWidth(int screenWidth) {
        int horizontalMargin = ConvertUtils.dip2px(getContext(), 16);
        int maxWidth = ConvertUtils.dip2px(getContext(), 560);
        return Math.min(screenWidth - horizontalMargin * 2, maxWidth);
    }

    private int fixedHeightWithoutContent() {
        // A constrained first pass can measure the trailing action row as zero height.
        int actionHeight = Math.max(actions.getMeasuredHeight(), primary.getMinimumHeight());
        return root.getPaddingTop()
                + root.getPaddingBottom()
                + measuredHeightWithMargins(header)
                + verticalMargins(contentScroll)
                + measuredHeightWithMargins(actionDivider)
                + actionHeight
                + verticalMargins(actions);
    }

    private int measuredHeightWithMargins(View view) {
        int height = view.getMeasuredHeight();
        if (view.getLayoutParams().height > 0) {
            height = Math.max(height, view.getLayoutParams().height);
        }
        return height + verticalMargins(view);
    }

    private int verticalMargins(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof ViewGroup.MarginLayoutParams)) return 0;
        ViewGroup.MarginLayoutParams margins = (ViewGroup.MarginLayoutParams) layoutParams;
        return margins.topMargin + margins.bottomMargin;
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
