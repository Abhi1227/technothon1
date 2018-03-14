package com.rbbn.technothon.RbbnEMS;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
//        Demo demo = Demo.values()[0];
//        demo.startActivity(this);
        Button nextButton = (Button) findViewById(R.id.nextBtn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.d("Message", "Button clicked");
//                Demo demo = Demo.values()[0];
//                demo.startActivity(getApplicationContext());
                presentActivity(v);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.nextBtn) {


        }


    }


    public void presentActivity(View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent = new Intent(this, DemoLikeTumblrActivity.class);
        intent.putExtra(DemoLikeTumblrActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(DemoLikeTumblrActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }
}