package com.example.chengen.crowdsafes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class HomePage extends Fragment implements View.OnClickListener {
    private int count =0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
       View v = inflater.inflate(R.layout.activity_home_page, container, false);
        ImageButton agg = (ImageButton) v.findViewById(R.id.ibAgg);
        ImageButton miss = (ImageButton) v.findViewById(R.id.ibMiss);
        ImageButton med = (ImageButton) v.findViewById(R.id.ibMed);
        ImageButton san = (ImageButton) v.findViewById(R.id.ibSan);
        ImageButton susp = (ImageButton) v.findViewById(R.id.ibSusp);
        ImageButton map = (ImageButton) v.findViewById(R.id.ibMap);
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
        switch (v.getId()){
            case R.id.ibAgg:
                count = 0;
                break;
            case R.id.ibMiss:
                count = 2;
                break;
            case R.id.ibMed:
                count = 4;
                break;
            case R.id.ibSan:
                count = 1;
                break;
            case R.id.ibSusp:
                count = 3;
                break;
            case R.id.ibMap:
                count = 10;
        }
        if(count == 10)
            startActivity(new Intent(getActivity(),MapsActivity.class).putExtra("type",count));
        else
            startActivity(new Intent(getActivity(),Reports.class).putExtra("page",count));
        getActivity().finish();
    }
}
