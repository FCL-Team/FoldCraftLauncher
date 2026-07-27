package com.tungsten.fcl.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The trust decision dialog.
 *
 * <p>Facts are rendered as labelled rows rather than concatenated into one text block. Beyond being
 * readable, that is what keeps a plugin's own strings from imitating the launcher's structure: a
 * heading is a view the launcher creates, never text a plugin can supply, and every value sits in a
 * bounded row so no amount of supplied text can grow the dialog or push later rows out of sight.
 */
final class PluginTrustDialog extends FCLDialog implements View.OnClickListener {
    enum Severity {
        INFO,
        WARNING,
        ERROR
    }

    interface Action {
        void run();
    }

    /** One labelled value. The label is a launcher resource; the value is data. */
    static final class Fact {
        private final int labelRes;
        private final String value;
        private final boolean monospace;

        private Fact(@StringRes int labelRes, String value, boolean monospace) {
            this.labelRes = labelRes;
            this.value = value;
            this.monospace = monospace;
        }

        static Fact of(@StringRes int labelRes, String value) {
            return new Fact(labelRes, value, false);
        }

        static Fact monospace(@StringRes int labelRes, String value) {
            return new Fact(labelRes, value, true);
        }
    }

    /** A titled group of facts. */
    static final class Section {
        private final int titleRes;
        private final List<Fact> facts;

        Section(@StringRes int titleRes, List<Fact> facts) {
            this.titleRes = titleRes;
            this.facts = facts;
        }
    }

    private final ImageFilterView icon;
    private final FCLTextView title;
    private final ScrollView scrollView;
    private final View content;
    private final FCLTextView summary;
    private final FCLTextView message;
    private final LinearLayoutCompat sections;
    private final FCLButton positive;
    private final FCLButton negative;
    private final View parent;

    private Action primaryAction;
    private Action secondaryAction;
    private boolean titleSet;

    @SuppressLint("UseCompatLoadingForDrawables")
    PluginTrustDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_plugin_trust);

        parent = findViewById(R.id.parent);
        icon = findViewById(R.id.image);
        title = findViewById(R.id.title);
        scrollView = findViewById(R.id.text_scroll);
        content = findViewById(R.id.content);
        summary = findViewById(R.id.summary);
        message = findViewById(R.id.message);
        sections = findViewById(R.id.sections);
        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);

        positive.setVisibility(View.GONE);
        negative.setVisibility(View.GONE);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
        positive.setSelected(true);
        negative.setSelected(true);

        setSeverity(Severity.INFO);
        checkHeight();
    }

    /**
     * Mirrors FCLAlertDialog: wrap the content when it fits on screen, otherwise cap the dialog and
     * let the scroll view take over, so the button row stays reachable.
     */
    private void checkHeight() {
        parent.post(() -> content.post(() -> {
            WindowManager wm = getWindow().getWindowManager();
            Point point = new Point();
            wm.getDefaultDisplay().getSize(point);
            int maxHeight = point.y - ConvertUtils.dip2px(getContext(), 30);
            if (parent.getMeasuredHeight() < maxHeight) {
                ViewGroup.LayoutParams layoutParams = scrollView.getLayoutParams();
                layoutParams.height = content.getMeasuredHeight();
                scrollView.setLayoutParams(layoutParams);
                getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            } else {
                getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, maxHeight);
            }
        }));
    }

    @Override
    public void onClick(View view) {
        Action action = view == positive ? secondaryAction : primaryAction;
        if (action != null) action.run();
        dismiss();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void setSeverity(Severity severity) {
        boolean alert = severity != Severity.INFO;
        // Non-transitive R classes: the shared icons and fallback titles belong to FCLLibrary.
        icon.setImageDrawable(getContext().getDrawable(alert
                ? com.tungsten.fcllibrary.R.drawable.ic_baseline_warning_24
                : com.tungsten.fcllibrary.R.drawable.ic_baseline_info_24));
        if (!titleSet) {
            title.setText(getContext().getString(alert
                    ? com.tungsten.fcllibrary.R.string.dialog_alert
                    : com.tungsten.fcllibrary.R.string.dialog_info));
        }
    }

    @Override
    public void setTitle(CharSequence titleText) {
        titleSet = true;
        title.setText(titleText);
    }

    void setSummary(String text) {
        setOptionalText(summary, text);
    }

    void setMessage(String text) {
        setOptionalText(message, text);
    }

    private void setOptionalText(FCLTextView view, String text) {
        view.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        view.setText(text);
        checkHeight();
    }

    void setSections(List<Section> sectionList) {
        sections.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (Section section : sectionList) {
            if (section.facts.isEmpty()) continue;
            View sectionView = inflater.inflate(R.layout.item_plugin_trust_section, sections, false);
            ((FCLTextView) sectionView.findViewById(R.id.section_title))
                    .setText(getContext().getString(section.titleRes));
            LinearLayoutCompat rows = sectionView.findViewById(R.id.section_rows);
            for (Fact fact : section.facts) {
                View row = inflater.inflate(
                        fact.monospace ? R.layout.item_plugin_trust_mono : R.layout.item_plugin_trust_fact,
                        rows,
                        false);
                ((FCLTextView) row.findViewById(R.id.fact_label))
                        .setText(getContext().getString(fact.labelRes));
                ((FCLTextView) row.findViewById(R.id.fact_value)).setText(fact.value);
                rows.addView(row);
            }
            sections.addView(sectionView);
        }
        checkHeight();
    }

    void setPrimaryButton(String text, Action action) {
        primaryAction = action;
        negative.setVisibility(View.VISIBLE);
        negative.setText(text);
    }

    void setPrimaryButtonEnabled(boolean enabled) {
        negative.setEnabled(enabled);
    }

    void setPrimaryButtonText(String text) {
        negative.setText(text);
    }

    void setSecondaryButton(String text, Action action) {
        secondaryAction = action;
        positive.setVisibility(View.VISIBLE);
        positive.setText(text);
    }

    static final class Builder {
        private final PluginTrustDialog dialog;
        private final List<Section> sections = new ArrayList<>();

        Builder(Context context) {
            dialog = new PluginTrustDialog(context);
        }

        PluginTrustDialog create() {
            dialog.setSections(Collections.unmodifiableList(sections));
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

        /** Skips a section with no usable facts rather than rendering an empty heading. */
        Builder addSection(@StringRes int titleRes, List<Fact> facts) {
            List<Fact> present = new ArrayList<>();
            for (Fact fact : facts) {
                if (fact != null && !TextUtils.isEmpty(fact.value)) present.add(fact);
            }
            if (!present.isEmpty()) sections.add(new Section(titleRes, present));
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
