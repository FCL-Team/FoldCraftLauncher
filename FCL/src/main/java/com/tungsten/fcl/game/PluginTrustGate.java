package com.tungsten.fcl.game;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.mio.data.Renderer;
import com.tungsten.fcl.R;
import com.tungsten.fclauncher.plugins.DriverPlugin;
import com.tungsten.fclauncher.plugins.FFmpegPlugin;
import com.tungsten.fclauncher.plugins.NativeLibPlugin;
import com.tungsten.fclauncher.plugins.PluginNativeLoadGuard;
import com.tungsten.fclauncher.plugins.RendererPlugin;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.verifiedpluginload.api.VerifiedPluginLoad;
import com.tungsten.verifiedpluginload.api.VerifiedPluginLoadBlocking;
import com.tungsten.verifiedpluginload.api.VerifiedPluginLoadRegistry;
import com.tungsten.verifiedpluginload.model.AuthorType;
import com.tungsten.verifiedpluginload.model.PluginLoadAuthorization;
import com.tungsten.verifiedpluginload.model.PluginTrustStatus;
import com.tungsten.verifiedpluginload.model.PluginVerificationResult;
import com.tungsten.verifiedpluginload.model.TrustActionResult;
import com.tungsten.verifiedpluginload.model.TrustActionStatus;
import com.tungsten.verifiedpluginload.model.TrustedAuthorInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/** Presents one source-bound confirmation at a time before any native plugin path is used. */
public final class PluginTrustGate {
    private static final String TAG = "VerifiedPluginLoad";
    private static final int UNKNOWN_PLUGIN_COOLDOWN_SECONDS = 6;
    private static final AtomicReference<CooldownState> ACTIVE_COOLDOWN = new AtomicReference<>();

    private PluginTrustGate() {
    }

    public static Task<List<PluginLoadAuthorization>> verifyForLaunch(Context context, Renderer renderer) {
        return PluginTrustListSync.awaitStartupRefresh(context).thenComposeAsync(ignored -> {
            VerifiedPluginLoad vpl = VerifiedPluginLoadRegistry.get(context);
            return verifyNext(context, vpl, collectCandidates(context, renderer), 0, new ArrayList<>());
        });
    }

    /** MainActivity invokes this for handled configuration changes such as screen rotation. */
    public static void resetUnknownPluginCooldown() {
        CooldownState state = ACTIVE_COOLDOWN.get();
        if (state == null || !state.dialog.isShowing()) return;
        int generation = state.generation.incrementAndGet();
        updateCooldown(state, UNKNOWN_PLUGIN_COOLDOWN_SECONDS, generation);
    }

    private static List<PluginCandidate> collectCandidates(Context context, Renderer renderer) {
        List<PluginCandidate> candidates = new ArrayList<>();
        String rendererPackage = RendererPlugin.getPluginPackageName(renderer);
        if (rendererPackage != null) candidates.add(new PluginCandidate(rendererPackage, R.string.plugin_type_renderer));

        DriverPlugin.Driver driver = DriverPlugin.getSelected();
        if (driver.getPackageName() != null) candidates.add(new PluginCandidate(driver.getPackageName(), R.string.plugin_type_driver));

        for (NativeLibPlugin.NativePlugin plugin : NativeLibPlugin.getPluginList()) {
            candidates.add(new PluginCandidate(plugin.getPackageName(), R.string.plugin_type_native));
        }

        FFmpegPlugin.discover(context);
        if (FFmpegPlugin.isAvailable) candidates.add(new PluginCandidate(FFmpegPlugin.PACKAGE_NAME, R.string.plugin_type_ffmpeg));
        return candidates;
    }

    private static Task<List<PluginLoadAuthorization>> verifyNext(
            Context context,
            VerifiedPluginLoad vpl,
            List<PluginCandidate> candidates,
            int index,
            List<PluginLoadAuthorization> authorizations
    ) {
        if (index >= candidates.size()) {
            return Task.completed(Collections.unmodifiableList(new ArrayList<>(authorizations)));
        }
        return Task.composeAsync(() -> {
            PluginCandidate candidate = candidates.get(index);
            PluginVerificationResult result = vpl.inspectInstalledPackage(candidate.packageName);
            logDecision(context, candidate, result);
            boolean allowUntrustedPlugins = allowsUntrustedPlugins(context);
            if (result.getStatus() == PluginTrustStatus.TRUSTED) {
                if (!PluginNativeLoadGuard.isExplicitKeyTrustAllowed(result.getTrustSource(), allowUntrustedPlugins)) {
                    return closeWithFailure(
                            context,
                            R.string.plugin_trust_title_key_trust_disabled,
                            R.string.plugin_trust_summary_key_trust_disabled,
                            context.getString(R.string.plugin_trust_key_trust_disabled_body),
                            generalDetails(context, candidate, result),
                            technicalDetails(context, candidate, result)
                    );
                }
                authorizations.add(requireAuthorization(result));
                return verifyNext(context, vpl, candidates, index + 1, authorizations);
            }
            if (result.getStatus() == PluginTrustStatus.PENDING_TRUST) {
                return requestAuthorTrust(context, vpl, candidate, result, candidates, index, authorizations);
            }
            if (result.getStatus() == PluginTrustStatus.UNTRUSTED) {
                if (allowUntrustedPlugins) {
                    return requestKeyTrust(context, vpl, candidate, result, candidates, index, authorizations);
                }
                return closeWithFailure(
                        context,
                        R.string.plugin_trust_title_untrusted,
                        R.string.plugin_trust_summary_untrusted,
                        context.getString(R.string.plugin_trust_untrusted_body),
                        generalDetails(context, candidate, result),
                        technicalDetails(context, candidate, result)
                );
            }
            if (result.getStatus() == PluginTrustStatus.BANNED) {
                String reason = result.getKeyDescription() == null
                        ? context.getString(R.string.plugin_trust_banned_reason_default)
                        : result.getKeyDescription();
                String warning = result.getAuthor() != null && result.getAuthor().getConfidence() == 0
                        ? context.getString(R.string.plugin_trust_banned_confidence_0)
                        : context.getString(R.string.plugin_trust_banned_confidence_known);
                return closeWithFailure(
                        context,
                        R.string.plugin_trust_title_banned,
                        R.string.plugin_trust_summary_banned,
                        context.getString(R.string.plugin_trust_banned_body, warning),
                        generalDetails(context, candidate, result),
                        technicalDetails(context, candidate, result) + context.getString(
                                R.string.plugin_trust_banned_technical_details,
                                reason
                        )
                );
            }
            return closeWithFailure(
                    context,
                    R.string.plugin_trust_title_failed,
                    R.string.plugin_trust_summary_failed,
                    context.getString(R.string.plugin_trust_failed_body),
                    generalDetails(context, candidate, result),
                    technicalDetails(context, candidate, result) + context.getString(
                            R.string.plugin_trust_failed_technical_details,
                            result.getDiagnostic().name()
                    )
            );
        });
    }

    private static Task<List<PluginLoadAuthorization>> requestAuthorTrust(
            Context context,
            VerifiedPluginLoad vpl,
            PluginCandidate candidate,
            PluginVerificationResult result,
            List<PluginCandidate> candidates,
            int index,
            List<PluginLoadAuthorization> authorizations
    ) {
        TrustedAuthorInfo author = result.getAuthor();
        if (author == null || author.getConfidence() == 0) {
            return closeWithFailure(
                    context,
                    R.string.plugin_trust_title_registered,
                    R.string.plugin_trust_summary_registered,
                    context.getString(
                            R.string.plugin_trust_registered_body,
                            confidenceText(context, author)
                    ),
                    generalDetails(context, candidate, result),
                    technicalDetails(context, candidate, result)
            );
        }
        CompletableFuture<Task<List<PluginLoadAuthorization>>> future = new CompletableFuture<>();
        AtomicBoolean settled = new AtomicBoolean(false);
        Schedulers.androidUIThread().execute(() -> {
            PluginTrustDialog dialog = new PluginTrustDialog.Builder(context)
                    .setSeverity(author.getConfidence() == 1
                            ? PluginTrustDialog.Severity.WARNING
                            : PluginTrustDialog.Severity.INFO)
                    .setCancelable(false)
                    .setTitle(context.getString(R.string.plugin_trust_title_registered))
                    .setSummary(context.getString(R.string.plugin_trust_summary_registered))
                    .setMessage(context.getString(
                            R.string.plugin_trust_registered_body,
                            confidenceText(context, author)
                    ))
                    .setGeneralDetails(generalDetails(context, candidate, result))
                    .setTechnicalDetails(technicalDetails(context, candidate, result))
                    .setSecondaryButton(context.getString(R.string.plugin_trust_cancel), () -> {
                        settled.set(true);
                        future.completeExceptionally(new CancellationException());
                    })
                    .setPrimaryButton(context.getString(R.string.plugin_trust_author_action), () -> {
                        settled.set(true);
                        future.complete(trustAuthorThenContinue(context, vpl, candidate, result, candidates, index, authorizations));
                    })
                    .create();
            dialog.setOnDismissListener(ignored -> {
                if (settled.compareAndSet(false, true)) future.completeExceptionally(new CancellationException());
            });
            dialog.show();
        });
        return Task.fromCompletableFuture(future).thenComposeAsync(task -> task);
    }

    private static Task<List<PluginLoadAuthorization>> trustAuthorThenContinue(
            Context context,
            VerifiedPluginLoad vpl,
            PluginCandidate candidate,
            PluginVerificationResult result,
            List<PluginCandidate> candidates,
            int index,
            List<PluginLoadAuthorization> authorizations
    ) {
        return Task.supplyAsync(() -> {
            TrustActionResult action = VerifiedPluginLoadBlocking.trustAuthor(vpl, result.getAuthor().getUuid());
            if (action.getStatus() != TrustActionStatus.SUCCESS) {
                throw new SecurityException("Could not store author trust: " + action.getStatus());
            }
            PluginVerificationResult refreshed = vpl.inspectInstalledPackage(candidate.packageName);
            if (refreshed.getStatus() != PluginTrustStatus.TRUSTED) {
                throw new SecurityException("Plugin is not trusted after author confirmation: " + refreshed.getStatus());
            }
            return refreshed;
        }).thenComposeAsync(refreshed -> {
            authorizations.add(requireAuthorization(refreshed));
            return verifyNext(context, vpl, candidates, index + 1, authorizations);
        });
    }

    private static Task<List<PluginLoadAuthorization>> requestKeyTrust(
            Context context,
            VerifiedPluginLoad vpl,
            PluginCandidate candidate,
            PluginVerificationResult result,
            List<PluginCandidate> candidates,
            int index,
            List<PluginLoadAuthorization> authorizations
    ) {
        if (result.getCurrentSignatures().isEmpty()) {
            return closeWithFailure(
                    context,
                    R.string.plugin_trust_title_failed,
                    R.string.plugin_trust_summary_failed,
                    context.getString(R.string.plugin_trust_failed_body),
                    generalDetails(context, candidate, result),
                    technicalDetails(context, candidate, result) + context.getString(
                            R.string.plugin_trust_failed_technical_details,
                            "APK_UNSIGNED"
                    )
            );
        }
        CompletableFuture<Task<List<PluginLoadAuthorization>>> future = new CompletableFuture<>();
        AtomicBoolean settled = new AtomicBoolean(false);
        Schedulers.androidUIThread().execute(() -> {
            PluginTrustDialog dialog = new PluginTrustDialog.Builder(context)
                    .setSeverity(PluginTrustDialog.Severity.WARNING)
                    .setCancelable(false)
                    .setTitle(context.getString(R.string.plugin_trust_title_untrusted))
                    .setSummary(context.getString(R.string.plugin_trust_summary_unknown))
                    .setMessage(context.getString(R.string.plugin_trust_unknown_body))
                    .setGeneralDetails(generalDetails(context, candidate, result))
                    .setTechnicalDetails(technicalDetails(context, candidate, result))
                    .setSecondaryButton(context.getString(R.string.plugin_trust_cancel), () -> {
                        settled.set(true);
                        future.completeExceptionally(new CancellationException());
                    })
                    .setPrimaryButton(context.getString(R.string.plugin_trust_key_wait, UNKNOWN_PLUGIN_COOLDOWN_SECONDS), () -> {
                        settled.set(true);
                        future.complete(trustKeyThenContinue(
                                context,
                                vpl,
                                candidate,
                                result,
                                candidates,
                                index,
                                authorizations
                        ));
                    })
                    .create();
            dialog.setPrimaryButtonEnabled(false);
            CooldownState cooldown = new CooldownState(dialog);
            ACTIVE_COOLDOWN.set(cooldown);
            dialog.setOnDismissListener(ignored -> {
                ACTIVE_COOLDOWN.compareAndSet(cooldown, null);
                if (settled.compareAndSet(false, true)) future.completeExceptionally(new CancellationException());
            });
            dialog.show();
            updateCooldown(cooldown, UNKNOWN_PLUGIN_COOLDOWN_SECONDS, cooldown.generation.get());
        });
        return Task.fromCompletableFuture(future).thenComposeAsync(task -> task);
    }

    private static Task<List<PluginLoadAuthorization>> trustKeyThenContinue(
            Context context,
            VerifiedPluginLoad vpl,
            PluginCandidate candidate,
            PluginVerificationResult result,
            List<PluginCandidate> candidates,
            int index,
            List<PluginLoadAuthorization> authorizations
    ) {
        return Task.supplyAsync(() -> {
            String keyHash = result.getCurrentSignatures().get(0).getValue();
            TrustActionResult action = VerifiedPluginLoadBlocking.trustKeyHash(vpl, keyHash);
            if (action.getStatus() != TrustActionStatus.SUCCESS) {
                throw new SecurityException("Could not store key trust: " + action.getStatus());
            }
            PluginVerificationResult refreshed = vpl.inspectInstalledPackage(candidate.packageName);
            if (refreshed.getStatus() != PluginTrustStatus.TRUSTED) {
                throw new SecurityException("Plugin is not trusted after key confirmation: " + refreshed.getStatus());
            }
            return refreshed;
        }).thenComposeAsync(refreshed -> {
            authorizations.add(requireAuthorization(refreshed));
            return verifyNext(context, vpl, candidates, index + 1, authorizations);
        });
    }

    private static void updateCooldown(CooldownState state, int remainingSeconds, int generation) {
        PluginTrustDialog dialog = state.dialog;
        if (ACTIVE_COOLDOWN.get() != state || state.generation.get() != generation || !dialog.isShowing()) return;
        if (remainingSeconds <= 0) {
            dialog.setPrimaryButtonText(dialog.getContext().getString(R.string.plugin_trust_key_action));
            dialog.setPrimaryButtonEnabled(true);
            return;
        }
        // A handled configuration change may restart the timer after this button was enabled.
        dialog.setPrimaryButtonEnabled(false);
        dialog.setPrimaryButtonText(dialog.getContext().getString(R.string.plugin_trust_key_wait, remainingSeconds));
        new Handler(Looper.getMainLooper()).postDelayed(
                () -> updateCooldown(state, remainingSeconds - 1, generation),
                1_000L
        );
    }

    private static Task<List<PluginLoadAuthorization>> closeWithFailure(
            Context context,
            int title,
            int summary,
            String message,
            String generalDetails,
            String technicalDetails
    ) {
        CompletableFuture<List<PluginLoadAuthorization>> future = new CompletableFuture<>();
        AtomicBoolean settled = new AtomicBoolean(false);
        Schedulers.androidUIThread().execute(() -> {
            PluginTrustDialog dialog = new PluginTrustDialog.Builder(context)
                    .setSeverity(PluginTrustDialog.Severity.ERROR)
                    .setCancelable(false)
                    .setTitle(context.getString(title))
                    .setSummary(context.getString(summary))
                    .setMessage(message)
                    .setGeneralDetails(generalDetails)
                    .setTechnicalDetails(technicalDetails)
                    .setPrimaryButton(context.getString(R.string.plugin_trust_close), () -> {
                        settled.set(true);
                        future.completeExceptionally(new CancellationException());
                    })
                    .create();
            dialog.setOnDismissListener(ignored -> {
                if (settled.compareAndSet(false, true)) future.completeExceptionally(new CancellationException());
            });
            dialog.show();
        });
        return Task.fromCompletableFuture(future);
    }

    private static boolean allowsUntrustedPlugins(Context context) {
        return context.getSharedPreferences("launcher", MODE_PRIVATE)
                .getBoolean("allow_untrusted_plugins", false);
    }

    private static PluginLoadAuthorization requireAuthorization(PluginVerificationResult result) {
        PluginLoadAuthorization authorization = result.toLoadAuthorization();
        if (authorization == null) throw new SecurityException("Trusted result does not contain a load authorization");
        return authorization;
    }

    private static void logDecision(Context context, PluginCandidate candidate, PluginVerificationResult result) {
        String sha256 = result.getMatchedSignature() == null ? "unknown" : result.getMatchedSignature().getSha256();
        Log.i(TAG, "Plugin verification: type=" + context.getString(candidate.typeNameRes)
                + ", package=" + candidate.packageName
                + ", version=" + result.getPackageInfo().getVersionName()
                + ", sha256=" + sha256
                + ", status=" + result.getStatus()
                + ", trustListVersion=" + result.getTrustListVersion());
    }

    private static String generalDetails(Context context, PluginCandidate candidate, PluginVerificationResult result) {
        String label = result.getPackageInfo().getApplicationLabel();
        if (label == null || label.isBlank()) label = candidate.packageName;
        String version = result.getPackageInfo().getVersionName() == null ? "-" : result.getPackageInfo().getVersionName();
        String details = context.getString(
                R.string.plugin_trust_general_details,
                label,
                version,
                context.getString(candidate.typeNameRes)
        );
        return result.getAuthor() == null ? details : details + authorDetails(context, result.getAuthor());
    }

    private static String technicalDetails(Context context, PluginCandidate candidate, PluginVerificationResult result) {
        String packageName = result.getPackageInfo().getPackageName() == null
                ? candidate.packageName
                : result.getPackageInfo().getPackageName();
        String sha256 = result.getCurrentSignatures().isEmpty() ? "-" : result.getCurrentSignatures().get(0).getSha256();
        return context.getString(R.string.plugin_trust_technical_details, packageName, formatFingerprint(sha256));
    }

    private static String formatFingerprint(String fingerprint) {
        if (fingerprint.length() <= 16) return fingerprint;
        StringBuilder formatted = new StringBuilder(fingerprint.length() + fingerprint.length() / 16);
        for (int index = 0; index < fingerprint.length(); index += 16) {
            if (index > 0) formatted.append('\n');
            formatted.append(fingerprint, index, Math.min(index + 16, fingerprint.length()));
        }
        return formatted.toString();
    }

    private static String authorDetails(Context context, TrustedAuthorInfo author) {
        String type = author.getType() == AuthorType.ORG
                ? context.getString(R.string.plugin_trust_author_org)
                : context.getString(R.string.plugin_trust_author_person);
        return context.getString(
                R.string.plugin_trust_author_details,
                author.getName(),
                type,
                author.getDescription() == null ? "-" : author.getDescription(),
                author.getWeb() == null ? "-" : author.getWeb()
        );
    }

    private static String confidenceText(Context context, TrustedAuthorInfo author) {
        if (author == null) return context.getString(R.string.plugin_trust_confidence_0);
        switch (author.getConfidence()) {
            case 2:
                return context.getString(R.string.plugin_trust_confidence_2);
            case 1:
                return context.getString(R.string.plugin_trust_confidence_1);
            default:
                return context.getString(R.string.plugin_trust_confidence_0);
        }
    }

    private static final class PluginCandidate {
        private final String packageName;
        private final int typeNameRes;

        private PluginCandidate(String packageName, int typeNameRes) {
            this.packageName = packageName;
            this.typeNameRes = typeNameRes;
        }
    }

    private static final class CooldownState {
        private final PluginTrustDialog dialog;
        private final AtomicInteger generation = new AtomicInteger();

        private CooldownState(PluginTrustDialog dialog) {
            this.dialog = dialog;
        }
    }
}
