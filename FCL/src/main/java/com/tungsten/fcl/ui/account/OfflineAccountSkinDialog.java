package com.tungsten.fcl.ui.account;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLRadioButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.skin.MinecraftSkinRenderer;
import com.tungsten.fcllibrary.skin.SkinGLSurfaceView;
import com.tungsten.fcllibrary.util.ConvertUtils;

public class OfflineAccountSkinDialog extends FCLDialog implements View.OnClickListener {

    private final AccountListItem accountListItem;

    private ConstraintLayout root;
    private SkinGLSurfaceView skinGLSurfaceView;
    private MinecraftSkinRenderer renderer;

    private FCLTextView title;
    private FCLButton positive;
    private FCLButton negative;

    private FCLRadioButton defaultSkin;
    private FCLRadioButton steve;
    private FCLRadioButton alex;
    private FCLRadioButton local;
    private FCLRadioButton csl;

    public OfflineAccountSkinDialog(@NonNull Context context, AccountListItem accountListItem) {
        super(context);
        this.accountListItem = accountListItem;

        getWindow().setBackgroundDrawable(null);
        WindowManager wm = getWindow().getWindowManager();
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        int maxHeight = point.y - ConvertUtils.dip2px(getContext(), 30);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, maxHeight);

        setContentView(R.layout.dialog_offline_account_skin);
        setCancelable(false);

        renderer = new MinecraftSkinRenderer(getContext(), true);
        renderer.character.setRunning(true);
        skinGLSurfaceView = findViewById(R.id.skin_view);
        skinGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        skinGLSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
        skinGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        skinGLSurfaceView.setZOrderOnTop(true);
        skinGLSurfaceView.setRenderer(renderer, 5f);
        skinGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        skinGLSurfaceView.setPreserveEGLContextOnPause(true);
        renderer.textureProperty().bind(accountListItem.textureProperty());

        root = findViewById(R.id.root);
        title = findViewById(R.id.title);

        defaultSkin = findViewById(R.id.default_skin);
        steve = findViewById(R.id.steve);
        alex = findViewById(R.id.alex);
        local = findViewById(R.id.local);
        csl = findViewById(R.id.csl);

        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        negative.post(() -> {
            int size = root.getHeight() - title.getHeight() - positive.getHeight() - ConvertUtils.dip2px(getContext(), 40);
            ViewGroup.LayoutParams layoutParams = skinGLSurfaceView.getLayoutParams();
            layoutParams.width = size;
            layoutParams.height = size;
            skinGLSurfaceView.setLayoutParams(layoutParams);

            positive.setOnClickListener(this);
            negative.setOnClickListener(this);
        });
    }

    @Override
    public void onClick(View view) {
        if (view == positive) {

        }
        if (view == negative) {
            dismiss();
        }
    }
}
