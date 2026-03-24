package com.tungsten.fcl.ui.account;

import static com.tungsten.fcl.game.TexturesLoader.getDefaultSkin;
import static com.tungsten.fclcore.util.Logging.LOG;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.util.FXUtils;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fclcore.auth.offline.OfflineAccount;
import com.tungsten.fclcore.auth.offline.Skin;
import com.tungsten.fclcore.auth.yggdrasil.TextureModel;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.browser.options.SelectionMode;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLRadioButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.skin.SkinCanvas;
import com.tungsten.fcllibrary.skin.SkinRenderer;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.util.ArrayList;
import java.util.logging.Level;

public class OfflineAccountSkinDialog extends FCLDialog implements View.OnClickListener {

    private final AccountListItem accountListItem;
    private final OfflineAccount account;

    private ConstraintLayout root;
    private SkinCanvas skinCanvas;
    private SkinRenderer renderer;

    private FCLTextView title;
    private FCLButton positive;
    private FCLButton negative;

    private FCLRadioButton defaultSkin;
    private FCLRadioButton steve;
    private FCLRadioButton alex;
    private FCLRadioButton local;
    private FCLRadioButton csl;

    private FCLLinearLayout localLayout;
    private FCLLinearLayout cslLayout;
    private FCLImageButton skinPath;
    private FCLImageButton capePath;
    private FCLTextView skinPathText;
    private FCLTextView capePathText;
    private FCLEditText cslUrl;

    private final InvalidationListener skinBinding;
    private final ObjectProperty<Skin.Type> typeProperty = new SimpleObjectProperty<>(this, "type", Skin.Type.DEFAULT);

    public OfflineAccountSkinDialog(@NonNull Context context, AccountListItem accountListItem) {
        super(context);
        this.accountListItem = accountListItem;
        this.account = (OfflineAccount) accountListItem.getAccount();

        WindowManager wm = getWindow().getWindowManager();
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = point.x * 2 / 3;
        if (point.y * 2 < point.x) {
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
        } else {
            params.height = point.y * 2 / 3;
        }
        getWindow().setAttributes(params);

        setContentView(R.layout.dialog_offline_account_skin);
        setCancelable(false);

        renderer = new SkinRenderer(getContext());
        skinCanvas = findViewById(R.id.skin_view);
        skinCanvas.setRenderer(renderer, 5f);

        root = findViewById(R.id.root);
        title = findViewById(R.id.title);

        defaultSkin = findViewById(R.id.default_skin);
        steve = findViewById(R.id.steve);
        alex = findViewById(R.id.alex);
        local = findViewById(R.id.local);
        csl = findViewById(R.id.csl);

        defaultSkin.setOnClickListener(this);
        steve.setOnClickListener(this);
        alex.setOnClickListener(this);
        local.setOnClickListener(this);
        csl.setOnClickListener(this);

        localLayout = findViewById(R.id.local_layout);
        cslLayout = findViewById(R.id.csl_layout);

        skinPath = findViewById(R.id.skin_path);
        capePath = findViewById(R.id.cape_path);
        skinPath.setOnClickListener(this);
        capePath.setOnClickListener(this);
        skinPathText = findViewById(R.id.skin_path_text);
        capePathText = findViewById(R.id.cape_path_text);

        cslUrl = findViewById(R.id.csl_url);

        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        negative.post(() -> {
            positive.setOnClickListener(this);
            negative.setOnClickListener(this);
        });

        if (account.getSkin() == null) {
            refreshRadio(0);
            typeProperty.set(Skin.Type.DEFAULT);
        } else {
            if (account.getSkin().getType() == Skin.Type.STEVE) {
                refreshRadio(1);
                typeProperty.set(Skin.Type.STEVE);
            } else if (account.getSkin().getType() == Skin.Type.ALEX) {
                refreshRadio(2);
                typeProperty.set(Skin.Type.ALEX);
            } else if (account.getSkin().getType() == Skin.Type.LOCAL_FILE) {
                refreshRadio(3);
                typeProperty.set(Skin.Type.LOCAL_FILE);
            } else if (account.getSkin().getType() == Skin.Type.CUSTOM_SKIN_LOADER_API) {
                refreshRadio(4);
                typeProperty.set(Skin.Type.CUSTOM_SKIN_LOADER_API);
            } else {
                refreshRadio(0);
                typeProperty.set(Skin.Type.DEFAULT);
            }
            skinPathText.setString(account.getSkin().getLocalSkinPath());
            capePathText.setString(account.getSkin().getLocalCapePath());
            cslUrl.setText(account.getSkin().getCslApi());
        }

        refreshSkin();
        skinBinding = FXUtils.observeWeak(this::refreshSkin, typeProperty, cslUrl.stringProperty(), skinPathText.stringProperty(), capePathText.stringProperty());
    }

    private void refreshSkin() {
        getSkin().load(account.getUsername())
                .whenComplete(Schedulers.androidUIThread(), (result, exception) -> {
                    if (exception != null) {
                        LOG.log(Level.WARNING, "Failed to load skin", exception);
                        Toast.makeText(getContext(), getContext().getString(R.string.message_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        if (result == null || result.getSkin() == null && result.getCape() == null) {
                            renderer.setTexture(getDefaultSkin(TextureModel.detectUUID(account.getUUID())).getImage(), null);
                            return;
                        }
                        renderer.setTexture(result.getSkin() != null ? result.getSkin().getImage() : getDefaultSkin(TextureModel.detectUUID(account.getUUID())).getImage(),
                                result.getCape() != null ? result.getCape().getImage() : null);
                    }
                }).start();
    }

    private void refreshRadio(int position) {
        defaultSkin.setChecked(position == 0);
        steve.setChecked(position == 1);
        alex.setChecked(position == 2);
        local.setChecked(position == 3);
        csl.setChecked(position == 4);

        localLayout.setVisibility(position == 3 ? View.VISIBLE : View.GONE);
        cslLayout.setVisibility(position == 4 ? View.VISIBLE : View.GONE);
    }

    private Skin getSkin() {
        return new Skin(typeProperty.get(), cslUrl.getStringValue() == null ? "" : cslUrl.getStringValue(), null, StringUtils.isBlank(skinPathText.getString()) ? null : skinPathText.getString(), StringUtils.isBlank(capePathText.getString()) ? null : capePathText.getString());
    }

    @Override
    public void onClick(View view) {
        if (view == defaultSkin) {
            refreshRadio(0);
            typeProperty.set(Skin.Type.DEFAULT);
        }
        if (view == steve) {
            refreshRadio(1);
            typeProperty.set(Skin.Type.STEVE);
        }
        if (view == alex) {
            refreshRadio(2);
            typeProperty.set(Skin.Type.ALEX);
        }
        if (view == local) {
            refreshRadio(3);
            typeProperty.set(Skin.Type.LOCAL_FILE);
        }
        if (view == csl) {
            refreshRadio(4);
            typeProperty.set(Skin.Type.CUSTOM_SKIN_LOADER_API);
        }

        if (view == skinPath) {
            FileBrowser.Builder builder = new FileBrowser.Builder(getContext());
            builder.setLibMode(LibMode.FILE_CHOOSER);
            builder.setSelectionMode(SelectionMode.SINGLE_SELECTION);
            ArrayList<String> suffix = new ArrayList<>();
            suffix.add(".png");
            builder.setSuffix(suffix);
            builder.create().browse(MainActivity.getInstance(), RequestCodes.SELECT_SKIN_CODE, ((requestCode, resultCode, data) -> {
                if (requestCode == RequestCodes.SELECT_SKIN_CODE && resultCode == Activity.RESULT_OK && data != null) {
                    String path = FileBrowser.getSelectedFiles(data).get(0);
                    skinPathText.setString(path);
                }
            }));
        }
        if (view == capePath) {
            FileBrowser.Builder builder = new FileBrowser.Builder(getContext());
            builder.setLibMode(LibMode.FILE_CHOOSER);
            builder.setSelectionMode(SelectionMode.SINGLE_SELECTION);
            ArrayList<String> suffix = new ArrayList<>();
            suffix.add(".png");
            builder.setSuffix(suffix);
            builder.create().browse(MainActivity.getInstance(), RequestCodes.SELECT_CAPE_CODE, ((requestCode, resultCode, data) -> {
                if (requestCode == RequestCodes.SELECT_CAPE_CODE && resultCode == Activity.RESULT_OK && data != null) {
                    String path = FileBrowser.getSelectedFiles(data).get(0);
                    capePathText.setString(path);
                }
            }));
        }

        if (view == positive) {
            account.setSkin(getSkin());
            accountListItem.refreshSkinBinding();
            dismiss();
        }
        if (view == negative) {
            dismiss();
        }
    }
}
