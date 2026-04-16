package com.tungsten.fcl.activity;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fcl.control.JarExecutorMenu;
import com.tungsten.fcl.control.MenuCallback;
import com.tungsten.fcl.control.MenuType;
import com.tungsten.fcl.control.OpenFolderDialog;
import com.tungsten.fcl.control.view.MenuView;
import com.tungsten.fcl.setting.GameOption;
import com.tungsten.fcl.terracotta.Terracotta;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.bridge.OpenFolderCallback;
import com.tungsten.fclauncher.keycodes.FCLKeycodes;
import com.tungsten.fclauncher.keycodes.LwjglGlfwKeycode;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fcllibrary.component.FCLActivity;

import org.lwjgl.glfw.CallbackBridge;

import java.util.Objects;
import java.util.logging.Level;

public class JVMActivity extends FCLActivity implements SurfaceHolder.Callback, OpenFolderCallback {

    private SurfaceView surfaceView;

    private MenuCallback menu;
    private static MenuType menuType;
    private static FCLBridge fclBridge;
    private boolean isTranslated = false;
    private static boolean isRunning = false;
    private long volumeDownTime = 0;

    public static void setFCLBridge(FCLBridge fclBridge, MenuType menuType) {
        JVMActivity.fclBridge = fclBridge;
        JVMActivity.menuType = menuType;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FCLBridge.setOpenFolderCallback(this);

        setContentView(R.layout.activity_jvm);
        if (menuType == null || fclBridge == null) {
            Logging.LOG.log(Level.WARNING, "Failed to get ControllerType or FCLBridge, task canceled.");
            return;
        }

        menu = menuType == MenuType.GAME ? new GameMenu() : new JarExecutorMenu();
        menu.setup(this, fclBridge);
        surfaceView = findViewById(R.id.surface_view);
        surfaceView.getHolder().addCallback(this);
        if (FCLBridge.FORCE_RESOLUTION) {
            ViewGroup.LayoutParams params = surfaceView.getLayoutParams();
            FCLBridge.FORCE_RESOLUTION_SCALE = (float) AndroidUtils.getScreenHeight() / FCLBridge.FORCE_RESOLUTION_HEIGHT;
            params.width = (int) (FCLBridge.FORCE_RESOLUTION_WIDTH * FCLBridge.FORCE_RESOLUTION_SCALE);
            params.height = (int) (FCLBridge.FORCE_RESOLUTION_HEIGHT * FCLBridge.FORCE_RESOLUTION_SCALE);
            FCLBridge.FORCE_RESOLUTION_START_SIZE = (AndroidUtils.getScreenWidth() - params.width) / 2;
            surfaceView.setLayoutParams(params);
            surfaceView.setX(FCLBridge.FORCE_RESOLUTION_START_SIZE);
        }

        addContentView(menu.getLayout(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (menuType == MenuType.GAME && ((GameMenu) menu).getMenuSetting().isDisableSoftKeyAdjust()) {
                return;
            }
            int screenHeight = getWindow().getDecorView().getHeight();
            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            if (screenHeight * 2 / 3 > rect.bottom) {
                surfaceView.setTranslationY(rect.bottom - screenHeight);
                isTranslated = true;
            } else if (isTranslated) {
                isTranslated = false;
                surfaceView.setTranslationY(0);
            }
        });
    }

    @Override
    public void onBrowse(String path) {
        OpenFolderDialog dialog = new OpenFolderDialog(this, path);
        dialog.show();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        if (menu == null || fclBridge == null) {
            return;
        }

        menu.onGraphicOutput();
        menu.getInput().initExternalController(menuType == MenuType.GAME ? surfaceView : menu.getLayout());
        fclBridge.setSurfaceDestroyed(false);
        fclBridge.setSurfaceHolder(holder);

        if (isRunning) {
            fclBridge.attachSurface(holder.getSurface());
            resizeSurface(surfaceView.getWidth(), surfaceView.getHeight());
            return;
        }

        isRunning = true;
        Logging.LOG.log(Level.INFO, "surface ready, start jvm now!");
        int[] size = getSurfaceSize(surfaceView.getWidth(), surfaceView.getHeight());
        if (menuType == MenuType.GAME) {
            GameOption gameOption = new GameOption(Objects.requireNonNull(menu.getBridge()).getGameDir());
            gameOption.set("fullscreen", "false");
            gameOption.set("overrideWidth", String.valueOf(size[0]));
            gameOption.set("overrideHeight", String.valueOf(size[1]));
            gameOption.save();
        }
        fclBridge.resizeSurface(size[0], size[1]);
        fclBridge.execute(holder.getSurface(), menu.getCallbackBridge());
        fclBridge.pushEventWindow(size[0], size[1]);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        if (fclBridge == null) {
            return;
        }
        fclBridge.setSurfaceHolder(holder);
        resizeSurface(width, height);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        if (fclBridge != null) {
            fclBridge.setSurfaceDestroyed(true);
            fclBridge.setSurfaceHolder(null);
        }
    }

    private int[] getSurfaceSize(int width, int height) {
        int targetWidth = menuType == MenuType.GAME
                ? (int) ((width + ((GameMenu) menu).getMenuSetting().getCursorOffset()) * fclBridge.getScaleFactor())
                : FCLBridge.DEFAULT_WIDTH;
        int targetHeight = menuType == MenuType.GAME
                ? (int) (height * fclBridge.getScaleFactor())
                : FCLBridge.DEFAULT_HEIGHT;
        if (FCLBridge.FORCE_RESOLUTION) {
            targetWidth = FCLBridge.FORCE_RESOLUTION_WIDTH;
            targetHeight = FCLBridge.FORCE_RESOLUTION_HEIGHT;
        }
        return new int[]{targetWidth, targetHeight};
    }

    private void resizeSurface(int width, int height) {
        if (menu == null || fclBridge == null) {
            return;
        }
        int[] size = getSurfaceSize(width, height);
        fclBridge.resizeSurface(size[0], size[1]);
        fclBridge.pushEventWindow(size[0], size[1]);
    }

    @Override
    protected void onPause() {
        if (menu != null) {
            menu.onPause();
        }
        CallbackBridge.nativeSetWindowAttrib(LwjglGlfwKeycode.GLFW_FOCUSED, 0);
        CallbackBridge.nativeSetWindowAttrib(LwjglGlfwKeycode.GLFW_HOVERED, 0);
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (menu != null) {
            menu.onResume();
        }
        CallbackBridge.nativeSetWindowAttrib(LwjglGlfwKeycode.GLFW_FOCUSED, 1);
        CallbackBridge.nativeSetWindowAttrib(LwjglGlfwKeycode.GLFW_HOVERED, 1);
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        CallbackBridge.nativeSetWindowAttrib(LwjglGlfwKeycode.GLFW_VISIBLE, 1);
    }

    @Override
    protected void onStop() {
        CallbackBridge.nativeSetWindowAttrib(LwjglGlfwKeycode.GLFW_VISIBLE, 0);
        super.onStop();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean handleEvent = true;
        if (menu != null && menuType == MenuType.GAME) {
            if (!(handleEvent = menu.getInput().handleKeyEvent(event))) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && !((GameMenu) menu).getTouchCharInput().isEnabled()) {
                    if (event.getAction() != KeyEvent.ACTION_UP) {
                        return true;
                    }
                    menu.getInput().sendKeyEvent(FCLKeycodes.KEY_ESC, true);
                    menu.getInput().sendKeyEvent(FCLKeycodes.KEY_ESC, false);
                    return true;
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                    MenuView menuView = ((GameMenu) menu).getMenuView();
                    if (menuView.getAlpha() == 0 || menuView.getVisibility() == View.INVISIBLE) {
                        DrawerLayout drawerLayout = (DrawerLayout) menu.getLayout();
                        if (drawerLayout.isDrawerOpen(GravityCompat.START) || drawerLayout.isDrawerOpen(GravityCompat.END)) {
                            if (event.getAction() == KeyEvent.ACTION_UP) {
                                drawerLayout.closeDrawers();
                                volumeDownTime = System.currentTimeMillis();
                            }
                        } else if (System.currentTimeMillis() - volumeDownTime > 800) {
                            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                return true;
                            }
                            drawerLayout.openDrawer(GravityCompat.START, true);
                            drawerLayout.openDrawer(GravityCompat.END, true);
                        } else {
                            volumeDownTime = System.currentTimeMillis();
                        }
                    }
                }
            }
        }
        return handleEvent;
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if (menu != null && menuType == MenuType.GAME && menu.getInput().handleGenericMotionEvent(event)) {
            return true;
        }
        return super.dispatchGenericMotionEvent(event);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (surfaceView != null) {
            surfaceView.post(() -> resizeSurface(surfaceView.getWidth(), surfaceView.getHeight()));
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (surfaceView != null) {
            surfaceView.post(() -> resizeSurface(surfaceView.getWidth(), surfaceView.getHeight()));
        }
    }

    @Override
    protected void onDestroy() {
        Terracotta.setWaiting(this, true);
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        CallbackBridge.nativeSetWindowAttrib(LwjglGlfwKeycode.GLFW_FOCUSED, hasFocus ? 1 : 0);
    }
}
