package com.tungsten.fcllibrary.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.View;

import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLDynamicIsland;

public class DynamicIslandAnim {

    private final FCLDynamicIsland view;

    private int mark;
    private Thread thread;
    private Handler handler;
    private ObjectAnimator expandScaleAnimatorX;
    private ObjectAnimator shrinkScaleAnimatorX;
    private ObjectAnimator expandScaleAnimatorY;
    private ObjectAnimator shrinkScaleAnimatorY;
    private ObjectAnimator expandAdjustAnimatorX;
    private ObjectAnimator shrinkAdjustAnimatorX;
    private ObjectAnimator expandAdjustAnimatorY;
    private ObjectAnimator shrinkAdjustAnimatorY;
    private ObjectAnimator hideAnimator;

    public DynamicIslandAnim(FCLDynamicIsland view) {
        this.view = view;
        this.handler = new Handler();
    }

    public void refresh(float scale) {
        mark++;
        if (thread != null) {
            thread.interrupt();
        }
        if (expandScaleAnimatorX != null && expandScaleAnimatorX.isRunning()) {
            expandScaleAnimatorX.cancel();
        }
        if (shrinkScaleAnimatorX != null && shrinkScaleAnimatorX.isRunning()) {
            shrinkScaleAnimatorX.cancel();
        }
        if (expandScaleAnimatorY != null && expandScaleAnimatorY.isRunning()) {
            expandScaleAnimatorY.cancel();
        }
        if (shrinkScaleAnimatorY != null && shrinkScaleAnimatorY.isRunning()) {
            shrinkScaleAnimatorY.cancel();
        }
        if (expandAdjustAnimatorX != null && expandAdjustAnimatorX.isRunning()) {
            expandAdjustAnimatorX.cancel();
        }
        if (shrinkAdjustAnimatorX != null && shrinkAdjustAnimatorX.isRunning()) {
            shrinkAdjustAnimatorX.cancel();
        }
        if (expandAdjustAnimatorY != null && expandAdjustAnimatorY.isRunning()) {
            expandAdjustAnimatorY.cancel();
        }
        if (shrinkAdjustAnimatorY != null && shrinkAdjustAnimatorY.isRunning()) {
            shrinkAdjustAnimatorY.cancel();
        }
        if (hideAnimator != null && hideAnimator.isRunning()) {
            hideAnimator.cancel();
        }
        int animationSpeed = ThemeEngine.getInstance().getTheme().getAnimationSpeed() * 100;
        int animationSpeedPri = (animationSpeed * 3) / 10;
        int animationSpeedSec = animationSpeed / 5;
        expandScaleAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", scale / 2f, 0.95f).setDuration(animationSpeedPri);
        shrinkScaleAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 0.95f, scale / 2f).setDuration(animationSpeedPri);
        expandScaleAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 0.95f).setDuration(animationSpeedPri);
        shrinkScaleAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 0.95f, 0.5f).setDuration(animationSpeedPri);
        expandAdjustAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 0.95f, 1f).setDuration(animationSpeedSec);
        shrinkAdjustAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.95f).setDuration(animationSpeedSec);
        expandAdjustAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 0.95f, 1f).setDuration(animationSpeedSec);
        shrinkAdjustAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.95f).setDuration(animationSpeedSec);
        hideAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f).setDuration(2000);
    }

    public void run(String text) {
        final int i = mark;
        view.setVisibility(View.VISIBLE);
        view.setAlpha(1f);
        shrinkScaleAnimatorX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.refresh(text);
                view.post(() -> {
                    expandScaleAnimatorX.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            expandAdjustAnimatorX.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    shrinkAdjustAnimatorX.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            thread = new Thread(() -> {
                                                try {
                                                    if (i == mark) {
                                                        Thread.sleep(2000);
                                                    }
                                                    if (!thread.isInterrupted() && i == mark) {
                                                        handler.post(() -> {
                                                            hideAnimator.addListener(new AnimatorListenerAdapter() {
                                                                @Override
                                                                public void onAnimationEnd(Animator animation) {
                                                                    super.onAnimationEnd(animation);
                                                                    view.setVisibility(View.GONE);
                                                                }
                                                            });
                                                            hideAnimator.start();
                                                        });
                                                    }
                                                } catch (InterruptedException ignore) {
                                                }
                                            });
                                            thread.start();
                                        }
                                    });
                                    shrinkAdjustAnimatorX.start();
                                    shrinkAdjustAnimatorY.start();
                                }
                            });
                            expandAdjustAnimatorX.start();
                            expandAdjustAnimatorY.start();
                        }
                    });
                    expandScaleAnimatorX.start();
                    expandScaleAnimatorY.start();
                });
            }
        });
        shrinkScaleAnimatorX.start();
        shrinkScaleAnimatorY.start();
    }
}
