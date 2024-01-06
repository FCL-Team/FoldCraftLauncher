package com.tungsten.fcllibrary.anim;

import android.view.View;
import android.view.animation.AnimationUtils;

public class DisplayAnimUtils {

    public static void showViewFromLeft(View view, boolean animation) {
        view.setVisibility(View.VISIBLE);
        if (animation) {
            view.setAnimation(AnimationUtils.makeInAnimation(view.getContext(), true));
        }
    }

    public static void hideViewToLeft(View view, boolean animation) {
        view.setVisibility(View.GONE);
        if (animation) {
            view.setAnimation(AnimationUtils.makeOutAnimation(view.getContext(), false));
        }
    }

    public static void showViewFromRight(View view, boolean animation) {
        view.setVisibility(View.VISIBLE);
        if (animation) {
            view.setAnimation(AnimationUtils.makeInAnimation(view.getContext(), false));
        }
    }

    public static void hideViewToRight(View view, boolean animation) {
        view.setVisibility(View.GONE);
        if (animation) {
            view.setAnimation(AnimationUtils.makeOutAnimation(view.getContext(), true));
        }
    }

    public static void showViewWithAnim(View view, int animId) {
        view.setVisibility(View.VISIBLE);
        view.setAnimation(AnimationUtils.loadAnimation(view.getContext(), animId));
    }

    public static void hideViewWithAnim(View view, int animId) {
        view.setVisibility(View.GONE);
        view.setAnimation(AnimationUtils.loadAnimation(view.getContext(), animId));
    }
}
