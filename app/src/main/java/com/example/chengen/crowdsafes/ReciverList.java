package com.example.chengen.crowdsafes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.net.URLConnection;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class ReciverList extends Fragment implements ListView.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener {
    private static DataAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Bitmap Photo1;
    private Bitmap Photo2;
    private Bitmap Photo3;
    private JSONObject missData;
    private static ListView listview;
    private final static String SERVER_ADDRESS = "http://crowdsafe.azurewebsites.net/";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_reciver_list, container, false);
        listview = (ListView) v.findViewById(R.id.reList);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        if (adapter != null)
            listview.setAdapter(adapter);
        else {
            adapter = new DataAdapter(getActivity(), R.layout.activity_reciver);
            Thread t = new Thread() {
                @Override
                public void run() {
                    super.run();
                    getMissDataFromDB();
                }
            };
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            listview.setAdapter(adapter);
            swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setOnRefreshListener(this);
            listview.setOnItemClickListener(this);
        }
        return v;
    }

    private void getMissDataFromDB() {
        try {
            InputStream is = new BufferedInputStream(getActivity().getAssets().open("cacerts.jks"));
            String st = is.toString();
            System.setProperty("javax.net.ssl.trustStore", st);
            Properties systemProps = System.getProperties();
            System.setProperties(systemProps);
            URL url = new URL("https://www.crowdsafes.com/seeReportMissing");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setHostnameVerifier(hv);
            con.setRequestMethod("GET");
            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader in = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            String inputLines;
            while ((inputLines = in.readLine()) != null) {
                sb.append(inputLines);
            }
            inputLines = sb.toString();
            JSONObject json = new JSONObject(inputLines);
            final JSONArray missDatas = json.getJSONArray("missing");
            for (int i = 0; i < missDatas.length(); i++) {
                Thread t1=null,t2=null,t3=null;
                missData = missDatas.getJSONObject(i);
                if (missData.getString("photovdo1").equals("null")){
                     t1 = new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Photo1 = getImages("abcd");
                                Photo1 = Bitmap.createScaledBitmap(Photo1,120,120,false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    t1.start();
                }
                if (missData.getString("photovdo1").equals("null")){
                    t2= new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Photo2 = getImages("dcba");
                                Photo2 = Bitmap.createScaledBitmap(Photo2, 120, 120, false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    t2.start();
                }
                if (missData.getString("photovdo1").equals("null")){
                    t3 = new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Photo3 = getImages("oooo");
                                Photo3 = Bitmap.createScaledBitmap(Photo3, 120, 120, false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    t3.start();
                }
                DataProvider dataProvider = new DataProvider("Missing", missData.getString("reportdescription"), Photo1
                        , Photo2, Photo3, "l", missData.getString("location"),
                        missData.getString("reward"), missData.getString("contactperson"), missData.getString("time"),
                        missData.getString("reporttype"), "");
                adapter.add(dataProvider);
                if(t1!=null)
                    t1.join();
                if(t2!=null)
                    t2.join();
                if(t3!=null)
                    t3.join();
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session) {
            return urlHostName.equals(session.getPeerHost());
        }
    };
    @Override
    public void onRefresh() {
        adapter.clear();
        Thread t2 = new Thread() {
            @Override
            public void run() {
                getMissDataFromDB();
            }
        };
        t2.start();
        try {
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DataProvider data = (DataProvider) adapter.getItem(position);
        Intent ii = new Intent(getActivity(), ReciverPhoto.class);
        ii.putExtra("type", data.getTitleData());
        ii.putExtra("reportType", data.getReportType());
        ii.putExtra("missingType", data.getMissingType());
        ii.putExtra("reportDescription", data.getSituationData());
        ii.putExtra("responseTo", data.getResponseTo());
        ii.putExtra("contact", data.getContact());
        byte[] bytes1 = null, bytes2 = null, bytes3 = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (data.getImagesData1() != null) {
            data.getImagesData1().compress(Bitmap.CompressFormat.PNG, 100, stream);
            bytes1 = stream.toByteArray();
        }
        if (data.getImagesData2() != null) {
            stream = new ByteArrayOutputStream();
            data.getImagesData2().compress(Bitmap.CompressFormat.PNG, 100, stream);
            bytes2 = stream.toByteArray();
        }
        if (data.getImagesData2() != null) {
            stream = new ByteArrayOutputStream();
            data.getImagesData2().compress(Bitmap.CompressFormat.PNG, 100, stream);
            bytes3 = stream.toByteArray();
        }
        ii.putExtra("photosOne", bytes1);
        ii.putExtra("photosTwo", bytes2);
        ii.putExtra("photosThree", bytes3);
        ii.putExtra("videos", data.getVideoData());
        ii.putExtra("location", data.getLocationData());
        ii.putExtra("locationDes", data.getLocationDesData());
        startActivity(ii);
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
}



