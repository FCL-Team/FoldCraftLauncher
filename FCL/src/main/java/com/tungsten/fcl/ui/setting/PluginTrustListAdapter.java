package com.tungsten.fcl.ui.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.tungsten.fcl.R;
import com.tungsten.fcl.game.PluginTrustManager;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.vpl.verifiedpluginload.model.AuthorType;
import com.vpl.verifiedpluginload.model.TrustedAuthorInfo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

final class PluginTrustListAdapter extends FCLAdapter {
    interface Listener {
        void onDetails(Row row);
        void onRevoke(Row row);
    }

    private final List<Row> rows;
    private final Listener listener;

    private PluginTrustListAdapter(Context context, List<Row> rows, Listener listener) {
        super(context);
        this.rows = rows;
        this.listener = listener;
    }

    static PluginTrustListAdapter forAuthors(Context context,
                                              List<PluginTrustManager.AuthorTrustEntry> entries,
                                              Listener listener) {
        List<Row> rows = new ArrayList<>();
        for (PluginTrustManager.AuthorTrustEntry entry : entries) {
            TrustedAuthorInfo author = entry.getAuthor();
            String title = author == null
                    ? context.getString(R.string.plugin_trust_management_unknown_author)
                    : author.getName();
            String type = author == null
                    ? context.getString(R.string.plugin_trust_management_author_unavailable)
                    : context.getString(author.getType() == AuthorType.ORG
                            ? R.string.plugin_trust_author_org
                            : R.string.plugin_trust_author_person);
            String subtitle = context.getString(
                    R.string.plugin_trust_management_author_subtitle,
                    type,
                    entry.getAuthorUuid()
            );
            rows.add(Row.author(entry, title, subtitle, impactText(context, entry.getAffectedPlugins())));
        }
        return new PluginTrustListAdapter(context, rows, listener);
    }

    static PluginTrustListAdapter forKeys(Context context,
                                           List<PluginTrustManager.KeyTrustEntry> entries,
                                           Listener listener) {
        List<Row> rows = new ArrayList<>();
        for (PluginTrustManager.KeyTrustEntry entry : entries) {
            List<PluginTrustManager.InstalledPlugin> affected = entry.getAffectedPlugins();
            String title = affected.size() == 1
                    ? affected.get(0).getLabel()
                    : context.getString(R.string.plugin_trust_management_certificate);
            String subtitle = context.getString(
                    R.string.plugin_trust_management_fingerprint,
                    abbreviateFingerprint(entry.getKeyHash().getSha256())
            );
            rows.add(Row.key(entry, title, subtitle, impactText(context, affected)));
        }
        return new PluginTrustListAdapter(context, rows, listener);
    }

    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public Row getItem(int position) {
        return rows.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_plugin_trust, parent, false);
            holder = new ViewHolder();
            holder.parent = convertView.findViewById(R.id.parent);
            holder.title = convertView.findViewById(R.id.title);
            holder.subtitle = convertView.findViewById(R.id.subtitle);
            holder.impact = convertView.findViewById(R.id.impact);
            holder.revoke = convertView.findViewById(R.id.revoke);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Row row = getItem(position);
        holder.title.setText(row.title);
        holder.subtitle.setText(row.subtitle);
        holder.impact.setText(row.impact);
        holder.parent.setOnClickListener(view -> listener.onDetails(row));
        holder.revoke.setOnClickListener(view -> listener.onRevoke(row));
        return convertView;
    }

    static String pluginNames(List<PluginTrustManager.InstalledPlugin> plugins) {
        Set<String> names = new LinkedHashSet<>();
        for (PluginTrustManager.InstalledPlugin plugin : plugins) names.add(plugin.getLabel());
        return String.join(", ", names);
    }

    private static String impactText(Context context, List<PluginTrustManager.InstalledPlugin> plugins) {
        return plugins.isEmpty()
                ? context.getString(R.string.plugin_trust_management_no_installed_impact)
                : context.getString(R.string.plugin_trust_management_impact, pluginNames(plugins));
    }

    private static String abbreviateFingerprint(String fingerprint) {
        if (fingerprint.length() <= 24) return fingerprint;
        return fingerprint.substring(0, 12) + "..." + fingerprint.substring(fingerprint.length() - 12);
    }

    private static final class ViewHolder {
        ConstraintLayout parent;
        FCLTextView title;
        FCLTextView subtitle;
        FCLTextView impact;
        FCLImageButton revoke;
    }

    static final class Row {
        enum Kind { AUTHOR, KEY }

        private final Kind kind;
        private final PluginTrustManager.AuthorTrustEntry authorEntry;
        private final PluginTrustManager.KeyTrustEntry keyEntry;
        private final String title;
        private final String subtitle;
        private final String impact;

        private Row(Kind kind,
                    PluginTrustManager.AuthorTrustEntry authorEntry,
                    PluginTrustManager.KeyTrustEntry keyEntry,
                    String title,
                    String subtitle,
                    String impact) {
            this.kind = kind;
            this.authorEntry = authorEntry;
            this.keyEntry = keyEntry;
            this.title = title;
            this.subtitle = subtitle;
            this.impact = impact;
        }

        static Row author(PluginTrustManager.AuthorTrustEntry entry, String title, String subtitle, String impact) {
            return new Row(Kind.AUTHOR, entry, null, title, subtitle, impact);
        }

        static Row key(PluginTrustManager.KeyTrustEntry entry, String title, String subtitle, String impact) {
            return new Row(Kind.KEY, null, entry, title, subtitle, impact);
        }

        Kind getKind() { return kind; }
        PluginTrustManager.AuthorTrustEntry getAuthorEntry() { return authorEntry; }
        PluginTrustManager.KeyTrustEntry getKeyEntry() { return keyEntry; }
        String getTitle() { return title; }
    }
}
