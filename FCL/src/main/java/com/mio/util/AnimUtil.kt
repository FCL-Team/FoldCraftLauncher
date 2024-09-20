package com.mio.util

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.view.View

class AnimUtil {

    companion object {
        @JvmStatic
        fun playTranslationY(
            view: View,
            duration: Long,
            vararg values: Float
        ): ObjectAnimator {
            return ObjectAnimator.ofFloat(view, "translationY", *values).apply {
                this.duration = duration
            }
        }

        @JvmStatic
        fun playTranslationY(
            views: List<View>,
            duration: Long,
            vararg values: Float
        ): List<ObjectAnimator> {
            return mutableListOf<ObjectAnimator>().apply {
                for (view in views) {
                    add(playTranslationY(view, duration, *values))
                }
            }
        }

        @JvmStatic
        fun playTranslationX(
            view: View,
            duration: Long,
            vararg values: Float
        ): ObjectAnimator {
            return ObjectAnimator.ofFloat(view, "translationX", *values).apply {
                this.duration = duration
            }
        }

        @JvmStatic
        fun playTranslationX(
            views: List<View>,
            duration: Long,
            vararg values: Float
        ): List<ObjectAnimator> {
            return mutableListOf<ObjectAnimator>().apply {
                for (view in views) {
                    add(playTranslationX(view, duration, *values))
                }
            }
        }

        @JvmStatic
        fun playTranslationZ(
            view: View,
            duration: Long,
            vararg values: Float
        ): ObjectAnimator {
            return ObjectAnimator.ofFloat(view, "translationZ", *values).apply {
                this.duration = duration
            }
        }

        @JvmStatic
        fun playTranslationZ(
            views: List<View>,
            duration: Long,
            vararg values: Float
        ): List<ObjectAnimator> {
            return mutableListOf<ObjectAnimator>().apply {
                for (view in views) {
                    add(playTranslationZ(view, duration, *values))
                }
            }
        }

        @JvmStatic
        fun playRotation(
            view: View,
            duration: Long,
            vararg values: Float
        ): ObjectAnimator {
            return ObjectAnimator.ofFloat(view, "rotation", *values).apply {
                this.duration = duration
            }
        }

        @JvmStatic
        fun playScaleX(
            view: View,
            duration: Long,
            vararg values: Float
        ): ObjectAnimator {
            return ObjectAnimator.ofFloat(view, "scaleX", *values).apply {
                this.duration = duration
            }
        }

        @JvmStatic
        fun playScaleX(
            views: List<View>,
            duration: Long,
            vararg values: Float
        ): List<ObjectAnimator> {
            return mutableListOf<ObjectAnimator>().apply {
                for (view in views) {
                    add(playScaleX(view, duration, *values))
                }
            }
        }

        @JvmStatic
        fun playScaleY(
            view: View,
            duration: Long,
            vararg values: Float
        ): ObjectAnimator {
            return ObjectAnimator.ofFloat(view, "scaleY", *values).apply {
                this.duration = duration
            }
        }

        @JvmStatic
        fun playScaleY(
            views: List<View>,
            duration: Long,
            vararg values: Float
        ): List<ObjectAnimator> {
            return mutableListOf<ObjectAnimator>().apply {
                for (view in views) {
                    add(playScaleY(view, duration, *values))
                }
            }
        }

        @JvmStatic
        fun playAlpha(
            view: View,
            duration: Long,
            vararg values: Float
        ): ObjectAnimator {
            return ObjectAnimator.ofFloat(view, "alpha", *values).apply {
                this.duration = duration
            }
        }

        @JvmStatic
        fun ObjectAnimator.delay(delayTime: Long): ObjectAnimator {
            this.startDelay = delayTime
            return this
        }

        @JvmStatic
        fun ObjectAnimator.interpolator(interpolator: TimeInterpolator): ObjectAnimator {
            this.interpolator = interpolator
            return this
        }

        @JvmStatic
        fun ObjectAnimator.startAfter(delayTime: Long) {
            this.startDelay = delayTime
            this.start()
        }
    }
}