package anim.eye.cool.anim.bezier;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.view.View;
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
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * Created by cool on 17-5-10.
 */

public final class BezierAnimationGenerator {

    public static final String ALPHA = "alpha";
    public static final String SCALE_X = "scaleX";
    public static final String SCALE_Y = "scaleY";
    public static final String ROTATION = "rotation";
    public static final String ROTATION_X = "rotationX";
    public static final String ROTATION_Y = "rotationY";

    private static Random sRandom = new Random();

    private static Interpolator[] sInterpolators = new Interpolator[]{
            new AccelerateInterpolator(),
            new DecelerateInterpolator(),
            new LinearInterpolator(),
            new AccelerateDecelerateInterpolator(),
            new AnticipateOvershootInterpolator(),
            new CycleInterpolator(-1),
            new OvershootInterpolator(),
            new BounceInterpolator(),
            new AnticipateInterpolator()
    };

    public static AnimatorSet randomAnimation(final View target, final RelativeLayout container, int defaultDuration) {
        AnimatorSet basicAnimatorSet = buildDefaultAnimatorSet(target, defaultDuration);

        // generate points
        PointF[] points = BezierAnimationGenerator.generateBezierPoints(container.getMeasuredWidth(), container.getMeasuredHeight(), target.getMeasuredHeight());
        return buildBezierAnimatorSet(basicAnimatorSet, points, target, defaultDuration);
    }

    private static AnimatorSet buildDefaultAnimatorSet(View target, int duration) {
        // 1.alpha动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, ALPHA, 0.5f, 1f);
        // 2.缩放动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, SCALE_X, 0.5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, SCALE_Y, 0.5f, 1f);
        // 3.旋转
        ObjectAnimator rotate = ObjectAnimator.ofFloat(target, ROTATION, 0f, 360f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(duration);
        // 同时执行以上4个动画
        set.playTogether(alpha, scaleX, scaleY, rotate);
        set.setTarget(target);
        return set;
    }

    private static AnimatorSet buildBezierAnimatorSet(AnimatorSet basicAnimatorSet, PointF[] points, View target, int duration) {
        ValueAnimator bezierAnimator = buildBezierAnimator(points, target, duration);

        AnimatorSet bezierSet = new AnimatorSet();
        // 序列执行
        // bezierSet.playSequentially(set, bezierAnimator);
        // 在..之前执行
        // bezierSet.play(set).before(bezierAnimator);
        //bezierSet.play(bezierAnimator).after(set);
        // 一起执行
        bezierSet.playTogether(basicAnimatorSet, bezierAnimator);

        //设置随机加速因子
        bezierSet.setInterpolator(randomInterpolator());
        bezierSet.setTarget(target);
        return bezierSet;
    }

    private static ValueAnimator buildBezierAnimator(PointF[] points, final View target, int duration) {

        // 属性动画不仅仅可以改变view的属性，还可以改变自定义的属性
        // 根据不同的估值器可以得到不同的动画效果
        //ValueAnimator animator = ValueAnimator.ofObject(new PointFEvaluator(new PointF(0, 600)), bezierView.getPointF()[0],
        //       bezierView.getPointF()[3]);
        // 估值器Evaluator，来控制view的行驶路径(不断修改pointF.x,pointF.y)
        ValueAnimator animator = ValueAnimator.ofObject(new BezierEvaluator(points[1],
                points[2]), points[0], points[3]);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                target.setX(pointF.x);
                target.setY(pointF.y);
                //TODO 这里可以定制各种各样的动画
                //   bezierView.getView().setRotationX(360 * animation.getAnimatedFraction());
                //   bezierView.getView().setRotationY(180 * animation.getAnimatedFraction());
                //   bezierView.getView().setAlpha(animation.getAnimatedFraction());
            }
        });
        animator.setTarget(target);
        animator.setDuration(duration);
        return animator;
    }

    private static Interpolator randomInterpolator() {
        int sub = sRandom.nextInt(sInterpolators.length);
        return sInterpolators[sub];
    }


    /**
     * @param activeWidth
     * @param activeHeight
     * @param viewHeight   子view的高度
     * @return
     */
    private static PointF[] generateBezierPoints(int activeWidth, int activeHeight, int viewHeight) {
        // 贝塞尔曲线的4个点
        PointF pointF0 = new PointF(sRandom.nextInt(activeWidth), activeHeight - viewHeight);
        PointF pointF1 = generateBezierPointF(activeWidth, activeHeight, 1);
        PointF pointF2 = generateBezierPointF(activeWidth, activeHeight, 2);
        PointF pointF3 = new PointF(sRandom.nextInt(activeWidth), 0);
        return new PointF[]{pointF0, pointF1, pointF2, pointF3};
    }

    private static PointF generateBezierPointF(int activeWidth, int activeHeight, int index) {
        PointF pointF = new PointF();
        // 波动的范围在0~activeWidth之间，也可以波动出去
        pointF.x = sRandom.nextInt(activeWidth / 2);
        if (index == 2) {
            //point2在右半屏
            pointF.x += activeWidth / 2;
        }
        // 为了向上波动，故 point2.y < point1.y
        int height = sRandom.nextInt(activeHeight / 2);
        if (index == 1) {
            height += activeHeight / 2;
        }
        pointF.y = height;
        return pointF;
    }
}
