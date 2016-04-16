package com.example.chengen.crowdsafes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class NavigationMenu extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private RelativeLayout drawerPane;
    private ListView listView;
    private List<NavItem> listNavItems;
    private List<Fragment> listFragments;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_menu);
        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        if(b!=null) {
            count = b.getInt("count");
        }
        SharedPreferences sharedPref =getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if (sharedPref.contains("username"))
            getSupportActionBar().setTitle("Hello: "+sharedPref.getString("username",""));
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
        listNavItems.add(new NavItem("Reports", home));
        listNavItems.add(new NavItem("Reported lists",list));
        listNavItems.add(new NavItem("Help", help));
        listNavItems.add(new NavItem("About...",about));
        listNavItems.add(new NavItem("Settings",setting));
        listFragments = new ArrayList<>();
        listFragments.add(new HomePage());
        listFragments.add(new ReciverList());
        listFragments.add(new Help());
        listFragments.add(new About());
        listFragments.add(new Setting());
        NavListAdapter navListAdapter = new NavListAdapter(getApplicationContext(),
                R.layout.nav_item_list,listNavItems);
        listView.setAdapter(navListAdapter);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content,listFragments.get(count)).commit();
        setTitle(listNavItems.get(count).getTitle());
        listView.setItemChecked(count, true);
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
        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }else if(item.getItemId()==R.id.action_loginsignup){
            SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(this,LoginPage.class));
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
            actionBarDrawerToggle.syncState();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Do you want to Exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    System.exit(0);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
