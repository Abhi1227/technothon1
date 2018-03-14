package com.rbbn.technothon.RbbnEMS;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;
import java.util.List;

public class DemoLikeTumblrActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String KEY_DEMO = "demo";
    Toast toast = null;
    ClipRevealFrame menuLayout;
    ArcLayout arcLayout;
    View centerItem;
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    View rootLayout;
    private int revealX;
    private int revealY;

    String[] nodeList;
    String[] drawables = {"round_button_blue", "round_button_green", "round_button_blue", "round_button_grey", "round_button_orange", "round_button_white"};

    //    public static void startActivity(Context context, Demo demo) {
//        Intent intent = new Intent(context, DemoLikeTumblrActivity.class);
//        intent.putExtra(KEY_DEMO, demo.name());
//        context.startActivity(intent);
//    }
//
//    private static Demo getDemo(Intent intent) {
//        return Demo.valueOf(intent.getStringExtra(KEY_DEMO));
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_like_tumblr);
        rootLayout = findViewById(R.id.root_layout);
        menuLayout = (ClipRevealFrame) findViewById(R.id.menu_layout);
        arcLayout = (ArcLayout) findViewById(R.id.arc_layout);
        nodeList = getResources().getStringArray(R.array.node_list);
        centerItem = findViewById(R.id.center_item);
        final Intent intent = getIntent();
        final float scale = this.getResources().getDisplayMetrics().density;

        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
            rootLayout.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);


            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            rootLayout.setVisibility(View.VISIBLE);
        }
        int size_pixels = (int) (100 * scale + 0.5f);
        int padding_pixels = (int) (20 * scale + 0.5f);
        for (int i = 0; i < nodeList.length; i++) {
            Button emsbttn = new Button(new ContextThemeWrapper(this, R.style.Item_RoundCircle), null, 0);
            emsbttn.setText(nodeList[i]);
            emsbttn.setTextSize(getResources().getDimension(R.dimen.item_font_size_wheel));
            emsbttn.setWidth(size_pixels);
            emsbttn.setHeight(size_pixels);

            emsbttn.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
//            emsbttn.setCompoundDrawablePadding(padding_pixels);
            emsbttn.setGravity(Gravity.CENTER);
//            emsbttn.setPadding(0,padding_pixels,0,0);
//            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) emsbttn.getLayoutParams();
//            params.gravity = Gravity.CENTER;
            emsbttn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_server1, 0, 0);

            String btnDrawableName;
            int j = i;
            while (j >= drawables.length) {
                j = j - drawables.length;
            }
            btnDrawableName = drawables[j];
            emsbttn.setBackground(getResources().getDrawable(getResources().getIdentifier(btnDrawableName, "drawable", getPackageName())));
            arcLayout.addView(emsbttn);
        }
        centerItem.setOnClickListener(this);
        for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
            arcLayout.getChildAt(i).setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            showToast((Button) v);
        }

    }

    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);
            List<Animator> animList = new ArrayList<>();
            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(400);
            circularReveal.setInterpolator(new DecelerateInterpolator());

            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
//            circularReveal.start();
            animList.add(circularReveal);
            menuLayout.setVisibility(View.VISIBLE);

            animList.add(createShowItemAnimator(centerItem));

            for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
                animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
            }

            AnimatorSet animSet = new AnimatorSet();
            animSet.playSequentially(animList);
            animSet.start();
        } else {
            finish();
        }
    }

    protected void unRevealActivity() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish();
        } else {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);
            List<Animator> animList = new ArrayList<>();

            for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
                animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
            }

            animList.add(createHideItemAnimator(centerItem));
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    rootLayout, revealX, revealY, finalRadius, 0);

            circularReveal.setDuration(400);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    menuLayout.setVisibility(View.INVISIBLE);
                    rootLayout.setVisibility(View.INVISIBLE);
                    finish();
                }
            });
//            circularReveal.start();
            animList.add(circularReveal);

            AnimatorSet animSet = new AnimatorSet();
            animSet.playSequentially(animList);
            animSet.start();

        }
    }

    private void showToast(Button btn) {
        if (toast != null) {
            toast.cancel();
        }

        String text = "Clicked: " + btn.getText();
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();

    }


    private Animator createShowItemAnimator(View item) {
        float dx = centerItem.getX() - item.getX();
        float dy = centerItem.getY() - item.getY();

        item.setScaleX(0f);
        item.setScaleY(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.scaleX(0f, 1f),
                AnimatorUtils.scaleY(0f, 1f),
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)
        );

        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(50);
        return anim;
    }

    private Animator createHideItemAnimator(final View item) {
        final float dx = centerItem.getX() - item.getX();
        final float dy = centerItem.getY() - item.getY();

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.scaleX(1f, 0f),
                AnimatorUtils.scaleY(1f, 0f),
                AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy)
        );

        anim.setInterpolator(new DecelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });
        anim.setDuration(50);
        return anim;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        unRevealActivity();
    }
}
