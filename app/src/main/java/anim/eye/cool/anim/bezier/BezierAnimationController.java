package anim.eye.cool.anim.bezier;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by cool on 17-5-10.
 */
// TODO edward 这里继承 relativelayout 并没有实际意义。因为其中做的事情，和 layout 并没有关系，并不需要去修改 Relativelayout。用组合的方式就行。
public class BezierAnimationController {
    private final static int MAX_INTERVAL = 1000;
    private final static int MIN_INTERVAL = 100;
    private final static int MAX_DURATION = 5000;
    private final static int MIN_DURATION = 1000;

    private int mInterval = 500; // 每间隔多少毫秒执行一次子动画
    private int mDuration = 3000; // 每个子动画执行的时间

    private boolean mRunning = false; //是否正在执行

    @Nullable
    private Animator.AnimatorListener mAnimationListener;

    private RelativeLayout mContainer;

    public void setContainer(RelativeLayout container) {
        mContainer = container;
    }

    private void addBezierAnimatedView() {

        // create view
        final View animatedView = AnimatedViewFactory.createView(mContainer);
        mContainer.addView(animatedView);

        // create animation set
        AnimatorSet bezierAnimatorSet = BezierAnimationGenerator.randomAnimation(animatedView, mContainer, mDuration);
        bezierAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 动画结束后，移除iv
                mContainer.removeView(animatedView);
            }
        });
        bezierAnimatorSet.addListener(mAnimationListener);
        bezierAnimatorSet.start();
    }

    private Handler mHandler = new Handler(new Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            addBezierAnimatedView();
            return true;
        }
    });

    public void start() {
        if (mRunning) return;
        mRunning = true;
        mHandler.sendEmptyMessageDelayed(0, mInterval);
    }

    public void stop() {
        mRunning = false;
        mHandler.removeMessages(0);
        mContainer.removeAllViews();
    }

    public void setAnimationListener(@Nullable Animator.AnimatorListener animationListener) {
        mAnimationListener = animationListener;
    }

    public void incDuration() {
        if (mRunning) {
            mDuration += 1000;
            mDuration = mDuration < MIN_DURATION ? MIN_DURATION : mDuration;
        }
    }

    public void decDuration() {
        if (mRunning) {
            mDuration -= 1000;
            mDuration = mDuration > MAX_DURATION ? MAX_DURATION : mDuration;
        }
    }


    public void incInterval() {
        if (mRunning) {
            mInterval += 100;
            mInterval = mInterval < MIN_INTERVAL ? MIN_INTERVAL : mInterval;
        }
    }

    public void decInterval() {
        if (mRunning) {
            mInterval -= 100;
            mInterval = mInterval > MAX_INTERVAL ? MAX_INTERVAL : mInterval;
        }
    }

}
