package com.example.chengen.crowdsafes;

import android.app.Activity;
import android.content.Intent;
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
        Thread welcomeThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(7000);
                } catch (Exception e) {

                } finally {
                    Intent i = new Intent(MainActivity.this, NavigationMenu.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}
