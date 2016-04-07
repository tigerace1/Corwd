package com.example.chengen.crowdsafes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ReciverPhoto extends AppCompatActivity implements View.OnClickListener{
    private String location,locationDes;
    private byte[] photoByte1,photoByte2,photoByte3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciver_photo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.crowdsafelogo);
        TextView Title = (TextView)findViewById(R.id.tvTitle);
        TextView Description=(TextView)findViewById(R.id.tvReportDescription);
        TextView LocationDes = (TextView)findViewById(R.id.tvLocationDes);
        ImageButton video = (ImageButton)findViewById(R.id.iBVideo);
        ImageButton map = (ImageButton)findViewById(R.id.iBmap);
        ImageButton photos =(ImageButton)findViewById(R.id.ibPhotos);
        video.setOnClickListener(this);
        map.setOnClickListener(this);
        photos.setOnClickListener(this);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        Title.setText("");
        Description.setText("");
        LocationDes.setText("");
        String situation ="";
        if(b!=null)
        {
            if(!(b.get("reportType")==null))
                 situation +="Report type: "+b.get("reportType")+"  ";
            if(!(b.get("missingType")==null))
                 situation +="Missing Object: "+b.get("missingType")+"  ";
            if(!(b.get("reportDesription")==null))
                situation +="Report Desription: "+b.get("reportDesription")+"  ";
            if(!(b.get("responseTo")==null))
                situation +="Response to: "+b.get("responseTo")+"  ";
            if(!(b.get("contact")==null))
                situation +="Contact: "+b.get("contact");
            if(!(b.get("type")==null))
                Title.setText((String) b.get("type"));
            if(b.get("location")!=null)
                location = b.get("location")+"";
            if(b.get("photosOne")!=null)
                photoByte1 = b.getByteArray("photosOne");
            if(b.get("photosTwo")!=null)
                photoByte2 = b.getByteArray("photosTwo");
            if(b.get("photosThree")!=null)
                photoByte3 = b.getByteArray("photosThree");
            if(photoByte1==null&&photoByte2==null&&photoByte3==null)
                photos.setClickable(false);
            System.out.println(photoByte1+"  "+photoByte2+"  "+photoByte3);
            if(!(b.get("locationDes")==null)) {
                locationDes = b.get("locationDes")+"";
                LocationDes.setText("Location Description: " + locationDes);
            }
            Description.setText(situation);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, NavigationMenu.class).putExtra("count",1));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibPhotos:
                Intent intent = new Intent(ReciverPhoto.this,ShowingPhoto.class);
                intent.putExtra("bitmaps1",photoByte1);
                intent.putExtra("bitmaps2",photoByte2);
                intent.putExtra("bitmaps3",photoByte3);
                startActivity(intent);
                break;
            case R.id.iBVideo:
               break;
            case R.id.iBmap:
               Intent intent4 = new Intent(this,MapsActivity.class);
               intent4.putExtra("Location",location);
               intent4.putExtra("LocDescription",locationDes);
               startActivity(intent4);
               break;
        }
    }
}
