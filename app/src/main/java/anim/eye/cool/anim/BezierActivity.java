package anim.eye.cool.anim;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import anim.eye.cool.anim.bezier.BezierLayout;

/**
 * Created by cool on 17-5-10.
 */
public class BezierActivity extends Activity {

    private BezierLayout mlLoveLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
        mlLoveLayout = (BezierLayout) findViewById(R.id.bezier_container);
    }

    public void start(View v) {
        mlLoveLayout.start();
    }

    public void stop(View v) {
        mlLoveLayout.stop();
    }

    public void speedUp(View view) {
        mlLoveLayout.getProperty().addInterval();
    }

    public void speedDown(View view) {
        mlLoveLayout.getProperty().delInterval();
    }
}
