package com.tungsten.fcllibrary.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;

import com.tungsten.fcllibrary.component.view.FCLDynamicIsland;

public class DynamicIslandAnim {

    private final FCLDynamicIsland view;

    private ObjectAnimator expandScaleAnimatorX;
    private ObjectAnimator shrinkScaleAnimatorX;
    private ObjectAnimator expandScaleAnimatorY;
    private ObjectAnimator shrinkScaleAnimatorY;
    private ObjectAnimator expandAdjustAnimatorX;
    private ObjectAnimator shrinkAdjustAnimatorX;
    private ObjectAnimator expandAdjustAnimatorY;
    private ObjectAnimator shrinkAdjustAnimatorY;

    public DynamicIslandAnim(FCLDynamicIsland view) {
        this.view = view;
    }

    public void refresh(float scale) {
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
        expandScaleAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", scale, 0.95f).setDuration(300);
        shrinkScaleAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 0.95f, scale).setDuration(300);
        expandScaleAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 0.95f, 0.95f).setDuration(300);
        shrinkScaleAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 0.95f, 0.95f).setDuration(300);
        expandAdjustAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 0.95f, 1f).setDuration(200);
        shrinkAdjustAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.95f).setDuration(200);
        expandAdjustAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 0.95f, 1f).setDuration(200);
        shrinkAdjustAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.95f).setDuration(200);
    }

    public void run(String text) {
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
