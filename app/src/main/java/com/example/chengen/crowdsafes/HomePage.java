package com.example.chengen.crowdsafes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

public class HomePage extends Fragment implements View.OnClickListener, Animation.AnimationListener {
    private ImageButton agg;
    private ImageButton miss;
    private ImageButton med;
    private ImageButton san;
    private ImageButton susp;
    private ImageButton map;
    private int count =0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.activity_home_page, container, false);
        agg = (ImageButton) v.findViewById(R.id.ibAgg);
        miss = (ImageButton) v.findViewById(R.id.ibMiss);
        med = (ImageButton) v.findViewById(R.id.ibMed);
        san = (ImageButton) v.findViewById(R.id.ibSan);
        susp = (ImageButton) v.findViewById(R.id.ibSusp);
        map = (ImageButton) v.findViewById(R.id.ibMap);
        agg.bringToFront();
        miss.bringToFront();
        med.bringToFront();
        san.bringToFront();
        susp.bringToFront();
        agg.setOnClickListener(this);
        miss.setOnClickListener(this);
        med.setOnClickListener(this);
        san.setOnClickListener(this);
        susp.setOnClickListener(this);
        map.setOnClickListener(this);
        return v;
    }
    @Override
    public void onClick(View v) {
        Animation homePage = AnimationUtils.loadAnimation(getActivity(),R.anim.anim_home_page);
        homePage.setAnimationListener(this);
        switch (v.getId()){
            case R.id.ibAgg:
                count = 0;
                agg.startAnimation(homePage);
                break;
            case R.id.ibMiss:
                count = 2;
                miss.startAnimation(homePage);
                break;
            case R.id.ibMed:
                count = 4;
                med.startAnimation(homePage);
                break;
            case R.id.ibSan:
                count = 1;
                san.startAnimation(homePage);
                break;
            case R.id.ibSusp:
                count = 3;
                susp.startAnimation(homePage);
                break;
            case R.id.ibMap:
                count = 10;
                map.startAnimation(homePage);

        }
    }
    @Override
    public void onPause() {
        super.onPause();
        agg.clearAnimation();
        miss.clearAnimation();
        med.clearAnimation();
        san.clearAnimation();
        susp.clearAnimation();
    }
    @Override
    public void onAnimationStart(Animation animation) {
    }
    @Override
    public void onAnimationEnd(Animation animation) {
        if(count == 10)
            startActivity(new Intent(getActivity(),MapsActivity.class).putExtra("type",count));
        else
            startActivity(new Intent(getActivity(),Reports.class).putExtra("page",count));
        getActivity().finish();
    }
    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
