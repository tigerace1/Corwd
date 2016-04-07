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
import android.widget.ImageView;
import android.widget.TextView;

public class HomePage extends Fragment implements View.OnClickListener, Animation.AnimationListener {
    private ImageButton agg;
    private ImageButton miss;
    private ImageButton med;
    private ImageButton san;
    private ImageButton susp;
    TextView tvAgg;
    TextView tvMiss;
    TextView tvMed;
    TextView tvSan;
    TextView tvSusp;
    private View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        v = inflater.inflate(R.layout.activity_home_page, container, false);
        agg = (ImageButton)v.findViewById(R.id.ibAgg);
        miss = (ImageButton)v.findViewById(R.id.ibMiss);
        med = (ImageButton)v.findViewById(R.id.ibMed);
        san = (ImageButton)v.findViewById(R.id.ibSan);
        susp = (ImageButton)v.findViewById(R.id.ibSusp);
        tvAgg = (TextView) v.findViewById(R.id.tvAgg);
        tvMiss = (TextView) v.findViewById(R.id.tvMiss);
        tvMed = (TextView) v.findViewById(R.id.tvMed);
        tvSan = (TextView) v.findViewById(R.id.tvSan);
        tvSusp = (TextView) v.findViewById(R.id.tvSusp);
        Animation an = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_text);
        tvAgg.startAnimation(an);
        tvMiss.startAnimation(an);
        tvMed.startAnimation(an);
        tvSan.startAnimation(an);
        tvSusp.startAnimation(an);
        agg.startAnimation(an);
        miss.startAnimation(an);
        med.startAnimation(an);
        san.startAnimation(an);
        susp.startAnimation(an);
        agg.setOnClickListener(this);
        miss.setOnClickListener(this);
        med.setOnClickListener(this);
        san.setOnClickListener(this);
        susp.setOnClickListener(this);
        return v;
    }
    @Override
    public void onClick(View v) {
        ImageView title = (ImageView)this.v.findViewById(R.id.ivTitle);
        Animation one = AnimationUtils.loadAnimation(getActivity(),R.anim.anim_one);
        Animation Ag = AnimationUtils.loadAnimation(getActivity(),R.anim.home_agg);
        Animation Sa = AnimationUtils.loadAnimation(getActivity(),R.anim.home_san);
        Animation Mi = AnimationUtils.loadAnimation(getActivity(),R.anim.home_miss);
        Animation Su = AnimationUtils.loadAnimation(getActivity(),R.anim.home_susp);
        Animation Me = AnimationUtils.loadAnimation(getActivity(),R.anim.home_med);
        one.setAnimationListener(this);
        Ag.setAnimationListener(this);
        Sa.setAnimationListener(this);
        Mi.setAnimationListener(this);
        Su.setAnimationListener(this);
        Me.setAnimationListener(this);
        Thread welcomeThread;
        switch (v.getId()){
            case R.id.ibAgg:
                title.setVisibility(View.INVISIBLE);
                agg.startAnimation(one);
                miss.startAnimation(one);
                med.startAnimation(one);
                san.startAnimation(one);
                susp.startAnimation(one);
                agg.startAnimation(Ag);
                miss.startAnimation(Mi);
                med.startAnimation(Me);
                san.startAnimation(Sa);
                susp.startAnimation(Su);
                welcomeThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            super.run();
                            sleep(780);
                        } catch (Exception e) {
                           e.printStackTrace();
                        } finally {
                            startActivity(new Intent(getActivity(), Reports.class).putExtra("page", 0));
                            getActivity().finish();
                        }
                    }
                };
                welcomeThread.start();
                break;
            case R.id.ibMiss:
                title.setVisibility(View.INVISIBLE);
                agg.startAnimation(one);
                miss.startAnimation(one);
                med.startAnimation(one);
                san.startAnimation(one);
                susp.startAnimation(one);
                agg.startAnimation(Ag);
                miss.startAnimation(Mi);
                med.startAnimation(Me);
                san.startAnimation(Sa);
                susp.startAnimation(Su);
                welcomeThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            super.run();
                            sleep(780);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            startActivity(new Intent(getActivity(), Reports.class).putExtra("page",2));
                            getActivity().finish();
                        }
                    }
                };
                welcomeThread.start();
                break;
            case R.id.ibMed:
                title.setVisibility(View.INVISIBLE);
                agg.startAnimation(one);
                miss.startAnimation(one);
                med.startAnimation(one);
                san.startAnimation(one);
                susp.startAnimation(one);
                agg.startAnimation(Ag);
                miss.startAnimation(Mi);
                med.startAnimation(Me);
                san.startAnimation(Sa);
                susp.startAnimation(Su);
                welcomeThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            super.run();
                            sleep(780);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            startActivity(new Intent(getActivity(), Reports.class).putExtra("page",4));
                            getActivity().finish();
                        }
                    }
                };
                welcomeThread.start();
                break;
            case R.id.ibSan:
                title.setVisibility(View.INVISIBLE);
                agg.startAnimation(one);
                miss.startAnimation(one);
                med.startAnimation(one);
                san.startAnimation(one);
                susp.startAnimation(one);
                agg.startAnimation(Ag);
                miss.startAnimation(Mi);
                med.startAnimation(Me);
                san.startAnimation(Sa);
                susp.startAnimation(Su);
                welcomeThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            super.run();
                            sleep(780);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            startActivity(new Intent(getActivity(), Reports.class).putExtra("page",1));
                            getActivity().finish();
                        }
                    }
                };
                welcomeThread.start();
                break;
            case R.id.ibSusp:
                title.setVisibility(View.INVISIBLE);
                agg.startAnimation(one);
                miss.startAnimation(one);
                med.startAnimation(one);
                san.startAnimation(one);
                susp.startAnimation(one);
                agg.startAnimation(Ag);
                miss.startAnimation(Mi);
                med.startAnimation(Me);
                san.startAnimation(Sa);
                susp.startAnimation(Su);
                welcomeThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            super.run();
                            sleep(780);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            startActivity(new Intent(getActivity(), Reports.class).putExtra("page",3));
                            getActivity().finish();
                        }
                    }
                };
                welcomeThread.start();
                break;
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
        ImageView iagg = (ImageView)v.findViewById(R.id.ivAgg);
        ImageView iMiss =(ImageView)v.findViewById(R.id.ivMiss);
        ImageView iMed = (ImageView)v.findViewById(R.id.ivMed);
        ImageView iSan = (ImageView)v.findViewById(R.id.ivSan);
        ImageView iSusp = (ImageView)v.findViewById(R.id.ivSusp);
        agg.setVisibility(View.INVISIBLE);
        miss.setVisibility(View.INVISIBLE);
        med.setVisibility(View.INVISIBLE);
        san.setVisibility(View.INVISIBLE);
        susp.setVisibility(View.INVISIBLE);
        iagg.setVisibility(View.VISIBLE);
        iMiss.setVisibility(View.VISIBLE);
        iMed.setVisibility(View.VISIBLE);
        iSan.setVisibility(View.VISIBLE);
        iSusp.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
