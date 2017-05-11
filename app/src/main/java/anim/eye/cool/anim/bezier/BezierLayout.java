package anim.eye.cool.anim.bezier;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by cool on 17-5-10.
 */
public class BezierLayout extends RelativeLayout {

    private Property mProperty;

    public BezierLayout(Context context) {
        this(context, null);
    }

    public BezierLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mProperty = new Property();
    }

    public Property getProperty() {
        return mProperty;
    }

    private void addBezierView() {
        final BezierView bezier = BezierFactory.get().createBezierView(this);
        bezier.startAnimation();
        if (mProperty.mRecycle) {
            mHandler.sendEmptyMessageDelayed(0, mProperty.mInterval);
        }
    }

    private Handler mHandler = new Handler(new Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            addBezierView();
            return true;
        }
    });

    public void start() {
        mHandler.removeMessages(0);
        mProperty.mRunning = true;
        addBezierView();
    }

    public void stop() {
        mProperty.mRunning = false;
        mHandler.removeMessages(0);
    }

    @Override
    protected void onDetachedFromWindow() {
        stop();
        super.onDetachedFromWindow();
    }

    public class Property {
        private final static int MAX_INTERVAL = 1000;
        private final static int MIN_INTERVAL = 100;

        private final static int MAX_DURATION = 5000;
        private final static int MIN_DURATION = 1000;

        private boolean mRecycle = true; // 是否循环发送
        private volatile int mInterval = 500; // 每间隔多少秒发射一个
        private volatile int mDuration = 3000; // 动画执行的时间

        private volatile boolean mRunning = false; //是否正在执行

        public int getDuration() {
            return mDuration;
        }

        public int getInterval() {
            return mInterval;
        }

        public void addDuration() {
            if (mRunning) {
                mDuration -= 1000;
                mDuration = mDuration < MIN_DURATION ? MIN_DURATION : mDuration;
            }
        }

        public void delDuration() {
            if (mRunning) {
                mDuration += 1000;
                mDuration = mDuration > MAX_DURATION ? MAX_DURATION : mDuration;
            }
        }

        public void addInterval() {
            if (mRunning) {
                mInterval -= 100;
                mInterval = mInterval < MIN_INTERVAL ? MIN_INTERVAL : mInterval;
            }
        }

        public void delInterval() {
            if (mRunning) {
                mInterval += 100;
                mInterval = mInterval > MAX_INTERVAL ? MAX_INTERVAL : mInterval;
            }
        }

        public Property setInterval(int interval) {
            this.mInterval = interval;
            return this;
        }

        public Property setDuration(int duration) {
            this.mDuration = duration;
            return this;
        }

        public Property setRecycle(boolean recycle) {
            this.mRecycle = recycle;
            return this;
        }
    }
}
