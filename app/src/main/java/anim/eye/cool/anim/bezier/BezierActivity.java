package anim.eye.cool.anim.bezier;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import anim.eye.cool.anim.R;

/**
 * Created by cool on 17-5-10.
 */
public class BezierActivity extends Activity {

    private BezierAnimationController mBezierAnimationController = new BezierAnimationController();
    private TextView mCountTv;
    private volatile int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.bezier_container);
        mCountTv = (TextView) findViewById(R.id.tv_count);
        mBezierAnimationController.setContainer(container );
        mBezierAnimationController.setAnimationListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mCountTv.setText("共执行了" + ++mCount + "次动画．");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mBezierAnimationController.start();
    }

    public void speedUp(View view) {
        mBezierAnimationController.decInterval();
    }

    public void speedDown(View view) {
        mBezierAnimationController.incInterval();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBezierAnimationController.start(); // TODO edward 3.0 以后会有更好的生命周期管理，有空可以研究一下。
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBezierAnimationController.stop();
    }
}
