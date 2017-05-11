package anim.eye.cool.anim.bezier;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

import anim.eye.cool.anim.R;

/**
 * Created by cool on 17-5-10.
 */

public class BezierFactory {

    private final static int[] BEZIER_RES = new int[]{R.drawable.red, R.drawable.green,
            R.drawable.gray, R.drawable.blue, R.drawable.yellow, R.mipmap.ic_launcher,
            R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5,
            R.drawable.p6, R.drawable.p7};

    private Random mRandom;

    private BezierFactory() {
        mRandom = new Random();
    }

    private static class SingleInstance {
        private static BezierFactory sInstance = new BezierFactory();
    }

    public static BezierFactory get() {
        return SingleInstance.sInstance;
    }

    public BezierView createBezierView(final BezierLayout container) {
        final ImageView iv = new ImageView(container.getContext());
        Drawable drawable = container.getResources().getDrawable(BEZIER_RES[mRandom.nextInt(BEZIER_RES.length)]);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        iv.setLayoutParams(params);
        iv.setImageDrawable(drawable);
        BezierView bezierView = new BezierView();
        bezierView.setView(iv);

        AnimatorSet set = AnimatorHelper.getAnimatorSet(container, iv, mRandom);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 动画结束后，移除iv
                container.removeView(iv);
            }
        });

        bezierView.setAnimatorSet(set);

        container.addView(bezierView.getView());
        return bezierView;
    }
}
