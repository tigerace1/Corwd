package com.example.chengen.crowdsafes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ShowingPhoto extends AppCompatActivity {
    ViewPager viewPager;
    SwipeAdapter adapter;
    Bitmap one,two,three;
    private final static String SERVER_ADDRESS = "http://crowdsafe.azurewebsites.net/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_photo);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        viewPager.setAdapter(adapter);
        new DownloadImages().execute();
    }
    private class DownloadImages extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Thread t = new Thread(){
                @Override
                public void run() {
                    super.run();
                    one = getImages("SmallOne");
                }
            };
            t.start();
            Thread t1 = new Thread(){
                @Override
                public void run() {
                    super.run();
                    one = getImages("SmallTwo");
                }
            };
            t1.start();
            Thread t2 = new Thread(){
                @Override
                public void run() {
                    super.run();
                    one = getImages("SmallThree");
                }
            };
            t2.start();
            try {
                t.join();
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new SwipeAdapter(getApplicationContext(),one,two,three);
            viewPager.setAdapter(adapter);
        }
    }
    private synchronized Bitmap getImages(String name) {
        String url = SERVER_ADDRESS + "pictures/" + name + ".JPG";
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setConnectTimeout(1000 * 30);
            connection.setReadTimeout(1000 * 30);
            return BitmapFactory.decodeStream((InputStream) connection.getContent(), null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, ReciverPhoto.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ReciverPhoto.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
