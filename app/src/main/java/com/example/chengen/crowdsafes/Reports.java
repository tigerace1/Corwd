package com.example.chengen.crowdsafes;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;
public class Reports extends AppCompatActivity implements ViewPager.OnPageChangeListener,TabHost.OnTabChangeListener,Parcelable{
    private ViewPager viewPager;
    private TabHost tabHost;
    List<Fragment> fragmentList;
    private static int i=0;
    public Reports(){}
    public static final Creator<Reports> CREATOR = new Creator<Reports>() {
        @Override
        public Reports createFromParcel(Parcel in) {
            return null;
        }
        @Override
        public Reports[] newArray(int size) {
            return new Reports[size];
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports_layout);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b!=null){
            i=b.getInt("page");
        }
        fragmentList = new ArrayList<>();
        fragmentList.add(new Aggression());
        fragmentList.add(new Sanitary());
        fragmentList.add(new Missing());
        fragmentList.add(new Suspicion());
        fragmentList.add(new Medical());
        initPager();
        initHost();
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
        tabHost=(TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        tabHost.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_layout));
        String[] tabNames = {"Aggression","Missing","Medical","Sanitary","Suspicion"};
        Bitmap agg= BitmapFactory.decodeResource(getResources(), R.drawable.btn_aggression_resp);
        Bitmap miss= BitmapFactory.decodeResource(getResources(), R.drawable.btn_missing_resp);
        Bitmap med = BitmapFactory.decodeResource(getResources(), R.drawable.btn_medical_resp);
        Bitmap san = BitmapFactory.decodeResource(getResources(), R.drawable.btn_sanitary_resp);
        Bitmap susp = BitmapFactory.decodeResource(getResources(), R.drawable.btn_suspicion_resp);
        Drawable[] icons={new BitmapDrawable(getResources(), agg),new BitmapDrawable(getResources(), san),
                new BitmapDrawable(getResources(), miss),new BitmapDrawable(getResources(), susp),
                new BitmapDrawable(getResources(), med)};
        for(int i=0;i<tabNames.length;i++){
            TabHost.TabSpec tabSpec;
            tabSpec = tabHost.newTabSpec(tabNames[i]);
            tabSpec.setIndicator("",icons[i]);
            tabSpec.setContent(new FakeContent(getApplication()));
            tabHost.addTab(tabSpec);
        }
        tabHost.setCurrentTab(i);
        tabHost.setOnTabChangedListener(this);
    }
    private void initPager(){
        FramentPageAdapter framentPageAdapter = new FramentPageAdapter(
                getSupportFragmentManager(), fragmentList);
        viewPager =(ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(framentPageAdapter);
        viewPager.setCurrentItem(i);
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
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(i);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_loginsignup){
            startActivity(new Intent(getApplicationContext(),LoginPage.class));
            finish();
            return true;
        }else if(item.getItemId()==android.R.id.home){
            startActivity(new Intent(getApplicationContext(),NavigationMenu.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("count",0));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
            startActivity(new Intent(Reports.this,NavigationMenu.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("count",0));
            finish();
    }
}

