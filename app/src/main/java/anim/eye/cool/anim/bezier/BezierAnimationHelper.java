package anim.eye.cool.anim.bezier;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import java.util.Random;

/**
 * Created by cool on 17-5-10.
 */

public final class BezierAnimationHelper {

    public static final String ALPHA = "alpha";
    public static final String SCALE_X = "scaleX";
    public static final String SCALE_Y = "scaleY";
    public static final String ROTATION = "rotation";
    public static final String ROTATION_X = "rotationX";
    public static final String ROTATION_Y = "rotationY";

    public static AnimatorSet getAnimatorSet(BezierView bezierView, Random random) {
        // TODO edward 这里并不需要 random 这个依赖。我猜是为了重复利用 random 来达到提升性能的目的。
        // 1.alpha动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(bezierView.getView(), ALPHA, 0.5f, 1f);
        // 2.缩放动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(bezierView.getView(), SCALE_X, 0.5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(bezierView.getView(), SCALE_Y, 0.5f, 1f);
        // 3.旋转
        ObjectAnimator rotate = ObjectAnimator.ofFloat(bezierView.getView(), ROTATION, 0f, 360f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(bezierView.getDuration() / 2);
        // 同时执行以上4个动画
        set.playTogether(alpha, scaleX, scaleY, rotate);
        set.setTarget(bezierView.getView());

        // 4.贝塞尔曲线动画
        ValueAnimator bezierAnimator = getBezierAnimator(bezierView);
        AnimatorSet bezierSet = new AnimatorSet();
        // 序列执行
        // bezierSet.playSequentially(set, bezierAnimator);
        // 在..之前执行
        // bezierSet.play(set).before(bezierAnimator);
        //bezierSet.play(bezierAnimator).after(set);
        // 一起执行
        bezierSet.playTogether(set, bezierAnimator);

        //设置随机加速因子
        bezierSet.setInterpolator(InterpolatorSets.getInterpolator(random));
        bezierSet.setTarget(bezierView.getView());
        return bezierSet;
    }

    private static ValueAnimator getBezierAnimator(final BezierView bezierView) {

        // 属性动画不仅仅可以改变view的属性，还可以改变自定义的属性
        // 根据不同的估值器可以得到不同的动画效果
        //ValueAnimator animator = ValueAnimator.ofObject(new PointFEvaluator(new PointF(0, 600)), bezierView.getPointF()[0],
        //       bezierView.getPointF()[3]);
        // 估值器Evaluator，来控制view的行驶路径(不断修改pointF.x,pointF.y)
        ValueAnimator animator = ValueAnimator.ofObject(new BezierEvaluator(bezierView.getPointF()[1],
                bezierView.getPointF()[2]), bezierView.getPointF()[0], bezierView.getPointF()[3]);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                bezierView.getView().setX(pointF.x);
                bezierView.getView().setY(pointF.y);
                //TODO 这里可以定制各种各样的动画
                //   bezierView.getView().setRotationX(360 * animation.getAnimatedFraction());
                //   bezierView.getView().setRotationY(180 * animation.getAnimatedFraction());
                //   bezierView.getView().setAlpha(animation.getAnimatedFraction());
            }
        });
        animator.setTarget(bezierView.getView());
        animator.setDuration(bezierView.getDuration());
        return animator;
    }


    private enum InterpolatorSets {
        // TODO edward enum 最好是每行一个
        // TODO edward 其实这里用 enum 并不是一个好主意。从这里的使用方式来看，你只是需要一个随机加速因子的生成器。比如 generate random interpolator
        ACCELERATE(new AccelerateInterpolator()),
        DECELERATE(new DecelerateInterpolator()),
        LINEAR(new LinearInterpolator()),
        ACCELERATE_DECELERATE(new AccelerateDecelerateInterpolator()),
        ANTICIPATE_OVERSHOOT(new AnticipateOvershootInterpolator()),
        CYCLE(new CycleInterpolator(-1)),
        OVERSHOOT(new OvershootInterpolator()),
        BOUNCE(new BounceInterpolator()),
        ANTICIPATE(new AnticipateInterpolator());

        InterpolatorSets(Interpolator interpolator) {
            this.mInterpolator = interpolator;
        }

        private Interpolator mInterpolator;

        public static Interpolator getInterpolator(Random random) {
            int index = random.nextInt(InterpolatorSets.values().length);
            return values()[index].mInterpolator;
        }
    }
}
