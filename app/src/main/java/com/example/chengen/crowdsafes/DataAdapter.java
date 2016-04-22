package com.example.chengen.crowdsafes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends ArrayAdapter implements Filterable {
    List adapterList = new ArrayList();
    public DataAdapter(Context context, int resource) {
        super(context, resource);
    }
    private static class Handler
    {
        TextView type;
        TextView reportDescription;
        TextView reportType;
        TextView missingType;
        TextView responseTo;
        TextView location;
        TextView locationDes;
        TextView contact;
        ImageView ImageOne;
        ImageView ImageTwo;
        ImageView ImageThree;
        TextView videoURL;
        ImageView videoIcon;
    }

    @Override
    public void add(Object object) {
        super.add(object);
        adapterList.add(object);
    }

    @Override
    public int getCount() {
        return this.adapterList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.adapterList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        final Handler handler;
        if(convertView==null)
        {
            final LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.activity_reciver,parent,false);
            handler = new Handler();
            handler.location = (TextView)row.findViewById(R.id.etLocation);
            handler.type = (TextView)row.findViewById(R.id.etType);
            handler.reportDescription = (TextView)row.findViewById(R.id.etReportDescription);
            handler.ImageOne = (ImageView)row.findViewById(R.id.ivListOne);
            handler.ImageTwo = (ImageView)row.findViewById(R.id.ivListTwo);
            handler.ImageThree = (ImageView)row.findViewById(R.id.ivListThree);
            handler.locationDes = (TextView)row.findViewById(R.id.etLocationDes);
            handler.reportType = (TextView)row.findViewById(R.id.etReportType);
            handler.responseTo = (TextView)row.findViewById(R.id.etResponseTo);
            handler.missingType = (TextView)row.findViewById(R.id.etMissingType);
            handler.contact = (TextView)row.findViewById(R.id.etContact);
            handler.videoURL = (TextView)row.findViewById(R.id.etVideo);
            handler.videoIcon = (ImageView) row.findViewById(R.id.ivVideoIcon);
            handler.ImageOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),ShowingPhoto.class);
                    v.getContext().startActivity(intent);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ((BitmapDrawable) handler.ImageOne.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b1 = baos.toByteArray();
                    baos = new ByteArrayOutputStream();
                    ((BitmapDrawable) handler.ImageOne.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b2 = baos.toByteArray();
                    baos = new ByteArrayOutputStream();
                    ((BitmapDrawable) handler.ImageOne.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100,baos);
                    byte[] b3 = baos.toByteArray();
                    intent.putExtra("bitmaps1",b1);
                    intent.putExtra("bitmaps2",b2);
                    intent.putExtra("bitmaps3",b3);
                }
            });
            row.setTag(handler);
        }
        else{
            handler= (Handler)row.getTag();
        }
        DataProvider provider;
        provider = (DataProvider)this.getItem(position);
        handler.type.setText(provider.getTitleData());
        handler.reportDescription.setText(provider.getSituationData());
        handler.locationDes.setText(provider.getLocationDesData());
        handler.location.setText(provider.getLocationData());
        handler.ImageOne.setImageDrawable(new BitmapDrawable(getContext().getResources(),provider.getImagesData1()));
        handler.ImageTwo.setImageDrawable(new BitmapDrawable(getContext().getResources(),provider.getImagesData2()));
        handler.ImageThree.setImageDrawable(new BitmapDrawable(getContext().getResources(),provider.getImagesData3()));
        handler.responseTo.setText(provider.getResponseTo());
        handler.contact.setText(provider.getContact());
        handler.reportType.setText(provider.getReportType());
        handler.missingType.setText(provider.getMissingType());
        if(provider.getVideoData().equals(""))
            handler.videoIcon.setBackgroundColor(Color.WHITE);
        handler.videoURL.setText(provider.getVideoData());
        return row;
    }

    @Override
    public Filter getFilter() {
        return super.getFilter();

    }
}
