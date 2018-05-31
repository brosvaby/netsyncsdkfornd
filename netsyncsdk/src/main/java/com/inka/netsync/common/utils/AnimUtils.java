package com.inka.netsync.common.utils;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Scroller;

/**
 * Created by birdgang on 2018. 1. 31..
 */

public class AnimUtils {

    public static final String TAG = "AnimUtils";

    private static final int ANIMATION_DURATION = 250;
    public static final int DURATION = ANIMATION_DURATION; // + 50;

    public static void transTopIn(View view, boolean overshoot, long duration) {
        view.setVisibility(View.VISIBLE);
        translate(view, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, duration, overshoot);
    }

    public static void transTopIn(View view, boolean overshoot) {
        view.setVisibility(View.VISIBLE);
        translate(view, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, ANIMATION_DURATION, overshoot);
    }

    public static void transTopOut(View view, boolean overshoot) {
        view.setVisibility(View.GONE);
        translate(view, 0.0f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f, ANIMATION_DURATION, overshoot);
    }

    public static void transBottomIn(View view, boolean overshoot) {
        view.setVisibility(View.VISIBLE);
        translate(view, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, ANIMATION_DURATION, overshoot);
    }

    public static void transBottomIn(View view, boolean overshoot, long duration) {
        view.setVisibility(View.VISIBLE);
        translate(view, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, duration, overshoot);
    }

    public static void transBottomOut(View view, boolean overshoot, long duration) {
        view.setVisibility(View.GONE);
        translate(view, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, duration, overshoot);
    }

    public static void transBottomOut(View view, boolean overshoot) {
        view.setVisibility(View.GONE);
        translate(view, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, ANIMATION_DURATION, overshoot);
    }

    public static void transLeftIn(View view, boolean overshoot) {
        view.setVisibility(View.VISIBLE);
        AnimUtils.translate(view, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, ANIMATION_DURATION, overshoot);
    }

    public static void transLeftOut(View view, boolean overshoot) {
        view.setVisibility(View.GONE);
        AnimUtils.translate(view, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f, ANIMATION_DURATION, overshoot);
    }

    public static void transLeftOut(View view, boolean overshoot, float startFade, float finishFade) {
        view.setVisibility(View.GONE);
        AnimUtils.translate(view, -0.7f, -1.0f, 0.0f, 0.0f, startFade, finishFade, ANIMATION_DURATION, overshoot);
    }

    public static void transRightIn(View view, boolean overshoot) {
        view.setVisibility(View.VISIBLE);
        AnimUtils.translate(view, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, ANIMATION_DURATION, overshoot);
    }

    public static void transRightOut(View view, boolean overshoot) {
        view.setVisibility(View.GONE);
        AnimUtils.translate(view, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, ANIMATION_DURATION, overshoot);
    }

    public static void translate(View view, float fromX, float toX, float fromY, float toY,
                                 float fromAlpha, float toAlpha, long duration, boolean overshoot) {
        final AnimationSet animSet = new AnimationSet(true);
        if (overshoot) {
            animSet.setInterpolator(AnimationUtils.loadInterpolator(view.getContext(),
                    android.R.anim.overshoot_interpolator));
        } else {
            animSet.setInterpolator(AnimationUtils.loadInterpolator(view.getContext(),
                    android.R.anim.decelerate_interpolator));
        }
        animSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        Animation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, fromX,
                Animation.RELATIVE_TO_SELF, toX,
                Animation.RELATIVE_TO_SELF, fromY,
                Animation.RELATIVE_TO_SELF, toY);
        anim.setDuration(duration);
        animSet.addAnimation(anim);

		/*if (overshoot) {
			anim = new AlphaAnimation(fromAlpha, toAlpha);
			anim.setDuration(duration);
			animSet.addAnimation(anim);
		}*/

        view.startAnimation(animSet);
    }

    public static void scaleIn(View view) {
        scaleIn(view, ANIMATION_DURATION);
    }

    public static void scaleOut(View view) {
        scaleOut(view, ANIMATION_DURATION);
    }

    public static void scaleIn(View view, int duration) {
        view.setVisibility(View.VISIBLE);
        AnimUtils.scale(view, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, android.R.anim.overshoot_interpolator, duration);
    }

    public static void scaleOut(View view, int ANIMATION_DURATION) {
        view.setVisibility(View.GONE);
        AnimUtils.scale(view, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, android.R.anim.overshoot_interpolator, ANIMATION_DURATION);
    }

    public static void scaleIn(View view, int interpolator, int duration) {
        view.setVisibility(View.VISIBLE);
        AnimUtils.scale(view, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, interpolator, duration);
    }

    public static void scaleOut(View view, int interpolator, int duration) {
        view.setVisibility(View.GONE);
        AnimUtils.scale(view, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, interpolator, ANIMATION_DURATION);
    }

    public static void scale(View view, float fromX, float toX, float fromY, float toY, float fromAlpha, float toAlpha, int interpolator, long duration) {
        final AnimationSet animSet = new AnimationSet(true);
        animSet.setFillAfter(true);
        if (interpolator > 0) {
            animSet.setInterpolator(AnimationUtils.loadInterpolator(view.getContext(), interpolator));
        }

        Animation anim = new ScaleAnimation(
                fromX, toX,
                fromY, toY,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(duration);
        animSet.addAnimation(anim);

        anim = new AlphaAnimation(fromAlpha, toAlpha);
        anim.setDuration(duration);
        animSet.addAnimation(anim);

        view.startAnimation(animSet);
    }

    public static void clickX(View view) {
        //click(view, 1.0f, 0.7f, 1.0f, 1.0f);
    }

    public static void clickY(View view) {
        //click(view, 1.0f, 1.0f, 1.0f, 0.7f);
    }

    public static void click(View view) {
        //click(view, 1.0f, 0.95f, 1.0f, 0.95f);
    }

    public static void click(View view, Runnable runnable) {
        //click(view, 1.0f, 0.95f, 1.0f, 0.95f);
        view.post(runnable);
    }

    public static void click(final View view, final float fromX, final float toX, final float fromY, final float toY) {
        Animation anim = new ScaleAnimation(
                fromX, toX, fromY, toY,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(AnimationUtils.loadInterpolator(view.getContext(),
                android.R.anim.linear_interpolator));
        anim.setDuration(100);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation anim = new ScaleAnimation(
                        toX, fromX, toY, fromY,
                        ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                        ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                anim.setInterpolator(AnimationUtils.loadInterpolator(view.getContext(),
                        android.R.anim.linear_interpolator));
                anim.setDuration(100);
                view.startAnimation(anim);
            }
        });

        view.startAnimation(anim);
    }

    public static void fadeIn(View view) {
        fadeIn(view, ANIMATION_DURATION);
    }

    public static void fadeOut(View view) {
        fadeOut(view, ANIMATION_DURATION);
    }

    public static void fadeIn(View view, int duration) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
            AnimUtils.fade(view, 0.0f, 1.0f, duration);
        }
    }

    public static void fadeOut(View view, int duration) {
        if (view.getVisibility() != View.GONE) {
            view.setVisibility(View.GONE);
            AnimUtils.fade(view, 1.0f, 0.0f, duration);
        }
    }

    public static void forceFadeIn(View view) {
        view.setVisibility(View.GONE);
        fadeIn(view, ANIMATION_DURATION);
    }

    public static void fade(View view, float fromAlpha, float toAlpha, long duration) {
        final Animation anim = new AlphaAnimation(fromAlpha, toAlpha);
        anim.setDuration(duration);

        view.startAnimation(anim);
    }

    public static void listIn(View view) {
        listIn(view, ANIMATION_DURATION);
    }

    public static void listIn(View view, int duration) {
        rotateX(view, 45.0f, 0.0f, view.getWidth() / 2.0f, view.getHeight() / 2.0f, 300.0f, duration);
    }

    public static void rotate(final View view, final float pivotX, final float pivotY) {
        final Rotate3DAnimation animation = new Rotate3DAnimation(0, 0, 0, 90, 0, 0, pivotX, pivotY, 250.0f, true);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setInterpolator(new AnticipateInterpolator());
        view.startAnimation(animation);
    }

    public static void rotateX(View view, float fromDegrees, float toDegrees) {
        rotate(view, fromDegrees, toDegrees, 0.0f, 0.0f, 0.0f, 0.0f,
                view.getWidth() / 2.0f, view.getHeight() / 2.0f, 300.0f, ANIMATION_DURATION);
    }

    public static void rotateX(View view, float fromDegrees, float toDegrees, float centerX, float centerY, float depthZ, int duration) {
        rotate(view, fromDegrees, toDegrees, 0.0f, 0.0f, 0.0f, 0.0f,
                centerX, centerY, depthZ, duration);
    }

    public static void rotateY(View view, float fromDegrees, float toDegrees, float centerX, float centerY, float depthZ, int duration) {
        rotate(view, 0.0f, 0.0f, fromDegrees, toDegrees, 0.0f, 0.0f,
                centerX, centerY, depthZ, duration);
    }

    public static void rotateZ(View view, float fromDegrees, float toDegrees) {
        rotate(view, 0.0f, 0.0f, 0.0f, 0.0f, fromDegrees, toDegrees,
                view.getWidth() / 2.0f, view.getHeight() / 2.0f, 0.0f, ANIMATION_DURATION);
    }

    public static void rotateZ(View view, float fromDegrees, float toDegrees, float centerX, float centerY, float depthZ, int duration) {
        rotate(view, 0.0f, 0.0f, 0.0f, 0.0f, fromDegrees, toDegrees,
                centerX, centerY, depthZ, duration);
    }

    public static void scale(View view, float from, float to) {
        rotate(view, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, view.getWidth() / 2.0f, view.getHeight() / 2.0f, 0.0f,
                0.0f, 0.0f, from, to, ANIMATION_DURATION);
    }

    public static void rotate(View view, float fromX, float toX,
                              float fromY, float toY, float fromZ, float toZ,
                              float centerX, float centerY, float depthZ, int duration) {
        final Animation anim = new Rotate3DAnimation(fromX, toX, fromY, toY, fromZ, toZ,
                centerX, centerY, depthZ, false);
		/*anim.setInterpolator(AnimationUtils.loadInterpolator(view.getContext(),
				android.R.anim.decelerate_interpolator));*/
        anim.setDuration(duration);
        view.startAnimation(anim);
    }

    public static void rotate(View view, float fromX, float toX,
                              float fromY, float toY, float fromZ, float toZ,
                              float centerX, float centerY, float depthZ,
                              float transX, float transY,
                              float scaleFrom, float scaleTo,
                              int duration) {
        final AnimationSet animSet = new AnimationSet(true);
		/*animSet.setInterpolator(AnimationUtils.loadInterpolator(view.getContext(),
				android.R.anim.accelerate_decelerate_interpolator));*/

        Animation anim = new Rotate3DAnimation(fromX, toX, fromY, toY, fromZ, toZ, centerX, centerY, depthZ, false);
        anim.setDuration(duration);
        animSet.addAnimation(anim);

        anim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        anim.setDuration(duration);
        animSet.addAnimation(anim);

        anim = new ScaleAnimation(
                scaleFrom, scaleTo,
                scaleFrom, scaleTo,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(duration);
        animSet.addAnimation(anim);

        view.startAnimation(animSet);
    }

    public static void resizeX(View view, int dx) {
        new LayoutResizer(view).startX(dx);
    }

    public static void resizeY(View view, int dy) {
        new LayoutResizer(view).startY(dy);
    }

    public static void resizeXY(View view, int dx, int dy) {
        new LayoutResizer(view).startXY(dx, dy);
    }

    public static void resizeX(View view, int dx, SimpleAnimationCallback cb) {
        new LayoutResizer(view).setCallback(cb).startX(dx);
    }

    public static void resizeX(View view, int dx, int duration, SimpleAnimationCallback cb) {
        new LayoutResizer(view).setCallback(cb).setDuration(duration).startX(dx);
    }

    public static void resizeX(View view, int dx, int duration, int interpolator, SimpleAnimationCallback cb) {
        new LayoutResizer(view).setCallback(cb).setDuration(duration).setInterpolator(interpolator).startX(dx);
    }

    public static void resizeY(View view, int dy, SimpleAnimationCallback cb) {
        new LayoutResizer(view).setCallback(cb).startY(dy);
    }

    public static void resizeY(View view, int dy, int duration, SimpleAnimationCallback cb) {
        new LayoutResizer(view).setCallback(cb).setDuration(duration).startY(dy);
    }
    public static void resizeY(View view, int dy, int duration) {
        new LayoutResizer(view).setDuration(duration).startY(dy);
    }

    public static void resizeY(View view, int dy, int duration, int interpolator, SimpleAnimationCallback cb) {
        new LayoutResizer(view).setCallback(cb).setDuration(duration).setInterpolator(interpolator).startY(dy);
    }

    public static class LayoutResizer implements Runnable {
        Scroller scroller;

        final View view;
        final ViewGroup.LayoutParams params;

        int mode;
        int origX, origY;
        int duration = ANIMATION_DURATION;
        int interpolator;

        SimpleAnimationCallback callback;

        public LayoutResizer(View v) {
            view = v;
            params = v.getLayoutParams();
        }

        public LayoutResizer setInterpolator(int interpolator) {
            this.interpolator = interpolator;
            return this;
        }

        public LayoutResizer setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public LayoutResizer setCallback(SimpleAnimationCallback cb) {
            callback = cb;
            return this;
        }

        public void startX(int x) {
            origX = params.width;
            if (origX == x) {
                if (callback != null) {
                    callback.onFinished();
                }
                return;
            }

            mode = 0;
            int dx = x - origX;

            start(0, 0, dx, 0, duration);
        }

        public void startY(int y) {
            origY = params.height;
            if (origY == y) {
                if (callback != null) {
                    callback.onFinished();
                }
                return;
            }

            mode = 1;
            int dy = y - origY;

            start(0, 0, 0, dy, duration);
        }

        public void startXY(int x, int y) {
            origX = params.width;
            origY = params.height;
            if ((origX == x) && (origY == y)) {
                if (callback != null) {
                    callback.onFinished();
                }
                return;
            }

            mode = 2;

            int dx = x - origX;
            int dy = y - origY;

            start(0, 0, dx, dy, duration);
        }

        private void start(int startX, int startY, int dx, int dy, int duration) {
            final Context context = view.getContext();
            if (interpolator > 0) {
                scroller = new Scroller(view.getContext(),
                        AnimationUtils.loadInterpolator(context, interpolator));
            } else {
                scroller = new Scroller(context);
            }

            scroller.startScroll(startX, startY, dx, dy, duration);
            view.post(this);
        }

        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                switch (mode) {
                    case 0:
                        params.width = origX + scroller.getCurrX();
                        break;
                    case 1:
                        params.height = origY + scroller.getCurrY();
                        break;
                    default:
                        params.width = origX + scroller.getCurrX();
                        params.height = origY + scroller.getCurrY();
                        break;
                }

                view.setLayoutParams(params);

                if (callback != null) {
                    callback.onProgress();
                }

                view.invalidate();
                view.post(this);
            } else {
                view.removeCallbacks(this);

                if (callback != null) {
                    callback.onFinished();
                }
            }
        }
    }


    public static class LayoutTranslator implements Runnable {
        final Scroller scroller;
        View view;
        float transX;

        public LayoutTranslator(Context context) {
            scroller = new Scroller(context);
        }

        public void startTranslateX(View view, float startX, float destX) {
            this.view = view;

            transX = startX;

            int dx = (int)(destX - startX);
            //LogUtils.msg(Level.D, TAG, "startTranslate view=" + view + ", dx=" + dx + ", transX=" + transX);

            if (dx == 0)
                return;

            scroller.startScroll(0, 0, dx, 0, ANIMATION_DURATION);
            view.post(this);
        }

        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                //LogUtils.msg(Level.D, TAG, "view=" + view + ", x=" + (transX + scroller.getCurrX()));
                view.setTranslationX(transX + scroller.getCurrX());

                view.removeCallbacks(this);
                view.post(this);
            } else {
                view.removeCallbacks(this);
            }
        }
    }


    public static class LayoutScroller implements Runnable {
        final Scroller scroller;
        View view;
        int origX;

        public LayoutScroller(Context context) {
            scroller = new Scroller(context);
        }

        public void startScroll(View v, int x) {
            view = v;

            origX = v.getScrollX();
            if (origX == x)
                return;

            int dx = x - origX;

            scroller.startScroll(0, 0, dx, 0, ANIMATION_DURATION);
            view.post(this);
        }

        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                view.scrollTo(origX + scroller.getCurrX(), 0);
                view.invalidate();
                view.removeCallbacks(this);
                view.post(this);
            } else {
                view.removeCallbacks(this);
            }
        }
    }


    public static class Rotate3DAnimation extends Animation {
        final Camera camera = new Camera();
        final float fromDegreesX;
        final float toDegreesX;
        final float fromDegreesY;
        final float toDegreesY;
        final float fromDegreesZ;
        final float toDegreesZ;
        final float centerX;
        final float centerY;
        final float depthZ;
        final boolean reverse;

        public Rotate3DAnimation(float fromDegreesX, float toDegreesX,
                                 float fromDegreesY, float toDegreesY,
                                 float fromDegreesZ, float toDegreesZ,
                                 float centerX, float centerY, float depthZ, boolean reverse) {
            this.fromDegreesX = fromDegreesX;
            this.toDegreesX = toDegreesX;
            this.fromDegreesY = fromDegreesY;
            this.toDegreesY = toDegreesY;
            this.fromDegreesZ = fromDegreesZ;
            this.toDegreesZ = toDegreesZ;
            this.centerX = centerX;
            this.centerY = centerY;
            this.depthZ = depthZ;
            this.reverse = reverse;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final Matrix matrix = t.getMatrix();

            camera.save();

            // Depth
            camera.translate(0.0f, 0.0f, reverse ? depthZ * interpolatedTime : depthZ * (1.0f - interpolatedTime));

            float degrees = fromDegreesX + ((toDegreesX - fromDegreesX) * interpolatedTime);
            camera.rotateX(degrees);
            degrees = fromDegreesY + ((toDegreesY - fromDegreesY) * interpolatedTime);
            camera.rotateY(degrees);
            degrees = fromDegreesZ + ((toDegreesZ - fromDegreesZ) * interpolatedTime);
            camera.rotateZ(degrees);

            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }

    public static final class SwapViews implements Runnable {
        final ViewGroup container;
        final View inView, outView;
        final boolean reverse;

        public SwapViews(ViewGroup container, View inView, View outView, boolean reverse) {
            this.container = container;
            this.inView = inView;
            this.outView = outView;
            this.reverse = reverse;
        }

        @Override
        public void run() {
            final float centerX = container.getWidth() / 2.0f;
            final float centerY = container.getHeight() / 2.0f;
            final Rotate3DAnimation animation;

            outView.setVisibility(View.GONE);
            inView.setVisibility(View.VISIBLE);
            inView.requestFocus();

            if (!reverse) {
                animation = new Rotate3DAnimation(270, 360, 0, 0, 0, 0, centerX, centerY, 100.0f, false);
            } else {
                animation = new Rotate3DAnimation(90, 0, 0, 0, 0, 0, centerX, centerY, 100.0f, false);
            }
            animation.setDuration(250);
            animation.setFillAfter(true);
            animation.setInterpolator(new DecelerateInterpolator());

            container.startAnimation(animation);
        }

    }

    public static interface AnimationCallback {
        public void onProgress();
        public void onFinished();
    }

    public static class SimpleAnimationCallback implements AnimationCallback {
        @Override
        public void onProgress() {}
        @Override
        public void onFinished() {}
    }
}
