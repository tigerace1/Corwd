package com.example.chengen.crowdsafes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import java.util.Stack;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class ReciverList extends Fragment implements ListView.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener {
    private static DataAdapter adapter;
    private DataProvider dataProvider;
    private String inputLines;
    private static Stack<String> type,reportType,missingType,reportDescription,responseTo,contact,location,locationDes,videoURL;
    private static Stack<Bitmap> photo1,photo2,photo3;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_reciver_list, container, false);
        ListView listview = (ListView) v.findViewById(R.id.reList);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        if(adapter!=null)
            listview.setAdapter(adapter);
        else {
            adapter = new DataAdapter(getActivity(), R.layout.activity_reciver);
            type = new Stack<>();
            reportType = new Stack<>();
            missingType = new Stack<>();
            reportDescription = new Stack<>();
            responseTo = new Stack<>();
            contact = new Stack<>();
            photo1 = new Stack<>();
            photo2 = new Stack<>();
            photo3 = new Stack<>();
            location = new Stack<>();
            locationDes = new Stack<>();
            videoURL = new Stack<>();
            Thread t = new Thread() {
                @Override
                public void run() {
                    super.run();
                    type.clear();
                    reportDescription.clear();
                    photo1.clear();
                    photo2.clear();
                    photo3.clear();
                    location.clear();
                    locationDes.clear();
                    responseTo.clear();
                    contact.clear();
                    reportType.clear();
                    missingType.clear();
                    videoURL.clear();
                    getAggDataFromDB();
                    getMissDataFromDB();
                }
            };
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < type.size(); i++) {
                dataProvider = new DataProvider(type.get(i), reportDescription.get(i), photo1.get(i),
                        photo2.get(i), photo3.get(i), videoURL.get(i), location.get(i),
                        locationDes.get(i), responseTo.get(i), contact.get(i), reportType.get(i), missingType.get(i));
                adapter.add(dataProvider);
            }
        }
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        listview.setOnItemClickListener(this);
        return v;
    }

    private void getAggDataFromDB(){
        try {
            InputStream is = new BufferedInputStream(getActivity().getAssets().open("cacerts.jks"));
            String st = is.toString();
            System.setProperty("javax.net.ssl.trustStore", st);
            Properties systemProps = System.getProperties();
            System.setProperties(systemProps);
            URL url = new URL("https://www.crowdsafes.com/seeReportAggression");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setHostnameVerifier(hv);
            con.setRequestMethod("GET");
            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader in = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            while ((inputLines = in.readLine()) != null) {
                sb.append(inputLines);
            }
            inputLines = sb.toString();
            in.close();
            JSONObject json = new JSONObject(inputLines);
            JSONArray aggDatas = json.getJSONArray("aggression");
            for (int i = 0; i <aggDatas.length(); i++) {
                JSONObject aggData = aggDatas.getJSONObject(i);
                type.push(aggData.getString("id"));
                reportDescription.push(aggData.getString("reportdescription"));
                byte[] ph1 = (byte[])aggData.get("photo1");
                byte[] ph2 = (byte[])aggData.get("photo2");
                byte[] ph3 = (byte[])aggData.get("photo3");
                if(ph1!=null)
                   photo1.push(stringToBitMap(new String(ph1)));
                photo2.push(stringToBitMap(new String(ph2)));
                photo3.push(stringToBitMap(new String(ph3)));
                videoURL.push(aggData.getString("videourl"));
                location.push(aggData.getString("location"));
                locationDes.push(aggData.getString("locationdescription"));
                reportType.push("");
                missingType.push("");
                responseTo.push("");
                contact.push("");
            }
        }catch(Exception e){
           e.printStackTrace();
        }
    }
    private void getMissDataFromDB(){
        try {
            InputStream is = new BufferedInputStream(getActivity().getAssets().open("cacerts.jks"));
            String st = is.toString();
            System.setProperty("javax.net.ssl.trustStore", st);
            Properties systemProps = System.getProperties();
            System.setProperties(systemProps);
            URL url = new URL("https://www.crowdsafes.com/seeReportsMissing");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setHostnameVerifier(hv);
            con.setRequestMethod("GET");
            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader in = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            while ((inputLines = in.readLine()) != null) {
                sb.append(inputLines);
            }
            inputLines = sb.toString();
            in.close();
            JSONObject json = new JSONObject(inputLines);
            JSONArray aggDatas = json.getJSONArray("missing");
            for (int i = 0; i <aggDatas.length(); i++) {
                JSONObject aggData = aggDatas.getJSONObject(i);
                type.push(aggData.getString("reporttype"));
                reportDescription.push(aggData.getString("txtField1"));
                byte[] ph1 = (byte[])aggData.get("photovdo1");
                byte[] ph2 = (byte[])aggData.get("photovdo2");
                byte[] ph3 = (byte[])aggData.get("photovdo3");
                if(ph1!=null)
                    photo1.push(stringToBitMap(new String(ph1)));
                if(ph2!=null)
                    photo2.push(stringToBitMap(new String(ph2)));
                if(ph3!=null)
                    photo3.push(stringToBitMap(new String(ph3)));
               // videoURL.push(aggData.getString("videourl"));
                location.push(aggData.getString("location"));
                locationDes.push(aggData.getString("reward"));
                reportType.push("");
                missingType.push("");
                responseTo.push("contactpersson");
                contact.push("time");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private static HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session) {
            return urlHostName.equals(session.getPeerHost());
        }
    };
    private Bitmap stringToBitMap(String photoString){
        try {
            byte [] encodeByte= Base64.decode(photoString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    private String setDistance(String yourLatLong) {
        String[] loc = yourLatLong.split(",");
        double lat1 = Double.parseDouble(loc[0].substring(1));
        double long1 = Double.parseDouble(loc[1].substring(0, loc.length - 1));
        double long2 = 0, lat2 = 0;
        double a = Math.sin((lat2 - lat1) / 2) * Math.sin((lat2 - lat1) / 2);
        double b = Math.cos(lat1) * Math.cos(lat2) * Math.sin((long2 - long1) / 2) * Math.sin((long2 - long1) / 2);
        double distance = 2 * (6356.752) * Math.sqrt(a + b);
        return distance+"";
    }
    @Override
    public void onRefresh() {
        System.out.println("AHAHAH");
        Thread t2 = new Thread(){
            @Override
            public void run() {
                type.clear();
                reportDescription.clear();
                photo1.clear();
                photo2.clear();
                photo3.clear();
                location.clear();
                locationDes.clear();
                responseTo.clear();
                contact.clear();
                reportType.clear();
                missingType.clear();
                videoURL.clear();
                super.run();
                getAggDataFromDB();
                getMissDataFromDB();
            }
        };
        t2.start();
        try {
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        adapter.clear();
        for (int i = 0; i < type.size(); i++) {
            dataProvider = new DataProvider(type.get(i), reportDescription.get(i), photo1.get(i), photo2.get(i),
                    photo3.get(i),videoURL.get(i),location.get(i), locationDes.get(i),responseTo.get(i),
                    contact.get(i),reportType.get(i),missingType.get(i));
            adapter.add(dataProvider);
        }
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DataProvider data = (DataProvider)adapter.getItem(position);
        Intent ii=new Intent(getActivity(), ReciverPhoto.class);
        ii.putExtra("type", data.getTitleData());
        ii.putExtra("reportType",data.getReportType());
        ii.putExtra("missingType",data.getMissingType());
        ii.putExtra("reportDescription", data.getSituationData());
        ii.putExtra("responseTo",data.getResponseTo());
        ii.putExtra("contact",data.getContact());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        data.getImagesData1().compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes1 = stream.toByteArray();
        stream = new ByteArrayOutputStream();
        data.getImagesData2().compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes2 = stream.toByteArray();
        stream = new ByteArrayOutputStream();
        data.getImagesData3().compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes3 = stream.toByteArray();
        System.out.println(bytes1+"  "+bytes2+"  "+bytes3);
        ii.putExtra("photosOne",bytes1);
        ii.putExtra("photosTwo",bytes2);
        ii.putExtra("photosThree",bytes3);
        ii.putExtra("videos",data.getVideoData());
        ii.putExtra("location",data.getLocationData());
        ii.putExtra("locationDes",data.getLocationDesData());
        startActivity(ii);
    }
}


