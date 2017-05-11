package anim.eye.cool.anim.bezier;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by cool on 17-5-10.
 */
public class BezierEvaluator implements TypeEvaluator<PointF> {

    private PointF mPointF1, mPointF2;

    public BezierEvaluator(PointF pointF1, PointF pointF2) {
        this.mPointF1 = pointF1;
        this.mPointF2 = pointF2;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {

        // 贝塞尔曲线的公式
        // fraction取值0~1,每次估值0.005递增
        float temp = 1 - fraction;
        PointF pointF = new PointF();
        pointF.x = startValue.x * temp * temp * temp + 3 * mPointF1.x * fraction
                * temp * temp + 3 * mPointF2.x * fraction * fraction
                * temp + endValue.x * fraction * fraction * fraction;

        pointF.y = startValue.y * temp * temp * temp + 3 * mPointF1.y * fraction
                * temp * temp + 3 * mPointF2.y * fraction * fraction
                * temp + endValue.y * fraction * fraction * fraction;
        return pointF;
    }
}
