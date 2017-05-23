package anim.eye.cool.anim.bezier;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

import anim.eye.cool.anim.R;

/**
 * Created by cool on 17-5-10.
 */

public final class AnimatedViewFactory {

    private final static int[] BEZIER_RES = new int[]{R.drawable.red, R.drawable.green,
            R.drawable.gray, R.drawable.blue, R.drawable.yellow, R.mipmap.ic_launcher,
            R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5,
            R.drawable.p6, R.drawable.p7};

    private static Random mRandom = new Random();

    private AnimatedViewFactory() {
    }

    /**
     * 创建View只需要传Context即可，但是考虑到连续调用会用到其他属性值．
     * 如若用一个包装类来传递又多此一举，故将父容器作为参数．
     *
     * @param container
     * @return
     */
    public static View createView(@NonNull final RelativeLayout container) {
        final ImageView iv = new ImageView(container.getContext());
        Drawable drawable = container.getResources().getDrawable(BEZIER_RES[mRandom.nextInt(BEZIER_RES.length)]);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        iv.setLayoutParams(params);
        iv.setImageDrawable(drawable);
        return iv;
    }
}
