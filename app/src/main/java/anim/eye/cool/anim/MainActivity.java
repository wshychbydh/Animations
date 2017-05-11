package anim.eye.cool.anim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import anim.eye.cool.anim.bezier.BezierActivity;
import anim.eye.cool.anim.keyframe.KeyframeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goBezier(View view) {
        startActivity(new Intent(this, BezierActivity.class));
    }

    public void goKeyframe(View view) {
        startActivity(new Intent(this, KeyframeActivity.class));
    }
}
