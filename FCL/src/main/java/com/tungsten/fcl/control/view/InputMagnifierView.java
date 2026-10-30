package com.tungsten.fcl.control.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.keyboard.TouchCharInput;
import com.tungsten.fcl.setting.MenuSetting;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLCheckBox;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.util.ConvertUtils;

import org.libsdl.app.SDLActivity;

public class InputMagnifierView extends FrameLayout {

    private final MagnifierViewport viewport;
    private final FrameLayout magnifierWindow;
    private final FrameLayout showButton;
    private final LinearLayout settingsPanel;
    private final FCLCheckBox autoShowCheckBox;
    private final Rect visibleFrame = new Rect();
    private final int[] locationOnScreen = new int[2];
    private final ViewTreeObserver.OnGlobalLayoutListener layoutListener = this::updateInputState;

    @Nullable
    private ViewTreeObserver viewTreeObserver;
    @Nullable
    private TextureView textureView;
    @Nullable
    private TouchCharInput touchCharInput;
    @Nullable
    private MenuSetting menuSetting;
    @Nullable
    private Bitmap frame;
    private boolean dismissedForInput;
    private boolean inputOccludesGame;

    public InputMagnifierView(@NonNull Context context) {
        this(context, null);
    }

    public InputMagnifierView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputMagnifierView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(false);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

        viewport = new MagnifierViewport(context);
        magnifierWindow = new FrameLayout(context);
        magnifierWindow.setClipToOutline(true);
        magnifierWindow.setElevation(dp(6));
        magnifierWindow.addView(viewport, new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        LayoutParams windowParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        windowParams.gravity = Gravity.CENTER_HORIZONTAL;
        windowParams.topMargin = dp(12);
        windowParams.bottomMargin = dp(12);
        addView(magnifierWindow, windowParams);

        LinearLayout toolbar = new LinearLayout(context);
        toolbar.setOrientation(LinearLayout.HORIZONTAL);
        toolbar.setBackground(roundedBackground(Color.argb(166, 0, 0, 0), 8));
        FCLImageButton menuButton = createIconButton(
                R.drawable.ic_baseline_more_horiz_24, R.string.control_info_more);
        menuButton.setOnClickListener(view -> toggleSettingsPanel());
        toolbar.addView(menuButton, new LinearLayout.LayoutParams(dp(40), dp(40)));
        FCLImageButton closeButton = createIconButton(R.drawable.ic_baseline_close_24, R.string.close);
        closeButton.setOnClickListener(view -> close());
        toolbar.addView(closeButton, new LinearLayout.LayoutParams(dp(40), dp(40)));
        LayoutParams toolbarParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.END);
        toolbarParams.setMargins(dp(8), dp(8), dp(8), dp(8));
        magnifierWindow.addView(toolbar, toolbarParams);

        settingsPanel = new LinearLayout(context);
        settingsPanel.setOrientation(LinearLayout.VERTICAL);
        settingsPanel.setBackground(roundedBackground(Color.argb(217, 0, 0, 0), 8));
        settingsPanel.setPadding(dp(12), dp(4), dp(12), dp(4));
        settingsPanel.setVisibility(GONE);
        autoShowCheckBox = new FCLCheckBox(context);
        autoShowCheckBox.setText(R.string.menu_settings_ime_magnifier_auto_show);
        autoShowCheckBox.setTextColor(Color.WHITE);
        autoShowCheckBox.setFocusable(false);
        settingsPanel.addView(autoShowCheckBox, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutParams settingsParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.END);
        settingsParams.topMargin = dp(56);
        settingsParams.rightMargin = dp(8);
        magnifierWindow.addView(settingsPanel, settingsParams);

        showButton = new FrameLayout(context);
        showButton.setBackground(roundedBackground(Color.argb(140, 0, 0, 0), 20));
        FCLImageButton showIcon = createIconButton(
                R.drawable.ic_baseline_search_24, R.string.game_magnifier_show);
        showIcon.setOnClickListener(view -> setMagnifierVisible(true));
        showButton.addView(showIcon, new LayoutParams(dp(40), dp(40)));
        LayoutParams showParams = new LayoutParams(dp(40), dp(40), Gravity.BOTTOM | Gravity.END);
        showParams.setMargins(dp(16), dp(16), dp(16), dp(16));
        addView(showButton, showParams);

        setVisibility(GONE);
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public void setup(@NonNull TextureView textureView, @NonNull TouchCharInput touchCharInput,
                      @NonNull MenuSetting menuSetting) {
        this.textureView = textureView;
        this.touchCharInput = touchCharInput;
        this.menuSetting = menuSetting;
        autoShowCheckBox.setChecked(menuSetting.isImeMagnifierAutoShow());
        autoShowCheckBox.setOnCheckedChangeListener((button, checked) ->
                menuSetting.setImeMagnifierAutoShow(checked));
        updateInputState();
    }

    public void captureFrame() {
        if (!isMagnifierVisible() || textureView == null) {
            return;
        }
        int width = textureView.getWidth();
        int height = textureView.getHeight();
        if (!textureView.isAvailable() || width <= 0 || height <= 0) {
            return;
        }
        if (frame == null || frame.getWidth() != width || frame.getHeight() != height) {
            frame = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        Bitmap captured;
        try {
            captured = textureView.getBitmap(frame);
        } catch (RuntimeException ignore) {
            return;
        }
        if (captured != null) {
            frame = captured;
            viewport.setFrame(captured);
        }
    }

    public boolean isMagnifierVisible() {
        return getVisibility() == VISIBLE && magnifierWindow.getVisibility() == VISIBLE;
    }

    private void close() {
        dismissedForInput = true;
        setMagnifierVisible(false);
    }

    private void toggleSettingsPanel() {
        settingsPanel.setVisibility(settingsPanel.getVisibility() == VISIBLE ? GONE : VISIBLE);
    }

    private void setMagnifierVisible(boolean visible) {
        magnifierWindow.setVisibility(visible ? VISIBLE : GONE);
        showButton.setVisibility(visible ? GONE : VISIBLE);
        if (!visible) {
            settingsPanel.setVisibility(GONE);
        } else {
            captureFrame();
        }
    }

    private void updateInputState() {
        if (textureView == null || touchCharInput == null || menuSetting == null) {
            return;
        }
        View rootView = getRootView();
        rootView.getWindowVisibleDisplayFrame(visibleFrame);
        boolean inputActive = touchCharInput.isEnabled() || SDLActivity.isSDLEditKeyboardShown();
        if (!inputActive) {
            dismissedForInput = false;
        }
        boolean occludes = inputActive && rootView.getHeight() > 0
                && rootView.getHeight() * 2 / 3 > visibleFrame.bottom;
        if (!occludes) {
            inputOccludesGame = false;
            setVisibility(GONE);
            return;
        }

        getLocationOnScreen(locationOnScreen);
        int availableHeight = Math.max(0, Math.min(getHeight(),
                visibleFrame.bottom - locationOnScreen[1]));
        int bottomPadding = Math.max(0, getHeight() - availableHeight);
        if (getPaddingBottom() != bottomPadding) {
            setPadding(0, 0, 0, bottomPadding);
        }
        int windowWidth = Math.round(getWidth() * 0.6f);
        ViewGroup.LayoutParams windowParams = magnifierWindow.getLayoutParams();
        if (windowParams.width != windowWidth) {
            windowParams.width = windowWidth;
            magnifierWindow.setLayoutParams(windowParams);
        }
        setVisibility(VISIBLE);
        if (!inputOccludesGame) {
            inputOccludesGame = true;
            setMagnifierVisible(!dismissedForInput && menuSetting.isImeMagnifierAutoShow());
        }
    }

    private void refreshTheme() {
        magnifierWindow.setBackground(roundedBackground(Color.BLACK, 12,
                ThemeEngine.getInstance().getTheme().getColor()));
    }

    private FCLImageButton createIconButton(int iconRes, int descriptionRes) {
        FCLImageButton button = new FCLImageButton(getContext());
        button.setImageResource(iconRes);
        button.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        button.setContentDescription(getContext().getString(descriptionRes));
        button.setFocusable(false);
        return button;
    }

    private GradientDrawable roundedBackground(int color, float radiusDp) {
        return roundedBackground(color, radiusDp, 0);
    }

    private GradientDrawable roundedBackground(int color, float radiusDp, int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radiusDp));
        if (strokeColor != 0) {
            drawable.setStroke(dp(2), strokeColor);
        }
        return drawable;
    }

    private int dp(float value) {
        return ConvertUtils.dip2px(getContext(), value);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(layoutListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (viewTreeObserver != null && viewTreeObserver.isAlive()) {
            viewTreeObserver.removeOnGlobalLayoutListener(layoutListener);
        }
        viewTreeObserver = null;
        super.onDetachedFromWindow();
    }

    private static class MagnifierViewport extends View {

        private static final float MIN_ZOOM = 1.0f;
        private static final float MAX_ZOOM = 4.0f;

        private final Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        private final Rect sourceRect = new Rect();
        private final Rect destinationRect = new Rect();
        private final ScaleGestureDetector scaleDetector;
        @Nullable
        private Bitmap frame;
        private float zoom = MIN_ZOOM;
        private float sourceCenterX;
        private float sourceCenterY = 1.0f;
        private float lastCentroidX;
        private float lastCentroidY;
        private float pendingPanX;
        private float pendingPanY;

        MagnifierViewport(@NonNull Context context) {
            super(context);
            setBackgroundColor(Color.BLACK);
            setFocusable(false);
            setClickable(true);
            scaleDetector = new ScaleGestureDetector(context,
                    new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                        @Override
                        public boolean onScale(@NonNull ScaleGestureDetector detector) {
                            applyGesture(detector.getScaleFactor(), detector.getFocusX(),
                                    detector.getFocusY(), pendingPanX, pendingPanY);
                            return true;
                        }
                    });
        }

        void setFrame(@NonNull Bitmap frame) {
            this.frame = frame;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (frame == null || frame.getWidth() <= 0 || frame.getHeight() <= 0
                    || getWidth() <= 0 || getHeight() <= 0) {
                return;
            }
            int sourceWidth = sourceWidth(zoom);
            int sourceHeight = sourceHeight(zoom);
            float centerX = clampedCenterX(sourceWidth);
            float centerY = clampedCenterY(sourceHeight);
            int left = clamp(Math.round(centerX - sourceWidth / 2.0f),
                    0, frame.getWidth() - sourceWidth);
            int top = clamp(Math.round(centerY - sourceHeight / 2.0f),
                    0, frame.getHeight() - sourceHeight);
            sourceRect.set(left, top, left + sourceWidth, top + sourceHeight);
            destinationRect.set(0, 0, getWidth(), getHeight());
            canvas.drawBitmap(frame, sourceRect, destinationRect, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getActionMasked();
            pendingPanX = 0.0f;
            pendingPanY = 0.0f;
            if (action == MotionEvent.ACTION_DOWN) {
                getParent().requestDisallowInterceptTouchEvent(true);
                lastCentroidX = event.getX();
                lastCentroidY = event.getY();
            } else if (action == MotionEvent.ACTION_POINTER_DOWN) {
                lastCentroidX = centroid(event, true);
                lastCentroidY = centroid(event, false);
            } else if (action == MotionEvent.ACTION_MOVE) {
                float centroidX = centroid(event, true);
                float centroidY = centroid(event, false);
                pendingPanX = centroidX - lastCentroidX;
                pendingPanY = centroidY - lastCentroidY;
                lastCentroidX = centroidX;
                lastCentroidY = centroidY;
            }

            scaleDetector.onTouchEvent(event);
            if (action == MotionEvent.ACTION_MOVE && event.getPointerCount() == 1) {
                applyGesture(1.0f, lastCentroidX, lastCentroidY, pendingPanX, pendingPanY);
            }
            if (action == MotionEvent.ACTION_POINTER_UP) {
                lastCentroidX = centroidExcluding(event, event.getActionIndex(), true);
                lastCentroidY = centroidExcluding(event, event.getActionIndex(), false);
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                finishGesture();
            }
            return true;
        }

        private void applyGesture(float scaleFactor, float focusX, float focusY,
                                  float panX, float panY) {
            if (frame == null || getWidth() <= 0 || getHeight() <= 0) {
                return;
            }
            float oldZoom = zoom;
            float newZoom = clamp(oldZoom * scaleFactor, MIN_ZOOM, MAX_ZOOM);
            float oldCenterX = clampedCenterX(sourceWidth(oldZoom));
            float oldCenterY = clampedCenterY(sourceHeight(oldZoom));
            float focusOffsetX = focusX - getWidth() / 2.0f;
            float focusOffsetY = focusY - getHeight() / 2.0f;
            float newCenterX = oldCenterX + focusOffsetX / oldZoom
                    - focusOffsetX / newZoom - panX / newZoom;
            float newCenterY = oldCenterY + focusOffsetY / oldZoom
                    - focusOffsetY / newZoom - panY / newZoom;
            zoom = newZoom;
            sourceCenterX = clamp(newCenterX / frame.getWidth(), 0.0f, 1.0f);
            sourceCenterY = clamp(newCenterY / frame.getHeight(), 0.0f, 1.0f);
            invalidate();
        }

        private void finishGesture() {
            if (frame == null || getWidth() <= 0 || getHeight() <= 0) {
                return;
            }
            sourceCenterX = clampedCenterX(sourceWidth(zoom)) / frame.getWidth();
            sourceCenterY = clampedCenterY(sourceHeight(zoom)) / frame.getHeight();
        }

        private int sourceWidth(float zoom) {
            if (frame == null) {
                return 1;
            }
            float width = getWidth() / zoom;
            return clamp(Math.round(width * sourceScale(width, getHeight() / zoom)),
                    1, frame.getWidth());
        }

        private int sourceHeight(float zoom) {
            if (frame == null) {
                return 1;
            }
            float height = getHeight() / zoom;
            return clamp(Math.round(height * sourceScale(getWidth() / zoom, height)),
                    1, frame.getHeight());
        }

        private float sourceScale(float width, float height) {
            if (frame == null) {
                return 1.0f;
            }
            return Math.min(1.0f, Math.min(frame.getWidth() / width, frame.getHeight() / height));
        }

        private float clampedCenterX(int sourceWidth) {
            if (frame == null) {
                return 0.0f;
            }
            float halfWidth = sourceWidth / 2.0f;
            return clamp(sourceCenterX * frame.getWidth(), halfWidth,
                    Math.max(halfWidth, frame.getWidth() - halfWidth));
        }

        private float clampedCenterY(int sourceHeight) {
            if (frame == null) {
                return 0.0f;
            }
            float halfHeight = sourceHeight / 2.0f;
            return clamp(sourceCenterY * frame.getHeight(), halfHeight,
                    Math.max(halfHeight, frame.getHeight() - halfHeight));
        }

        private static float centroid(MotionEvent event, boolean x) {
            return centroidExcluding(event, -1, x);
        }

        private static float centroidExcluding(MotionEvent event, int excludedIndex, boolean x) {
            float sum = 0.0f;
            int count = 0;
            for (int i = 0; i < event.getPointerCount(); i++) {
                if (i != excludedIndex) {
                    sum += x ? event.getX(i) : event.getY(i);
                    count++;
                }
            }
            return count == 0 ? 0.0f : sum / count;
        }

        private static float clamp(float value, float min, float max) {
            return Math.max(min, Math.min(max, value));
        }

        private static int clamp(int value, int min, int max) {
            return Math.max(min, Math.min(max, value));
        }
    }
}
