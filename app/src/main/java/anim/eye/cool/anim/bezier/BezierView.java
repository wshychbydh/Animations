package anim.eye.cool.anim.bezier;

import android.animation.AnimatorSet;
import android.view.View;

/**
 * Created by cool on 17-5-10.
 */

public class BezierView {

    private View mView;
    private AnimatorSet mAnimatorSet;

    public AnimatorSet getAnimatorSet() {
        return mAnimatorSet;
    }

    public void setAnimatorSet(AnimatorSet animatorSet) {
        mAnimatorSet = animatorSet;
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        mView = view;
    }

    public void startAnimation() {
        if (mAnimatorSet != null) mAnimatorSet.start();
    }

}
