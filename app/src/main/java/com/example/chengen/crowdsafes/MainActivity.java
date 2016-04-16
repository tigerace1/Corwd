package com.example.chengen.crowdsafes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = (ImageView)findViewById(R.id.iBLoading);
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_loading);
        imageView.startAnimation(animAlpha);
        animAlpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                if (sharedPref.contains("firstName")) {
                    startActivity(new Intent(MainActivity.this, NavigationMenu.class));
                    finish();
                } else {
                    startActivity(new Intent(MainActivity.this, NavigationMenu.class));
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
