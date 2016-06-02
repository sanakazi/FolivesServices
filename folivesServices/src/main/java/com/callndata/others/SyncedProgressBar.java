package com.callndata.others;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ProgressBar;

import java.lang.reflect.Field;

/**
 * @author Oleg Vaskevich
 */
public class SyncedProgressBar extends ProgressBar {

    public SyncedProgressBar(Context context) {
        super(context);
        modifyAnimation();
    }

    public SyncedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        modifyAnimation();
    }

    public SyncedProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        modifyAnimation();
    }

    @Override
    public void setVisibility(int v) {
        super.setVisibility(v);
        modifyAnimation();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        modifyAnimation();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        modifyAnimation();
    }

    @Override
    public synchronized void setIndeterminate(boolean indeterminate) {
        super.setIndeterminate(indeterminate);
        modifyAnimation();
    }

    public void modifyAnimation() {
        Field mAnim;
        try {
            mAnim = ProgressBar.class.getDeclaredField("mAnimation");
            mAnim.setAccessible(true);
            AlphaAnimation anim = (AlphaAnimation) mAnim.get(this);
            if (anim == null)
                return;

            // set offset to that animations start at same time
            long duration = anim.getDuration();
            long timeOffset = System.currentTimeMillis() % duration;
            anim.setDuration(duration);
            anim.setStartOffset(-timeOffset);
            anim.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    animation.setStartOffset(0);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }
            });
        } catch (Exception e) {
            Log.d("SPB", "that didn't work out...", e);
            return;
        }
        postInvalidate();
    }

}