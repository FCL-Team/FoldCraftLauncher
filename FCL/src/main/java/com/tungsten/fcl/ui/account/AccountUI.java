package com.tungsten.fcl.ui.account;

import static com.tungsten.fclcore.util.Logging.LOG;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;

import com.tungsten.fcl.R;
import com.tungsten.fcl.game.TexturesLoader;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fclauncher.FCLPath;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.auth.AuthInfo;
import com.tungsten.fclcore.auth.AuthenticationException;
import com.tungsten.fclcore.auth.ClassicAccount;
import com.tungsten.fclcore.auth.CredentialExpiredException;
import com.tungsten.fclcore.auth.OAuthAccount;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorAccount;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.skin.InvalidSkinException;
import com.tungsten.fclcore.util.skin.NormalizedSkin;
import com.tungsten.fcllibrary.component.ui.FCLCommonUI;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;
import com.tungsten.fcllibrary.skin.GameCharacter;
import com.tungsten.fcllibrary.skin.MinecraftSkinRenderer;
import com.tungsten.fcllibrary.skin.SkinGLSurfaceView;

import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

public class AccountUI extends FCLCommonUI implements View.OnClickListener {

    private SkinGLSurfaceView skinGLSurfaceView;
    private MinecraftSkinRenderer renderer;

    private AppCompatImageView avatarView;
    private FCLTextView name;
    private FCLTextView description;
    private FCLImageButton refresh;
    private FCLImageButton editSkin;
    private FCLProgressBar refreshProgress;
    private FCLProgressBar skinProgress;
    private FCLButton addOfflineAccount;
    private FCLButton addMicrosoftAccount;
    private FCLButton addExternalAccount;

    private ListView listView;
    private AccountListAdapter accountListAdapter;

    public AccountUI(Context context, FCLUILayout parent, int id) {
        super(context, parent, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        renderer = new MinecraftSkinRenderer(getContext(), true);
        skinGLSurfaceView = findViewById(R.id.skin_view);
        ViewGroup.LayoutParams layoutParamsSkin = skinGLSurfaceView.getLayoutParams();
        layoutParamsSkin.width = (int) (((View) skinGLSurfaceView.getParent().getParent()).getMeasuredWidth() * 0.3f);
        layoutParamsSkin.height = (int) Math.min(((View) skinGLSurfaceView.getParent().getParent()).getMeasuredWidth() * 0.3f, ((View) skinGLSurfaceView.getParent().getParent()).getMeasuredHeight());
        skinGLSurfaceView.setLayoutParams(layoutParamsSkin);

        skinGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        skinGLSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
        skinGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        skinGLSurfaceView.setZOrderOnTop(true);
        skinGLSurfaceView.setRenderer(renderer, 5f);
        skinGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        skinGLSurfaceView.setPreserveEGLContextOnPause(true);

        avatarView = findViewById(R.id.avatar);
        ViewGroup.LayoutParams layoutParamsAvatar = avatarView.getLayoutParams();
        layoutParamsAvatar.width = (int) (((View) avatarView.getParent().getParent()).getMeasuredWidth() * 0.08f);
        layoutParamsAvatar.height = (int) (((View) avatarView.getParent().getParent()).getMeasuredWidth() * 0.08f);
        avatarView.setLayoutParams(layoutParamsAvatar);

        refresh = findViewById(R.id.refresh);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        editSkin = findViewById(R.id.skin);
        refreshProgress = findViewById(R.id.refresh_progress);
        skinProgress = findViewById(R.id.skin_progress);
        addOfflineAccount = findViewById(R.id.offline);
        addMicrosoftAccount = findViewById(R.id.microsoft);
        addExternalAccount = findViewById(R.id.external);
        refresh.setOnClickListener(this);
        editSkin.setOnClickListener(this);
        addOfflineAccount.setOnClickListener(this);
        addMicrosoftAccount.setOnClickListener(this);
        addExternalAccount.setOnClickListener(this);

        listView = findViewById(R.id.list);
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh();
        skinGLSurfaceView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        skinGLSurfaceView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isShowing()) {
            skinGLSurfaceView.onResume();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        skinGLSurfaceView.onPause();
    }

    @Override
    public void refresh() {
        avatarView.setBackground(TexturesLoader.avatarBinding(Accounts.getSelectedAccount(), (int) (((View) avatarView.getParent().getParent()).getMeasuredWidth() * 0.15f)).get());
        try {
            NormalizedSkin normalizedSkin = new NormalizedSkin(Accounts.getSelectedAccount() == null ? BitmapFactory.decodeStream(AccountUI.class.getResourceAsStream("/assets/img/alex.png")) : TexturesLoader.skinBinding(Accounts.getSelectedAccount()).get().getImage());
            renderer.mCharacter = new GameCharacter(normalizedSkin.isSlim());
            renderer.updateTexture(normalizedSkin.isOldFormat() ? normalizedSkin.getNormalizedTexture() : normalizedSkin.getOriginalTexture(), Accounts.getSelectedAccount() == null ? null : TexturesLoader.capeBinding(Accounts.getSelectedAccount()).get());
        } catch (InvalidSkinException e) {
            e.printStackTrace();
        }
        if (accountListAdapter == null) {
            accountListAdapter = new AccountListAdapter(getContext(), Accounts.getAccounts());
            listView.setAdapter(accountListAdapter);
        } else {
            accountListAdapter.notifyDataSetChanged();
        }
        name.setText(Accounts.getSelectedAccount() == null ? getContext().getString(R.string.account_state_no_account) : Accounts.getSelectedAccount().getUsername());
        description.setVisibility(Accounts.getSelectedAccount() == null ? View.INVISIBLE : View.VISIBLE);
        if (Accounts.getSelectedAccount() != null) {
            String loginTypeName = Accounts.getLocalizedLoginTypeName(getContext(), Accounts.getAccountFactory(Accounts.getSelectedAccount()));
            StringProperty subtitle = new SimpleStringProperty();
            if (Accounts.getSelectedAccount() instanceof AuthlibInjectorAccount) {
                AuthlibInjectorServer server = ((AuthlibInjectorAccount) Accounts.getSelectedAccount()).getServer();
                subtitle.bind(Bindings.concat(
                        loginTypeName, ", ", getContext().getString(R.string.account_injector_server), ": ",
                        Bindings.createStringBinding(server::getName, server)));
            } else {
                subtitle.set(loginTypeName);
            }
            description.setText(subtitle.get());
        }
        UIManager.getInstance().getMainUI().refresh();
    }

    @Override
    public void onClick(View view) {
        if (view == refresh) {
            refresh.setVisibility(View.GONE);
            refreshProgress.setVisibility(View.VISIBLE);
            refreshAsync()
                    .whenComplete(Schedulers.androidUIThread(), ex -> {
                        refresh.setVisibility(View.VISIBLE);
                        refreshProgress.setVisibility(View.GONE);

                        if (ex != null) {
                            Toast.makeText(getContext(), Accounts.localizeErrorMessage(getContext(), ex), Toast.LENGTH_SHORT).show();
                        }

                        refresh();
                    })
                    .start();
        }
        if (view == editSkin) {

        }
        if (view == addOfflineAccount) {
            CreateAccountDialog dialog = new CreateAccountDialog(getContext(), Accounts.FACTORY_OFFLINE);
            dialog.show();
        }
        if (view == addMicrosoftAccount) {
            CreateAccountDialog dialog = new CreateAccountDialog(getContext(), Accounts.FACTORY_MICROSOFT);
            dialog.show();
        }
        if (view == addExternalAccount) {
            CreateAccountDialog dialog = new CreateAccountDialog(getContext(), Accounts.FACTORY_AUTHLIB_INJECTOR);
            dialog.show();
        }
    }

    public Task<?> refreshAsync() {
        return Task.runAsync(() -> {
            if (Accounts.getSelectedAccount() == null)
                return;

            Accounts.getSelectedAccount().clearCache();
            try {
                Accounts.getSelectedAccount().logIn();
            } catch (CredentialExpiredException e) {
                try {
                    logIn(Accounts.getSelectedAccount());
                } catch (CancellationException e1) {
                    // ignore cancellation
                } catch (Exception e1) {
                    LOG.log(Level.WARNING, "Failed to refresh " + Accounts.getSelectedAccount() + " with password", e1);
                    throw e1;
                }
            } catch (AuthenticationException e) {
                LOG.log(Level.WARNING, "Failed to refresh " + Accounts.getSelectedAccount() + " with token", e);
                throw e;
            }
        });
    }

    public static AuthInfo logIn(Account account) throws CancellationException, AuthenticationException, InterruptedException {
        if (account instanceof ClassicAccount) {
            CountDownLatch latch = new CountDownLatch(1);
            AtomicReference<AuthInfo> res = new AtomicReference<>(null);
            ClassicAccountLoginDialog dialog = new ClassicAccountLoginDialog(FCLPath.CONTEXT, (ClassicAccount) account, it -> {
                res.set(it);
                latch.countDown();
            }, latch::countDown);
            dialog.show();
            latch.await();
            return Optional.ofNullable(res.get()).orElseThrow(CancellationException::new);
        } else if (account instanceof OAuthAccount) {
            CountDownLatch latch = new CountDownLatch(1);
            AtomicReference<AuthInfo> res = new AtomicReference<>(null);
            OAuthAccountLoginDialog dialog = new OAuthAccountLoginDialog(FCLPath.CONTEXT, (OAuthAccount) account, it -> {
                res.set(it);
                latch.countDown();
            }, latch::countDown);
            dialog.show();
            latch.await();
            return Optional.ofNullable(res.get()).orElseThrow(CancellationException::new);
        }
        return account.logIn();
    }
}
