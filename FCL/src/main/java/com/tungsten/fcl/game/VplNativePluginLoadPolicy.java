package com.tungsten.fcl.game;

import android.content.Context;

import com.tungsten.fclauncher.FCLConfig;
import com.tungsten.fclauncher.plugins.NativePluginAuthorization;
import com.tungsten.fclauncher.plugins.NativePluginLoadPolicy;
import com.tungsten.fclauncher.plugins.PluginNativeLoadGuard;
import com.vpl.verifiedpluginload.api.VerifiedPluginLoad;
import com.vpl.verifiedpluginload.api.VerifiedPluginLoadRegistry;
import com.vpl.verifiedpluginload.model.PluginLoadAuthorization;
import com.vpl.verifiedpluginload.model.PluginTrustStatus;
import com.vpl.verifiedpluginload.model.PluginVerificationResult;
import com.vpl.verifiedpluginload.model.TrustSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/** Host-side VPL policy passed through the launcher's generic native-plugin policy slot. */
public final class VplNativePluginLoadPolicy implements NativePluginLoadPolicy {
    private final boolean enabled;
    private final List<PluginLoadAuthorization> authorizations;

    public VplNativePluginLoadPolicy(boolean enabled, List<PluginLoadAuthorization> authorizations) {
        this.enabled = enabled;
        this.authorizations = Collections.unmodifiableList(new ArrayList<>(authorizations));
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void verify(FCLConfig config) throws IOException {
        if (!enabled) return;

        Context context = config.getContext();
        VerifiedPluginLoad vpl = VerifiedPluginLoadRegistry.get(context);
        boolean allowUntrustedPlugins = context.getSharedPreferences("launcher", Context.MODE_PRIVATE)
                .getBoolean("allow_untrusted_plugins", false);

        List<NativePluginAuthorization> nativeAuthorizations = new ArrayList<>();
        for (PluginCandidateRepository.PluginCandidate candidate : PluginCandidateRepository.forLaunch(context, config.getRenderer())) {
            PluginVerificationResult result = vpl.inspectInstalledPackage(candidate.getPackageName());
            if (result.getStatus() != PluginTrustStatus.TRUSTED) {
                throw new IOException("Plugin " + candidate.getPackageName() + " is not trusted: " + result.getStatus());
            }
            if (!isExplicitKeyTrustAllowed(result.getTrustSource(), allowUntrustedPlugins)) {
                throw new IOException("Plugin " + candidate.getPackageName()
                        + " is trusted only by an individual signature hash while untrusted plugin loading is disabled");
            }

            String apkPath = result.getPackageInfo().getApkPath();
            String nativeLibraryDirectory = result.getPackageInfo().getNativeLibraryDirectory();
            Long versionCode = result.getPackageInfo().getVersionCode();
            Set<String> currentSignatures = result.getCurrentSignatures().stream()
                    .map(keyHash -> keyHash.getValue())
                    .collect(Collectors.toSet());
            boolean authorized = authorizations.stream().anyMatch(authorization ->
                    authorization.getPackageName().equals(candidate.getPackageName())
                            && Objects.equals(authorization.getApkPath(), apkPath)
                            && Objects.equals(authorization.getVersionCode(), versionCode)
                            && authorization.getCurrentSignatures()
                            .equals(new LinkedHashSet<>(result.getCurrentSignatures()))
            );
            if (!authorized) {
                throw new IOException("Plugin " + candidate.getPackageName()
                        + " has no matching pre-launch verification authorization");
            }
            nativeAuthorizations.add(new NativePluginAuthorization(
                    candidate.getPackageName(),
                    apkPath,
                    nativeLibraryDirectory,
                    versionCode,
                    currentSignatures
            ));
        }

        PluginNativeLoadGuard.verifyNativePluginLoads(config, nativeAuthorizations);
    }

    private static boolean isExplicitKeyTrustAllowed(TrustSource trustSource, boolean allowUntrustedPlugins) {
        return trustSource != TrustSource.KEY || allowUntrustedPlugins;
    }
}
