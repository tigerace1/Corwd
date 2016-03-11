package com.example.chengen.crowdsafes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class NavigationMenu extends AppCompatActivity {
    DrawerLayout drawerLayout;
    RelativeLayout drawerPane;
    ListView listView;
    List<NavItem> listNavItems;
    List<Fragment> listFragments;
    ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_menu);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout =(DrawerLayout)findViewById(R.id.drawer_layout);
        drawerPane = (RelativeLayout)findViewById(R.id.drawer_pane);
        listView = (ListView) findViewById(R.id.nav_list);
        listNavItems = new ArrayList<>();
        Bitmap home = BitmapFactory.decodeResource(getResources(),R.drawable.newreport);
        Bitmap list = BitmapFactory.decodeResource(getResources(),R.drawable.lists);
        Bitmap help = BitmapFactory.decodeResource(getResources(),R.drawable.newhe);
        Bitmap about = BitmapFactory.decodeResource(getResources(),R.drawable.newin);
        Bitmap setting = BitmapFactory.decodeResource(getResources(),R.drawable.newsetting);
        listNavItems.add(new NavItem("Reports",home));
        listNavItems.add(new NavItem("Reported lists",list));
        listNavItems.add(new NavItem("Help", help));
        listNavItems.add(new NavItem("About...",about));
        listNavItems.add(new NavItem("Settings",setting));
        NavListAdapter navListAdapter = new NavListAdapter(getApplicationContext(),
                R.layout.nav_item_list,listNavItems);
        listView.setAdapter(navListAdapter);
        listFragments = new ArrayList<>();
        listFragments.add(new HomePage());
        listFragments.add(new Help());
        listFragments.add(new Setting());
        listFragments.add(new About());
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content,listFragments.get(0)).commit();
        setTitle(listNavItems.get(0).getTitle());
        listView.setItemChecked(0, true);
        drawerLayout.closeDrawer(drawerPane);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_content, listFragments.get(position)).commit();
                setTitle(listNavItems.get(position).getTitle());
                listView.setItemChecked(position, true);
                drawerLayout.closeDrawer(drawerPane);
            }
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,
                R.string.drawer_opened,R.string.drawer_closed){
            @Override
            public void onDrawerOpened(View drawerView){
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView){
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }
        };
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}
