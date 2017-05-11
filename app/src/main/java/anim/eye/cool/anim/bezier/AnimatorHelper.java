package anim.eye.cool.anim.bezier;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.Random;

/**
 * Created by cool on 17-5-10.
 */

public class AnimatorHelper {

    public static final String ALPHA = "alpha";
    public static final String SCALE_X = "scaleX";
    public static final String SCALE_Y = "scaleY";
    public static final String ROTATION = "rotation";

    public static AnimatorSet getAnimatorSet(BezierLayout container, View itemView, Random random) {
        // 1.alpha动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(itemView, ALPHA, 0.1f, 1f);
        // 2.缩放动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(itemView, SCALE_X, 0.1f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(itemView, SCALE_Y, 0.1f, 1f);
        // 3.旋转
        ObjectAnimator rotate = ObjectAnimator.ofFloat(itemView, ROTATION, 0f, 360f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(2000);
        // 同时执行4个动画
        set.playTogether(alpha, scaleX, scaleY, rotate);
        set.setTarget(itemView);

        // 4.贝塞尔曲线动画
        ValueAnimator bezierAnimator = getBezierAnimator(container, itemView, random);
        AnimatorSet bezierSet = new AnimatorSet();
        bezierSet.playTogether(set, bezierAnimator);
        // 序列执行
        // bezierSet.playSequentially(set, bezierAnimator);
        //设置加速因子
        bezierSet.setInterpolator(InterpolatorSets.getInterpolator(random));
        bezierSet.setTarget(itemView);
        return bezierSet;
    }

    private static ValueAnimator getBezierAnimator(BezierLayout container, final View itemView, Random random) {
        // 贝塞尔曲线的4个点(不断修改ImageView的坐标-PointF)
        PointF pointF0 = new PointF((container.getMeasuredWidth() - itemView.getMeasuredWidth()) / 2,
                container.getMeasuredHeight() - itemView.getMeasuredHeight());
        PointF pointF1 = getPointF(container, 1, random);
        PointF pointF2 = getPointF(container, 2, random);
        PointF pointF3 = new PointF(random.nextInt(container.getMeasuredWidth()), 0);

        // 估值器Evaluator，来控制view的行驶路径(不断修改pointF.x,pointF.y)
        BezierEvaluator evaluator = new BezierEvaluator(pointF1, pointF2);
        // 属性动画不仅仅可以改变view的属性，还可以改变自定义的属性
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, pointF0,
                pointF3);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                itemView.setX(pointF.x);
                itemView.setY(pointF.y);
                itemView.setRotation(360 * animation.getAnimatedFraction());
                itemView.setAlpha(animation.getAnimatedFraction());
            }
        });
        animator.setTarget(itemView);
        animator.setDuration(container.getProperty().getDuration());
        return animator;
    }


    private enum InterpolatorSets {
        ACC(new AccelerateInterpolator()), DEC(new DecelerateInterpolator()),
        LINE(new LinearInterpolator()), ACC_DEC(new AccelerateDecelerateInterpolator()),
        AOI(new AnticipateOvershootInterpolator());

        InterpolatorSets(Interpolator interpolator) {
            this.mInterpolator = interpolator;
        }

        private Interpolator mInterpolator;

        public static Interpolator getInterpolator(Random random) {
            int index = random.nextInt(InterpolatorSets.values().length);
            return values()[index].mInterpolator;
        }
    }

    private static PointF getPointF(BezierLayout container, int index, Random random) {
        PointF pointF = new PointF();
        // 波动的范围在layout内部，也可以波动出去
        pointF.x = random.nextInt(container.getMeasuredWidth());
        // 为了向上波动，故保证 point2.y < point1.y
        int height = random.nextInt(container.getMeasuredHeight() / 2);
        if (index == 1) {
            height += container.getMeasuredHeight() / 2;
        }
        pointF.y = height;
        return pointF;
    }
}
