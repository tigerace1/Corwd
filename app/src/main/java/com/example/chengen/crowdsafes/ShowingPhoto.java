package com.example.chengen.crowdsafes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class ShowingPhoto extends AppCompatActivity {
    ViewPager viewPager;
    SwipeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_photo);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        byte[] photoByte1=null;
        byte[] photoByte2=null;
        byte[] photoByte3=null;
        if(b!=null)
        {
            if(b.get("bitmaps1")!=null)
                photoByte1 = b.getByteArray("bitmaps1");
            if(b.get("bitmaps2")!=null)
                photoByte2 = b.getByteArray("bitmaps2");
            if(b.get("bitmaps3")!=null)
                photoByte3 = b.getByteArray("bitmaps3");
        }
        Bitmap bitmapOne = stringToBitMap(photoByte1);
        Bitmap bitmapTwo = stringToBitMap(photoByte2);
        Bitmap bitmapThree = stringToBitMap(photoByte3);
        System.out.println(bitmapOne+" "+bitmapTwo+"  "+bitmapThree);
        adapter = new SwipeAdapter(this,bitmapOne,bitmapTwo,bitmapThree);
        viewPager.setAdapter(adapter);
    }
    private Bitmap stringToBitMap(byte[] photoString){
        try {
            return BitmapFactory.decodeByteArray(photoString, 0, photoString.length);
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
