package com.tungsten.fcl.ui.account;

import static com.tungsten.fclcore.util.Logging.LOG;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.logging.Level;

import static java.util.Collections.emptySet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.game.TexturesLoader;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fcl.ui.compose.dialog.MiuixOAuthAccountLoginDialog;
import com.tungsten.fcl.ui.compose.dialog.MiuixOfflineAccountSkinDialog;
import com.tungsten.fcl.ui.main.compose.ComposeMainUI;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.auth.AuthInfo;
import com.tungsten.fclcore.auth.AuthenticationException;
import com.tungsten.fclcore.auth.ClassicAccount;
import com.tungsten.fclcore.auth.CredentialExpiredException;
import com.tungsten.fclcore.auth.OAuthAccount;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorAccount;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer;
import com.tungsten.fclcore.auth.microsoft.MicrosoftAccount;
import com.tungsten.fclcore.auth.offline.OfflineAccount;
import com.tungsten.fclcore.auth.yggdrasil.CompleteGameProfile;
import com.tungsten.fclcore.auth.yggdrasil.TextureType;
import com.tungsten.fclcore.auth.yggdrasil.YggdrasilAccount;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fclcore.util.observable.FlowBridge;
import com.tungsten.fclcore.util.skin.InvalidSkinException;
import com.tungsten.fclcore.util.skin.NormalizedSkin;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.browser.options.SelectionMode;
import com.tungsten.fcllibrary.component.ui.FCLCommonUI;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.util.ConvertUtils;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class AccountListItem {

    private final Context context;
    private final Account account;
    private final MutableStateFlow<String> title = StateFlowKt.MutableStateFlow(null);
    private final MutableStateFlow<String> subtitle = StateFlowKt.MutableStateFlow(null);
    private final MutableStateFlow<Drawable> image = StateFlowKt.MutableStateFlow(null);
    private final MutableStateFlow<Bitmap[]> texture = StateFlowKt.MutableStateFlow(null);
    private FlowSubscriptions.Subscription imageSubscription;
    private FlowSubscriptions.Subscription textureSubscription;

    public AccountListItem(Context context, Account account) {
        this.context = context;
        this.account = account;

        String loginTypeName = Accounts.getLocalizedLoginTypeName(context, Accounts.getAccountFactory(account));
        if (account instanceof AuthlibInjectorAccount) {
            AuthlibInjectorServer server = ((AuthlibInjectorAccount) account).getServer();
            FlowSubscriptions.subscribeWithCurrent(server.revisionFlow(), v -> subtitle.setValue(
                    loginTypeName + ", " + context.getString(R.string.account_injector_server) + ": " + server.getName()));
        } else {
            subtitle.setValue(loginTypeName);
        }

        if (account instanceof OfflineAccount || account.getUsername().isEmpty()) {
            FlowSubscriptions.subscribeWithCurrent(account.revisionFlow(), v -> title.setValue(account.getCharacter()));
        } else {
            String prefix = account.getUsername() + " - ";
            FlowSubscriptions.subscribeWithCurrent(account.revisionFlow(), v -> title.setValue(prefix + account.getCharacter()));
        }

        bindSkinFlows();
    }

    private void bindSkinFlows() {
        imageSubscription = FlowSubscriptions.subscribeWithCurrent(
                TexturesLoader.avatarFlow(account, ConvertUtils.dip2px(context, 30f)),
                image::setValue);
        textureSubscription = FlowSubscriptions.subscribeWithCurrent(
                TexturesLoader.textureFlow(account),
                texture::setValue);
    }

    public Task<?> refreshAsync() {
        return Task.runAsync(() -> {
            account.clearCache();
            try {
                account.logIn();
            } catch (CredentialExpiredException e) {
                try {
                    logIn(account);
                } catch (CancellationException e1) {
                    // ignore cancellation
                } catch (Exception e1) {
                    LOG.log(Level.WARNING, "Failed to refresh " + account + " with password", e1);
                    throw e1;
                }
            } catch (AuthenticationException e) {
                LOG.log(Level.WARNING, "Failed to refresh " + account + " with token", e);
                throw e;
            }
        });
    }

    public StateFlow<Boolean> canUploadSkin() {
        if (account instanceof YggdrasilAccount) {
            if (account instanceof AuthlibInjectorAccount) {
                AuthlibInjectorAccount aiAccount = (AuthlibInjectorAccount) account;
                StateFlow<Optional<CompleteGameProfile>> profile = FlowBridge.asStateFlow(
                        aiAccount.getYggdrasilService().getProfileRepository().binding(aiAccount.getUUID()));
                MutableStateFlow<Boolean> result = StateFlowKt.MutableStateFlow(canUploadSkin(profile.getValue()));
                FlowSubscriptions.subscribe(profile, p -> result.setValue(canUploadSkin(p)));
                return result;
            } else {
                return StateFlowKt.MutableStateFlow(true);
            }
        } else if (account instanceof OfflineAccount || account instanceof MicrosoftAccount) {
            return StateFlowKt.MutableStateFlow(true);
        } else {
            return StateFlowKt.MutableStateFlow(false);
        }
    }

    private static boolean canUploadSkin(Optional<CompleteGameProfile> profile) {
        Set<TextureType> uploadableTextures = profile
                .map(AuthlibInjectorAccount::getUploadableTextures)
                .orElse(emptySet());
        return uploadableTextures.contains(TextureType.SKIN);
    }

    /**
     * @return the skin upload task, null if no file is selected
     */
    @Nullable
    public CompletableFuture<Task<?>> uploadSkin() {
        CompletableFuture<Task<?>> completableFuture = new CompletableFuture<>();
        if (account instanceof OfflineAccount) {
            // GL 预览 AndroidView 保留原生渲染
            new MiuixOfflineAccountSkinDialog(context, this).show();
            completableFuture.complete(null);
            return completableFuture;
        }
        if (account instanceof MicrosoftAccount) {
            AndroidUtils.openLink(context, "https://www.minecraft.net/msaprofile/mygames/editskin");
            completableFuture.complete(null);
            return completableFuture;
        }
        if (!(account instanceof YggdrasilAccount)) {
            completableFuture.complete(null);
            return completableFuture;
        }

        FileBrowser.Builder builder = new FileBrowser.Builder(context);
        builder.setTitle(context.getString(R.string.account_skin_upload));
        builder.setLibMode(LibMode.FILE_CHOOSER);
        builder.setSelectionMode(SelectionMode.SINGLE_SELECTION);
        ArrayList<String> suffix = new ArrayList<>();
        suffix.add(".png");
        builder.setSuffix(suffix);

        CountDownLatch latch = new CountDownLatch(1);

        Schedulers.androidUIThread().execute(() -> builder.create().browse(MainActivity.getInstance(), RequestCodes.SELECT_SKIN_CODE, (requestCode, resultCode, data) -> {
            if (requestCode == RequestCodes.SELECT_SKIN_CODE && resultCode == Activity.RESULT_OK && data != null) {
                String selectedFile = FileBrowser.getSelectedFiles(data).get(0);
                if (selectedFile == null) {
                    completableFuture.complete(null);
                }
                completableFuture.complete(
                        refreshAsync()
                                .thenRunAsync(() -> {
                                    Bitmap skinImg;
                                    try {
                                        skinImg = BitmapFactory.decodeFile(selectedFile);
                                    } catch (Exception e) {
                                        throw new InvalidSkinException("Failed to read skin image", e);
                                    }
                                    if (skinImg == null) {
                                        throw new InvalidSkinException("Failed to read skin image");
                                    }
                                    NormalizedSkin skin = new NormalizedSkin(skinImg);
                                    String model = skin.isSlim() ? "slim" : "";
                                    LOG.info("Uploading skin [" + selectedFile + "], model [" + model + "]");
                                    ((YggdrasilAccount) account).uploadSkin(model, new File(selectedFile).toPath());
                                })
                                .thenComposeAsync(refreshAsync())
                                .whenComplete(Schedulers.androidUIThread(), e -> {
                                    if (e != null) {
                                        FCLAlertDialog.Builder builder1 = new FCLAlertDialog.Builder(context);
                                        builder1.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                                        builder1.setMessage(Accounts.localizeErrorMessage(context, e));
                                        builder1.setNegativeButton(context.getString(com.tungsten.fcllibrary.R.string.dialog_positive), null);
                                        builder1.create().show();
                                    }
                                })
                );
            } else {
                completableFuture.complete(null);
            }
            latch.countDown();
        }));

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return completableFuture;
    }

    public void refreshSkinBinding() {
        // 重绑语义保留：cancel 旧订阅 + 新订阅（对齐原 unbind/bind）
        if (imageSubscription != null) imageSubscription.cancel();
        if (textureSubscription != null) textureSubscription.cancel();
        bindSkinFlows();
        MainActivity.getInstance().refreshAvatar(account);
        // 6.1 批 3：mainUI 固定为 ComposeMainUI（旧 MainUI 已删除），refreshSkin 契约不变
        FCLCommonUI mainUI = UIManager.getInstance().getMainUI();
        if (mainUI instanceof ComposeMainUI) {
            ((ComposeMainUI) mainUI).refreshSkin(account);
        }
    }

    public static AuthInfo logIn(Account account) throws CancellationException, AuthenticationException, InterruptedException {
        if (account instanceof ClassicAccount) {
            CountDownLatch latch = new CountDownLatch(1);
            AtomicReference<AuthInfo> res = new AtomicReference<>(null);
            Schedulers.androidUIThread().execute(() -> {
                ClassicAccountLoginDialog dialog = new ClassicAccountLoginDialog(FCLPath.CONTEXT, (ClassicAccount) account, it -> {
                    res.set(it);
                    latch.countDown();
                }, latch::countDown);
                dialog.show();
            });
            latch.await();
            return Optional.ofNullable(res.get()).orElseThrow(CancellationException::new);
        } else if (account instanceof OAuthAccount) {
            CountDownLatch latch = new CountDownLatch(1);
            AtomicReference<AuthInfo> res = new AtomicReference<>(null);
            Schedulers.androidUIThread().execute(() -> {
                // success/failed 回调契约不变，latch 阻塞语义由本方法持有，与弹窗实现解耦
                Consumer<AuthInfo> success = it -> {
                    res.set(it);
                    latch.countDown();
                };
                Runnable failed = latch::countDown;
                new MiuixOAuthAccountLoginDialog(FCLPath.CONTEXT, (OAuthAccount) account, success, failed).show();
            });
            latch.await();
            return Optional.ofNullable(res.get()).orElseThrow(CancellationException::new);
        }
        return account.logIn();
    }

    public void remove() {
        Accounts.removeAccount(account);
    }

    public Account getAccount() {
        return account;
    }

    public String getTitle() {
        return title.getValue();
    }

    public void setTitle(String title) {
        this.title.setValue(title);
    }

    public MutableStateFlow<String> titleFlow() {
        return title;
    }

    public String getSubtitle() {
        return subtitle.getValue();
    }

    public void setSubtitle(String subtitle) {
        this.subtitle.setValue(subtitle);
    }

    public MutableStateFlow<String> subtitleFlow() {
        return subtitle;
    }

    public Drawable getImage() {
        return image.getValue();
    }

    public void setImage(Drawable image) {
        this.image.setValue(image);
    }

    public MutableStateFlow<Drawable> imageFlow() {
        return image;
    }

    public Bitmap[] getTexture() {
        return texture.getValue();
    }

    public void setTexture(Bitmap[] texture) {
        this.texture.setValue(texture);
    }

    public MutableStateFlow<Bitmap[]> textureFlow() {
        return texture;
    }
}
