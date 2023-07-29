package com.tungsten.fcl.ui.account;

import static com.tungsten.fclcore.fakefx.beans.binding.Bindings.createBooleanBinding;
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
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.binding.ObjectBinding;
import com.tungsten.fclcore.fakefx.beans.binding.StringBinding;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.fakefx.beans.value.ObservableBooleanValue;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.skin.InvalidSkinException;
import com.tungsten.fclcore.util.skin.NormalizedSkin;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.browser.options.SelectionMode;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.util.ConvertUtils;

public class AccountListItem {

    private final Context context;
    private final Account account;
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty subtitle = new SimpleStringProperty();
    private final ObjectProperty<Drawable> image = new SimpleObjectProperty<>();
    private final ObjectProperty<Bitmap[]> texture = new SimpleObjectProperty<>();

    public AccountListItem(Context context, Account account) {
        this.context = context;
        this.account = account;

        String loginTypeName = Accounts.getLocalizedLoginTypeName(context, Accounts.getAccountFactory(account));
        if (account instanceof AuthlibInjectorAccount) {
            AuthlibInjectorServer server = ((AuthlibInjectorAccount) account).getServer();
            subtitle.bind(Bindings.concat(
                    loginTypeName, ", ", context.getString(R.string.account_injector_server), ": ",
                    Bindings.createStringBinding(server::getName, server)));
        } else {
            subtitle.set(loginTypeName);
        }

        StringBinding characterName = Bindings.createStringBinding(account::getCharacter, account);
        if (account instanceof OfflineAccount) {
            title.bind(characterName);
        } else {
            title.bind(
                    account.getUsername().isEmpty() ? characterName :
                            Bindings.concat(account.getUsername(), " - ", characterName));
        }

        image.bind(TexturesLoader.avatarBinding(account, ConvertUtils.dip2px(context, 30f)));
        texture.bind(TexturesLoader.textureBinding(account));
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

    public ObservableBooleanValue canUploadSkin() {
        if (account instanceof YggdrasilAccount) {
            if (account instanceof AuthlibInjectorAccount) {
                AuthlibInjectorAccount aiAccount = (AuthlibInjectorAccount) account;
                ObjectBinding<Optional<CompleteGameProfile>> profile = aiAccount.getYggdrasilService().getProfileRepository().binding(aiAccount.getUUID());
                return createBooleanBinding(() -> {
                    Set<TextureType> uploadableTextures = profile.get()
                            .map(AuthlibInjectorAccount::getUploadableTextures)
                            .orElse(emptySet());
                    return uploadableTextures.contains(TextureType.SKIN);
                }, profile);
            } else {
                return createBooleanBinding(() -> true);
            }
        } else if (account instanceof OfflineAccount || account instanceof MicrosoftAccount) {
            return createBooleanBinding(() -> true);
        } else {
            return createBooleanBinding(() -> false);
        }
    }

    /**
     * @return the skin upload task, null if no file is selected
     */
    @Nullable
    public CompletableFuture<Task<?>> uploadSkin() {
        CompletableFuture<Task<?>> completableFuture = new CompletableFuture<>();
        if (account instanceof OfflineAccount) {
            OfflineAccountSkinDialog dialog = new OfflineAccountSkinDialog(context, this);
            dialog.show();
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
        image.unbind();
        texture.unbind();
        image.bind(TexturesLoader.avatarBinding(account, ConvertUtils.dip2px(context, 30f)));
        texture.bind(TexturesLoader.textureBinding(account));
        MainActivity.getInstance().refreshAvatar(account);
        UIManager.getInstance().getMainUI().refreshSkin(account);
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
                OAuthAccountLoginDialog dialog = new OAuthAccountLoginDialog(FCLPath.CONTEXT, (OAuthAccount) account, it -> {
                    res.set(it);
                    latch.countDown();
                }, latch::countDown);
                dialog.show();
            });
            latch.await();
            return Optional.ofNullable(res.get()).orElseThrow(CancellationException::new);
        }
        return account.logIn();
    }

    public void remove() {
        Accounts.getAccounts().remove(account);
    }

    public Account getAccount() {
        return account;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getSubtitle() {
        return subtitle.get();
    }

    public void setSubtitle(String subtitle) {
        this.subtitle.set(subtitle);
    }

    public StringProperty subtitleProperty() {
        return subtitle;
    }

    public Drawable getImage() {
        return image.get();
    }

    public void setImage(Drawable image) {
        this.image.set(image);
    }

    public ObjectProperty<Drawable> imageProperty() {
        return image;
    }

    public Bitmap[] getTexture() {
        return texture.get();
    }

    public void setTexture(Bitmap[] texture) {
        this.texture.set(texture);
    }

    public ObjectProperty<Bitmap[]> textureProperty() {
        return texture;
    }
}
