package com.tungsten.fcllibrary.skin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.ObjectPropertyBase;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.skin.InvalidSkinException;
import com.tungsten.fclcore.util.skin.NormalizedSkin;
import com.tungsten.fcllibrary.util.ConvertUtils;

import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SkinRenderer implements GLSurfaceView.Renderer {

    private final Context context;
    private final SkinModel skinModel;
    private boolean updateBitmapSkin;
    private boolean slim;
    private Bitmap skin;
    private Bitmap cape;
    private int[] modelTextureData;
    private ObjectProperty<Bitmap[]> textureProperty;

    public SkinRenderer(Context context) {
        this.context = context;
        this.skinModel = new SkinModel();
        this.skinModel.setRunning(true);
        this.updateBitmapSkin = false;
        this.slim = false;
        this.skin = BitmapFactory.decodeStream(SkinRenderer.class.getResourceAsStream("/assets/img/alex.png"));
        this.cape = null;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        this.modelTextureData = loadTexture(this.skin, this.cape);
        gl10.glEnable(GL10.GL_BLEND);
        gl10.glCullFace(GL10.GL_FRONT);
        gl10.glShadeModel(GL10.GL_SMOOTH);
        gl10.glEnable(GL10.GL_RGBA);
        gl10.glEnable(GL10.GL_DEPTH_TEST);
        gl10.glEnable(GL10.GL_ALPHA_TEST);
        gl10.glAlphaFunc(GL10.GL_GREATER, 0.1f);
        gl10.glDepthFunc(GL10.GL_LEQUAL);
        gl10.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        gl10.glClearDepthf(1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        gl10.glViewport(0, 0, i, i1);
        gl10.glMatrixMode(GL10.GL_PROJECTION);
        gl10.glLoadIdentity();
        final float i2 = i1 / 2.0f / (float) Math.tan(ConvertUtils.d2r(22.5f));
        GLU.gluPerspective(gl10, 45.0f, (float) i / i1, 0.5f, Math.max(1500.0f, i2));
        gl10.glMatrixMode(GL10.GL_MODELVIEW);
        gl10.glLoadIdentity();
        GLU.gluLookAt(gl10, i / 2.0f, i1 / 2.0f, i2, i / 2.0f, i1 / 2.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        gl10.glEnable(GL10.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        if (this.updateBitmapSkin) {
            this.modelTextureData = loadTexture(this.skin, this.cape);
            this.updateBitmapSkin = false;
        }
        gl10.glClear(16640);
        gl10.glEnable(GL10.GL_TEXTURE_2D);
        gl10.glLoadIdentity();
        gl10.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        gl10.glTranslatef(0.0f, 0.0f, -60.0f);
        gl10.glPushMatrix();
        gl10.glBindTexture(GL10.GL_TEXTURE_2D, modelTextureData[0]);
        this.skinModel.drawBodyModel(gl10, slim);
        if (cape != null /*&& cape.getWidth() == 64 && cape.getHeight() == 32*/) {
            gl10.glBindTexture(GL10.GL_TEXTURE_2D, modelTextureData[1]);
            this.skinModel.drawCapeModel(gl10);
        }
        gl10.glPopMatrix();
        gl10.glLoadIdentity();
    }

    public void updateTexture(Bitmap skin, Bitmap cape) {
        Schedulers.androidUIThread().execute(() -> {
            try {
                NormalizedSkin normalizedSkin = new NormalizedSkin(skin);
                this.slim = normalizedSkin.isSlim();
                this.skin = normalizedSkin.isOldFormat() ? normalizedSkin.getNormalizedTexture() : normalizedSkin.getOriginalTexture();
                this.cape = cape;
                this.updateBitmapSkin = true;
            } catch (InvalidSkinException e) {
                e.printStackTrace();
                Toast.makeText(context, "Skin Renderer: " + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public SkinModel getSkinModel() {
        return skinModel;
    }

    public final void setTexture(Bitmap skin, Bitmap cape) {
        this.textureProperty().set(new Bitmap[] { skin, cape });
    }

    public final Bitmap[] getTexture() {
        return textureProperty().get();
    }

    public final ObjectProperty<Bitmap[]> textureProperty() {
        if (textureProperty == null) {
            textureProperty = new ObjectPropertyBase<Bitmap[]>() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        Bitmap[] texture = get();
                        updateTexture(texture[0], texture[1]);
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "texture";
                }
            };
            textureProperty.set(new Bitmap[] { BitmapFactory.decodeStream(SkinRenderer.class.getResourceAsStream("/assets/img/alex.png")), null });
        }

        return textureProperty;
    }

    public static int[] loadTexture(@NotNull final Bitmap skin, @Nullable final Bitmap cape) {
        final int[] textures = new int[2];
        GLES20.glGenTextures(2, textures, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, skin, 0);
        if (cape != null) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, cape, 0);
        }
        if (textures[0] == 0 || (cape != null && textures[1] == 0)) {
            throw new RuntimeException("Error loading texture.");
        }
        return textures;
    }
}
