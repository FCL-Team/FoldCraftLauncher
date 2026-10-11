package com.tungsten.fcl.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fcl.control.JarExecutorMenu;
import com.tungsten.fcl.control.MenuCallback;
import com.tungsten.fcl.control.MenuType;
import com.tungsten.fcl.control.view.MenuView;
import com.tungsten.fcl.game.sdl.SdlBridge;
import com.mio.flite.FliteTts;
import com.mio.service.GameService;
import com.tungsten.fcl.setting.GameOption;
import com.tungsten.fcl.terracotta.Terracotta;
import com.mio.util.AndroidUtilKt;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.keycodes.FCLKeycodes;
import com.tungsten.fclauncher.keycodes.LwjglGlfwKeycode;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fcllibrary.component.FCLActivity;

import org.libsdl.app.SDLActivity;
import org.lwjgl.glfw.CallbackBridge;

import java.util.Objects;
import java.util.logging.Level;

public class JVMActivity extends FCLActivity implements TextureView.SurfaceTextureListener {

    private TextureView textureView;

    /** 游戏渲染用的 native Surface，保留引用供系统降档后重新发起刷新率投票 */
    @Nullable
    private Surface gameSurface;

    /** 已请求的目标刷新率，0 表示未请求；DisplayListener 据此判断系统是否中途降档 */
    private float requestedRefreshRate = 0f;

    /** 系统中途降档（智能刷新率/省电策略）后重新发起请求；1s 冷却防御部分 ROM 频繁回调 */
    private final DisplayManager.DisplayListener displayListener = new DisplayManager.DisplayListener() {
        private long lastRequestAt = 0L;

        @Override
        public void onDisplayAdded(int displayId) {
        }

        @Override
        public void onDisplayRemoved(int displayId) {
        }

        @Override
        public void onDisplayChanged(int displayId) {
            if (SystemClock.uptimeMillis() - lastRequestAt < 1000L) return;
            Display display = currentDisplay();
            if (display == null
                    || display.getDisplayId() != displayId
                    || requestedRefreshRate <= 0f
                    || display.getMode().getRefreshRate() >= requestedRefreshRate - 0.1f) {
                return;
            }
            lastRequestAt = SystemClock.uptimeMillis();
            applyMaxRefreshRatePolicy();
        }
    };

    private MenuCallback menu;
    private static MenuType menuType;
    private static FCLBridge fclBridge;
    private boolean isTranslated = false;
    private static boolean isRunning = false;
    private long volumeDownTime = 0;
    /** 上次 letterbox 计算使用的窗口尺寸，布局回调据此判断窗口尺寸是否变化 */
    private int lastLetterboxWidth = -1;
    private int lastLetterboxHeight = -1;

    public static void setFCLBridge(FCLBridge fclBridge, MenuType menuType) {
        JVMActivity.fclBridge = fclBridge;
        JVMActivity.menuType = menuType;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_jvm);
        if (menuType == null || fclBridge == null) {
            Logging.LOG.log(Level.WARNING, "Failed to get ControllerType or FCLBridge, task canceled.");
            return;
        }

        menu = menuType == MenuType.GAME ? new GameMenu() : new JarExecutorMenu();
        menu.setup(this, fclBridge);
        GameService.start(this);
        textureView = findViewById(R.id.texture_view);
        textureView.setSurfaceTextureListener(this);
        if (FCLBridge.FORCE_RESOLUTION) {
            applyForceResolutionLayout();
        }

        addContentView(menu.getLayout(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        applyMaxRefreshRatePolicy();
        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        if (displayManager != null) {
            displayManager.registerDisplayListener(displayListener, null);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (menuType == MenuType.GAME && ((GameMenu) menu).getMenuSetting().isDisableSoftKeyAdjust()) {
                return;
            }
            int screenHeight = getWindow().getDecorView().getHeight();
            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            if (screenHeight * 2 / 3 > rect.bottom) {
                textureView.setTranslationY(rect.bottom - screenHeight);
                isTranslated = true;
            } else if (isTranslated) {
                isTranslated = false;
                textureView.setTranslationY(0);
            }
        });
        // 布局回调驱动强制分辨率 letterbox:小窗/分屏/全屏切换等窗口尺寸变化后按当前窗口重算。
        // onConfigurationChanged 等回调期窗口尺寸尚未稳定(还是过渡值),letterbox 以布局结果为准;
        // 对比基准与计算基准同为 TextureView 父容器实测尺寸,保证输入换算与显示区域同源
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (!FCLBridge.FORCE_RESOLUTION) {
                return;
            }
            ViewGroup container = (ViewGroup) textureView.getParent();
            if (container == null) {
                return;
            }
            int width = container.getWidth();
            int height = container.getHeight();
            if (width == 0 || (width == lastLetterboxWidth && height == lastLetterboxHeight)) {
                return;
            }
            applyForceResolutionLayout();
        });
    }

    /**
     * 应用/撤销强制分辨率的 TextureView letterbox 布局：开启时视图按 FORCE_RESOLUTION_SCALE
     * 等比缩放并水平居中，关闭时恢复全屏。游戏内菜单实时修改强制分辨率时也会调用。
     */
    public void applyForceResolutionLayout() {
        ViewGroup.LayoutParams params = textureView.getLayoutParams();
        // letterbox 基准用 TextureView 父容器的实测尺寸,与视图布局落定区域同源,
        // 吸收小窗等窗口形态的装饰 inset 差异;容器与本窗口均尚未布局时(onCreate 早期)
        // 回退启动器快照,布局落定后由布局回调按实测重算
        ViewGroup container = (ViewGroup) textureView.getParent();
        View decorView = getWindow().getDecorView();
        int screenWidth;
        int screenHeight;
        if (container.getWidth() > 0 && container.getHeight() > 0) {
            screenWidth = container.getWidth();
            screenHeight = container.getHeight();
        } else if (decorView.getWidth() > 0 && decorView.getHeight() > 0) {
            screenWidth = decorView.getWidth();
            screenHeight = decorView.getHeight();
        } else {
            screenWidth = AndroidUtilKt.getScreenWidth();
            screenHeight = AndroidUtilKt.getScreenHeight();
        }
        lastLetterboxWidth = screenWidth;
        lastLetterboxHeight = screenHeight;
        if (FCLBridge.FORCE_RESOLUTION) {
            FCLBridge.FORCE_RESOLUTION_SCALE = (float) screenHeight / FCLBridge.FORCE_RESOLUTION_HEIGHT;
            params.width = (int) (FCLBridge.FORCE_RESOLUTION_WIDTH * FCLBridge.FORCE_RESOLUTION_SCALE);
            params.height = (int) (FCLBridge.FORCE_RESOLUTION_HEIGHT * FCLBridge.FORCE_RESOLUTION_SCALE);
            FCLBridge.FORCE_RESOLUTION_START_SIZE = (screenWidth - params.width) / 2;
            textureView.setX(FCLBridge.FORCE_RESOLUTION_START_SIZE);
        } else {
            FCLBridge.FORCE_RESOLUTION_SCALE = -1;
            FCLBridge.FORCE_RESOLUTION_START_SIZE = -1;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            textureView.setX(0);
        }
        textureView.setLayoutParams(params);
    }

    /** 按当前模式计算游戏渲染窗口尺寸：强制分辨率取固定值,普通模式 = 视图尺寸 × windowScale(宽含 cursorOffset),工具页用默认值 */
    private int[] resolveWindowSize(int viewWidth, int viewHeight) {
        if (menuType != MenuType.GAME) {
            return new int[]{FCLBridge.DEFAULT_WIDTH, FCLBridge.DEFAULT_HEIGHT};
        }
        if (FCLBridge.FORCE_RESOLUTION) {
            return new int[]{FCLBridge.FORCE_RESOLUTION_WIDTH, FCLBridge.FORCE_RESOLUTION_HEIGHT};
        }
        double factor = fclBridge.getScaleFactor();
        int width = (int) ((viewWidth + ((GameMenu) menu).getMenuSetting().getCursorOffset()) * factor);
        int height = (int) (viewHeight * factor);
        return new int[]{width, height};
    }

    /**
     * 应用“请求最高刷新率”策略（游戏菜单设置开关，默认开）：
     * 窗口级 preferredDisplayModeId 硬请求同分辨率下的最高刷新率档位，
     * surface 级 setFrameRate 投票（CHANGE_FRAME_RATE_ALWAYS，非无缝切档的机型也生效）。
     * 关闭时清除两类请求，回落系统自适应刷新。
     */
    public void applyMaxRefreshRatePolicy() {
        Display display = currentDisplay();
        if (display == null) return;
        if (!isMaxRefreshRateEnabled()) {
            requestedRefreshRate = 0f;
            setPreferredDisplayModeId(0);
            clearSurfaceFrameRate();
            return;
        }
        Display.Mode best = maxRefreshRateMode(display);
        requestedRefreshRate = best.getRefreshRate();
        setPreferredDisplayModeId(best.getModeId());
        voteMaxDisplayRefreshRate();
    }

    private boolean isMaxRefreshRateEnabled() {
        return menu instanceof GameMenu
                && ((GameMenu) menu).getMenuSetting().isRequestMaxRefreshRate();
    }

    /** 当前关联的 Display；API 30 前走已废弃的 getDefaultDisplay */
    @Nullable
    private Display currentDisplay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return getDisplay();
        }
        @SuppressWarnings("deprecation")
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display;
    }

    /**
     * surface 级投票：请求系统将屏幕切换到设备支持的最高刷新率，避免游戏帧率被系统限制在自选的较低刷新档位
     *
     * 参考 MinecraftGLSurface（https://github.com/AngelAuraMC/Amethyst-Android/blob/v3_openjdk/app_pojavlauncher/src/main/java/net/kdt/pojavlaunch/MinecraftGLSurface.java）
     */
    private void voteMaxDisplayRefreshRate() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return;
        Display display = currentDisplay();
        if (gameSurface == null || display == null) return;
        gameSurface.setFrameRate(
                maxRefreshRateMode(display).getRefreshRate(),
                Surface.FRAME_RATE_COMPATIBILITY_DEFAULT,
                Surface.CHANGE_FRAME_RATE_ALWAYS
        );
    }

    /** 在与当前模式同分辨率的档位中取最高刷新率档（只比同分辨率，避免选到低分辨率高刷档） */
    private Display.Mode maxRefreshRateMode(Display display) {
        Display.Mode current = display.getMode();
        Display.Mode best = current;
        for (Display.Mode mode : display.getSupportedModes()) {
            if (mode.getPhysicalWidth() == current.getPhysicalWidth()
                    && mode.getPhysicalHeight() == current.getPhysicalHeight()
                    && mode.getRefreshRate() > best.getRefreshRate()) {
                best = mode;
            }
        }
        return best;
    }

    /** modeId 传 0 表示清除窗口的显示模式偏好 */
    private void setPreferredDisplayModeId(int modeId) {
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        if (attributes.preferredDisplayModeId == modeId) return;
        attributes.preferredDisplayModeId = modeId;
        getWindow().setAttributes(attributes);
    }

    private void clearSurfaceFrameRate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && gameSurface != null) {
            gameSurface.clearFrameRate();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
        Surface nativeSurface = new Surface(surfaceTexture);
        gameSurface = nativeSurface;
        applyMaxRefreshRatePolicy();
        if (isRunning) {
            fclBridge.setSurfaceTexture(surfaceTexture);
            CallbackBridge.setupBridgeWindow(nativeSurface);
            SdlBridge.prepareSurface(this, nativeSurface, (ViewGroup) textureView.getParent(), this);
            // surface 重建后新 SurfaceTexture 的 buffer 回落视图尺寸,须按当前模式恢复渲染尺寸并
            // 重发窗口状态,否则渲染 buffer 与游戏窗口尺寸错位(黑边、点击与 UI 不对齐)
            int[] size = resolveWindowSize(i, i1);
            surfaceTexture.setDefaultBufferSize(size[0], size[1]);
            CallbackBridge.windowWidth = size[0];
            CallbackBridge.windowHeight = size[1];
            SdlBridge.syncResolution(size[0], size[1]);
            fclBridge.pushEventWindow(size[0], size[1]);
            menu.onGraphicOutput();
            return;
        }
        isRunning = true;
        Logging.LOG.log(Level.INFO, "surface ready, start jvm now!");
        fclBridge.setSurfaceDestroyed(false);
        int[] size = resolveWindowSize(i, i1);
        int width = size[0];
        int height = size[1];
        if (menuType == MenuType.GAME) {
            menu.getInput().initExternalController(textureView);
            GameOption gameOption = new GameOption(Objects.requireNonNull(menu.getBridge()).getGameDir());
            gameOption.set("fullscreen", "false");
            gameOption.set("overrideWidth", String.valueOf(width));
            gameOption.set("overrideHeight", String.valueOf(height));
            gameOption.save();
        }
        surfaceTexture.setDefaultBufferSize(width, height);
        // SDL 集成：初始化 SDL 运行时并绑定 Surface（游戏 JVM 侧 SDL_Init 时再完成加载）
        CallbackBridge.windowWidth = width;
        CallbackBridge.windowHeight = height;
        SdlBridge.prepareSurface(this, nativeSurface, (ViewGroup) textureView.getParent(), this);
        fclBridge.execute(nativeSurface, menu.getCallbackBridge());
        fclBridge.setSurfaceTexture(surfaceTexture);
        fclBridge.pushEventWindow(width, height);
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
        int[] size = resolveWindowSize(i, i1);
        int width = size[0];
        int height = size[1];
        surfaceTexture.setDefaultBufferSize(width, height);
        CallbackBridge.windowWidth = width;
        CallbackBridge.windowHeight = height;
        // SDL 侧同步分辨率
        SdlBridge.syncResolution(width, height);
        fclBridge.pushEventWindow(width, height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
        fclBridge.setSurfaceDestroyed(true);
        gameSurface = null;
        if (SdlBridge.getSdlEnabled() && SDLActivity.getSDLSurface() != null) {
            SDLActivity.getSDLSurface().surfaceDestroyed();
        }
        return true;
    }

    private int output = 0;

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {
        if (output == 1) {
            menu.onGraphicOutput();
            output++;
        }
        if (output < 1) {
            output++;
        }
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
                    if (event.getAction() != KeyEvent.ACTION_UP)
                        return true;
                    menu.getInput().sendKeyEvent(FCLKeycodes.KEY_ESC, true);
                    menu.getInput().sendKeyEvent(FCLKeycodes.KEY_ESC, false);
                    return true;
                } else if ((event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP)) {
                    MenuView menuView = ((GameMenu) menu).getMenuView();
                    if (menuView.getAlpha() == 0 || menuView.getVisibility() == View.INVISIBLE) {
                        DrawerLayout drawerLayout = (DrawerLayout) menu.getLayout();
                        if (drawerLayout.isDrawerOpen(GravityCompat.START) || drawerLayout.isDrawerOpen(GravityCompat.END)) {
                            if (event.getAction() == KeyEvent.ACTION_UP) {
                                drawerLayout.closeDrawers();
                                volumeDownTime = System.currentTimeMillis();
                            }
                        } else {
                            if (System.currentTimeMillis() - volumeDownTime > 800) {
                                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                    return true;
                                } else {
                                    drawerLayout.openDrawer(GravityCompat.START, true);
                                    drawerLayout.openDrawer(GravityCompat.END, true);
                                }
                            } else {
                                volumeDownTime = System.currentTimeMillis();
                            }
                        }
                    }
                }
            }
        }
        return handleEvent;
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if (menu != null && menuType == MenuType.GAME) {
            if (menu.getInput().handleGenericMotionEvent(event)) {
                return true;
            }
        }
        return super.dispatchGenericMotionEvent(event);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (textureView != null && textureView.getSurfaceTexture() != null) {
            textureView.post(() -> onSurfaceTextureSizeChanged(textureView.getSurfaceTexture(), textureView.getWidth(), textureView.getHeight()));
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 窗口尺寸变化(分屏、小窗、折叠屏等)后恢复渲染状态:buffer 与窗口事件按新视图尺寸重发,
        // 强制分辨率 letterbox 由布局回调按稳定后的窗口尺寸重算(回调期 decorView 尺寸尚未稳定)
        if (textureView != null && textureView.getSurfaceTexture() != null) {
            textureView.post(() -> onSurfaceTextureSizeChanged(textureView.getSurfaceTexture(), textureView.getWidth(), textureView.getHeight()));
        }
    }

    @Override
    protected void onDestroy() {
        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        if (displayManager != null) {
            displayManager.unregisterDisplayListener(displayListener);
        }
        Terracotta.setWaiting(this, true);
        CallbackBridge.resetInputState();
        SdlBridge.reset();
        FliteTts.shutdown();
        GameService.stop(this);
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        CallbackBridge.nativeSetWindowAttrib(LwjglGlfwKeycode.GLFW_FOCUSED, hasFocus ? 1 : 0);
        if (menu == null || menuType != MenuType.GAME) {
            return;
        }
        if (!hasFocus) {
            CallbackBridge.resetInputState();
            // 窗口失焦后系统会释放捕获并可能吞掉按键 UP，Java 侧状态一并复位防卡键
            menu.getInput().resetExternalMouseState();
        } else {
            menu.getInput().ensurePointerCapture();
        }
    }

    /**
     * SDL 会在窗口创建时按窗口宽高动态请求方向，可能切到 sensorPortrait，
     * 此处强制锁定横向（跟随传感器），保证游戏画面方向一致
     */
    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

    /**
     * SDL 原生层（Android_JNI_ShowMessageBox）会在宿主对象的运行时类上按名
     * 查找 messageboxShowMessageBox；本宿主非 SDLActivity 子类，必须桥接到
     * SDLActivity 的静态实现，否则查找失败会带着 pending 异常触发 JniAbort
     */
    @Keep
    public int messageboxShowMessageBox(int flags, String title, String message,
                                        int[] buttonFlags, int[] buttonIds,
                                        String[] buttonTexts, int[] colors) {
        return SDLActivity.messageboxShowMessageBox(this, flags, title, message,
                buttonFlags, buttonIds, buttonTexts, colors);
    }
}
