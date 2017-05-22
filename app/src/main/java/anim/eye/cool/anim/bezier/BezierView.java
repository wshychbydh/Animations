package anim.eye.cool.anim.bezier;

import android.animation.AnimatorSet;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by cool on 17-5-10.
 */

public class BezierView { // TODO edward 名字取的不合适。看代码的话，这里是希望把一个 view 与相应的动画属性存在一起。

    private View mView;  //执行动画的view

    private PointF[] mPointF = new PointF[3]; //贝塞尔公式需要的4个基点

    private Rect mActiveRect = new Rect();  //动画区间

    private int mDuration;  //执行动画时间

    private AnimatorSet mAnimatorSet; // TODO edward 这个并没有用到

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public AnimatorSet getAnimatorSet() {
        return mAnimatorSet;
    }

    public void setAnimatorSet(AnimatorSet animatorSet) {
        mAnimatorSet = animatorSet;
    }

    public Rect getActiveRect() {
        return mActiveRect;
    }

    public void setActiveRect(Rect activeRect) {
        mActiveRect = activeRect;
    }

    public PointF[] getPointF() {
        return mPointF;
    }

    public void setPointF(PointF[] pointF) {
        mPointF = pointF;
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        mView = view;
    }

    public int getActiveWith() { // TODO edward With -> Width
        return mActiveRect.right - mActiveRect.left;
    }

    public int getActiveHeight() {
        return mActiveRect.bottom - mActiveRect.top;
    }
}
