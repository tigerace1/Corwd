package com.example.chengen.crowdsafes;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;
public class HomePage extends Fragment implements ViewPager.OnPageChangeListener,TabHost.OnTabChangeListener{
    private ViewPager viewPager;
    private TabHost tabHost;
    private View v;
    private int i=0;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.home_page_layout, container, false);
        initPager();
        initHost();
        i++;
        return v;
    }
    private class FakeContent implements TabHost.TabContentFactory{
        Context context;
        public FakeContent(Context context){
            this.context=context;
        }
        @Override
        public View createTabContent(String tag) {
            View fakeview = new View(context);
            fakeview.setMinimumHeight(0);
            fakeview.setMinimumWidth(0);
            return fakeview;
        }
    }
    private void initHost(){
        tabHost=(TabHost) v.findViewById(R.id.tabHost);
        tabHost.setup();
        String[] tabNames = {"Aggression","Missing","Medical","Sanitary","Suspicion"};
        Bitmap agg= BitmapFactory.decodeResource(getResources(), R.drawable.btn_aggression_resp);
        Bitmap miss= BitmapFactory.decodeResource(getResources(), R.drawable.btn_missing_resp);
        Bitmap med = BitmapFactory.decodeResource(getResources(), R.drawable.btn_medical_resp);
        Bitmap san = BitmapFactory.decodeResource(getResources(), R.drawable.btn_sanitary_resp);
        Bitmap susp = BitmapFactory.decodeResource(getResources(), R.drawable.btn_suspicion_resp);
        Drawable[] icons={new BitmapDrawable(getResources(), agg),new BitmapDrawable(getResources(), miss),
                new BitmapDrawable(getResources(), med),new BitmapDrawable(getResources(), san),
                new BitmapDrawable(getResources(), susp)};
        for(int i=0;i<tabNames.length;i++){
            TabHost.TabSpec tabSpec;
            tabSpec = tabHost.newTabSpec(tabNames[i]);
            tabSpec.setIndicator("",icons[i]);
            tabSpec.setContent(new FakeContent(getActivity()));
            tabHost.addTab(tabSpec);
        }
        tabHost.setOnTabChangedListener(this);
    }
    private void initPager(){
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new Aggression());
        fragmentList.add(new Missing());
        fragmentList.add(new Medical());
        fragmentList.add(new Sanitary());
        fragmentList.add(new Suspicion());
        FramentPageAdapter framentPageAdapter = new FramentPageAdapter(
                getChildFragmentManager(), fragmentList);
        viewPager =(ViewPager)v.findViewById(R.id.viewpager);
        viewPager.setAdapter(framentPageAdapter);
        viewPager.setOnPageChangeListener(this);
    }
    @Override
    public void onTabChanged(String tabId) {
        int selectedItem = tabHost.getCurrentTab();
        viewPager.setCurrentItem(selectedItem);
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    @Override
    public void onPageSelected(int position) {
        tabHost.setCurrentTab(position);
    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
