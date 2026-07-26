package com.tungsten.fcl.game;

import android.content.Context;

import com.vpl.verifiedpluginload.api.VerifiedPluginLoad;
import com.vpl.verifiedpluginload.api.VerifiedPluginLoadBlocking;
import com.vpl.verifiedpluginload.api.VerifiedPluginLoadRegistry;
import com.vpl.verifiedpluginload.model.KeyHash;
import com.vpl.verifiedpluginload.model.PluginVerificationResult;
import com.vpl.verifiedpluginload.model.TrustActionResult;
import com.vpl.verifiedpluginload.model.TrustedAuthorInfo;
import com.vpl.verifiedpluginload.model.UserTrustSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Builds user-facing trust records without hiding stale or uninstalled entries. */
public final class PluginTrustManager {
    private PluginTrustManager() {
    }

    public static TrustManagementData load(Context context) {
        VerifiedPluginLoad vpl = VerifiedPluginLoadRegistry.get(context);
        List<InstalledPlugin> installedPlugins = new ArrayList<>();
        for (PluginCandidateRepository.PluginCandidate candidate : PluginCandidateRepository.allInstalled(context)) {
            PluginVerificationResult result = vpl.inspectInstalledPackage(candidate.getPackageName());
            String label = result.getPackageInfo().getApplicationLabel();
            if (label == null || label.isBlank()) label = candidate.getPackageName();
            installedPlugins.add(new InstalledPlugin(
                    candidate.getPackageName(),
                    label,
                    result.getPackageInfo().getVersionName(),
                    candidate.getTypeNameRes(),
                    result.getAuthor() == null ? null : result.getAuthor().getUuid(),
                    result.getCurrentSignatures()
            ));
        }
        return build(vpl.getUserTrustSnapshot(), vpl.getTrustedAuthors(), installedPlugins);
    }

    static TrustManagementData build(
            UserTrustSnapshot snapshot,
            List<TrustedAuthorInfo> trustedAuthors,
            List<InstalledPlugin> installedPlugins
    ) {
        Map<String, TrustedAuthorInfo> authorsByUuid = new HashMap<>();
        for (TrustedAuthorInfo author : trustedAuthors) {
            authorsByUuid.put(author.getUuid(), author);
        }

        List<AuthorTrustEntry> authorEntries = new ArrayList<>();
        for (String authorUuid : snapshot.getTrustedAuthorUuids()) {
            List<InstalledPlugin> affected = new ArrayList<>();
            for (InstalledPlugin plugin : installedPlugins) {
                if (authorUuid.equals(plugin.getAuthorUuid())) affected.add(plugin);
            }
            authorEntries.add(new AuthorTrustEntry(authorUuid, authorsByUuid.get(authorUuid), affected));
        }
        authorEntries.sort((left, right) -> left.displayName().compareToIgnoreCase(right.displayName()));

        List<KeyTrustEntry> keyEntries = new ArrayList<>();
        for (KeyHash keyHash : snapshot.getTrustedKeyHashes()) {
            List<InstalledPlugin> affected = new ArrayList<>();
            for (InstalledPlugin plugin : installedPlugins) {
                if (plugin.getCurrentSignatures().contains(keyHash)) affected.add(plugin);
            }
            keyEntries.add(new KeyTrustEntry(keyHash, affected));
        }
        keyEntries.sort((left, right) -> left.getKeyHash().getSha256().compareTo(right.getKeyHash().getSha256()));

        return new TrustManagementData(authorEntries, keyEntries, snapshot.getRecoveredFromCorruption());
    }

    public static TrustActionResult revokeAuthor(Context context, String authorUuid) {
        return VerifiedPluginLoadBlocking.revokeAuthorTrust(
                VerifiedPluginLoadRegistry.get(context),
                authorUuid
        );
    }

    public static TrustActionResult revokeKey(Context context, KeyHash keyHash) {
        return VerifiedPluginLoadBlocking.revokeKeyHashTrust(
                VerifiedPluginLoadRegistry.get(context),
                keyHash.getValue()
        );
    }

    public static final class TrustManagementData {
        private final List<AuthorTrustEntry> authorEntries;
        private final List<KeyTrustEntry> keyEntries;
        private final boolean recoveredFromCorruption;

        private TrustManagementData(
                List<AuthorTrustEntry> authorEntries,
                List<KeyTrustEntry> keyEntries,
                boolean recoveredFromCorruption
        ) {
            this.authorEntries = Collections.unmodifiableList(new ArrayList<>(authorEntries));
            this.keyEntries = Collections.unmodifiableList(new ArrayList<>(keyEntries));
            this.recoveredFromCorruption = recoveredFromCorruption;
        }

        public List<AuthorTrustEntry> getAuthorEntries() {
            return authorEntries;
        }

        public List<KeyTrustEntry> getKeyEntries() {
            return keyEntries;
        }

        public boolean isRecoveredFromCorruption() {
            return recoveredFromCorruption;
        }
    }

    public static final class AuthorTrustEntry {
        private final String authorUuid;
        private final TrustedAuthorInfo author;
        private final List<InstalledPlugin> affectedPlugins;

        private AuthorTrustEntry(String authorUuid, TrustedAuthorInfo author, List<InstalledPlugin> affectedPlugins) {
            this.authorUuid = authorUuid;
            this.author = author;
            this.affectedPlugins = Collections.unmodifiableList(new ArrayList<>(affectedPlugins));
        }

        public String getAuthorUuid() {
            return authorUuid;
        }

        public TrustedAuthorInfo getAuthor() {
            return author;
        }

        public List<InstalledPlugin> getAffectedPlugins() {
            return affectedPlugins;
        }

        private String displayName() {
            return author == null ? authorUuid : author.getName();
        }
    }

    public static final class KeyTrustEntry {
        private final KeyHash keyHash;
        private final List<InstalledPlugin> affectedPlugins;

        private KeyTrustEntry(KeyHash keyHash, List<InstalledPlugin> affectedPlugins) {
            this.keyHash = keyHash;
            this.affectedPlugins = Collections.unmodifiableList(new ArrayList<>(affectedPlugins));
        }

        public KeyHash getKeyHash() {
            return keyHash;
        }

        public List<InstalledPlugin> getAffectedPlugins() {
            return affectedPlugins;
        }
    }

    public static final class InstalledPlugin {
        private final String packageName;
        private final String label;
        private final String versionName;
        private final int typeNameRes;
        private final String authorUuid;
        private final List<KeyHash> currentSignatures;

        public InstalledPlugin(
                String packageName,
                String label,
                String versionName,
                int typeNameRes,
                String authorUuid,
                List<KeyHash> currentSignatures
        ) {
            this.packageName = packageName;
            this.label = label;
            this.versionName = versionName;
            this.typeNameRes = typeNameRes;
            this.authorUuid = authorUuid;
            this.currentSignatures = Collections.unmodifiableList(new ArrayList<>(currentSignatures));
        }

        public String getPackageName() {
            return packageName;
        }

        public String getLabel() {
            return label;
        }

        public String getVersionName() {
            return versionName;
        }

        public int getTypeNameRes() {
            return typeNameRes;
        }

        public String getAuthorUuid() {
            return authorUuid;
        }

        public List<KeyHash> getCurrentSignatures() {
            return currentSignatures;
        }
    }
}
